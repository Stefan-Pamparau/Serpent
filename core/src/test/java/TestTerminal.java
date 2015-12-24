public class TestTerminal {
    public static void main(String[] args) {
        Terminal term = new Terminal();
        Thread t = new Thread(term);
        t.start();
        System.out.println(term);
    }
}