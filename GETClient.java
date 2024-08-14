import java.net.*;
import java.io.*;

public class GETClient {
	private static int port;
	private static String serverName;
	private static Socket socket;
	private BufferedWriter bufwriter;
	private BufferedReader bufreader;
	private String head;

	//construct
	public GETClient(String name){
		init();
		String[] splited = name.split(":");
		serverName = splited[0];
		this.port = Integer.parseInt(splited[1]);
	}

	//initalisation
	private void init(){
		port = 0;
		serverName = "";
		socket = null;
		bufwriter = null;
		bufreader = null;
		head = "PUT /atom.xml HTTP/1.1"+"\n"
		+ "User-Agent: ATOMClient/1/0"+"\n"
		+ "Content-Type: application/atom+xml;type=entry" +"\n"
		+ "Content-Length: nnn"+"\n"
		+ "\n";
	}

	//establish connection with aggregation SERVER,if work return 1;
	//otherwise, return 0
	public int connect(){
      	try {
         	socket = new Socket(serverName, port);
			this.bufwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return 1;
      	} catch (IOException e) {
         	e.printStackTrace();
      	}
      	return 0;
	}

	//send message to server
	public void sendMessage(String message) {
		try {
			this.bufwriter.write(message+"\n");
			this.bufwriter.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//send "STOP" to server
	public void stopSendMessage() {
		try {
			this.bufwriter.write("STOP\n");
			this.bufwriter.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//reveive message from aggregation server and return the message.
	//if failed return "";
	public String receiveMessage() {
		String fianl_result = "";
		try {
			String result = "";
			while ((result = this.bufreader.readLine()) != null) {
              	if(result.equals("STOP")){
              		return fianl_result;
              	}else if(result.length()>4){
              		fianl_result= fianl_result + result + "\n";
              	}else if(result.length()>2){
              		fianl_result= fianl_result + result;
              	}
            }
			return fianl_result;
		} catch (IOException e1) {
			return "";
		}
	}

	//disconnection with server
	public int disconnect(){
		try{
			bufwriter.close();
			bufreader.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Client socket close failed!!");
         	e.printStackTrace();
      	}
      	return 1;
	}

	//main function. test purpose only
   	// public static void main(String [] args) {
    //   	GETClient tmp = new GETClient(args[0]);
    //   	tmp.connect();
    //   	tmp.sendMessage("GET");
    //   	tmp.stopSendMessage();
    //   	System.out.println(tmp.receiveMessage());
    //   	// tmp.receiveMessage();
    //   	tmp.disconnect();
   	// }
}