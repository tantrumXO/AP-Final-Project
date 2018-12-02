package assets;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Player implements Serializable{
	
	private static final long serialVersionUID = 12L;
	private String Name;
	private int Score;
	private LocalDateTime date;
	/**
	 * Constructor
	 */
	public Player() {
		Name = "John";
		Score =0;
		date = null;
	}
	
	
	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}


	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getScore() {
		return Score;
	}
	public void setScore(int score) {
		Score = score;
	}

	/**
	 * The to string method
	 */
	public String toString() {
		return this.Name + " " + this.Score + " " + this.date;
	}
	
}
