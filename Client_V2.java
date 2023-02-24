import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;

public class Client_V2 extends JFrame {
    private JFrame frame;
    private JLabel indication;
    private JTextField connection;
    private JButton submit;
    private JTextField zoneDeText;
    protected static JTextArea textArea;
    protected static String pseudo;
    private static SuperSocket superSocket;

    public Client_V2(){
        // Fenêtre de chat
        frame = new JFrame("Chat");
        frame.setLayout(null);
        frame.setSize(713,485);
        // Taille réelle : ~ 700, 450

        indication = new JLabel("Entrer votre pseudo");
        indication.setLocation(250,100);
        frame.add(indication);

        connection = new JTextField("Entrer votre pseudo");
        connection.setBounds(200,225,150,20);
        connection.addActionListener(this::pseudoActionPerformed);
        connection.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                connection.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                connection.setText("Entrer votre pseudo");
            }
        });
        frame.add(connection);

        submit = new JButton("Submit");
        submit.setBounds(350,225,80,20);
        submit.addActionListener(this::pseudoActionPerformed);
        frame.add(submit);

        frame.setVisible(true);
    }

    public void pseudoActionPerformed(ActionEvent e){
        pseudo = connection.getText().toUpperCase();
        try {
            superSocket.outs.writeUTF(pseudo);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        frame.remove(connection);
        frame.remove(submit);

        textArea = new JTextArea();
        textArea.setBounds(0,19,700,381);
        textArea.setBackground(new Color(80, 4, 114)); // #500472
        textArea.setFont(new Font("SERIF", Font.PLAIN,15));
        textArea.setForeground(new Color(255,255,255));
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        scrollPane.setBounds(0,19,700,381);
        frame.add(scrollPane);

        zoneDeText = new JTextField("",50);
        zoneDeText.setBounds(0,400,630,50);
        zoneDeText.addActionListener(this::sendActionPerformed);
        frame.add(zoneDeText);

        JButton send = new JButton("Send");
        send.setBounds(630,400,70,50);
        send.setBackground(new Color(121,203,184));
        send.addActionListener(this::sendActionPerformed);
        frame.add(send);


        JPanel panel = new JPanel();
        panel.setBounds(0,0,700,20);
        panel.setBackground(new Color(121,203,184)); // #79cbb8
        JLabel labelPseudo = new JLabel("Connected as "+pseudo);
        panel.add(labelPseudo);
        frame.add(panel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    superSocket.outs.writeUTF("/q");
                    frame.dispose();
                    System.exit(0);
                } catch (IOException exception){
                    exception.printStackTrace();
                }
            }
        });


        frame.repaint();
        frame.invalidate();
        frame.validate();
    }
    public void sendActionPerformed(ActionEvent e){
        try {
            String message = zoneDeText.getText();
            if (message.equals("/q")){
                superSocket.outs.writeUTF(message);
                frame.dispose();
                System.exit(0);
            }
            superSocket.outs.writeUTF(message);
            zoneDeText.setText("");
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            Socket client = new Socket("localhost", 9999);
            System.out.println("Client connecté");

            superSocket = new SuperSocket(client);
            new Client_V2();

            Thread thread = new ThreadClient(superSocket);
            thread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
