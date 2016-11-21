package Game.Server;

import Game.GameConstants;
import Util.MyUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/*
���ϵؽ����¿ͻ��ˣ�û����һ���ͻ����¿�һ���̴߳�����ÿͻ��˵�ͨ�š�
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

                //�ȴ�����
                System.out.println("�ȴ��ͻ�����");
                Socket socket = serverSocket.accept();
                System.out.println("���ӳɹ�");

                socket.setTcpNoDelay(true);
                String receiveMessage = MyUtils.ReceiveMessage(socket);

                //���ռ�����ֵ���Ϣ
                //string ��Ϊ���ݲ��� ���βΣ���
                Scanner scanner=new Scanner(receiveMessage);
                receiveMessage=scanner.nextLine();//�����ջ���@@
                scanner.close();

                String cm = MyUtils.DeMessage_cm(receiveMessage), userName = MyUtils.DeMessage_cont(receiveMessage);

                if (cm.equals(client_req_checkNameRepeat)) {
                    if (server.isNameRepeat(userName)) {
                        //ֱ�ӷ��ͽ��
                        MyUtils.SendMessage(socket, TRUE);//�����ظ�
                        continue;//����whileѭ��
                    } else {
                        MyUtils.SendMessage(socket, FALSE);

                        // �ͻ��˽��������������߳�  #����# �� �ÿͻ��˵�ͨ��
                        ServerInputThread sit=new ServerInputThread(socket, gameInfo);
                        sit.setServer(server);
                        sit.start();

                        //Ҫ�� ���� client ������Ϸ��Ϣ
                        server.addNewPlayer(new Player(userName, 0, player_not_inGame, socket));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}