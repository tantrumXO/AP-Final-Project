package assets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard implements Serializable{
	
	private static final long serialVersionUID = 11L;
	ArrayList<Player> players;
	public Leaderboard() {
		players = new ArrayList<Player>();
	}
	
	public void addplayer(Player p) {
		players.add(p);
		Collections.sort(players,new PlayerComp());
		while(players.size()>10) { 
			players.remove(10);
		}
	}
	
}

class PlayerComp implements Comparator<Player>{
	 
    @Override
    public int compare(Player e1, Player e2) {
        if(e1.getScore() < e2.getScore()){
            return 1;
        } else {
            return -1;
        }
    }

}
