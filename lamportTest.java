public class lamportTest {
    public static void main(String [] args) {
        //test lamport clock
        lamportClock tmp = new lamportClock(0);
        System.out.println(tmp.getNext());
        System.out.println(tmp.getNext());
        System.out.println(tmp.getNext());
        //new lamport clock coming
        System.out.println(tmp.toLocallamport(2));
        System.out.println(tmp.toLocallamport(2));
        System.out.println(tmp.toLocallamport(3));
        System.out.println(tmp.getNext());
        System.out.println(tmp.toLocallamport(10));
        System.out.println(tmp.getNext());
    }
}