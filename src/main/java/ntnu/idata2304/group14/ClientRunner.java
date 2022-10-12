package ntnu.idata2304.group14;

public class ClientRunner {
    private static final String SERVER_ADDRESS = "129.241.152.12";
    private static final int SERVER_PORT= 1234;

    public static void main(String[] args) {
        UDPclient udPclient = new UDPclient();
        udPclient.run("task", SERVER_ADDRESS, SERVER_PORT);
    }
}
