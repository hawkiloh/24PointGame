package Game.Server;

import java.net.Socket;

public class Player {
    public String name;
    public int score;
    public String game_state;
    Socket socket;

    public Player(String name, int score, String game_state, Socket socket) {
        this.name = name;
        this.score = score;
        this.game_state = game_state;

        this.socket=socket;//用于服务器访问玩家
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlayer_state() {
        return game_state;
    }

    public void setPlayer_state(String game_state) {
        this.game_state = game_state;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


}
