package assets;

import java.io.Serializable;
import java.util.Random;


public class Barricade implements Serializable  {
	private static final long serialVersionUID = 6L;
	public String block_path;
	public boolean block_appear;
	private double x0;
	private double y0;
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private double x3;
	private double y3;
	private double x4;
	private double y4;
	private double x5;
	private double y5;
	private double x6;
	private double y6;
	private double x7;
	private double y7;
	private double x8;
	private double y8;
	private double x9;
	private double y9;
	
	
	public Barricade() {
		
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
	
	public double getX0() {
		return x0;
	}

	public void setX0(double x0) {
		this.x0 = x0;
	}

	public double getY0() {
		return y0;
	}

	public void setY0(double y0) {
		this.y0 = y0;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getY2() {
		return y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

	public double getX3() {
		return x3;
	}

	public void setX3(double x3) {
		this.x3 = x3;
	}

	public double getY3() {
		return y3;
	}

	public void setY3(double y3) {
		this.y3 = y3;
	}

	public double getX4() {
		return x4;
	}

	public void setX4(double x4) {
		this.x4 = x4;
	}

	public double getY4() {
		return y4;
	}

	public void setY4(double y4) {
		this.y4 = y4;
	}

	public double getX5() {
		return x5;
	}

	public void setX5(double x5) {
		this.x5 = x5;
	}

	public double getY5() {
		return y5;
	}

	public void setY5(double y5) {
		this.y5 = y5;
	}

	public double getX6() {
		return x6;
	}

	public void setX6(double x6) {
		this.x6 = x6;
	}

	public double getY6() {
		return y6;
	}

	public void setY6(double y6) {
		this.y6 = y6;
	}

	public double getX7() {
		return x7;
	}

	public void setX7(double x7) {
		this.x7 = x7;
	}

	public double getY7() {
		return y7;
	}

	public void setY7(double y7) {
		this.y7 = y7;
	}

	public double getX8() {
		return x8;
	}

	public void setX8(double x8) {
		this.x8 = x8;
	}

	public double getY8() {
		return y8;
	}

	public void setY8(double y8) {
		this.y8 = y8;
	}

	public double getX9() {
		return x9;
	}

	public void setX9(double x9) {
		this.x9 = x9;
	}

	public double getY9() {
		return y9;
	}

	public void setY9(double y9) {
		this.y9 = y9;
	}

	private int generateRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max-min)+1)+min;
		return randomNum;
	}
}
