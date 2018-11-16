package view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import assets.Ball;
import assets.Barricade;
import assets.Coin;
import assets.Destroy_Blocks;
import assets.Magnet;
import assets.Shield;
import assets.SnakeButton;
import assets.Wall;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameView {
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	
	private static final int game_height = 800;
	private static final int game_width = 600;
	
	private Stage menuStage;
	private ImageView Snake;
	
	private boolean leftKeyPressed;
	private boolean rightKeyPressed;
	private boolean paused;
	private AnimationTimer gameTimer;
	
	private GridPane gridPane1;
	private GridPane gridPane2;
	private final static String background_url = "resources/game_background.png";
	private Wall wall1;
	private Wall wall2;
	private ImageView[] wall1_display;
	private ImageView[] wall2_display;
	private Coin coin;
	private ImageView coin_display;
	private Magnet magnet;
	private ImageView magnet_display;
	private Shield shield;
	private ImageView shield_display;
	private Destroy_Blocks destroy_blocks;
	private ImageView destroy_blocks_display;
	private Ball ball;
	private ImageView ball_display;
	private Barricade barricade;
	private ImageView barricade_display;
	private SnakeButton backButton;
	private Random randomPosGen;
	private Label points_label;
	private int points = 0;
	private final static int snake_edge = 10;
	private final static int coin_edge = 20;
	private final static int wall_edge = 75/2+10;
	
	public GameView() {
		initializeStage();
		createKeyListeners();
		randomPosGen = new Random();
	}
	
	public void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.LEFT) {
					leftKeyPressed = true;
				}
				else if (event.getCode() == KeyCode.RIGHT) {
					rightKeyPressed = true;
				}
				else if (event.getCode() == KeyCode.ESCAPE) {
					if (paused==true) {
						paused = false;
						gameTimer.start();
						backButton.setVisible(false);
					}
					else {
						paused = true;
						gameTimer.stop();
						backButton.setVisible(true);
					}
				}
			}
		});
		
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.LEFT) {
					leftKeyPressed = false;
				}
				else if (event.getCode() == KeyCode.RIGHT) {
					rightKeyPressed = false;
				}
			}
		});
	}
	
	public void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, game_width, game_height);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
		gameStage.setResizable(false);
		gameStage.initStyle(StageStyle.UNDECORATED);
		gameStage.setTitle("Snake VS Blocks");
		wall1_display = new ImageView[8];
		wall2_display = new ImageView[8];
		for(int i=0;i<8;i++) {
			wall1_display[i] = new ImageView();
			wall2_display[i] = new ImageView();
		}
		coin_display = new ImageView();
		magnet_display = new ImageView();
		shield_display = new ImageView();
		destroy_blocks_display = new ImageView();
		ball_display = new ImageView();
		barricade_display = new ImageView();
		paused = false;
		wall1 = new Wall();
		wall2 = new Wall();
		coin = new Coin();
		magnet = new Magnet();
		shield = new Shield();
		ball = new Ball();
		barricade = new Barricade();
		destroy_blocks = new Destroy_Blocks();
	}
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground();
		createSnake();
		createInitWall();
		createCoin(coin, -(game_height + game_height/2));
		createMagnet(magnet, -2000);
		createShield(shield, -2400);
		createdestroy_block(destroy_blocks, -2800);
		createBall(ball,-800);
		createBarricade(barricade,-100);
		createGameLoop();
		
		points_label = new Label();
		points_label.setText(Integer.toString(points));
		points_label.setFont(Font.font("Verdana", 30));
		points_label.setLayoutX(game_width/2-15);
		points_label.setLayoutY(10);
		points_label.setTextFill(Color.web("#ffffff"));
		gamePane.getChildren().add(points_label);
		
		backButton = new SnakeButton("QUIT");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuStage.show();
				try {
					
					coin.sety(coin_display.getLayoutY());
					coin.setx(coin_display.getLayoutX());
					serialize_coin(coin);
					
					magnet.sety(magnet_display.getLayoutY());
					magnet.setx(magnet_display.getLayoutX());
					serialize_magnet(magnet);
					
					shield.sety(shield_display.getLayoutY());
					shield.setx(shield_display.getLayoutX());
					serialize_shield(shield);
					
					destroy_blocks.sety(destroy_blocks_display.getLayoutY());
					destroy_blocks.setx(destroy_blocks_display.getLayoutX());
					serialize_destroy_blocks(destroy_blocks);
					
					ball.sety(ball_display.getLayoutY());
					ball.setx(ball_display.getLayoutX());
					
					barricade.sety(barricade_display.getLayoutY());
					barricade.setx(barricade_display.getLayoutX());
					serialize_barricade(barricade);
					
					ball.sety(ball_display.getLayoutY());
					ball.setx(ball_display.getLayoutX());
					serialize_ball(ball);
					
					wall1.sety(wall1_display[0].getLayoutY());
					serialize_wall1(wall1);
					wall2.sety(wall2_display[0].getLayoutY());
					serialize_wall2(wall2);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gameStage.close();
			}
		});
		backButton.setLayoutX(30);
		backButton.setLayoutY(45);
		gamePane.getChildren().add(backButton);
		backButton.setVisible(false);
		
		gameStage.show();
	}
	public void LoadSavedGame(Stage menuStage) throws ClassNotFoundException, IOException {
		coin = deserialize_coin();
		coin_display.setLayoutX(coin.getx());
		coin_display.setLayoutY(coin.gety());
		
		magnet = deserialize_magnet();
		magnet_display.setLayoutX(magnet.getx());
		magnet_display.setLayoutY(magnet.gety());
		
		shield = deserialize_shield();
		shield_display.setLayoutX(shield.getx());
		shield_display.setLayoutY(shield.gety());
		
		destroy_blocks = deserialize_destroy_blocks();
		destroy_blocks_display.setLayoutX(destroy_blocks.getx());
		destroy_blocks_display.setLayoutY(destroy_blocks.gety());
		
		ball = deserialize_ball();
		ball_display.setLayoutX(ball.getx());
		ball_display.setLayoutY(ball.gety());
		
		barricade = deserialize_barricade();
		barricade_display.setLayoutX(barricade.getx());
		barricade_display.setLayoutY(barricade.gety());
		
		wall1 = deserialize_wall1();
		for(int i=0;i<8;i++) {
			wall1_display[i].setLayoutX(75*(i));
			wall1_display[i].setLayoutY(wall1.gety());
		}
		wall2 = deserialize_wall2();
		for(int i=0;i<8;i++) {
			wall2_display[i].setLayoutX(75*(i));
			wall2_display[i].setLayoutY(wall2.gety());
		}
		
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground();
		createSnake();
		loadWall(wall1,wall2);
		loadCoin(coin);
		loadMagnet(magnet);
		loadShield(shield);
		loadDestroy_Blocks(destroy_blocks);
		loadBall(ball);
		loadBarricade(barricade);
		createGameLoop();
		
		backButton = new SnakeButton("QUIT");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuStage.show();
				try {
					
					coin.sety(coin_display.getLayoutY());
					coin.setx(coin_display.getLayoutX());
					serialize_coin(coin);
					
					
					magnet.sety(magnet_display.getLayoutY());
					magnet.setx(magnet_display.getLayoutX());
					serialize_magnet(magnet);
					
					
					shield.sety(shield_display.getLayoutY());
					shield.setx(shield_display.getLayoutX());
					serialize_shield(shield);
					
					
					destroy_blocks.sety(destroy_blocks_display.getLayoutY());
					destroy_blocks.setx(destroy_blocks_display.getLayoutX());
					serialize_destroy_blocks(destroy_blocks);
					
					
					ball.sety(ball_display.getLayoutY());
					ball.setx(ball_display.getLayoutX());
					serialize_ball(ball);
					
					
					barricade.sety(barricade_display.getLayoutY());
					barricade.setx(barricade_display.getLayoutX());
					serialize_barricade(barricade);
					
					
					wall1.sety(wall1_display[0].getLayoutY());
					serialize_wall1(wall1);
					
					wall2.sety(wall2_display[0].getLayoutY());
					serialize_wall2(wall2);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gameStage.close();
			}
		});
		backButton.setLayoutX(30);
		backButton.setLayoutY(45);
		gamePane.getChildren().add(backButton);
		backButton.setVisible(false);
		
		gameStage.show();
	}
	
	private void createCoin(Coin coin , int y ) {
		coin = new Coin();
		if(coin.block_appear==true) {
			coin_display = new ImageView(coin.block_path);
			coin_display.setLayoutY(y);
			Random rand = new Random();
			coin_display.setLayoutX(60*rand.nextInt(10));
			gamePane.getChildren().add(coin_display);
		}
	}
	private void loadCoin(Coin coin) {
		if(coin.block_appear==true) {
			coin_display = new ImageView(coin.block_path);
			coin_display.setLayoutY(coin.gety());
			coin_display.setLayoutX(coin.getx());
			gamePane.getChildren().add(coin_display);
		}
	}
	private void moveCoin() {
		
		double coin_y = 0;
	
		if(coin_display!=null) {
			coin_display.setLayoutY(coin_display.getLayoutY() + 5);
			coin_y =coin_display.getLayoutY();
		}
	
		
		if(coin_y >= 800) {
			createCoin(coin, -50);
		}	
	}
	public static void serialize_coin(Coin root)throws IOException{
        ObjectOutputStream out = null;
        if (root==null){
            root = new Coin();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_coin.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static Coin deserialize_coin() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Coin newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_coin.txt"));
            newroot = (Coin) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
    
    private void createMagnet(Magnet magnet , int y ) {
		magnet = new Magnet();
		if(magnet.block_appear==true) {
			magnet_display = new ImageView(magnet.block_path);
			magnet_display.setLayoutY(y);
			Random rand = new Random();
			magnet_display.setLayoutX(60*rand.nextInt(10));
			gamePane.getChildren().add(magnet_display);
		}
	}
    private void loadMagnet( Magnet magnet) {
		if(magnet.block_appear==true) {
			magnet_display = new ImageView(magnet.block_path);
			magnet_display.setLayoutY(magnet.gety());
			magnet_display.setLayoutX(magnet.getx());
			gamePane.getChildren().add(magnet_display);
		}
	}
    private void moveMagnet() {
		
		double magnet_y = 0;
	
		if(magnet_display!=null) {
			magnet_display.setLayoutY(magnet_display.getLayoutY() + 5);
			//System.out.println(magnet_display.getLayoutY() + " " + magnet_display.isVisible());
			magnet_y =magnet_display.getLayoutY();
		}
	
		
		if(magnet_y >= 800) {
			createMagnet(magnet, -2400);
		}	
	}	
    public static void serialize_magnet(Magnet root)throws IOException{
        ObjectOutputStream out = null;
        if (root==null){
            root = new Magnet();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_magnet.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static Magnet deserialize_magnet() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Magnet newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_magnet.txt"));
            newroot = (Magnet) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
    
    private void createShield(Shield shield , int y ) {
		shield = new Shield();
		if(shield.block_appear==true) {
			shield_display = new ImageView(shield.block_path);
			shield_display.setLayoutY(y);
			Random rand = new Random();
			shield_display.setLayoutX(60*rand.nextInt(10));
			gamePane.getChildren().add(shield_display);
		}
	}
	private void loadShield(Shield shield) {
		if(shield.block_appear==true) {
			shield_display = new ImageView(shield.block_path);
			shield_display.setLayoutY(shield.gety());
			shield_display.setLayoutX(shield.getx());
			gamePane.getChildren().add(shield_display);
		}
	}
	private void moveShield() {
		
		double shield_y = 0;
	
		if(shield_display!=null) {
			shield_display.setLayoutY(shield_display.getLayoutY() + 5);
			//System.out.println(shield_display.getLayoutY() + " " + shield_display.isVisible());
			shield_y =shield_display.getLayoutY();
		}
	
		
		if(shield_y >= 800) {
			createShield(shield, -3000);
		}	
	}
	public static void serialize_shield(Shield root)throws IOException{
		ObjectOutputStream out = null;
        if (root==null){
            root = new Shield();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_shield.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static Shield deserialize_shield() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Shield newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_shield.txt"));
            newroot = (Shield) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
    
	private void createdestroy_block(Destroy_Blocks destroy_blocks , int y ) {
		destroy_blocks = new Destroy_Blocks();
		if(destroy_blocks.block_appear==true) {
			destroy_blocks_display = new ImageView(destroy_blocks.block_path);
			destroy_blocks_display.setLayoutY(y);
			Random rand = new Random();
			destroy_blocks_display.setLayoutX(60*rand.nextInt(10));
			gamePane.getChildren().add(destroy_blocks_display);
		}
	}
	private void loadDestroy_Blocks(Destroy_Blocks destroy_blocks) {
		if(destroy_blocks.block_appear==true) {
			destroy_blocks_display = new ImageView(destroy_blocks.block_path);
			destroy_blocks_display.setLayoutY(destroy_blocks.gety());
			destroy_blocks_display.setLayoutX(destroy_blocks.getx());
			gamePane.getChildren().add(destroy_blocks_display);
		}
	}
	private void movedestroy_blocks() {
		
		double destroy_blocks_y = 0;
	
		if(destroy_blocks_display!=null) {
			destroy_blocks_display.setLayoutY(destroy_blocks_display.getLayoutY() + 5);
			//System.out.println(destroy_blocks_display.getLayoutY() + " " + destroy_blocks_display.isVisible());
			destroy_blocks_y =destroy_blocks_display.getLayoutY();
		}
	
		
		if(destroy_blocks_y >= 800) {
			createdestroy_block(destroy_blocks, -50);
		}	
	}
	public static void serialize_destroy_blocks(Destroy_Blocks root)throws IOException{
        ObjectOutputStream out = null;
        if (root==null){
            root = new Destroy_Blocks();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_destroy_blocks.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static Destroy_Blocks deserialize_destroy_blocks() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Destroy_Blocks newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_destroy_blocks.txt"));
            newroot = (Destroy_Blocks) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }	
    
    private void createBall(Ball ball , int y ) {
		ball = new Ball();
		if(ball.block_appear==true) {
			ball_display = new ImageView(ball.block_path);
			ball_display.setLayoutY(y);
			Random rand = new Random();
			ball_display.setLayoutX(60*rand.nextInt(10));
			gamePane.getChildren().add(ball_display);
		}
	}
    private void loadBall(Ball ball) {
		if(ball.block_appear==true) {
			ball_display = new ImageView(ball.block_path);
			ball_display.setLayoutY(ball.gety());
			ball_display.setLayoutX(ball.getx());
			gamePane.getChildren().add(ball_display);
		}
	}
    private void moveBall() {
		
		double ball_y = 0;
	
		if(ball_display!=null) {
			ball_display.setLayoutY(ball_display.getLayoutY() + 5);
			//System.out.println(ball_display.getLayoutY() + " " + ball_display.isVisible());
			ball_y =ball_display.getLayoutY();
		}
	
		
		if(ball_y >= 800) {
			createBall(ball, -50);
		}	
	}
	public static void serialize_ball(Ball root)throws IOException{
        ObjectOutputStream out = null;
        if (root==null){
            root = new Ball();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_ball.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static Ball deserialize_ball() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Ball newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_ball.txt"));
            newroot = (Ball) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
    
    private void createBarricade(Barricade barricade , int y ) {
		barricade = new Barricade();
		if(barricade.block_appear==true) {
			barricade_display = new ImageView(barricade.block_path);
			barricade_display.setLayoutY(y);
			Random rand = new Random();
			barricade_display.setLayoutX(60*rand.nextInt(10));
			gamePane.getChildren().add(barricade_display);
		}
	}
    private void loadBarricade(Barricade barricade) {
		if(barricade.block_appear==true) {
			barricade_display = new ImageView(barricade.block_path);
			barricade_display.setLayoutY(barricade.gety());
			barricade_display.setLayoutX(barricade.getx());
			gamePane.getChildren().add(barricade_display);
		}
	}
	private void moveBarricade() {
		
		double barricade_y = 0;
	
		if(barricade_display!=null) {
			barricade_display.setLayoutY(barricade_display.getLayoutY() + 5);
			//System.out.println(barricade_display.getLayoutY() + " " + barricade_display.isVisible());
			barricade_y =barricade_display.getLayoutY();
		}
	
		
		if(barricade_y >= 800) {
			createBarricade(barricade, -50);
		}	
	}
	public static void serialize_barricade(Barricade root)throws IOException{
        ObjectOutputStream out = null;
        if (root==null){
            root = new Barricade();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_barricade.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static Barricade deserialize_barricade() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Barricade newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_barricade.txt"));
            newroot = (Barricade) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
	
	private void createInitWall() {
		createWall1(wall1,-(game_height/2));
		createWall2(wall2,-(game_height + game_height/2));
	}
	private void createWall1(Wall wall1, int Y) {
		wall1 = new Wall();
		System.out.println("vdjvbvbvxbvxblkxbklbldb");
		for(int i=0; i<8; i++) {
			if(wall1.block_appear[i]==true) {
				wall1_display[i] = new ImageView(wall1.block_path[i]);
				wall1_display[i].setLayoutY(Y);
				wall1_display[i].setLayoutX(75*(i));
				gamePane.getChildren().add(wall1_display[i]);
			}
		}
	}
	private void createWall2(Wall wall2, int Y) {
		wall2 = new Wall();
		for(int i=0; i<8; i++) {
			if(wall2.block_appear[i]==true) {
				wall2_display[i] = new ImageView(wall2.block_path[i]);
				wall2_display[i].setLayoutY(Y);
				wall2_display[i].setLayoutX(75*(i));
				gamePane.getChildren().add(wall2_display[i]);
			}
		}
	}
	private void loadWall(Wall wall1, Wall wall2) {
		for(int i=0; i<8; i++) {
			if(wall1.block_appear[i]==true) {
				wall1_display[i] = new ImageView(wall1.block_path[i]);
				wall1_display[i].setLayoutY(wall1.gety());
				wall1_display[i].setLayoutX(75*(i));
				gamePane.getChildren().add(wall1_display[i]);
			}
		}
		for(int i=0; i<8; i++) {
			if(wall2.block_appear[i]==true) {
				wall2_display[i] = new ImageView(wall2.block_path[i]);
				wall2_display[i].setLayoutY(wall2.gety());
				wall2_display[i].setLayoutX(75*(i));
				gamePane.getChildren().add(wall2_display[i]);
			}
		}
	}
	private void moveWall() {
		
		double wall1_y = 0;
		double wall2_y = 0;
		
		for(int i=0; i<8; i++) {
			if(wall1_display[i]!=null) {
				wall1_display[i].setLayoutY(wall1_display[i].getLayoutY() + 5);
				//System.out.print(wall1_display[i].getLayoutY() + " " );
				wall1_y = wall1_display[i].getLayoutY();
			}
		}
		System.out.println();
		for(int i=0; i<8; i++) {
			if(wall2_display[i]!=null) {
				wall2_display[i].setLayoutY(wall2_display[i].getLayoutY() + 5);
				wall2_y = wall2_display[i].getLayoutY();
			}
		}
		if(wall1_y >= 800) {
			createWall1(wall1, -(game_height + game_height/2));
		}
		
		if(wall2_y >= 800) {
			createWall2(wall2, -(game_height + game_height/2));
		}
	}
	public static void serialize_wall1(Wall root)throws IOException{
        ObjectOutputStream out = null;
        if (root==null){
            root = new Wall();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_wall1.txt"));
            out.writeObject(root);
         
        }
        finally {
            out.close();
        }
    }
    public static Wall deserialize_wall1() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Wall newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_wall1.txt"));
            newroot = (Wall) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
    public static void serialize_wall2(Wall root)throws IOException{
        ObjectOutputStream out = null;
        if (root==null){
            root = new Wall();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_wall2.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static Wall deserialize_wall2() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        Wall newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_wall2.txt"));
            newroot = (Wall) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
	
	private void createSnake() {
		Snake = new ImageView("resources/ball.png");
		Snake.setLayoutX(game_width/2);
		Snake.setLayoutY(game_height - 200);
		gamePane.getChildren().add(Snake);
	}
	private void moveSnake() {
		if(leftKeyPressed && !rightKeyPressed) {
			if(Snake.getLayoutX() > 0) {
				Snake.setLayoutX(Snake.getLayoutX() - 10);
			}
		}
		if(!leftKeyPressed && rightKeyPressed) {
			if(Snake.getLayoutX() < 580) {
				Snake.setLayoutX(Snake.getLayoutX() + 10);
			}
		}
	}
	
	private void createGameLoop() {
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				moveBackground();
				moveWall();
				moveSnake();
				moveCoin();
				moveMagnet();
				moveShield();
				movedestroy_blocks();
				moveBall();
				moveBarricade();
				collisionHandling();
			}
		};
		
		gameTimer.start();
	}
	
	private void createBackground() {
		gridPane1 = new GridPane();
		gridPane2 = new GridPane();
		
		ImageView backgroundImage1 = new ImageView(background_url);
		ImageView backgroundImage2 = new ImageView(background_url);
		GridPane.setConstraints(backgroundImage1, 1, 1);
		GridPane.setConstraints(backgroundImage2, 1, 1);
		gridPane1.getChildren().add(backgroundImage1);
		gridPane2.getChildren().add(backgroundImage2);
		
		gridPane2.setLayoutY(-800);
		gamePane.getChildren().addAll(gridPane1, gridPane2);
	}
	private void moveBackground() {
		gridPane1.setLayoutY(gridPane1.getLayoutY() + 1);
		gridPane2.setLayoutY(gridPane2.getLayoutY() + 1);
		
		if(gridPane1.getLayoutY() >= 800) {
			gridPane1.setLayoutY(-800);
		}
		if(gridPane2.getLayoutY() >= 800) {
			gridPane2.setLayoutY(-800);
		}
	}
	
	private void collisionHandling() {
		
		for(int i=0; i<wall2_display.length; i++) {
			if(wall2_display[i]!=null) {
				if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, wall2_display[i].getLayoutX()+75/2, wall2_display[i].getLayoutY()+75/2) ) {
					//setPosition(wall2_display[i]);
					gamePane.getChildren().remove(wall2_display[i]);
					updatePoints(-1);
				}
			}
		}
		
		for(int i=0; i<wall1_display.length; i++) {
			if(wall1_display[i]!=null) {
				if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, wall1_display[i].getLayoutX()+75/2, wall1_display[i].getLayoutY()+75/2) ) {
					//setPosition(wall1_display[i]);
					gamePane.getChildren().remove(wall1_display[i]);
					updatePoints(-1);
				}
			}
		}
		
		if(coin_display!=null) {
			if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, coin_display.getLayoutX()+75/2, coin_display.getLayoutY()+75/2) ) {
				//setPosition(coin_display);
				gamePane.getChildren().remove(coin_display);
				updatePoints(1);
			}
		}
		if(magnet_display!=null) {
			if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, magnet_display.getLayoutX()+75/2, magnet_display.getLayoutY()+75/2) ) {
				//setPosition(magnet_display);
				gamePane.getChildren().remove(magnet_display);
			}
		}
		if(shield_display!=null) {
			if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, shield_display.getLayoutX()+75/2, shield_display.getLayoutY()+75/2) ) {
				//setPosition(shield_display);
				gamePane.getChildren().remove(shield_display);
			}
		}
		if(destroy_blocks_display!=null) {
			if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, destroy_blocks_display.getLayoutX()+75/2, destroy_blocks_display.getLayoutY()+75/2) ) {
				//setPosition(destroy_blocks_display);
				gamePane.getChildren().remove(destroy_blocks_display);
			}
		}
		if(ball_display!=null) {
			if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, ball_display.getLayoutX()+75/2, ball_display.getLayoutY()+75/2) ) {
				//setPosition(ball_display);
				gamePane.getChildren().remove(ball_display);
			}
		}
		
		
	}
	
	private double calcDist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	private void updatePoints(int i) {
		points+=i;
		points_label.setText(Integer.toString(points));
	}
	
}