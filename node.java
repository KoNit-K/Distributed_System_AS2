import java.util.*; 

public class node {
    private String info;
    private String author;
    private int lamport;

    //initial
    public node(String name,int lamport,String info) {
        this.info = info;
        this.author = name;
        this.lamport = lamport;
    }

    //set name
    public void setName(String name) {
        author = name;
    }

    //set message
    public void setInfo(String info) {
        this.info = info;
    }

    //set lamport clock
    public void setLamport(int time) {
        lamport = time;
    }

    //return lamport clock value
    public int getLamport() {
        return lamport;
    }

    //return name
    public String getName() {
        return author;
    }

    //return message
    public String getInfo() {
        return info;
    }

}