package Util;

import Game.GameConstants;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public final class MyUtils implements GameConstants {
    public static final Dimension GetParentSize(){
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }

    public static final  void SendMessage(Socket socket, String messge) throws IOException {
//         OutputStream os=socket.getOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        pw.println(messge);
        pw.flush();
    }

    public static final  String ReceiveMessage(Socket socket) throws IOException {
//        InputStream is=socket.getInputStream();

        DataInputStream dis=new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        byte[] bytes=new byte[512];//0.5kb
        int length=dis.read(bytes);
        String message=new String(bytes,0,length);


//        System.out.println(message);

        return message;

    }

    public static final String BuildComm(String cm, String content) {
        return cm + SEP + content;
    }

    public static final String DeMessage_cm(String message) {
        int index = message.indexOf(SEP);
        //仅仅是指令或内容，没有分隔符
        if(index==-1)return message;
        return message.substring(0, index);
    }

    public static final String DeMessage_cont(String message) {
        int index = message.indexOf(SEP);

        if(index==-1)return message;
        return message.substring(index + 1);
    }
    public static final ArrayList<String> DeMessge_strings(String message){
        Scanner scanner=new Scanner(message);
        ArrayList<String> strings=new ArrayList<>();
        while (scanner.hasNext()){
            strings.add(scanner.next());
        }
        return strings;
    }
    public static final String CalExpression(String exp){


        return FALSE;
    }

}
