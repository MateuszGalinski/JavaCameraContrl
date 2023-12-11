package clientpackage;

import java.io.*;
import java.net.*;

public class SocketClient implements AutoCloseable {
    private Socket clientSocket = null;
    private InputStream inputS = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    public byte[] receiveMat() throws IOException {
        int size = input.readInt();
        byte[] byteArray = new byte[size];
        int bytesRead = 0;
        int current = 0;
        while (bytesRead < size) {
            current = inputS.read(byteArray, bytesRead, size - bytesRead);
            bytesRead += current;

            if(current == -1){
                break;
            }
        }
        return byteArray;

    }

    public void sendData(String data){
        try {
            output.writeUTF(data);
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public String receiveData(){
        String data = "";
        try {
            data = input.readUTF();
        }
        catch(IOException i) {
            System.out.println(i);
        }

        return data;
    }

    public SocketClient(String address, int port) throws UnknownHostException, IOException {
        clientSocket = new Socket(address, port);
        System.out.println("Connected");
        inputS = clientSocket.getInputStream();
        input = new DataInputStream(inputS);
        output = new DataOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void close() {
        try {
            input.close();
            output.close();
            clientSocket.close();
            System.out.println("Clossing client...");
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

}
