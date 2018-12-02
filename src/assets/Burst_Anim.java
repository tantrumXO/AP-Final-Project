package assets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Burst_Anim {
	public Circle c1;
	public Circle c2;
	public Circle c3;
	public Circle c4;
	public Circle c5;
	public Circle c6;
	public Circle c7;
	
	public Burst_Anim(int x, int y) {
		c1 = new Circle();
		c1.setCenterX(x);
		c1.setCenterY(y);
		c1.setRadius(2);
		c1.setFill(Color.MEDIUMPURPLE);
		
		c2 = new Circle();
		c2.setCenterX(x);
		c2.setCenterY(y);
		c2.setRadius(4);
		c2.setFill(Color.MEDIUMPURPLE);
		
		c3 = new Circle();
		c3.setCenterX(x);
		c3.setCenterY(y);
		c3.setRadius(6);
		c3.setFill(Color.MEDIUMPURPLE);
		
		c4 = new Circle();
		c4.setCenterX(x);
		c4.setCenterY(y);
		c4.setRadius(8);
		c4.setFill(Color.MEDIUMPURPLE);
		
		c5 = new Circle();
		c5.setCenterX(x);
		c5.setCenterY(y);
		c5.setRadius(6);
		c5.setFill(Color.MEDIUMPURPLE);
		
		c6 = new Circle();
		c6.setCenterX(x);
		c6.setCenterY(y);
		c6.setRadius(4);
		c6.setFill(Color.MEDIUMPURPLE);
		
		c7 = new Circle();
		c7.setCenterX(x);
		c7.setCenterY(y);
		c7.setRadius(2);
		c7.setFill(Color.MEDIUMPURPLE);
	}
}
