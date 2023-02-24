import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurProjet {
    protected static ArrayList<SuperSocket> superSocketsList = new ArrayList<>();

    public static void main(String[] args) {
        try{
            ServeurProjet serveur = new ServeurProjet();
            ServerSocket ss = new ServerSocket(9999);
            System.out.println("Serveur créé");
            while (true){
                Socket socket = ss.accept();
                SuperSocket superSocket = new SuperSocket(socket);
                superSocketsList.add(superSocket);
                Thread thread = new ThreadServeur(serveur, superSocket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAll(String pseudo, String message) throws IOException {
        for (SuperSocket superSocket: superSocketsList) {
            superSocket.outs.writeUTF(pseudo);
            superSocket.outs.writeUTF(message);
        }
    }

    public void connectionMessage(SuperSocket superSocket, String pseudo) throws IOException {
        for (SuperSocket superSock : superSocketsList){
            if(!(superSock.equals(superSocket))){
                superSock.outs.writeUTF(pseudo);
                superSock.outs.writeUTF(" CONNECTED");
            }
        }
        System.out.println(pseudo + " CONNECTED");
    }
    public void closeClient(SuperSocket client, String pseudo) throws IOException {
        superSocketsList.remove(client);
        for (SuperSocket superSocket: superSocketsList){
            superSocket.outs.writeUTF(pseudo);
            superSocket.outs.writeUTF(" DISCONNECTED");
        }
        System.out.println(pseudo + " DISCONNECTED");
    }
}