package Game.Client;

import Game.GameConstants;
import Util.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class GameHall extends JFrame implements GameConstants {
    ClientInputThread cit;
    GameFrame gameFrame;

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void setCit(ClientInputThread cit) {
        this.cit = cit;
    }

    private Socket socket;

    private JLabel jLabel_welcome,jLabel_count,count,jLabel_state,state;
    private JButton jbutton_create,jbutton_join;

    public void setPlayersCount(String count) {
        this.count.setText(count);
    }

    public void setGameState(String state) {
        this.state.setText(state);
    }

    public void setJbutton_create(boolean bool) {
        this.jbutton_create.setEnabled(bool);
    }

    public void setJbutton_join(boolean bool) {
        this.jbutton_join.setEnabled(bool);
    }

    private String username="";

    private void initGameHall(){
        jLabel_welcome=new JLabel("��ӭ "+username+" ��ң�ף����Ϸ��죡");
        jLabel_count=new JLabel("��Ϸ��������");
        count=new JLabel("0");
        jLabel_state=new JLabel("��Ϸ״̬");
        state=new JLabel("-------");
        jbutton_create=new JButton("�����µ���Ϸ");
        jbutton_join=new JButton("�����Ѵ��ڵ���Ϸ");

        jbutton_create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(getWidth()+" "+getHeight());

                createEvent();
            }
        });
        jbutton_join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(getWidth()+" "+getHeight());
                joinEvent();

            }
        });

        FlowLayout flowLayout=new FlowLayout(FlowLayout.CENTER,20,20);
        JPanel p1=new JPanel();
        p1.add(jLabel_welcome);
        p1.setOpaque(false);

        JPanel p2=new JPanel(flowLayout);
        p2.add(jLabel_count);
        p2.add(count);
        p2.setOpaque(false);


        JPanel p3=new JPanel(flowLayout);
        p3.add(jLabel_state);
        p3.add(state);
        p3.setOpaque(false);



        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        jLabel_welcome.setAlignmentX(CENTER_ALIGNMENT);
        jbutton_create.setAlignmentX(CENTER_ALIGNMENT);
        jbutton_join.setAlignmentX(CENTER_ALIGNMENT);

        add(jLabel_welcome);
        add(p2);
        add(p3);
        add(jbutton_create);
        add(Box.createGlue());
        add(jbutton_join);
        add(Box.createGlue());

//        getContentPane().setBackground(Color.green);

    }

    private void joinEvent() {
        //��ʾ��Ϸ����

        setVisible(false);
        gameFrame.setVisible(true);
        try {
            MyUtils.SendMessage(socket,client_btn_join);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createEvent() {
        //��ʾ��Ϸ����
        setVisible(false);
        gameFrame.setVisible(true);
        /*����ʽ�����������������Ϸ��table��*
        ������ͨ������ť�¼�������table
         */
//        gameFrame.renewGameFrame();
        try {
            MyUtils.SendMessage(socket,client_btn_create);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public GameHall(Socket socket, String username){
        setTitle("��Ϸ����-"+username);
        this.username=username;
        initGameHall();
        setSize(360,260);
//        pack();
//        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.socket=socket;

    }

    public void renewGameHall(){
        //���������Ϸ������Ϣ
        try {
            /**
             * ֱ�ӷ���ָ����������ݣ�ָ��з��������ض������߳��յ���
             ������������Ϣ�����̵߳������̡߳�
             */
            //debug
            System.out.println("��ʼ���͸���hall����");
            MyUtils.SendMessage(socket,client_req_renew_hall);
            System.out.println("���͸���hall����ɹ�");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        GameHall gameHall=new GameHall(null, null);
        gameHall.setVisible(true);
    }

}
