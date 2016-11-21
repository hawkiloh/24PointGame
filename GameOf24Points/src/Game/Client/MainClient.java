package Game.Client;

import Game.GameConstants;
import Util.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class MainClient extends JFrame implements GameConstants {
    private JLabel jLabel_userName, jLabel_address, jLabel_port;
    private JTextField userName, address, field_port;
    private JButton jbutton;
    InetAddress inetAddress;
    Socket socket;
    ClientInputThread cit;


    public ClientInputThread getCit() {
        return cit;
    }

    MainClient() {
        setTitle("游戏登陆");
        initComponents();
        setSize(320, 320);
        setVisible(true);
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        jbutton = new JButton("登陆");
        jLabel_userName = new JLabel("用户名");
        jLabel_address = new JLabel("服务器地址");
        jLabel_port = new JLabel("服务器端口");
        userName = new JTextField(10);
        address = new JTextField("192.168.199.222", 10);
        field_port = new JTextField("5000", 10);

        userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    land();
                }
            }
        });

        jbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                land();
            }
        });
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,50,10));
        GridLayout gridLayout = new GridLayout(3, 1, 80, 50);

        JPanel p1 = new JPanel(gridLayout);
        p1.add(jLabel_userName);
        p1.add(jLabel_address);
        p1.add(jLabel_port);

        JPanel p2 = new JPanel(gridLayout);
        p2.add(userName);
        p2.add(address);
        p2.add(field_port);


        jPanel.add(p1);
        jPanel.add(p2);
        add(Box.createGlue());
        add(jPanel);
        jbutton.setAlignmentX(CENTER_ALIGNMENT);
        add(jbutton);
        add(Box.createVerticalStrut(10));

    }

    void land() {
        String username = userName.getText().trim();
        if (username.equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "请输入有效的用户名！");
            return;
        }
        int port = Integer.parseInt(field_port.getText());
        try {

            inetAddress = InetAddress.getByName(address.getText());
            //尝试连接服务器
            System.out.println("请求与服务器连接");
            socket = new Socket(inetAddress, port);
            socket.setTcpNoDelay(true);
            System.out.println("连接成功");
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(getContentPane(), "连接失败，请检查服务器地址和端口！");
            return;
        }
        //连接成功
        //判断名字是否重复
        try {
            MyUtils.SendMessage(socket, MyUtils.BuildComm(client_req_checkNameRepeat, username));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String isNameRepeat = null;
        try {
            //接收的直接是#内容（已换行）
            isNameRepeat = MyUtils.ReceiveMessage(socket);
            Scanner scanner=new Scanner(isNameRepeat);
            isNameRepeat=scanner.nextLine();
            scanner.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }


        if (isNameRepeat.equals(TRUE)) {
            JOptionPane.showMessageDialog(getContentPane(), "用户名已存在！");
        } else {
            //启动新窗口

            GameHall gameHall = new GameHall(socket, username);
            /*
              为了方便 让 客户端的接收输入线程得到游戏窗口对象，在这里新建GameFrame;
              但 gameframe 并没有初始化
             */

            GameFrame gameFrame=new GameFrame(socket);
            gameFrame.setTitle("24点游戏-"+username);
            gameFrame.setUserName(username);

            setVisible(false);
            gameHall.setVisible(true);
            gameHall.setGameFrame(gameFrame);
            gameFrame.setGameHall(gameHall);
            gameFrame.initGameFrame();


            //与服务器的通信线程（不断监控来自服务器的输入）
            cit = new ClientInputThread(socket);
            gameHall.setCit(cit);
//            gameFrame.set
            cit.setGameHall(gameHall);
            cit.setGameFrame(gameFrame);
            cit.start();

            //在游戏大厅建立后，向服务器请求更新内容
            gameHall.renewGameHall();
        }
    }

    public static void main(String[] args) {
        new MainClient();
    }
}
