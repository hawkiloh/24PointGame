package Game.Server;

import Game.GameConstants;

public class TimeThread extends Thread implements GameConstants{
    MainServer server;

    public TimeThread(MainServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        int time=total_time;
        while(time>=0){
            server.sendTimeToAllGaming(time+"");
            try {
                sleep(998);
            } catch (InterruptedException e) {
                this.interrupt();
                return;
            }
            --time;
        }
        server.gameOver();
    }
}
