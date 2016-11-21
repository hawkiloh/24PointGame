package Game.Server;

import Game.GameConstants;
import Util.MyUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
该服务器的线程与客户端一 一对应，实现独立通信
该线程在游戏大厅线程建立后启动。
该线程能响应游戏大厅的点击事件，建立与游戏窗口的通信
 */
public class ServerInputThread extends Thread implements GameConstants{
    Socket socket;
    GameInfo gameInfo;
    MainServer server;

    public ServerInputThread(Socket socket, GameInfo gameInfo) {
        this.socket = socket;
        this.gameInfo = gameInfo;
    }

    public void setServer(MainServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while(true){
            String message=null;
            try {
                //debug
                System.out.println("server waiting for input");
                message= MyUtils.ReceiveMessage(socket);
                System.out.println(message);
                System.out.println("receive successfully");

                Scanner scanner=new Scanner(message);
                while (scanner.hasNext()){
                    cope(scanner.nextLine());
                }
            } catch (IOException e) {
                System.out.println("与客户端的连接断开");
                JOptionPane.showMessageDialog(null,"有玩家退出了游戏，程序将关闭");
                System.exit(1);
                break;
            }
        }
    }

    private void cope(String message) {
        String cm=MyUtils.DeMessage_cm(message);
        String cont=MyUtils.DeMessage_cont(message);
        //处理收到的指令
        switch (cm){
            case client_req_renew_hall:
                try {
                    MyUtils.SendMessage(socket,MyUtils.BuildComm(ser_send_playerCount,gameInfo.getCount_playes()+""));
                    MyUtils.SendMessage(socket,MyUtils.BuildComm(ser_send_gameState,gameInfo.getGame_state()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case client_btn_create:
                server.newGameCreated(socket);
                break;
            case client_btn_join:
                server.newPlayerJoinGame(socket);
                break;
            case client_btn_ready:
                System.out.println("ready");
                server.btnReady(socket);
                System.out.println("ok");
                break;
            case client_btn_exit:
                server.btnExit(socket);
                break;
            case client_btn_send_chat:
                server.sendChatToAllGaming(socket,cont);
                break;
            case client_req_inc_score:
                server.incPlayerScore(socket,cont);
                break;
        }
    }
}
