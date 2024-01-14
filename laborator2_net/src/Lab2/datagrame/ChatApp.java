package Lab2.datagrame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

public class ChatApp {
    private static final int PORT = 4445;
    private static final int MAX_PACKET_SIZE = 1024;

    private DatagramSocket socket;
    private InetAddress address;

    private JTextArea messageArea;
    private JTextField messageField;

    public ChatApp() {
        try {
            socket = new DatagramSocket(PORT);
            address = InetAddress.getByName("localhost");
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        JFrame frame = createFrame();

        messageArea = createMessageArea();
        messageField = createMessageField();
        JButton sendButton = createSendButton();

        JPanel bottomPanel = createBottomPanel();

        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        startReceiveThread();

        frame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Chat App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        return frame;
    }

    private JTextArea createMessageArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        return area;
    }

    private JTextField createMessageField() {
        JTextField field = new JTextField();
        field.addActionListener(e -> sendMessage(field.getText()));
        return field;
    }

    private JButton createSendButton() {
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage(messageField.getText()));
        return sendButton;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(createSendButton(), BorderLayout.EAST);
        return bottomPanel;
    }

    private void startReceiveThread() {
        Thread receiveThread = new Thread(() -> {
            while (true) {
                byte[] buffer = new byte[MAX_PACKET_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    messageArea.append("Received: " + message + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        receiveThread.start();
    }

    private void sendMessage(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);

        try {
            socket.send(packet);
            messageArea.append("Sent: " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatApp();
    }
}
