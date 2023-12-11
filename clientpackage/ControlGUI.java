package clientpackage;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.UnknownHostException;

public class ControlGUI extends JFrame {
    private SocketClient socketClient;
    private JLabel cameraView;

    public ControlGUI() {
        setTitle("Camera App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");

        cameraView = new JLabel();
        cameraView.setHorizontalAlignment(SwingConstants.CENTER);
        add(cameraView, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        buttonsPanel.add(leftButton);
        buttonsPanel.add(rightButton);
        add(buttonsPanel, BorderLayout.SOUTH);

        leftButton.addActionListener(e -> socketClient.sendData("RotationA"));
        rightButton.addActionListener(e -> socketClient.sendData("RotationB"));
        try{
            socketClient = new SocketClient("192.168.100.27", 5000);
            new Thread(this::receiveImages).start();
        }
        catch(UnknownHostException e){
            System.out.println(e);
        }
        catch(IOException u){
            System.out.println(u);
        }
    }

    private void receiveImages() {
        while (true) {
            try{
                ImageIcon imageIcon = new ImageIcon(socketClient.receiveMat());
                cameraView.setIcon(imageIcon);
            } 
            catch (IOException e) {
                System.out.println("Disconnected from host");
                socketClient.close();
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlGUI app = new ControlGUI();
            app.setSize(800, 600);
            app.setVisible(true);
        });
    }
}
