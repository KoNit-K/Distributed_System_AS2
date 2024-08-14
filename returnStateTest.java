import java.net.*;
import java.io.*;

public class returnStateTest{
   	public static void main(String [] args) {
   		try{
        //send a new ATOM feed 
	      Thread object = new Thread(new Content("localhost:4567","feed1.txt",0,""));
    		object.start();
        Thread.sleep(1000);
        //send a duplicate ATOM feed
        Thread object1 = new Thread(new Content("localhost:4567","feed1.txt",0,""));
        object1.start();
        Thread.sleep(1000);
        //Sending no content to the server
        Thread object2 = new Thread(new Content("localhost:4567","feed1.txt",1,""));
        object2.start();
        Thread.sleep(1000);
        //send a  request other than GET or PUT 
        Thread object3 = new Thread(new client("localhost:4567","qwe"));
        object3.start();
        Thread.sleep(1000);
        //send a ATOM XML which does not make sense
        Thread object4 = new Thread(new Content("localhost:4567","feed1.txt",1,"qwe"));
        object4.start();
   		}catch (Exception e) {
			  //throw error
        System.err.println("thread exception:" + e.toString());
		  }

   	}
}
