package Game.Client;

import Game.GameConstants;
import Util.CalExpress;
import Util.MyUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;

public class GameFrame extends JFrame implements GameConstants, ActionListener, KeyListener {
    Socket socket;
    GameHall gameHall;

    String userName;
    PlainDocument document = new PlainDocument();
    SimpleAttributeSet sas = new SimpleAttributeSet();
    JButton jButton_com;
    JPanel p1, p2;
    JTextArea jTextArea;
    JButton jButton_time, jButton_timeshow;
    JTextField jTextField_cal, jTextField_com;
    DefaultTableModel tableModel = new DefaultTableModel(null, new String[]{"用户", "分数", "状态"});
    JTable table = new JTable(tableModel);
    Caret caret;
    ImageIcon icon = new ImageIcon(this.getClass().getResource("/img/bgp7.jpg"));
    private boolean isGaming = false;
    private JButton jButton_1, jButton_2, jButton_3, jButton_4, jButton_add, jButton_sub, jButton_mul, jButton_div;
    private JButton jButton_left, jButton_right, jButton_clear, jButton_delete, jButton_cal, jButton_ready, jButton_exit;
//    ImageIcon icon = new ImageIcon("img/bgp7.jpg");


    public GameFrame(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame(null);
        gameFrame.initGameFrame();
        gameFrame.setVisible(true);
    }

