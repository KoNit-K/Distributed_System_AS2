import java.net.*;
import java.io.*;

public class timeoutTest{
   	public static void main(String [] args) {
   		try{
   			//send a normal PUT request and check the result in server side
	      	Thread object = new Thread(new Content("localhost:4567","feed1.txt",0,""));
    		object.start();
   		}catch (Exception e) {
			//throw error
            System.err.println("thread exception:" + e.toString());
		}

   	}
}

