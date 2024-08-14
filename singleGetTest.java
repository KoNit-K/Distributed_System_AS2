import java.net.*;
import java.io.*;

public class singleGetTest{
   	public static void main(String [] args) {
   		try{
        //send PUT request of ATOM feed 1
	      Thread object = new Thread(new Content("localhost:4567","feed1.txt",0,""));
    		object.start();
    		Thread.sleep(1000);
         //send PUT request of ATOM feed 2
    		Thread object1 = new Thread(new Content("localhost:4567","feed2.txt",0,""));
    		object1.start();
    		Thread.sleep(1000);
         //send GET request 
    		Thread object2 = new Thread(new client("localhost:4567","GET"));
    		object2.start();
   		}catch (Exception e) {
			//throw error
            System.err.println("thread exception:" + e.toString());
		}

   	}
}
