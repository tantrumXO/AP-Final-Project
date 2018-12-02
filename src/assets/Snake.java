package assets;

public class Snake {
	private int length;
	private boolean dead;
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public Snake() {
		this.length = 6;
		this.dead = false;
	}
	public int getlen() {
		return length;
	}
	public void setlen(int i) {
		length+=i;
		if(length<=0) {
			dead = true;
			//length =0;
		}
	}
}
