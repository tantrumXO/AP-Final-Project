package assets;

import java.util.Random;


public class Barricade {
	public String block_path;
	public int length;
	
	public Barricade() {
		Random rand = new Random();
		length = rand.nextInt(3);
		if(length==0) {
			block_path = new String("resources/barricade3.png");	
		}
		else if(length==1) {
			block_path = new String("resources/barricade3.png");	
		}
		else {
			block_path = new String("resources/barricade2.png");	
		}
	}
}
