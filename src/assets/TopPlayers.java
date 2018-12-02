package assets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TopPlayers implements Serializable{
	
	private static final long serialVersionUID = 11L;
	ArrayList<Player> players;
	Player lastplayer;
	/**
	 * Constructor
	 */
	public TopPlayers() {
		lastplayer = new Player();
		players = new ArrayList<Player>();
	}
	/**
	 * Adds a new player to the list
	 * @param p
	 */
	public void addplayer(Player p) {
		players.add(p);
		Collections.sort(players,new PlayerComp());
		while(players.size()>10) { 
			players.remove(10);
		}
	}
	/**
	 * returns the list
	 * @return
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	/**
	 * get the last Player
	 * @return
	 */
	public Player getLastplayer() {
		return lastplayer;
	}
	
	/**
	 * sets last player
	 * @param lastplayer
	 */
	public void setLastplayer(Player lastplayer) {
		this.lastplayer = lastplayer;
	}
	
}
/**
 * a class for using Comparators
 * @author shrey
 *
 */
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
