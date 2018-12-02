package assets;

import java.io.Serializable;

import javafx.scene.text.Text;

public class GameElements implements Serializable{
	private int snakevalue;
	private boolean has_magnet;
	private boolean has_shield;
	private int points;
	private int[] wall1values;
	private int[] wall2values;
	public GameElements() {
		this.snakevalue = 7;
		this.has_magnet = false;
		this.has_shield = false;
		this.points = 0;
		this.wall1values = new int[8];
		this.wall2values = new int[8];
				
	}
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
	public int getSnakevalue() {
		return snakevalue;
	}
	public void setSnakevalue(int snakevalue) {
		this.snakevalue = snakevalue;
	}
	public boolean isHas_magnet() {
		return has_magnet;
	}
	public void setHas_magnet(boolean has_magnet) {
		this.has_magnet = has_magnet;
	}
	public boolean isHas_shield() {
		return has_shield;
	}
	public void setHas_shield(boolean has_shield) {
		this.has_shield = has_shield;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int[] getWall1values() {
		return wall1values;
	}
	public void setWall1values(int[] wall1values) {
		this.wall1values = wall1values;
	}
	public int[] getWall2values() {
		return wall2values;
	}
	public void setWall2values(int[] wall2values) {
		this.wall2values = wall2values;
	}	
	
}
