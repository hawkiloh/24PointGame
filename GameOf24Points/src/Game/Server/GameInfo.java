package Game.Server;

import java.util.ArrayList;

/*
ֻ����һ����Ϸ

��Ϸ��Ϣ�������������Ϸ������1������Ϸ״̬����Ϸ�Ĵ𰸡�����б�������Ϸ������б���Ϸ׼������������Ϸ���������
 */
public class GameInfo {
    int Count_playes;
//    int Count_games;
    String game_state;
    String game_answer;
    ArrayList<Player> players;
    //������Ϸ�е�����б�
    ArrayList<Player> players_gaming=new ArrayList<>();
    int Count_ready=0;
    int Count_resolve=0;

    public GameInfo(int count_playes, int count_games, String game_state, ArrayList<Player> players) {
        Count_playes = count_playes;
//        Count_games = count_games;
        this.game_state = game_state;
        this.players = players;
    }

    public void setGame_answer(String game_answer) {
        this.game_answer = game_answer;
    }

    public ArrayList<Player> getPlayers_gaming() {
        return players_gaming;
    }

    public int getCount_playes() {
        return Count_playes;
    }

    public void setCount_playes(int count_playes) {
        Count_playes = count_playes;
    }

//    public int getCount_games() {
//        return Count_games;
//    }
//
//    public void setCount_games(int count_games) {
//        Count_games = count_games;
//    }

    public String getGame_state() {
        return game_state;
    }

    public void setGame_state(String game_state) {
        this.game_state = game_state;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
