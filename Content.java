// made for create multi thread and automated test
public class Content implements Runnable {
	private String serverName;
	private String inputfile;
	private String port;
	private int mode;
    private String errorhandling;
	//construct
    public Content(String input,String inputfile,int mode,String errorhandling) throws Exception {
		String[] splited = input.split(":");
		this.serverName = splited[0];
		this.port = splited[1];
		this.inputfile = inputfile;
		this.mode = mode;
		this.errorhandling = errorhandling;
    }

    public void run() {
      	ContentServer socket = new ContentServer(serverName,port);
      	socket.connect();
  		//tranfer input file to xml format
  		toXml loadinput = new toXml();
      	String getInput = loadinput.readFile(inputfile);
      	if(mode == 0){
      		//add head to PUT request
      		socket.sendMessage(socket.addHead(getInput));
      	}else{
      		//send an empty content or ATOM XML does not make sense
      		socket.sendMessage(socket.addHead(errorhandling));
      	}
      	socket.stopSendMessage();
      	socket.receiveMessage();
      	socket.disconnect();
	}
}