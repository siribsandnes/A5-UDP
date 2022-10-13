package ntnu.idata2304.group14;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPclient {

    public void run(String initMessage, String serverAdress, int serverPort){
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            sendToServer(clientSocket, initMessage, serverAdress, serverPort); // Sends message "task" to server
            String sentenceFromServer = getFromServer(clientSocket); // Gets sentence from server
            String processedSentence = processServerResponse(sentenceFromServer); //Process the sentence from server
            sendToServer(clientSocket, processedSentence, serverAdress, serverPort); //Sends sentence type and amount of words to server
            String responseFromServer = getFromServer(clientSocket); //Gets response from server, either ok or error


            System.out.println("Sentence from server: " + sentenceFromServer);
            System.out.println("Sentence type and length: " + processedSentence);
            System.out.println("Response from server: " + responseFromServer);


            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToServer(DatagramSocket socket, String message, String serverAdress, int serverPort ){

       try {
           byte[] messageInBytes = message.getBytes();
           InetAddress serverAddress = InetAddress.getByName(serverAdress);
           DatagramPacket sendPacket = new DatagramPacket(messageInBytes, messageInBytes.length, serverAddress,
                   serverPort);
           socket.send(sendPacket);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    private String getFromServer(DatagramSocket socket){
       try {
           byte[] responseDataBuffer = new byte[1024];
           DatagramPacket receivePacket = new DatagramPacket(responseDataBuffer, responseDataBuffer.length);
           socket.receive(receivePacket);
           String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
           return modifiedSentence;

       }catch (IOException e){
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
        int amountOfWords = words.length;

        if(amountOfWords == 1){
            amountOfWords = 0;
        }
        String wordCount = Integer.toString(amountOfWords);
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
