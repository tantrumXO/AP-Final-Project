package assets;

import java.io.Serializable;

import javafx.scene.text.Text;

/**
 * @author shrey
 *
 */
public class GameElements implements Serializable{
	
	private int snakevalue;
	private boolean has_magnet;
	private boolean has_shield;
	private int points;
	private int[] wall1values;
	private int[] wall2values;
	/**
	 *  default constructor of Game elements
	 */
	public GameElements() {
		this.snakevalue = 7;
		this.has_magnet = false;
		this.has_shield = false;
		this.points = 0;
		this.wall1values = new int[8];
		this.wall2values = new int[8];
				
	}
	/**
	 * Constructor of GameElements class 
	 * @param snakevalue contains length of snake
	 * @param has_magnet checks if the a player has magnet powerup activated
	 * @param has_shield checks if the a player has shield powerup activated
	 * @param points total points of player
	 * @param wall1values values in wall1
	 * @param wall2values values in wall2
	 */
	public GameElements(int snakevalue, boolean has_magnet, boolean has_shield, int points, Text[] wall1values,
			Text[] wall2values) {
		this.snakevalue = snakevalue;
		this.has_magnet = has_magnet;
		this.has_shield = has_shield;
		this.points = points;
		this.wall1values = new int[8];
		this.wall2values = new int[8];
		for(int i=0;i<8;i++) {
			this.wall1values[i] = Integer.parseInt(wall1values[i].getText());
			this.wall1values[i] = Integer.parseInt(wall1values[i].getText());	
		}
	}
	
	@Override
	public String toString() {
		return "GameElements [snakevalue=" + snakevalue + ", has_magnet=" + has_magnet + ", has_shield=" + has_shield
				+ ", points=" + points + "]";
	}
	/**
	 * 
	 * @return returns snake length
	 */
	public int getSnakevalue() {
		return snakevalue;
	}
	/**
	 * 
	 * @param snakevalue sets snake length
	 */
	public void setSnakevalue(int snakevalue) {
		this.snakevalue = snakevalue;
	}
	/**
	 *  returns has_magnet
	 * @return
	 */
	public boolean isHas_magnet() {
		return has_magnet;
	}
	/**
	 * settter for has magnet
	 * @param has_magnet
	 */
	public void setHas_magnet(boolean has_magnet) {
		this.has_magnet = has_magnet;
	}
	/**
	 * getter for has_shield
	 * @return
	 */
	public boolean isHas_shield() {
		return has_shield;
	}
	/**
	 *  setter for has shield
	 * @param has_shield
	 */
	public void setHas_shield(boolean has_shield) {
		this.has_shield = has_shield;
	}
	/**
	 * returns total points
	 * @return
	 */
	public int getPoints() {
		return points;
	}
	/**
	 * sets points value
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	/**
	 * getter for wall1 values
	 * @return
	 */
	public int[] getWall1values() {
		return wall1values;
	}
	/**
	 * setter for wall1
	 * @param wall1values
	 */
	public void setWall1values(int[] wall1values) {
		this.wall1values = wall1values;
	}
	/**
	 * returns wall2 values
	 * @return
	 */
	public int[] getWall2values() {
		return wall2values;
	}
	/**
	 * sets wall2 values
	 * @param wall2values
	 */
	public void setWall2values(int[] wall2values) {
		this.wall2values = wall2values;
	}	
	
}
