package assets;

import java.io.Serializable;
import java.util.Random;

public class Wall implements Serializable {
	private static final long serialVersionUID = 7L;
	public String[] block_path;
	public boolean[] block_appear;
	private double y;
	public Wall() {
		y=0;
		block_path = new String[8];
		block_appear = new boolean[8];
		
		block_path[0] = "resources/block1.png";
		block_path[1] = "resources/block2.png";
		block_path[2] = "resources/block3.png";
		block_path[3] = "resources/block4.png";
		block_path[4] = "resources/block5.png";
		block_path[5] = "resources/block6.png";
		block_path[6] = "resources/block7.png";
		block_path[7] = "resources/block8.png";
		
		for(int i=0; i<8; i++) {
			int prob = generateRandom(1,10);
			if(prob<=7) {
				block_appear[i] = true;
			}
			else {
				block_appear[i] = false;
			}
		}
	}
	public double gety() {
		return y;
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