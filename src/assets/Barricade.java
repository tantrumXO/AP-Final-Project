package assets;

import java.util.Random;


public class Barricade {
	public String block_path;
	public boolean block_appear;
	
	public Barricade() {
		Random rand = new Random();
		int x = rand.nextInt(3);
		//System.out.println(x);
		if(x==0) {
			block_path = new String("resources/barricade3.png");	
		}
		else if(x==2) {
			block_path = new String("resources/barricade.png");	
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
		
	private int generateRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max-min)+1)+min;
		return randomNum;
	}
}
