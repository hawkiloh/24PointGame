//package Util;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class servertest {
//    ServerSocket serverSocket=new ServerSocket(6000);
//
//    public servertest() throws IOException {
//        while(true){
//
//            System.out.println("waiting for connection");
//            Socket socket=serverSocket.accept();
//            System.out.println("connect ok");
//            MyUtils.SendMessage(socket,"from ");
//            MyUtils.SendMessage(socket,"server");
//
//            String message=null;
//            Scanner scanner;
//            while(true){
//                message=MyUtils.ReceiveMessage(socket);
//                scanner=new Scanner(message);
//                while(scanner.hasNext()){
//                    System.out.println("oneline:\t"+scanner.nextLine());
//                }
//            }
////            System.out.println(MyUtils.ReceiveMessage(socket));
//        }
//
//    }
//
//    public static void main(String[] args) throws IOException {
//        new servertest();
//    }
//}
