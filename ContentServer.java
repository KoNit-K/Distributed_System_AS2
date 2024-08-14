import java.net.*;
import java.io.*;

public class ContentServer {
	private static int port;
	private static String serverName;
	private static Socket socket;
	private BufferedWriter bufwriter;
	private BufferedReader bufreader;
	private String head;

	//construct
	public ContentServer(String name,String port){
		init();
		serverName = name;
		this.port = Integer.parseInt(port);
	}

	//initialisation
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

	//establish connection with aggregationserver. return 1 if succeful
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

	//send message to aggregation server
	public void sendMessage(String message) {
		try {
			this.bufwriter.write(message+"\n");
			this.bufwriter.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//send "stop" to aggregation server.
	public void stopSendMessage() {
		try {
			this.bufwriter.write("STOP\n");
			this.bufwriter.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	//add head of PUT to message
	public String addHead(String message){
		String tmp = "";
		tmp =head+message;
		return tmp;
	}

	//receive message from aggregation server and print it out
	public String receiveMessage() {
		try {
			String result = "";
			while ((result = this.bufreader.readLine()) != null) {
              	if("STOP".equals(result)){
              		return "";
              	}
              	System.out.println(result);
            }
			return result;
		} catch (IOException e1) {
			return "";
		}
	}

	//close the socket
	public int disconnect(){
		try{
			bufwriter.close();
			bufreader.close();
			socket.close();
		} catch (IOException e) {
			// System.out.println("Client socket close failed!!");
         	e.printStackTrace();
      	}
      	return 1;
	}

	//main function, test only
  //  	public static void main(String [] args) {
  //     	ContentServer tmp = new ContentServer("localhost",args[0]);
  //     	tmp.connect();
  //     	String input ="";
  //     	String in = "";
  //     	try{
		// 	File file = new File("example.txt");
		// 	BufferedReader br = new BufferedReader(new FileReader(file));
		// 	while ((in = br.readLine()) != null){
		// 		input = in;
		// 	}
		// } catch (IOException e1) {
		// 	e1.printStackTrace();
		// }
  //     	tmp.sendMessage(tmp.addHead(input));
  //     	// tmp.sendMessage("GET");
  //     	tmp.stopSendMessage();
  //     	tmp.receiveMessage();
  //     	tmp.disconnect();
  //  	}
}