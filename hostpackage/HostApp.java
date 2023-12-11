package hostpackage;

import java.io.*;
import java.util.*;

class SendThread extends Thread{
    private final HostApp appToRunFor;
    private final SocketServer server;

    public SendThread(HostApp hostApp, SocketServer hostServer){
        appToRunFor = hostApp;
        server = hostServer;
    }


    public void run() {
        CameraGrabber Grabber = new CameraGrabber();
        
        while(appToRunFor.checkIfRunning()){
            try{
                server.sendMat(Grabber.getFrame());
            }
            catch(IOException e){
                appToRunFor.stopRunning();
                Grabber.close();
                break;
            }
        }

        Grabber.close();
    }
}

class ReceiveThread extends Thread{
    private final HostApp appToRunFor;
    private final SocketServer server;
    private final CameraRotator rotator;

    public ReceiveThread(HostApp hostApp, SocketServer hostServer, CameraRotator hostRotator){
        appToRunFor = hostApp;
        server = hostServer;
        rotator = hostRotator;
    }

    public void run() {
        String data = "";

        while(appToRunFor.checkIfRunning()){
            try{
                data = server.receiveData();
                if(data.equals("RotationA")){
                    rotator.rotateDirectionA();
                }
                else if(data.equals("RotationB")){
                    rotator.rotateDirectionB();
                }
            }
            catch(IOException e){
                appToRunFor.stopRunning();
                break;
            }
            catch(InterruptedException e){
                appToRunFor.stopRunning();
                break;
            }

            System.out.println(data);
        }
    }
}

public class HostApp {
    private final SendThread sendThread;
    private final ReceiveThread receiveThread;
    private SocketServer host;
    private CameraRotator rotator;
    private boolean isRunning;

    public synchronized boolean checkIfRunning(){
        return isRunning;
    }

    public synchronized void stopRunning(){
        isRunning = false;
    }

    public HostApp(){
        isRunning = true;
        host = new SocketServer(5000); 
        rotator = new CameraRotator();
        sendThread = new SendThread(this, host);
        receiveThread = new ReceiveThread(this, host, rotator);
    }

    public static void main(String[] args){
        HostApp app = new HostApp();

        app.sendThread.start();
        app.receiveThread.start();

        while(app.checkIfRunning()){}

        app.host.close();
    }
}