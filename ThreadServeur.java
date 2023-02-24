import java.io.IOException;

public class ThreadServeur extends Thread{
    private ServeurProjet serveur;
    private SuperSocket superSocket;
    protected String pseudo;

    ThreadServeur(ServeurProjet serveur, SuperSocket superSocket) throws IOException {
        this.serveur = serveur;
        this.superSocket = superSocket;
    }
    @Override
    public void run(){
        try {
            pseudo = superSocket.ins.readUTF();
            serveur.connectionMessage(superSocket,pseudo);
            while(true) {
                String message = superSocket.ins.readUTF();
                if(message.equals("/q")){
                    serveur.closeClient(superSocket,pseudo);
                    return;
                }
                else
                    serveur.sendAll(pseudo, message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