    void initGameFrame() {
        Font font_big = new Font(Font.SERIF, Font.BOLD, 80);
        Font font_mid = new Font(null, Font.CENTER_BASELINE, 30);
        Font font_small = new Font(null, Font.BOLD, 20);

        jButton_1 = new JButton("-");
        jButton_2 = new JButton("-");
        jButton_3 = new JButton("-");
        jButton_4 = new JButton("-");

        jButton_add = new JButton("+");
        jButton_sub = new JButton("-");
        jButton_mul = new JButton("*");
        jButton_div = new JButton("/");
        jButton_left = new JButton("(");
        jButton_right = new JButton(")");

        jButton_clear = new JButton("clear");
        jButton_delete = new JButton("delete");
        jButton_cal = new JButton("calculate");
        jButton_ready = new JButton("准备");
        jButton_exit = new JButton("退出");

        jButton_time = new JButton("倒计时");
        jButton_timeshow = new JButton(total_time + "");

        jButton_com = new JButton("send");
        jTextArea = new JTextArea(10, 25);
        jTextField_cal = new JTextField(20);
        jTextField_com = new JTextField(15);

        jButton_1.setFont(font_big);
        jButton_2.setFont(font_big);
        jButton_3.setFont(font_big);
        jButton_4.setFont(font_big);

        jButton_add.setFont(font_small);
        jButton_sub.setFont(font_small);
        jButton_mul.setFont(font_small);
        jButton_div.setFont(font_small);
        jButton_left.setFont(font_small);
        jButton_right.setFont(font_small);
        jButton_delete.setFont(font_small);
        jButton_clear.setFont(font_small);
        jButton_cal.setFont(font_small);

        jTextField_cal.setFont(font_mid);
//        jTextField_com.setFont(font_small);
//        jButton_com.setFont(font_small);

        jTextField_cal.setMaximumSize(new Dimension(400, 0));
        jTextField_com.setMaximumSize(new Dimension(200, 0));

        jTextField_cal.setDocument(document);
        caret = jTextField_cal.getCaret();
        caret.setVisible(true);


        //事件注册
        jButton_1.addActionListener(this);
        jButton_2.addActionListener(this);
        jButton_3.addActionListener(this);
        jButton_4.addActionListener(this);

        jButton_ready.addActionListener(this);
        jButton_exit.addActionListener(this);

        jButton_add.addActionListener(this);
        jButton_sub.addActionListener(this);
        jButton_mul.addActionListener(this);
        jButton_div.addActionListener(this);
        jButton_left.addActionListener(this);
        jButton_right.addActionListener(this);

        jButton_clear.addActionListener(this);
        jButton_delete.addActionListener(this);
        jButton_cal.addActionListener(this);

        jButton_com.addActionListener(this);

        jTextField_cal.addKeyListener(this);
        jTextField_com.addKeyListener(this);

        p1 = new JPanel(new BorderLayout());

        Component v = Box.createHorizontalBox();
        Box box_time = Box.createHorizontalBox();
        box_time.add(v);
        box_time.add(jButton_time);
        box_time.add(Box.createHorizontalStrut(20));
        box_time.add(jButton_timeshow);
        box_time.add(v);
        p1.add(box_time, BorderLayout.NORTH);

        JPanel p1_cen = new JPanel();
        p1_cen.setLayout(new BoxLayout(p1_cen, BoxLayout.Y_AXIS));
        Box box_card = Box.createHorizontalBox();
        box_card.add(Box.createHorizontalStrut(80));
        box_card.add(jButton_1);
        box_card.add(Box.createHorizontalStrut(50));
        box_card.add(jButton_2);
        box_card.add(Box.createHorizontalStrut(50));
        box_card.add(jButton_3);
        box_card.add(Box.createHorizontalStrut(50));
        box_card.add(jButton_4);
        box_card.add(Box.createHorizontalStrut(80));

        JPanel p1_op = new JPanel();
        p1_op.setOpaque(false);
        p1_op.add(jButton_ready);
        p1_op.add(jButton_exit);

//        JPanel p1_b1=new JPanel();
        Box p1_b1 = Box.createHorizontalBox();
        p1_b1.add(jButton_add);
        p1_b1.add(Box.createHorizontalStrut(20));
        p1_b1.add(jButton_sub);
        p1_b1.add(Box.createHorizontalStrut(20));
        p1_b1.add(jButton_mul);
        p1_b1.add(Box.createHorizontalStrut(20));
        p1_b1.add(jButton_div);
        p1_b1.add(Box.createHorizontalStrut(20));
        p1_b1.add(jButton_left);
        p1_b1.add(Box.createHorizontalStrut(20));
        p1_b1.add(jButton_right);

//        JPanel p1_b2=new JPanel();
        Box p1_b2 = Box.createHorizontalBox();
        p1_b2.add(jButton_clear);
        p1_b2.add(Box.createHorizontalStrut(20));
        p1_b2.add(jButton_delete);
        p1_b2.add(Box.createHorizontalStrut(20));
        p1_b2.add(jButton_cal);

        p1_cen.add(Box.createVerticalStrut(30));
        p1_cen.add(box_card);
        p1_cen.add(Box.createVerticalStrut(30));
        p1_cen.add(p1_op);
        p1_cen.add(Box.createVerticalStrut(20));
        p1_cen.add(jTextField_cal);
        p1_cen.add(Box.createVerticalStrut(5));

        p1_cen.add(p1_b1);
        p1_cen.add(p1_b2);
        p1_cen.add(Box.createVerticalGlue());
        p1_cen.add(Box.createVerticalStrut(50));

        p1.add(p1_cen, BorderLayout.CENTER);

        JPanel p1_south = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p1_south.setOpaque(false);
        JLabel jLabel_userName = new JLabel("玩家：\t" + userName);
        p1_south.add(jLabel_userName);
        p1.add(p1_south, BorderLayout.SOUTH);

        p2 = new JPanel();
//        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.setLayout(new BorderLayout(50, 50));

        table.setPreferredScrollableViewportSize(new Dimension(180, 130));
//        table.setSize(100, 100);

//        Box box_table=Box.createHorizontalBox();
//        box_table.add(Box.createHorizontalStrut(80));
//        JScrollPane jpTable=new JScrollPane(table);
//        box_table.add(jpTable);
//        box_table.add(Box.createHorizontalStrut(20));

        Box box_table = Box.createVerticalBox();
        box_table.add(Box.createVerticalStrut(10));
        box_table.add(new JScrollPane(table));

        JPanel p2_text = new JPanel();
        p2_text.setOpaque(false);
        p2_text.add(jTextField_com);
        p2_text.add(jButton_com);

        Box box2_south = Box.createVerticalBox();
        box2_south.add(new JScrollPane(jTextArea));
        box2_south.add(p2_text);

        Box box21 = Box.createHorizontalBox();
        Box box22 = Box.createHorizontalBox();

        box21.add(box_table);
        box21.add(Box.createHorizontalStrut(20));

        box22.add(box2_south);
        box22.add(Box.createHorizontalStrut(20));

        p2.add(box21, BorderLayout.NORTH);
        p2.add(box22, BorderLayout.SOUTH);


        add(p1, BorderLayout.CENTER);
        add(p2, BorderLayout.EAST);


        p1_cen.setOpaque(false);

        setlook();


    }

