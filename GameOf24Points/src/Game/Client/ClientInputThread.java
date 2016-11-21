package Game.Client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import Game.*;
import Util.MyUtils;

import javax.swing.*;

/*
不断接受服务器的信息。
 */
public class ClientInputThread extends Thread implements GameConstants {
    Socket socket;
    GameHall gameHall;
    GameFrame gameFrame;
//    MainClient client;

    public GameFrame getGameFrame() {
        return gameFrame;
    }

//    public void setClient(MainClient client) {
//        this.client = client;
//    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setGameHall(GameHall gameHall) {
        this.gameHall = gameHall;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public ClientInputThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println("client waiting for input");
                String message = MyUtils.ReceiveMessage(socket);
                System.out.println(message);
                System.out.println("receive successfully");

                Scanner scanner=new Scanner(message);
                while(scanner.hasNext()){
                    cope(scanner.nextLine());
                }
                scanner.close();

            } catch (IOException e) {
                //程序退出
                System.out.println("与服务器的连接断开");
                JOptionPane.showMessageDialog(null,"与服务器失去连接，程序将退出！");
                System.exit(1);
                break;

            }


        }
    }

    private void cope(String message) {
        String cm = MyUtils.DeMessage_cm(message);
        String cont = MyUtils.DeMessage_cont(message);
        //获取多个参数
        ArrayList<String> arrayList=MyUtils.DeMessge_strings(cont);
        switch (cm){
            case ser_send_gameState:
                gameHall.setGameState(cont);
                if(cont.equals(game_not_created)){
                    gameHall.setJbutton_create(true);
                    gameHall.setJbutton_join(false);
                }else if(cont.equals(game_created_and_can_join)){
                    gameHall.setJbutton_create(false);
                    gameHall.setJbutton_join(true);
                }else if(cont.equals(game_started)){
                    gameHall.setJbutton_create(false);
                    gameHall.setJbutton_join(false);
                }
                break;
            case ser_send_playerCount:
                gameHall.setPlayersCount(cont);
                break;
            case ser_send_4num:
                gameFrame.set4Num(arrayList.get(0),arrayList.get(1),arrayList.get(2),arrayList.get(3));
                break;
            case ser_send_time:
                gameFrame.setTime(cont);
                break;
            case ser_send_chat:
                gameFrame.appendText(cont);
                break;
            case ser_table_incr_player:
                gameFrame.increasePlayer(arrayList.get(0),arrayList.get(1),arrayList.get(2));
                break;
            case ser_table_remv_player:
                gameFrame.removePlayer(cont);
                break;
            case ser_table_inc_player_score:
                gameFrame.setTablePlayerScore(arrayList.get(0),arrayList.get(1));
                break;
            case ser_send_game_end:
                //游戏结束，重置用户的状态和按钮（准备、时间）
                gameFrame.gameOver(cont);
                break;
            case ser_table_set_player_state:
                gameFrame.updatePlayerState(arrayList.get(0),arrayList.get(1));
                break;

        }
    }
}
