//package Util;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class clienttest {
//    Socket socket;
//
//    public clienttest() throws IOException {
//        socket=new Socket("localhost",6000);
//        System.out.println("connect server successfully");
//        MyUtils.SendMessage(socket,"hello ");
//        MyUtils.SendMessage(socket,"world");
//
//        String message=null;
//        Scanner scanner;
//        while(true){
//            message=MyUtils.ReceiveMessage(socket);
//            scanner=new Scanner(message);
//            while(scanner.hasNext()){
//                System.out.println("oneline:\t"+scanner.nextLine());
//            }
//        }
////        System.out.println(MyUtils.ReceiveMessage(socket));
//
//    }
//
//    public static void main(String[] args) throws IOException {
//        new clienttest();
//    }
//}
