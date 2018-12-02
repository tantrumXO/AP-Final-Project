package assets;

import java.io.Serializable;
import java.util.Random;


public class Shield implements Serializable {
	private static final long serialVersionUID = 2L;
	public String block_path;
	public boolean block_appear;
	private double x;
	private double y;
	public Shield() {
		x=0;
		y=0;
		block_path = new String("resources/shield.png");			
		
			int prob = generateRandom(1,100);
			if(prob<=90) {
				block_appear = true;
			}
			else {
				block_appear = false;
			} 	
	}
	public double getx() {
		return x;
	}
	public double gety() {
		return y;
	}
	public void setx(double d) {
		x = d;
	}
	public void sety(double a) {
		y = a;
	}
	private int generateRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max-min)+1)+min;
		return randomNum;
	}
}