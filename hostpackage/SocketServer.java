package hostpackage;
import java.io.*;
import java.net.*;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import hostpackage.CameraGrabber;

public class SocketServer implements AutoCloseable {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    public void sendMat(Mat frame) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, matOfByte);
        byte[] byteArray = matOfByte.toArray();


        output.writeInt(byteArray.length);

        output.write(byteArray, 0, byteArray.length);
        output.flush();
    }
    
    public String receiveData() throws IOException{
        String data = "";
        data = input.readUTF();

        return data;
    }

    public SocketServer(int port)
    {

        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
 
            socket = server.accept();
            System.out.println("Client accepted");
 
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }
    
    @Override
    public void close(){
        try {
            System.out.println("Closing connection");
            socket.close();
            input.close();

        }
        catch(IOException i) {
            System.out.println(i);
        }
    }


}
