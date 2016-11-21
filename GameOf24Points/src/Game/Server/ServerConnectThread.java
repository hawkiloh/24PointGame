package Game.Server;

import Game.GameConstants;
import Util.MyUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/*
不断地接受新客户端，没接受一个客户端新开一个线程处理与该客户端的通信。
 */

public class ServerConnectThread extends Thread implements GameConstants {
    private ServerSocket serverSocket;
    MainServer server;
    GameInfo gameInfo;
    Player player;

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ServerConnectThread(String name, ServerSocket serverSocket, MainServer server) {
        super(name);
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {

                //等待连接
                System.out.println("等待客户连接");
                Socket socket = serverSocket.accept();
                System.out.println("连接成功");

                socket.setTcpNoDelay(true);
                String receiveMessage = MyUtils.ReceiveMessage(socket);

                //接收检查名字的消息
                //string 作为传递参数 是形参！！
                Scanner scanner=new Scanner(receiveMessage);
                receiveMessage=scanner.nextLine();//不接收换行@@
                scanner.close();

                String cm = MyUtils.DeMessage_cm(receiveMessage), userName = MyUtils.DeMessage_cont(receiveMessage);

                if (cm.equals(client_req_checkNameRepeat)) {
                    if (server.isNameRepeat(userName)) {
                        //直接发送结果
                        MyUtils.SendMessage(socket, TRUE);//名字重复
                        continue;//继续while循环
                    } else {
                        MyUtils.SendMessage(socket, FALSE);

                        // 客户端将启动，启动新线程  #接收# 与 该客户端的通信
                        ServerInputThread sit=new ServerInputThread(socket, gameInfo);
                        sit.setServer(server);
                        sit.start();

                        //要给 所有 client 更新游戏信息
                        server.addNewPlayer(new Player(userName, 0, player_not_inGame, socket));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}