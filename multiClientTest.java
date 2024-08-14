import java.net.*;
import java.io.*;

public class multiClientTest{
   	public static void main(String [] args) {
   		try{
        //create a ATOM feed.
	      Thread object = new Thread(new Content("localhost:4567","feed1.txt",0,""));
    		object.start();
    		Thread.sleep(1500);
        //GET request
    		Thread object2 = new Thread(new client("localhost:4567","GET"));
    		object2.start();
        Thread.sleep(500);
        //other GET request
    		Thread object3 = new Thread(new client("localhost:4567","GET"));
    		object3.start();
        Thread.sleep(500);
        //other GET request
        Thread object4 = new Thread(new client("localhost:4567","GET"));
        object4.start();


   		}catch (Exception e) {
			//throw error
            System.err.println("thread exception:" + e.toString());
		  }
   	}
}

