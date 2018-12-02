package assets;

public class Snake {
	private int length;
	private boolean dead;
	public Snake() {
		this.length = 5;
		this.dead = false;
	}
	public int getlen() {
		return length;
	}
	public void setlen(int i) {
		length+=i;
		if(length<=0) {
			dead = true;
			length =0;
		}
	}
}
