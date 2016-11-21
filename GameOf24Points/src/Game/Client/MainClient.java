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
        setTitle("��Ϸ��½");
        initComponents();
        setSize(320, 320);
        setVisible(true);
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        jbutton = new JButton("��½");
        jLabel_userName = new JLabel("�û���");
        jLabel_address = new JLabel("��������ַ");
        jLabel_port = new JLabel("�������˿�");
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
            JOptionPane.showMessageDialog(getContentPane(), "��������Ч���û�����");
            return;
        }
        int port = Integer.parseInt(field_port.getText());
        try {

            inetAddress = InetAddress.getByName(address.getText());
            //�������ӷ�����
            System.out.println("���������������");
            socket = new Socket(inetAddress, port);
            socket.setTcpNoDelay(true);
            System.out.println("���ӳɹ�");
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(getContentPane(), "����ʧ�ܣ������������ַ�Ͷ˿ڣ�");
            return;
        }
        //���ӳɹ�
        //�ж������Ƿ��ظ�
        try {
            MyUtils.SendMessage(socket, MyUtils.BuildComm(client_req_checkNameRepeat, username));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String isNameRepeat = null;
        try {
            //���յ�ֱ����#���ݣ��ѻ��У�
            isNameRepeat = MyUtils.ReceiveMessage(socket);
            Scanner scanner=new Scanner(isNameRepeat);
            isNameRepeat=scanner.nextLine();
            scanner.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }


        if (isNameRepeat.equals(TRUE)) {
            JOptionPane.showMessageDialog(getContentPane(), "�û����Ѵ��ڣ�");
        } else {
            //�����´���

            GameHall gameHall = new GameHall(socket, username);
            /*
              Ϊ�˷��� �� �ͻ��˵Ľ��������̵߳õ���Ϸ���ڶ����������½�GameFrame;
              �� gameframe ��û�г�ʼ��
             */

            GameFrame gameFrame=new GameFrame(socket);
            gameFrame.setTitle("24����Ϸ-"+username);
            gameFrame.setUserName(username);

            setVisible(false);
            gameHall.setVisible(true);
            gameHall.setGameFrame(gameFrame);
            gameFrame.setGameHall(gameHall);
            gameFrame.initGameFrame();


            //���������ͨ���̣߳����ϼ�����Է����������룩
            cit = new ClientInputThread(socket);
            gameHall.setCit(cit);
//            gameFrame.set
            cit.setGameHall(gameHall);
            cit.setGameFrame(gameFrame);
            cit.start();

            //����Ϸ����������������������������
            gameHall.renewGameHall();
        }
    }

    public static void main(String[] args) {
        new MainClient();
    }
}
