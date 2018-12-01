package assets;

import java.io.Serializable;
import java.util.Random;


public class Barricade implements Serializable  {
	private static final long serialVersionUID = 6L;
	public String block_path;
	public boolean block_appear;
	private double x;
	private double y;
	
	public Barricade() {
		this.x=0;
		this.y=0;
		Random rand = new Random();
		int xx = rand.nextInt(3);
		//System.out.println(x);
		if(xx==0) {
			block_path = new String("resources/barricade3.png");	
		}
		else if(xx==2) {
			block_path = new String("resources/barricade3.png");	
		}
		else {
			block_path = new String("resources/barricade2.png");	
		}
				
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
