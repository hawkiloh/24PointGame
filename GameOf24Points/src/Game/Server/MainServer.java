package Game.Server;

import Game.GameConstants;
import Util.Cal24Points;
import Util.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
���������̣߳����������пͻ��˵�ͨ��
 */
public class MainServer extends JFrame implements GameConstants {
    GameInfo gameInfo;
    ArrayList<Player> players;
    ArrayList<Player> players_gaming;
    TimeThread tt;

    private JLabel jLabel_state, jLabel_port, jLabel_count, state, count;
    private JTextField jTextField_port;
    ServerConnectThread sst;

    private JButton jButton_start;
    public JTextArea jTextArea_message;

    private FlowLayout flowLayout;

    private JScrollPane jScrollPane;

    public MainServer() {
        setTitle("������");
        Dimension location = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) location.getWidth() / 3, (int) location.getHeight() / 3);

        initComponents();
        setResizable(false);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void initComponents() {
        jLabel_state = new JLabel("����״̬");
        jLabel_port = new JLabel("�������˿�");
        jLabel_count = new JLabel("��������");
        state = new JLabel("δ����");
        jTextField_port = new JTextField("5000", 10);
        count = new JLabel("0");
        jButton_start = new JButton("����");
        jTextArea_message = new JTextArea(13, 27);

        //�����ı���ֻ����������
        jTextField_port.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if (keyChar < KeyEvent.VK_0 || keyChar > KeyEvent.VK_9) {
                    e.consume();
                }
            }
        });

        jButton_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(getWidth()+" "+getHeight());
