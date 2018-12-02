package assets;

public interface Control_Function {
	public void createSnake();
	public void createCoin(Coin coin , int y );
	public void createMagnet(Magnet magnet , int y);
	public void createShield(Shield shield, int y);
	public void createBall(Ball ball, int y);
	public void createBarricade(Barricade barricade,int y);
	public void loadWall();
	public void loadWall(Wall wall1, Wall wall2);
	public void loadCoin(Coin coin);
	public void moveWall();
	public void setsnaketail();
	public void moveCoin();
	public void loadShield(Shield shield);
	public void moveBall();
	public void loadBall(Ball ball);
	public void loadDestroy_Blocks(Destroy_Blocks destroy_blocks);
	public void createdestroy_block(Destroy_Blocks destroy_blocks , int y );
	public void moveShield();
	public void moveMagnet();
}
