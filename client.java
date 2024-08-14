// made for create multi thread and automated test
public class client implements Runnable {
	private String serverName_port;
	private String input;

	//construct
    public client(String name_port,String input) throws Exception {
		this.serverName_port = name_port;
		this.input = input;
    }

    //new GETclient
    public void run() {
      	GETClient socket = new GETClient(serverName_port);
      	socket.connect();
      	socket.sendMessage(input);
      	socket.stopSendMessage();
      	System.out.println(socket.receiveMessage());
      	socket.disconnect();
	}
}