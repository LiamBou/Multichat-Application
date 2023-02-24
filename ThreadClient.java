import java.io.IOException;

public class ThreadClient extends Thread{
    private SuperSocket superSocket;
    ThreadClient(SuperSocket client) throws IOException {
        this.superSocket = client;
    }
    @Override
    public void run(){
        try{
            while(true) {
                String autrePseudo = superSocket.ins.readUTF();
                String message = superSocket.ins.readUTF();
                Client_V2.textArea.append("[ "+autrePseudo+" ] : "+message+"\n");
                Client_V2.textArea.setCaretPosition(Client_V2.textArea.getDocument().getLength());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
