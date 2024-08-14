import java.net.*;
import java.io.*;
import java.util.*;  
import javax.xml.parsers.*;

import org.xml.sax.*;
public class AggregationServer {
    private ServerSocket serverSocket;
    private static ArrayList<node> feeds = new ArrayList<node>();
    private static lamportClock lamport;
    //time out is 15 seconds
    private static int time_out = 15000;
    //default port is 4567
    private static int default_port = 4567;

    //construct
    public AggregationServer(){
        lamport = new lamportClock(0);  
    }

    //create server socket and wait for conneting
    public void start(int port) {
        try{
            System.out.println("Server is up.....");
            serverSocket = new ServerSocket(port);
            while (true)
                new ClientHandler(serverSocket.accept()).start();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //close the server
    public void stop() {
        try{
            System.out.println("server close successful");
            serverSocket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //new thread for new client
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private node currentMsg;
        //contruct 
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        //send message to client or content creators
        private synchronized void sendMessage(String message) {
                try {
                    this.writer.write(message+"\n");
                    this.writer.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            
        }

        //check the input. if it is not a valid input return "500";
        //otherwise, call searchfeed();
        public int isValidXML(String message){
            FormatXML trasfer = new FormatXML();
            String input = "";
            try {
                input = trasfer.prettyFormat(message);
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new InputSource(new StringReader(input)));
                searchFeed(input);
                return 1;
            } catch (Exception e) {
            }
            sendMessage("500");
            sendMessage("STOP");
            return 0;
        }

        //if this is the first time ATOM feed created,reply 201
        //otherwiese, reply 200
        public synchronized int searchFeed(String message){
            String[] splited = message.split("<id>"); 
            String[] splited1 = splited[1].split("</id>");
            String id = splited1[0];
            String[] splitedtmp = message.split("<updated>"); 
            String[] splitedtmp1 = splitedtmp[1].split("</updated>");
            int time =Integer.parseInt(splitedtmp1[0]);
            for (node s:feeds ) {
                if(s.getName().equals(id)){
                    sendMessage("200");
                    sendMessage("STOP");
                    feeds.remove(s);
                    int clock = lamport.toLocallamport(time);
                    node newFeed = new node(id,clock,message);
                    addNewFeed(newFeed);
                    currentMsg = s;
                    return 0;
                }
            }
            sendMessage("201");
            sendMessage("STOP");
            int clock = lamport.toLocallamport(time);
            node newFeed = new node(id,time,message);
            currentMsg = newFeed;
            addNewFeed(newFeed);
            return 1;
        }

        //add new feed and remove the oldest one if there is 25 feeds in sotrage
        public synchronized void addNewFeed(node newNode){
            sortByLamport();
            if(feeds.size()>=25){
                feeds.remove(0);
            }
            feeds.add(newNode);
        }

        //using selection sort to sort feeds by lamport clock
        public synchronized void sortByLamport(){
            for (int i =0;i<feeds.size() ;i++ ) {
                node keeper = feeds.get(i);
                int smallest = keeper.getLamport();
                int position =0;
                int counter = 0;
                // Find the minimum element in feeds[i...end]
                for (node s:feeds ) {
                    if(counter>i){
                        if(s.getLamport()<smallest){
                            keeper = s;
                            smallest = s.getLamport();
                            position = counter;
                        }
                    }
                    counter++;
                }
                //swap feeds[i] and sthe minimum element in feeds[i...end]
                node tmp = feeds.get(i);
                feeds.set(i,keeper);
                feeds.set(position,tmp);
            }
        }

        //store feeds into local filesystem
        public synchronized void writeToFile(){
            try{
                PrintWriter writer = new PrintWriter("filesystem.txt", "UTF-8");
                for (node s:feeds ) {
                    writer.println("NODE");
                    writer.println(s.getName());
                    writer.println(s.getLamport());
                    writer.println(s.getInfo());
                }
                writer.close();
            }catch (IOException e) {
                 e.printStackTrace();
            }
        }

        //get all the feeds from storage and merge into one string 
        public synchronized String reply(){
            int length = feeds.size();
            int counter =0;
            String result = "";
            synchronized(this){
                for (node s:feeds ) {
                    String tmp = s.getInfo();
                    if(counter==0){
                        if(length !=1){
                            String[] splited = tmp.split("</feed>");
                            result = splited[0];
                        }else{
                            return tmp;
                        }
                    }else if(counter == length-1){
                        String[] splited = tmp.split(">",2);
                        String[] splited1 = splited[1].split(">",2);
                        result += splited1[1];
                    }else{
                        String[] splited = tmp.split(">",2);
                        String[] splited1 = splited[1].split(">",2);
                        String[] splited2 = splited1[1].split("</feed>");
                        result += splited2[0];
                    }
                    counter++;
                }
            }
            return result;
        }

        //count down timer, close socket when time-out
        class RemindTask extends TimerTask {
            public void run() {
                System.out.println("content creator time out");
                try{
                    feeds.remove(currentMsg);
                    // AggregationServer.display();
                    writer.close();
                    reader.close();                    
                    clientSocket.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }                
            }
        }

        //communicate with client and content creators
        public void run() {
            try{
                Timer timer;
                timer = new Timer();
                timer.schedule(new RemindTask(),time_out);
                this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String inputLine = "";
                String id = "";
                int counter = 0;
                String infor = "";
                while ((inputLine = this.reader.readLine()) != null) {

                    String[] input = inputLine.split(" ");
                    if(counter < 5){
                        if(counter == 0){
                            if("PUT /atom.xml HTTP/1.1".equals(inputLine)){
                                //PUT req is recevied, check the format
                                System.out.println(input[0]);
                            }else if("GET".equals(inputLine)){
                                System.out.println(input[0]);
                                //GET req is recevied, send all feeds to Client
                                String tmpmessage=reply();
                                sendMessage(tmpmessage);
                                sendMessage("STOP");
                                return;
                            }else{
                                //received request other than GET or PUT
                                sendMessage("400");
                                sendMessage("STOP");
                                return;
                            }
                        }
                    }else{
                        if(counter == 5 && "".equals(inputLine)){
                            sendMessage("204");
                            sendMessage("STOP");
                            return;
                        }
                        //message end here
                        if ("STOP".equals(inputLine)) {
                            isValidXML(infor);
                            writeToFile();
                            infor = "";
                            counter = 0;
                        }else{
                            infor += inputLine+"\n";
                        }
                    }
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new RemindTask(),time_out);
                    counter++;
                }
            }catch (IOException e) {
                //System.out.println("Socket closed");
            }
        }
    }

    public static void main(String [] args) {
      AggregationServer server = new AggregationServer();
      //if the port have been given, use the given port
      if(args.length == 1){
        server.start(Integer.parseInt(args[0]));
      }else if(args.length == 0){
        //default port if no input
        server.start(default_port);
      }else{
        System.out.println("Error: input should be a port number or nothing (default port 4567)");
      }

   }
}