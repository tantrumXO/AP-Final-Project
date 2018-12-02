package assets;

import java.io.Serializable;
import java.util.Date;

public class Player implements Serializable{
	
	private static final long serialVersionUID = 12L;
	private String Name;
	private int Score;
	private Date date;
	public Player() {
		Name = "John";
		Score =0;
		date = getDate();
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
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

	
	public String toString() {
		return this.Name + " " + this.Score + " " + this.date;
	}
	
}
