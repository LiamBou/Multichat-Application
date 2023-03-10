import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SuperSocket {
    protected Socket socket;
    protected DataOutputStream outs;
    protected DataInputStream ins;

    SuperSocket(Socket socket){
        this.socket = socket;
        try {
            this.outs = new DataOutputStream(socket.getOutputStream());
            this.ins = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
