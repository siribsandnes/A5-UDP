package ntnu.idata2304.group14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPclient {
    public void run(String initMessage, String serverAdress, int serverPort){
        try {
            // Send a datagram with the message to the server
            byte[] dataToSend = initMessage.getBytes();
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverAdress);
            DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, serverAddress,
                    serverPort);
            clientSocket.send(sendPacket);

            // Wait for a response from the server
            byte[] responseDataBuffer = new byte[1024]; // Reserve a bit more space than one would normally need
            DatagramPacket receivePacket = new DatagramPacket(responseDataBuffer, responseDataBuffer.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

            // Release all the resources allocated for the socket - the conversation is done
            // Note: there is no "real closing" of the socket at the networking level, because no connection was
            // ever established. The .close() is more for releasing the memory which is not needed anymore
            clientSocket.close();

            System.out.println("Response from the server: " + modifiedSentence);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