//                System.out.println(jTextField_port.getHeight());
                int port = Integer.parseInt(jTextField_port.getText());
                try {
                    //�����ض��˿ڵķ�����
                    ServerSocket serverSocket = new ServerSocket(port);
//                    ServerSocket serverSocket = new ServerSocket();
//                    serverSocket.bind(new InetSocketAddress(InetAddress.getByName("123.207.85.14"),80));

                    //�����������ɹ�
                    /*�������ʼ����Ϸ��Ϣ*/
                    gameInfo = new GameInfo(0, 0, game_not_created, new ArrayList<Player>());
                    //���̼߳�ؿͻ��˵�����
                    sst = new ServerConnectThread("�������߳�", serverSocket, MainServer.this);
                    sst.setGameInfo(gameInfo);
                    sst.start();

                    //����������Ϸ�е���Ҷ���
                    players_gaming = gameInfo.getPlayers_gaming();
                    //���ü�ʱ��

                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(getContentPane(), "����������ʧ�ܣ�����˿�");
                    return;
                }

                players = gameInfo.getPlayers();
                state.setText("������");
                jTextArea_message.append("�����������ɹ�\n");
                jButton_start.setEnabled(false);

            }
        });

        JPanel panel_state, panel_port, panel_count, panel_message;

        panel_state = new JPanel();
        panel_state.setLayout(new BoxLayout(panel_state, BoxLayout.X_AXIS));
        panel_state.add(jLabel_state);
        panel_state.add(Box.createHorizontalStrut(40));
        panel_state.add(state);

        panel_port = new JPanel();
        panel_port.setLayout(new BoxLayout(panel_port, BoxLayout.X_AXIS));
        panel_port.add(jLabel_port);
        panel_port.add(Box.createHorizontalStrut(28));
        panel_port.add(jTextField_port);
        panel_port.add(Box.createHorizontalStrut(40));
        panel_port.add(jButton_start);
        panel_port.add(Box.createHorizontalStrut(40));

        panel_count = new JPanel();
        panel_count.setLayout(new BoxLayout(panel_count, BoxLayout.X_AXIS));
        panel_count.add(jLabel_count);
        panel_count.add(Box.createHorizontalStrut(40));
        panel_count.add(count);

        panel_message = new JPanel();
        panel_message.setLayout(new BoxLayout(panel_message, BoxLayout.X_AXIS));
        panel_message.add(new JScrollPane(jTextArea_message));
        panel_message.setBorder(BorderFactory.createTitledBorder("������Ϣ"));

        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        panel_state.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_count.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_port.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_message.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(panel_state);
        add(panel_count);
        add(panel_port);

        add(panel_message);
        add(Box.createVerticalStrut(20));
    }

    boolean isNameRepeat(String username) {
        for (Player player : gameInfo.getPlayers()) {
            if (player.name.equals(username)) {
                return true;
            }
        }
        return false;
    }

    void addNewPlayer(Player player) {
        players.add(player);

        //����server״̬
        int curCount = gameInfo.getCount_playes() + 1;
        gameInfo.setCount_playes(curCount);
        count.setText(curCount + "");
        jTextArea_message.append("�û�" + player.name + "��½��\n");
//
//        //��������client״̬
//        String count_players = curCount + "";
//        String game_state = gameInfo.getGame_state();
//        for (Player p : players) {
//            Socket socket = p.getSocket();
//            try {
//                MyUtils.SendMessage(socket, MyUtils.BuildComm(ser_send_playerCount, count_players));
//                MyUtils.SendMessage(socket, MyUtils.BuildComm(ser_send_gameState, game_state));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }


    }

    void gameStart(Socket socket) {
        Player player = getPlayerBySocket(socket);
        player.setPlayer_state(player_not_ready);
        //֪ͨ������Ϸ�����İ�ť����
    }

    Player getPlayerBySocket(Socket socket) {
        for (Player player : players) {
            if (player.getSocket() == socket) return player;
        }
        return null;
    }

    /*�µ���Ϸ���ڱ���������������ȡ�û������¸��û���Ϣ��������Ϸ���ڵ�table��ʾ��
    * ������Ϸ����Ϣ��������Ϸ״̬�������͸������û�*/
    void newGameCreated(Socket playerSocket) {

        Player player = getPlayerBySocket(playerSocket);
        player.setPlayer_state(player_not_ready);

        //����������Ϸ�е����
        players_gaming.add(player);
//        gameInfo.setCount_games(++curGameCount);
        gameInfo.setGame_state(game_created_and_can_join);

        sendGameStateToAll(game_created_and_can_join);
        //����һ��socket��Ӧ#��Ϸ��#�û�����Ϣ
        TableAddNewPlayer(playerSocket, player);
    }


    //����Ҽ�����Ϸ���ȸ������table��������Ѵ��ڵ������Ϣ���ڸ�����������û�
    void newPlayerJoinGame(Socket socket) {
        //������������Ϣ
        for (Player player : players_gaming) {
            //�����Ѵ��ڵ����
            TableAddNewPlayer(socket, player);//forѭ������player
        }
        //�����û�Ϊ��Ϸ�û�
        Player player = getPlayerBySocket(socket);
        player.setPlayer_state(player_not_ready);
        players_gaming.add(player);


        //���������player
        for (Player p : players_gaming) {//forѭ������socket
            Socket pSocket = p.getSocket();
            TableAddNewPlayer(pSocket, player);
        }

    }

    //������Ϸ״̬�����пͻ���
    void sendGameStateToAll(String gameState) {
        String message = MyUtils.BuildComm(ser_send_gameState, gameState);
        for (Player player : players) {
            Socket socket = player.getSocket();
            try {
                MyUtils.SendMessage(socket, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void UpdateAllHall() {

    }


    void btnReady(Socket playerSocket) {
        gameInfo.Count_ready++;
        //�����������table��״̬
        Player player = getPlayerBySocket(playerSocket);
        player.game_state = player_ready;
        TableUpdatePlayerState(player);

        if (gameInfo.Count_ready == players_gaming.size()) {
            //������Ϸ��Ҷ���׼������Ϸ��ʼ�����Ҳ�׼����Ҽ���
            sendGameStateToAll(game_started);
            //�����ĸ����������ɽ��ԣ�����������(��������Ϸ�е����)����ʼ��ʱ
            int num1, num2, num3, num4;
            //����4���н����
            while (true) {
                num1 = genRandomNum();
                num2 = genRandomNum();
                num3 = genRandomNum();
                num4 = genRandomNum();
                String gen4Nums = Cal24Points.isCanWorkOut24(num1, num2, num3, num4);

                if (!gen4Nums.equals(FALSE)) {
                    jTextArea_message.append(gen4Nums + "\n");
                    gameInfo.setGame_answer(gen4Nums);
                    break;
                }
            }
            send4numToAllGaming(num1 + "", num2 + "", num3 + "", num4 + "");
            tt = new TimeThread(MainServer.this);
            tt.start();
        }
    }

    //����˳���Ϸ���ڣ�����table��gameinfo
    //�ͻ��˻�ɾ����ҵ�table��Ϣ
    void btnExit(Socket socket) {
        //�ȶ�playergaming ȥ������ң�Ȼ����playgaming������Ҹ���table
        Player removePlayer = getPlayerBySocket(socket);
        //������Ϸ��Ϣ
        removePlayer.setPlayer_state(player_not_inGame);
        players_gaming.remove(removePlayer);

        //����table
        String userName=removePlayer.getName();
        for (Player p:players_gaming) {
            Socket pSocket=p.getSocket();
            try {
                MyUtils.SendMessage(pSocket,MyUtils.BuildComm(ser_table_remv_player,userName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    void gameOver() {
        /*���ͱ��ʽ�Ľ����
        ������Ϸ״̬�����ڵ�δ��ʼ�����û�״̬��δ׼����
         */
        //֪ͨ��Ϸ���������ʹ�
        String message=MyUtils.BuildComm(ser_send_game_end,gameInfo.game_answer);
        for (Player player : players_gaming) {
            Socket socket = player.getSocket();
            try {
                MyUtils.SendMessage(socket, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //������Ϸ�����ļ��밴ť����
        sendGameStateToAll(game_created_and_can_join);

        //������Ϸ��Ϣ
        resetGameInfo();

    }
//    void setTableAllPlayerState(String userState){
//        for(Player player:players_gaming){
//            Socket socket=player.getSocket();
//            try {
//                MyUtils.SendMessage(socket,MyUtils.BuildComm(ser_table_set_player_state,userState));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    void incPlayerScore(Socket socket, String time) {
        /*
        * ����Ƿ�������Ҷ�����˴𰸣����������Ϸ*/
        //����������ӷ���������������ҵ�table��
        gameInfo.Count_resolve++;

        Player incScoreplayer = getPlayerBySocket(socket);//���ӷ��������
        String incScore = calculatePlayerScore(Integer.parseInt(time));//���ַ���
        int orginScore = incScoreplayer.getScore();//��ʷ����
        int newScore = Integer.parseInt(incScore) + orginScore;
        incScoreplayer.setScore(newScore);

        //����������ҵĸ����ӷ����������Ϣ
        String userName=incScoreplayer.getName();
        String userNameAndScore = userName+ " " + newScore;
        String chatMessage=MyUtils.BuildComm(ser_send_chat,"���"+userName+"��"+time+"s ʱ"+"�����24��");
        for (Player player : players_gaming) {
            //��ÿһλ��ҵ�table�ϵ�ĳһ������������ӷ���
            Socket socket1 = player.getSocket();
            setTablePlayerScore(socket1, userNameAndScore);

            //���ͽ����Ŀ����Ϣ
            try {
                MyUtils.SendMessage(socket1,chatMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (gameInfo.Count_resolve == players_gaming.size()) {
            //������Ϸ���������µ���ʱ�̣߳�������Ϸ�������
            renewTimeThread();
            //����gameinfo������׼�������������������ҵ���Ϸ״̬
            gameOver();
        }
    }

    private void resetGameInfo() {
        gameInfo.Count_ready = 0;//׼������
        gameInfo.Count_resolve = 0;//������������
        gameInfo.setGame_answer(null);
        for (Player player : players_gaming) {
            player.game_state = player_not_ready;//����ÿ����ҵ�״̬
        }
    }

    private void renewTimeThread() {
        tt.interrupt();
    }

    String calculatePlayerScore(int time) {
        //�ٷ���
        int score = (int) (time * 1.0 / total_time * 100);
        return score + "";
    }

    //table���-----begin-------------------------

    //���ض�������Ҹ���ĳ��ҵķ���
    void setTablePlayerScore(Socket socket, String playerNameAndScore) {

        try {
            MyUtils.SendMessage(socket, MyUtils.BuildComm(ser_table_inc_player_score, playerNameAndScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //���ض�socket����table����һ����player#��#
    //����������Ҽ��뵽��Ϸʱ��for����е���
    void TableAddNewPlayer(Socket socket, Player player) {
        try {
            MyUtils.SendMessage(socket, MyUtils.BuildComm(ser_table_incr_player, player.getName() + " " + player.getScore() + " " +
                    player.getPlayer_state()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void TableUpdatePlayer(Player player) {

    }

    //����������ҵ�table��ĳ�û���״̬
    void TableUpdatePlayerState(Player player) {
        String nameAndState = player.getName() + " " + player.getPlayer_state();
        try {
            for (Player p : players_gaming) {
                Socket socket = p.getSocket();
                MyUtils.SendMessage(socket, MyUtils.BuildComm(ser_table_set_player_state, nameAndState));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //table���------------end-----------------

    void send4numToAllGaming(String num1, String num2, String num3, String num4) {
        for (Player player : players_gaming) {
            Socket socket = player.getSocket();
            try {
                MyUtils.SendMessage(socket, MyUtils.BuildComm(
                        GameConstants.ser_send_4num, num1 + " " + num2 + " " + num3 + " " + num4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendTimeToAllGaming(String time) {
        for (Player player : players_gaming) {
            Socket socket = player.getSocket();
            try {
                MyUtils.SendMessage(socket, MyUtils.BuildComm(ser_send_time, time));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    int genRandomNum() {
        while (true) {
            int num = (int) (Math.random() * 14);
            if (num != 0) return num;
        }
    }


    void sendChatToAllGaming(Socket socket, String content) {
        Player player = getPlayerBySocket(socket);
        //�������ֵĶԻ���Ϣ
        String message = player.getName() + ":  " + content;
        for (Player p : players_gaming) {
            Socket pSocket = p.getSocket();
            try {
                MyUtils.SendMessage(pSocket, MyUtils.BuildComm(ser_send_chat, message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        new MainServer();
    }


}
