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
服务器总线程，处理与所有客户端的通信
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
        setTitle("服务器");
        Dimension location = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) location.getWidth() / 3, (int) location.getHeight() / 3);

        initComponents();
        setResizable(false);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void initComponents() {
        jLabel_state = new JLabel("运行状态");
        jLabel_port = new JLabel("服务器端口");
        jLabel_count = new JLabel("在线人数");
        state = new JLabel("未启动");
        jTextField_port = new JTextField("5000", 10);
        count = new JLabel("0");
        jButton_start = new JButton("启动");
        jTextArea_message = new JTextArea(13, 27);

        //设置文本框只能输入数字
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
                    //建立特定端口的服务器
                    ServerSocket serverSocket = new ServerSocket(port);
//                    ServerSocket serverSocket = new ServerSocket();
//                    serverSocket.bind(new InetSocketAddress(InetAddress.getByName("123.207.85.14"),80));

                    //服务器启动成功
                    /*在这里初始化游戏信息*/
                    gameInfo = new GameInfo(0, 0, game_not_created, new ArrayList<Player>());
                    //新线程监控客户端的连接
                    sst = new ServerConnectThread("服务器线程", serverSocket, MainServer.this);
                    sst.setGameInfo(gameInfo);
                    sst.start();

                    //设置正在游戏中的玩家对象
                    players_gaming = gameInfo.getPlayers_gaming();
                    //设置计时器

                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(getContentPane(), "服务器启动失败，请检查端口");
                    return;
                }

                players = gameInfo.getPlayers();
                state.setText("已启动");
                jTextArea_message.append("服务器启动成功\n");
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
        panel_message.setBorder(BorderFactory.createTitledBorder("运行信息"));

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

        //更新server状态
        int curCount = gameInfo.getCount_playes() + 1;
        gameInfo.setCount_playes(curCount);
        count.setText(curCount + "");
        jTextArea_message.append("用户" + player.name + "登陆了\n");
//
//        //更新所有client状态
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
        //通知所有游戏大厅的按钮更新
    }

    Player getPlayerBySocket(Socket socket) {
        for (Player player : players) {
            if (player.getSocket() == socket) return player;
        }
        return null;
    }

    /*新的游戏窗口被建立，服务器获取用户，更新该用户信息（包括游戏窗口的table显示）
    * 更新游戏总信息（仅是游戏状态），发送给所有用户*/
    void newGameCreated(Socket playerSocket) {

        Player player = getPlayerBySocket(playerSocket);
        player.setPlayer_state(player_not_ready);

        //增加正在游戏中的玩家
        players_gaming.add(player);
//        gameInfo.setCount_games(++curGameCount);
        gameInfo.setGame_state(game_created_and_can_join);

        sendGameStateToAll(game_created_and_can_join);
        //更新一个socket对应#游戏中#用户的信息
        TableAddNewPlayer(playerSocket, player);
    }


    //新玩家加入游戏，先给该玩家table添加所有已存在的玩家信息，在给所有添加新用户
    void newPlayerJoinGame(Socket socket) {
        //添加已有玩家信息
        for (Player player : players_gaming) {
            //增加已存在的玩家
            TableAddNewPlayer(socket, player);//for循环更改player
        }
        //更新用户为游戏用户
        Player player = getPlayerBySocket(socket);
        player.setPlayer_state(player_not_ready);
        players_gaming.add(player);


        //增加新玩家player
        for (Player p : players_gaming) {//for循环更改socket
            Socket pSocket = p.getSocket();
            TableAddNewPlayer(pSocket, player);
        }

    }

    //发送游戏状态给所有客户端
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
        //更新所有玩家table的状态
        Player player = getPlayerBySocket(playerSocket);
        player.game_state = player_ready;
        TableUpdatePlayerState(player);

        if (gameInfo.Count_ready == players_gaming.size()) {
            //所有游戏玩家都已准备，游戏开始，并且不准新玩家加入
            sendGameStateToAll(game_started);
            //生成四个数，（检测可解性），发送数据(给所有游戏中的玩家)，开始计时
            int num1, num2, num3, num4;
            //生成4个有解的数
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

    //玩家退出游戏窗口，更新table，gameinfo
    //客户端会删除玩家的table信息
    void btnExit(Socket socket) {
        //先对playergaming 去除该玩家，然后在playgaming遍历玩家更新table
        Player removePlayer = getPlayerBySocket(socket);
        //更新游戏信息
        removePlayer.setPlayer_state(player_not_inGame);
        players_gaming.remove(removePlayer);

        //更新table
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
        /*发送表达式的解答结果
        更新游戏状态（存在但未开始），用户状态（未准备）
         */
        //通知游戏结束，发送答案
        String message=MyUtils.BuildComm(ser_send_game_end,gameInfo.game_answer);
        for (Player player : players_gaming) {
            Socket socket = player.getSocket();
            try {
                MyUtils.SendMessage(socket, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //设置游戏大厅的加入按钮可用
        sendGameStateToAll(game_created_and_can_join);

        //更新游戏信息
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
        * 检查是否所有玩家都解出了答案，是则更新游戏*/
        //玩家请求增加分数，更新所有玩家的table表
        gameInfo.Count_resolve++;

        Player incScoreplayer = getPlayerBySocket(socket);//增加分数的玩家
        String incScore = calculatePlayerScore(Integer.parseInt(time));//当轮分数
        int orginScore = incScoreplayer.getScore();//历史分数
        int newScore = Integer.parseInt(incScore) + orginScore;
        incScoreplayer.setScore(newScore);

        //更新所有玩家的该增加分数的玩家信息
        String userName=incScoreplayer.getName();
        String userNameAndScore = userName+ " " + newScore;
        String chatMessage=MyUtils.BuildComm(ser_send_chat,"玩家"+userName+"在"+time+"s 时"+"解出了24！");
        for (Player player : players_gaming) {
            //对每一位玩家的table上的某一个玩家设置增加分数
            Socket socket1 = player.getSocket();
            setTablePlayerScore(socket1, userNameAndScore);

            //发送解出题目的消息
            try {
                MyUtils.SendMessage(socket1,chatMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (gameInfo.Count_resolve == players_gaming.size()) {
            //当轮游戏结束，更新倒计时线程，更新游戏窗口面板
            renewTimeThread();
            //更新gameinfo，重置准备人数，解答人数，玩家的游戏状态
            gameOver();
        }
    }

    private void resetGameInfo() {
        gameInfo.Count_ready = 0;//准备人数
        gameInfo.Count_resolve = 0;//解决问题的人数
        gameInfo.setGame_answer(null);
        for (Player player : players_gaming) {
            player.game_state = player_not_ready;//更新每个玩家的状态
        }
    }

    private void renewTimeThread() {
        tt.interrupt();
    }

    String calculatePlayerScore(int time) {
        //百分制
        int score = (int) (time * 1.0 / total_time * 100);
        return score + "";
    }

    //table相关-----begin-------------------------

    //给特定流的玩家更新某玩家的分数
    void setTablePlayerScore(Socket socket, String playerNameAndScore) {

        try {
            MyUtils.SendMessage(socket, MyUtils.BuildComm(ser_table_inc_player_score, playerNameAndScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //对特定socket，给table增加一个新player#行#
    //可以在有玩家加入到游戏时在for语句中调用
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

    //更新所有玩家的table上某用户的状态
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

    //table相关------------end-----------------

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
        //整合名字的对话消息
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