    private void setlook() {
//        p1.setBorder(BorderFactory.createLoweredSoftBevelBorder());
//        p2.setBorder(BorderFactory.createRaisedBevelBorder());
//        pack();
//        setVisible(true);
//        setMinimumSize(new Dimension(800, 725));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

//        setSize(1366, 768);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        p2.setOpaque(false);
        p1.setOpaque(false);

        ((JPanel) getContentPane()).setOpaque(false);

        Dimension screenSize = MyUtils.GetParentSize();

        //限制界面大小
//        Image temp = icon.getImage().getScaledInstance(1366, 768,
//                icon.getImage().SCALE_DEFAULT);

        Image temp = icon.getImage().getScaledInstance(screenSize.width, screenSize.height,
                icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        JLabel lp = new JLabel(icon);
//        lp.setBounds(0, 0, 1366, 768);
        lp.setBounds(0, 0, screenSize.width, screenSize.height);
        getLayeredPane().add(lp, new Integer(Integer.MIN_VALUE));


        table.setEnabled(false);

//        Color color = new Color(195,188,160);
//        Color color =Color.lightGray;
//        Container c = table.getParent();
//        if (c instanceof JViewport) {
//            JViewport jp = (JViewport) c;
//            jp.setBackground(color);
//        }
//        jTextArea.setBackground(color);
        setResizable(false);

        setUndecorated(true);

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //    void renewGameFrame(){
//        try {
//            MyUtils.SendMessage(socket, client_req_renew_hall);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void setGameHall(GameHall gameHall) {
        this.gameHall = gameHall;
    }

    void set4Num(String num1, String num2, String num3, String num4) {
        jButton_1.setText(num1);
        jButton_2.setText(num2);
        jButton_3.setText(num3);
        jButton_4.setText(num4);

        //设置游戏状态为开始
        isGaming = true;
        setTableState(player_gaming);
    }

    void appendText(String text) {
        jTextArea.append(text + "\n");
    }

    void increasePlayer(String username, String score, String playerState) {
        tableModel.addRow(new String[]{username, score, playerState});
    }

    void removePlayer(String username) {
        tableModel.removeRow(SearchUser(username));
    }

    //设置table上的特定玩家的分数
    void setTablePlayerScore(String username, String score) {
        int userIndex = SearchUser(username);

        tableModel.setValueAt(score, userIndex, 1);

    }

    //更新特定玩家的游戏状态
    void updatePlayerState(String username, String playerState) {
        int userIndex = SearchUser(username);
        tableModel.setValueAt(playerState, userIndex, 2);
    }


    int SearchUser(String userName) {
        int rows = tableModel.getRowCount();
        for (int i = 0; i < rows; i++) {
            String name = (String) tableModel.getValueAt(i, 0);
            if (name.equals(userName)) {
                return i;
            }
        }
        return -1;
    }

    //重设用户状态，设准备按钮可用
    void gameOver(String answer) {
        jButton_ready.setEnabled(true);
        jButton_exit.setEnabled(true);
        jButton_timeshow.setText(total_time + "");
        isGaming = false;
        appendText("游戏结束，系统的答案是:  " + answer);
        //改变一列的用户状态
        setTableState(player_not_ready);
    }

    //改变一列的用户状态
    private void setTableState(String playerState) {
        int rows = tableModel.getRowCount();
        for (int i = 0; i < rows; i++) {
            tableModel.setValueAt(playerState, i, 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //处理四个按钮
        Object source = e.getSource();
        Caret caret = jTextField_cal.getCaret();


        //处理发送聊天的文本框
        if (source == jButton_com) {
            try {
                MyUtils.SendMessage(socket, MyUtils.BuildComm(client_btn_send_chat, jTextField_com.getText()));
                jTextField_com.setText("");
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        //处理计算表达式的文本框
        try {
            if (source == jButton_1) {
                caret.setVisible(true);
                document.insertString(caret.getDot(), jButton_1.getText(), sas);

            } else if (source == jButton_2) {
                caret.setVisible(true);
                document.insertString(caret.getDot(), jButton_2.getText(), sas);
            } else if (source == jButton_3) {
                caret.setVisible(true);
                document.insertString(caret.getDot(), jButton_3.getText(), sas);
            } else if (source == jButton_4) {
                caret.setVisible(true);
                document.insertString(caret.getDot(), jButton_4.getText(), sas);
            } else {
                String actionCommand = e.getActionCommand();

                switch (actionCommand) {
                    case "+":
                        document.insertString(caret.getDot(), "+", sas);
                        break;
                    case "-":
                        document.insertString(caret.getDot(), "-", sas);
                        break;
                    case "*":
                        document.insertString(caret.getDot(), "*", sas);
                        break;
                    case "/":
                        document.insertString(caret.getDot(), "/", sas);
                        break;
                    case "(":
                        document.insertString(caret.getDot(), "(", sas);
                        break;
                    case ")":
                        document.insertString(caret.getDot(), ")", sas);
                        break;
                    case "clear":
                        jTextField_cal.setText("");
                        break;
                    case "delete":
                        int caretIndex = caret.getDot();
                        String text = jTextField_cal.getText();
                        if (caretIndex == 0) return;
                        String text1 = text.substring(0, caretIndex - 1);
                        String text2 = text.substring(caretIndex);
                        jTextField_cal.setText(text1 + text2);
                        caret.setDot(caretIndex - 1);
                        caret.setVisible(true);
                        break;
                    case "calculate":
                        calculate();

                        break;
                    case "准备":
                        MyUtils.SendMessage(socket, client_btn_ready);
                        jButton_ready.setEnabled(false);
                        jButton_exit.setEnabled(false);
                        break;
                    case "退出":
                        //清除table
                        setVisible(false);
                        gameHall.setVisible(true);
                        while (tableModel.getRowCount() > 0) {
                            tableModel.removeRow(0);
                        }

                        MyUtils.SendMessage(socket, client_btn_exit);

                        //移除table上的信息

                        break;
                }

            }

        } catch (BadLocationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void calculate() {
         /*计算表达式
         * 1.表达式格式不正确
            2.计算结果不是24
           * 3.计算结果是24*/

        try {
            int result = CalExpress.calculate(jTextField_cal.getText());
            if (result == div_error) {
                JOptionPane.showMessageDialog(this, "表达式中存在整除问题，请检查表达式", "error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (result == 24) {
                //仅当游戏中时发送增加分数的命令
                if (isGaming) MyUtils.SendMessage(socket, MyUtils.BuildComm(client_req_inc_score, getTime()));
                JOptionPane.showMessageDialog(this, "恭喜你成功解出了24！");

            } else JOptionPane.showMessageDialog(this, "表达式计算结果不是24，请检查表达式！");
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(this, "表达式计算发生错误，请检查表达式！", "error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTime() {
        return jButton_timeshow.getText();
    }

    void setTime(String time) {
        jButton_timeshow.setText(time);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextField_cal.isFocusOwner()) {
                calculate();
            } else if (jTextField_com.isFocusOwner()) {
                try {
                    MyUtils.SendMessage(socket, MyUtils.BuildComm(client_btn_send_chat, jTextField_com.getText()));
                    jTextField_com.setText("");
                    return;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
