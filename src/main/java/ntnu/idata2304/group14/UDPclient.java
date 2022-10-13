package ntnu.idata2304.group14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPclient {
    public void run(String initMessage, String serverAdress, int serverPort){
        try {
            // Sends message to server
            byte[] initMessageBytes = initMessage.getBytes();
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverAdress);
            DatagramPacket sendPacket = new DatagramPacket(initMessageBytes, initMessageBytes.length, serverAddress,
                    serverPort);
            clientSocket.send(sendPacket);

            //waits for response
            byte[] responseDataBuffer = new byte[1024]; // Reserve a bit more space than one would normally need
            DatagramPacket receivePacket = new DatagramPacket(responseDataBuffer, responseDataBuffer.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println("Response from server: " + modifiedSentence);
            System.out.println( processServerResponse(modifiedSentence));

            //
            byte[] answerToSensor = processServerResponse(modifiedSentence).getBytes();
            DatagramPacket sendPacket2 = new DatagramPacket(answerToSensor, answerToSensor.length, serverAddress,serverPort);
            clientSocket.send(sendPacket2);

            byte[] responseDataBuffer2 = new byte[1024];
            DatagramPacket receivePacket2 = new DatagramPacket(responseDataBuffer2,responseDataBuffer2.length);
            clientSocket.receive(receivePacket2);
            String modifiedReply = new String(receivePacket2.getData(), 0, receivePacket2.getLength());

            System.out.println("Second response from server: " + modifiedReply);



            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String processServerResponse(String serverResponse){
        String wordCount;
        String typeOfSentence;

        wordCount = checkWordCount(serverResponse);
        typeOfSentence = getSentenceType(serverResponse);

        return typeOfSentence + " " + wordCount;
    }

    private String checkWordCount(String sentence){
        String [] words = sentence.split(" ");
        String wordCount = Integer.toString(words.length);
        return wordCount;
    }

    private String getSentenceType(String sentence){
        String lastCharacter = sentence.substring(sentence.length() -1);
        if(lastCharacter.equals("?")){
            return "question";
        }else{
            return "statement";
        }
    }

}
