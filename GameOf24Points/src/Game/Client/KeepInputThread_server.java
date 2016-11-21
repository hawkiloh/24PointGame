/*
package Game.Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class KeepInputThread_server extends Thread{
    Socket socket;
    DataInputStream dis;
    public KeepInputThread_server(Socket socket) {
        this.socket = socket;
        try {
            dis=new DataInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while(true){
//            String string=dis.
        }
    }
}
*/
