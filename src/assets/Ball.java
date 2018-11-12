package assets;

import java.util.Random;


public class Ball {
	public String block_path;
	public boolean block_appear;
	
	public Ball() {
	
		block_path = new String("resources/ball.png");			
		
			int prob = generateRandom(1,100);
			if(prob<=90) {
				block_appear = true;
			}
			else {
				block_appear = false;
			}
		
	}
		
	private int generateRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max-min)+1)+min;
		return randomNum;
	}
}
