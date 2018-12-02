package view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import assets.Ball;
import assets.Barricade;
import assets.Burst_Anim;
import assets.Coin;
import assets.Destroy_Blocks;
import assets.Magnet;
import assets.Player;
import assets.Shield;
import assets.Snake;
import assets.SnakeButton;
import assets.TopPlayers;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameView {
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private int game_speed;
	private Player player;
	private static final int game_height = 800;
	private static final int game_width = 600;
	private TopPlayers topplayers;
	private Stage menuStage;
	private Snake snake;
	private ImageView snake_display;
	private ArrayList<ImageView> snake_tail;
	
	private Text snake_value;
	private Text ball_value;
	
	private boolean leftKeyPressed;
	private boolean rightKeyPressed;
	private boolean paused;
	private AnimationTimer gameTimer;
	private AnimationTimer woahTimer;
	
	private Text[] wall1_values;
	private Text[] wall2_values;
	
	private GridPane gridPane1;
	private GridPane gridPane2;
	private boolean has_shield;
	private double shield_time;
	private boolean has_magnet;
	private double magnet_time;
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
	private ImageView[] barricade_display;
	private SnakeButton backButton;
	private SnakeButton restartButton;
	private Random randomPosGen;
	private Label points_label;
	private int points = 0;
	private final static int snake_edge = 10;
	private static int coin_edge = 20;
	private final static int wall_edge = 75/2+10;
	private Burst_Anim burst;
	private Stage mainStage;
	
	public GameView(Stage mainStage) {
		game_speed = 5;
		this.mainStage = mainStage;
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
						restartButton.setVisible(false);
					}
					else {
						paused = true;
						gameTimer.stop();
						backButton.setVisible(true);
						restartButton.setVisible(true);
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
		player = new Player();
		topplayers = new TopPlayers();
		wall1_display = new ImageView[8];
		wall2_display = new ImageView[8];
		wall1_values = new Text[8];
		wall2_values = new Text[8];
		for(int i=0;i<8;i++) {
			wall1_display[i] = new ImageView();
			wall2_display[i] = new ImageView();
			wall1_values[i] = new Text();
			wall2_values[i] = new Text();
		}
		snake_tail = new ArrayList<ImageView>();
		coin_display = new ImageView();
		magnet_display = new ImageView();
		shield_display = new ImageView();
		destroy_blocks_display = new ImageView();
		ball_display = new ImageView();
		barricade_display = new ImageView[10];
		paused = false;
		wall1 = new Wall();
		wall2 = new Wall();
		coin = new Coin();
		magnet = new Magnet();
		shield = new Shield();
		ball = new Ball();
		barricade = new Barricade();
		destroy_blocks = new Destroy_Blocks();
		
		burst = new Burst_Anim(300,300);
		try {
			topplayers = deserialize_topplayer();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		Animation(300,400);
		
		
		points_label = new Label();
		points_label.setText(Integer.toString(points));
		points_label.setFont(Font.font("Verdana", 30));
		points_label.setLayoutX(game_width/2-15);
		points_label.setLayoutY(10);
		points_label.setTextFill(Color.web("#ffffff"));
		gamePane.getChildren().add(points_label);
		
		backButton = new SnakeButton("QUIT");
		restartButton = new SnakeButton("RESTART");
		
		restartButton.setOnAction(new EventHandler<ActionEvent>( ) {
			@Override
			public void handle(ActionEvent event) {
				GameView gameManager = new GameView(mainStage);
				gameManager.createNewGame(mainStage);
				gameStage.close();
			}
		});
		
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
					ball.setValue(Integer.parseInt(ball_value.getText()));
					serialize_ball(ball);
					
					barricade.setX0(barricade_display[0].getLayoutX());
					barricade.setY0(barricade_display[0].getLayoutY());
					barricade.setX1(barricade_display[1].getLayoutX());
					barricade.setY1(barricade_display[1].getLayoutY());
					barricade.setX2(barricade_display[2].getLayoutX());
					barricade.setY2(barricade_display[2].getLayoutY());
					barricade.setX3(barricade_display[3].getLayoutX());
					barricade.setY3(barricade_display[3].getLayoutY());
					barricade.setX4(barricade_display[4].getLayoutX());
					barricade.setY4(barricade_display[4].getLayoutY());
					barricade.setX5(barricade_display[5].getLayoutX());
					barricade.setY5(barricade_display[5].getLayoutY());
					barricade.setX6(barricade_display[6].getLayoutX());
					barricade.setY6(barricade_display[6].getLayoutY());
					barricade.setX7(barricade_display[7].getLayoutX());
					barricade.setY7(barricade_display[7].getLayoutY());
					barricade.setX8(barricade_display[8].getLayoutX());
					barricade.setY8(barricade_display[8].getLayoutY());
					barricade.setX9(barricade_display[9].getLayoutX());
					barricade.setY9(barricade_display[9].getLayoutY());
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
		
		restartButton.setLayoutX(380);
		restartButton.setLayoutY(45);
		gamePane.getChildren().add(restartButton);
		restartButton.setVisible(false);
		
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
		ball_value = new Text(Integer.toString(ball.getValue()));
		ball_display.setLayoutX(ball.getx());
		ball_display.setLayoutY(ball.gety());
		
		barricade = deserialize_barricade();
		for(int i=0;i<10;i++) {
			barricade_display[i] = new ImageView("resources/barricade3.png");
		}
		barricade_display[0].setLayoutX(barricade.getX0());
		barricade_display[0].setLayoutY(barricade.getY0());
		barricade_display[1].setLayoutX(barricade.getX1());
		barricade_display[1].setLayoutY(barricade.getY1());
		barricade_display[2].setLayoutX(barricade.getX2());
		barricade_display[2].setLayoutY(barricade.getY2());
		barricade_display[3].setLayoutX(barricade.getX3());
		barricade_display[3].setLayoutY(barricade.getY3());
		barricade_display[4].setLayoutX(barricade.getX4());
		barricade_display[4].setLayoutY(barricade.getY4());
		barricade_display[5].setLayoutX(barricade.getX5());
		barricade_display[5].setLayoutY(barricade.getY5());
		barricade_display[6].setLayoutX(barricade.getX6());
		barricade_display[6].setLayoutY(barricade.getY6());
		barricade_display[7].setLayoutX(barricade.getX7());
		barricade_display[7].setLayoutY(barricade.getY7());
		barricade_display[8].setLayoutX(barricade.getX8());
		barricade_display[8].setLayoutY(barricade.getY8());
		barricade_display[9].setLayoutX(barricade.getX9());
		barricade_display[9].setLayoutY(barricade.getY9());
		
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
		Animation(300,400);
		
		
		points_label = new Label();
		points_label.setText(Integer.toString(points));
		points_label.setFont(Font.font("Verdana", 30));
		points_label.setLayoutX(game_width/2-15);
		points_label.setLayoutY(10);
		points_label.setTextFill(Color.web("#ffffff"));
		gamePane.getChildren().add(points_label);
		
		backButton = new SnakeButton("QUIT");
		restartButton = new SnakeButton("RESTART");
		
		restartButton.setOnAction(new EventHandler<ActionEvent>( ) {
			@Override
			public void handle(ActionEvent event) {
				GameView gameManager = new GameView(mainStage);
				gameManager.createNewGame(mainStage);
				gameStage.close();
			}
		});
		
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
					ball.setValue(Integer.parseInt(ball_value.getText()));
					serialize_ball(ball);
					
					barricade.setX0(barricade_display[0].getLayoutX());
					barricade.setY0(barricade_display[0].getLayoutY());
					barricade.setX1(barricade_display[1].getLayoutX());
					barricade.setY1(barricade_display[1].getLayoutY());
					barricade.setX2(barricade_display[2].getLayoutX());
					barricade.setY2(barricade_display[2].getLayoutY());
					barricade.setX3(barricade_display[3].getLayoutX());
					barricade.setY3(barricade_display[3].getLayoutY());
					barricade.setX4(barricade_display[4].getLayoutX());
					barricade.setY4(barricade_display[4].getLayoutY());
					barricade.setX5(barricade_display[5].getLayoutX());
					barricade.setY5(barricade_display[5].getLayoutY());
					barricade.setX6(barricade_display[6].getLayoutX());
					barricade.setY6(barricade_display[6].getLayoutY());
					barricade.setX7(barricade_display[7].getLayoutX());
					barricade.setY7(barricade_display[7].getLayoutY());
					barricade.setX8(barricade_display[8].getLayoutX());
					barricade.setY8(barricade_display[8].getLayoutY());
					barricade.setX9(barricade_display[9].getLayoutX());
					barricade.setY9(barricade_display[9].getLayoutY());
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
		
		restartButton.setLayoutX(380);
		restartButton.setLayoutY(45);
		gamePane.getChildren().add(restartButton);
		restartButton.setVisible(false);
		
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
			coin_display.setLayoutY(coin_display.getLayoutY() + game_speed);
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
			magnet_display.setLayoutY(magnet_display.getLayoutY() + game_speed);
			//System.out.println(magnet_display.getLayoutY() + " " + magnet_display.isVisible());
			magnet_y =magnet_display.getLayoutY();
		}
	
		
		if(magnet_y >= 800) {
			createMagnet(magnet, -2400);
		}	
		if(magnet_time !=0) {
			
			magnet_time+=200;
			
		}
		if(magnet_time>=100000) {
			has_magnet = false;
			magnet_time = 0;
		}
		if(magnet_time==0) {
			coin_edge =20;
		}
		else {
			coin_edge = 200;
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
			shield_display.setLayoutY(shield_display.getLayoutY() + game_speed);
			//System.out.println(shield_display.getLayoutY() + " " + shield_display.isVisible());
			shield_y =shield_display.getLayoutY();
		}
	
		
		if(shield_y >= 800) {
			createShield(shield, -3000);
		}
		if(shield_time !=0) {
			
			shield_time+=200;
			
		}
		if(shield_time>=100000) {
			has_shield = false;
			shield_time = 0;
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
			destroy_blocks_display.setLayoutY(destroy_blocks_display.getLayoutY() + game_speed);
			//System.out.println(destroy_blocks_display.getLayoutY() + " " + destroy_blocks_display.isVisible());
			destroy_blocks_y =destroy_blocks_display.getLayoutY();
		}
	
		
		if(destroy_blocks_y >= 800) {
			createdestroy_block(destroy_blocks, -2400);
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
		Random rand = new Random();
		ball_value = new Text(Integer.toString(rand.nextInt(4)+1));
		ball_value.setFill(Color.WHITE);
		ball_value.setStyle("-fx-font: 10 Helvetica;");
		ball_value.setLayoutX(ball_display.getLayoutX() + 7);
		ball_value.setLayoutY(ball_display.getLayoutY() + 13);
		gamePane.getChildren().add(ball_value);
	}
    private void loadBall(Ball ball) {
		if(ball.block_appear==true) {
			ball_display = new ImageView(ball.block_path);
			ball_display.setLayoutY(ball.gety());
			ball_display.setLayoutX(ball.getx());
			gamePane.getChildren().add(ball_display);
		}
		ball_value = new Text(Integer.toString(ball.getValue()));
		ball_value.setFill(Color.WHITE);
		ball_value.setStyle("-fx-font: 10 Helvetica;");
		ball_value.setLayoutX(ball_display.getLayoutX() + 7);
		ball_value.setLayoutY(ball_display.getLayoutY() + 13);
		gamePane.getChildren().add(ball_value);
	}
    private void moveBall() {
		
		double ball_y = 0;
	
		if(ball_display!=null) {
			ball_display.setLayoutY(ball_display.getLayoutY() + game_speed);
			ball_value.setLayoutY(ball_value.getLayoutY() + game_speed);
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
    	
		for(int i=0; i<barricade_display.length; i++) {
			barricade = new Barricade();
			barricade_display[i] = new ImageView(barricade.block_path);
			int x = randomPosGen.nextInt(9);
			barricade_display[i].setLayoutX(75+75*x);
			barricade_display[i].setLayoutY(-(225+randomPosGen.nextInt(game_height)));
			gamePane.getChildren().add(barricade_display[i]);
		}
	}
    private void loadBarricade(Barricade barricade) {
		if(barricade.block_appear==true) {
			barricade_display[0].setLayoutX(barricade.getX0());
			barricade_display[0].setLayoutY(barricade.getY0());
			barricade_display[1].setLayoutX(barricade.getX1());
			barricade_display[1].setLayoutY(barricade.getY1());
			barricade_display[2].setLayoutX(barricade.getX2());
			barricade_display[2].setLayoutY(barricade.getY2());
			barricade_display[3].setLayoutX(barricade.getX3());
			barricade_display[3].setLayoutY(barricade.getY3());
			barricade_display[4].setLayoutX(barricade.getX4());
			barricade_display[4].setLayoutY(barricade.getY4());
			barricade_display[5].setLayoutX(barricade.getX5());
			barricade_display[5].setLayoutY(barricade.getY5());
			barricade_display[6].setLayoutX(barricade.getX6());
			barricade_display[6].setLayoutY(barricade.getY6());
			barricade_display[7].setLayoutX(barricade.getX7());
			barricade_display[7].setLayoutY(barricade.getY7());
			barricade_display[8].setLayoutX(barricade.getX8());
			barricade_display[8].setLayoutY(barricade.getY8());
			barricade_display[9].setLayoutX(barricade.getX9());
			barricade_display[9].setLayoutY(barricade.getY9());
			for(int i=0;i<10;i++) {
				gamePane.getChildren().add(barricade_display[i]);
			}
		}
	}
	private void moveBarricade() {
		
		for(int i=0; i<barricade_display.length; i++) {
			barricade_display[i].setLayoutY(barricade_display[i].getLayoutY()+game_speed);
		}
		
		for(int i=0; i<barricade_display.length; i++) {
			if(barricade_display[i].getLayoutY()>800) {
				barricade_display[i].setLayoutY(-(225+randomPosGen.nextInt(game_height)));
			}
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
		wall1 = new Wall(snake.getlen()+1);
		Y = Y +200;
		for(int i=0; i<8; i++) {
			if(wall1.block_appear[i]==true) {
				wall1_display[i] = new ImageView(wall1.block_path[i]);
				wall1_values[i] = new Text(Integer.toString(wall1.values[i]));
				wall1_values[i].setFill(Color.WHITE);
				wall1_values[i].setStyle("-fx-font: 16 Helvetica;");
				
				
				wall1_display[i].setLayoutY(Y);
				wall1_values[i].setLayoutY(Y+75/2);
				wall1_display[i].setLayoutX(75*(i));
				wall1_values[i].setLayoutX(75*i + 75/2);
				
				gamePane.getChildren().add(wall1_display[i]);
				gamePane.getChildren().add(wall1_values[i]);
			}
		}
	}
	private void createWall2(Wall wall2, int Y) {
		wall2 = new Wall(snake.getlen()+1);
		Y = Y+200;
		for(int i=0; i<8; i++) {
			if(wall2.block_appear[i]==true) {
				wall2_display[i] = new ImageView(wall2.block_path[i]);
				wall2_values[i] = new Text(Integer.toString(wall2.values[i]));
				wall2_values[i].setFill(Color.WHITE);
				wall2_values[i].setStyle("-fx-font: 16 Helvetica;");
				
				wall2_display[i].setLayoutY(Y);
				wall2_values[i].setLayoutY(Y+75/2);
				wall2_display[i].setLayoutX(75*(i));
				wall2_values[i].setLayoutX(75*i + 75/2);
				
				gamePane.getChildren().add(wall2_display[i]);
				gamePane.getChildren().add(wall2_values[i]);
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
				
				wall1_values[i] = new Text(Integer.toString(wall1.values[i]));
				wall1_values[i].setFill(Color.WHITE);
				wall1_values[i].setStyle("-fx-font: 16 Helvetica;");
				wall1_values[i].setLayoutY(wall1_display[i].getLayoutY()+75/2);
				wall1_values[i].setLayoutX(75*i + 75/2);
				gamePane.getChildren().add(wall1_values[i]);
			}
		}
		for(int i=0; i<8; i++) {
			if(wall2.block_appear[i]==true) {
				wall2_display[i] = new ImageView(wall2.block_path[i]);
				wall2_display[i].setLayoutY(wall2.gety());
				wall2_display[i].setLayoutX(75*(i));
				gamePane.getChildren().add(wall2_display[i]);
				
				wall2_values[i] = new Text(Integer.toString(wall2.values[i]));
				wall2_values[i].setFill(Color.WHITE);
				wall2_values[i].setStyle("-fx-font: 16 Helvetica;");
				wall2_values[i].setLayoutY(wall2_display[i].getLayoutY()+75/2);
				wall2_values[i].setLayoutX(75*i + 75/2);
				gamePane.getChildren().add(wall2_values[i]);
			}
		}
	}
	private void moveWall() {
		
		double wall1_y = 0;
		double wall2_y = 0;
		if(wall1_display[0]!=null && wall2_display[0]!=null  ) {
			if(wall1_display[0].getLayoutY()< 0 && wall1_display[0].getLayoutY() < 0  ) {
				if(wall1_display[0].getLayoutY() >= wall2_display[0].getLayoutY()  && (wall1_display[0].getLayoutY() - wall2_display[0].getLayoutY() < 300)) {
					for(int i=0;i<wall2_display.length;i++ ) {
						wall2_display[i].setLayoutY(wall2_display[i].getLayoutY() - 500);
						wall2_values[i].setLayoutY(wall2_values[i].getLayoutY() - 500);
					}
				}
				
				if(wall2_display[0].getLayoutY() >= wall1_display[0].getLayoutY()  && (wall2_display[0].getLayoutY() - wall1_display[0].getLayoutY() < 300)) {
					for(int i=0;i<wall1_display.length;i++ ) {
						wall1_display[i].setLayoutY(wall1_display[i].getLayoutY() - 500);
						wall1_values[i].setLayoutY(wall1_values[i].getLayoutY() - 500);
					}
				}
			}
		}
		
		for(int i=0; i<8; i++) {
			if(wall1_display[i]!=null) {
				wall1_display[i].setLayoutY(wall1_display[i].getLayoutY() + game_speed);
				wall1_values[i].setLayoutY(wall1_values[i].getLayoutY() + game_speed);
				//System.out.print(wall1_display[i].getLayoutY() + " " );
				wall1_y = wall1_display[i].getLayoutY();
			}
		}
		//System.out.println();
		for(int i=0; i<8; i++) {
			if(wall2_display[i]!=null) {
				wall2_display[i].setLayoutY(wall2_display[i].getLayoutY() + game_speed);
				wall2_values[i].setLayoutY(wall2_values[i].getLayoutY() + game_speed);
				wall2_y = wall2_display[i].getLayoutY();
			}
		}
		if(wall1_y >= 800) {
			for(int i=0;i<8;i++) {
				gamePane.getChildren().remove(wall1_display[i]);
				gamePane.getChildren().remove(wall1_values[i]);
			}
			createWall1(wall1, -(game_height + game_height/2));
		}
		
		if(wall2_y >= 800) {
			for(int i=0;i<8;i++) {
				gamePane.getChildren().remove(wall2_display[i]);
				gamePane.getChildren().remove(wall2_values[i]);
			}
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
		snake = new Snake();
		snake_display = new ImageView("resources/ball.png");
		snake_display.setLayoutX(game_width/2);
		snake_display.setLayoutY(game_height - 200);
		gamePane.getChildren().add(snake_display);
		snake_value = new Text(Integer.toString(snake.getlen()));
		snake_value.setFill(Color.WHITE);
		snake_value.setStyle("-fx-font: 10 Helvetica;");
		snake_value.setLayoutX(game_width/2 + 7);
		snake_value.setLayoutY(game_height - 200 + 13);
		gamePane.getChildren().add(snake_value);
	}
	private void moveSnake() {
		
		ArrayList<Integer> left = new ArrayList<>();
		ArrayList<Integer> right = new ArrayList<>();
		
		boolean wall_check = false;
		for(int i=0; i<barricade_display.length; i++) {
			if(snake_display.getLayoutY()>barricade_display[i].getLayoutY() && snake_display.getLayoutY()<barricade_display[i].getLayoutY()+225 && barricade_display[i].getLayoutX()<snake_display.getLayoutX()) {
				wall_check = true;
				left.add(i);
			}
			if(snake_display.getLayoutY()>barricade_display[i].getLayoutY() && snake_display.getLayoutY()<barricade_display[i].getLayoutY()+225 && barricade_display[i].getLayoutX()>snake_display.getLayoutX()) {
				wall_check = true;
				right.add(i);
			}
		}
		
		if(leftKeyPressed && !rightKeyPressed) {
			if(snake_display.getLayoutX() > 0) {
				
				if(wall_check) {
					boolean close_check = false;
					for(int i=0; i<left.size(); i++) {
						if((snake_display.getLayoutX()) - (barricade_display[left.get(i)].getLayoutX()) < 15) {
							close_check = true;
						
						}
					}
					if(!close_check) {
						snake_display.setLayoutX(snake_display.getLayoutX() - 10);
						snake_value.setLayoutX(snake_value.getLayoutX() - 10);
					}
				}
				else {
					snake_display.setLayoutX(snake_display.getLayoutX() - 10);
					snake_value.setLayoutX(snake_value.getLayoutX() - 10);
				}
				//System.out.println(" snake_display x is - - " + snake_display.getLayoutX() );
				/*for(int i=0;i<snake_tail.size();i++) {
					snake_tail.get(i).setX(snake_display.getX());
				}*/
			}
		}
		if(!leftKeyPressed && rightKeyPressed) {
			if(snake_display.getLayoutX() < 580) {
				
				if(wall_check) {
					boolean close_check = false;
					for(int i=0; i<right.size(); i++) {
						if((barricade_display[right.get(i)].getLayoutX()) - (snake_display.getLayoutX()) < 30) {
							close_check = true;
							
						}
					}
					if(!close_check) {
						snake_display.setLayoutX(snake_display.getLayoutX() + 10);
						snake_value.setLayoutX(snake_value.getLayoutX() + 10);
					} 
				}
				else {
					snake_display.setLayoutX(snake_display.getLayoutX() + 10);
					snake_value.setLayoutX(snake_value.getLayoutX() + 10);
				}
			}
		}
		snake_value.setText(Integer.toString(snake_tail.size()+1));
		for(int i=0;i<snake_tail.size();i++) {
			snake_tail.get(i).setLayoutX(snake_display.getLayoutX());
			snake_tail.get(i).setLayoutY(snake_display.getLayoutY()+ (i+1)*20);
		}
		gamePane.getChildren().remove(snake_display);
		for(int i=0;i<snake_tail.size();i++) {
			gamePane.getChildren().remove(snake_tail.get(i));
		}
		for(int i=snake_tail.size()-1;i>=0;i--) {
			gamePane.getChildren().add(snake_tail.get(i));
		}
		gamePane.getChildren().add(snake_display);
		gamePane.getChildren().remove(snake_value);
		gamePane.getChildren().add(snake_value);
	}
	private void setsnaketail() {
		int x = snake.getlen();
		if(snake_tail==null) {
			snake_tail = new ArrayList<ImageView>();
		}
		if(snake_tail.size()==x) {
			return;
		}
		else if(snake_tail.size() > x) {
			while(snake_tail.size() > x && snake_tail.size()>0) {
				gamePane.getChildren().remove(snake_tail.get(snake_tail.size()-1));
				snake_tail.remove(snake_tail.size()-1);
			}
		}
		else if(snake_tail.size() < x) {
			while(snake_tail.size() < x) {
				ImageView tail = new ImageView("resources/ball.png");
				tail.setLayoutX(snake_display.getLayoutX());
				tail.setLayoutY(snake_display.getLayoutY()+ 20*snake_tail.size());
				snake_tail.add(tail);
				gamePane.getChildren().add(snake_tail.get(snake_tail.size()-1));
				
			}
		}
		/*for(int i=0;i<snake_tail.size();i++) {
			snake_tail.get(i).setLayoutX(snake_display.getLayoutX());
			snake_tail.get(i).setLayoutY(snake_display.getLayoutY()+ (i+1)*5);
		}*/
	}
	
	private void createGameLoop() {
		long startTime = System.currentTimeMillis();
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				setSpeed();
				try {
					checkEnd();
				} catch (IOException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				moveBackground();
				moveWall();
				moveSnake();
				setsnaketail();
				moveCoin();
				moveMagnet();
				moveShield();
				movedestroy_blocks();
				moveBall();
				moveBarricade();
				moveCircles();
				try {
					collisionHandling();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.print(snake.getlen() + " x is - " + snake_display.getLayoutX() + " y is -" + snake_display.getLayoutY() + " hi " +snake_tail.size() + "  :::: ");
				//System.out.println(magnet_time + " hsjvdsv" + shield_time + " sss " + gridPane1.getLayoutY() + " ll " +wall1_display[1].getLayoutY() + " hello " + wall2_display[1].getLayoutY() );
			}
		};
		
		gameTimer.start();
	}
	
	private void setSpeed() {
		if(snake.getlen()<10) {
			game_speed = 5;
		}
		else if(snake.getlen()<15) {
			game_speed = 7;
		}
		else if(snake.getlen()<20) {
			game_speed = 10;
		}
		else {
			game_speed = 15;
		}
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
	
	private void collisionHandling() throws InterruptedException {
		for(int i=0; i<wall2_display.length; i++) {
			if(wall2_display[i]!=null) {
				if((snake_edge + wall_edge) >= calcDist(snake_display.getLayoutX()+10, snake_display.getLayoutY()+10, wall2_display[i].getLayoutX()+75/2, wall2_display[i].getLayoutY()+75/2) ) {
					if(wall2_display[i].isVisible() && gamePane.getChildren().contains(wall2_display[i])) {
						wall2_display[i].setLayoutY(-1000);
						
						
						updatePoints(Math.min(Integer.parseInt(wall2_values[i].getText()),snake.getlen()+1));
						
						if(!has_shield) {
							snake.setlen(-Integer.parseInt(wall2_values[i].getText()));
						}
						gamePane.getChildren().remove(wall2_display[i]);
						gamePane.getChildren().remove(wall2_values[i]);
						//System.out.println("hello");
						
						positionBurst(snake_display.getLayoutX()-300, snake_display.getLayoutY()-400);
					}
					//setPosition(wall2_display[i]);
				}
			}
		}
		
		for(int i=0; i<wall1_display.length; i++) {
			if(wall1_display[i]!=null) {
				if((snake_edge + wall_edge) >= calcDist(snake_display.getLayoutX()+10, snake_display.getLayoutY()+10, wall1_display[i].getLayoutX()+75/2, wall1_display[i].getLayoutY()+75/2) ) {
					if(wall1_display[i].isVisible() && gamePane.getChildren().contains(wall1_display[i])) {
						wall1_display[i].setLayoutY(-1000);
						
						updatePoints(Math.min(Integer.parseInt(wall1_values[i].getText()),snake.getlen()+1));
						
						if(!has_shield) {
							snake.setlen(-Integer.parseInt(wall1_values[i].getText()));
						}
						gamePane.getChildren().remove(wall1_display[i]);
						gamePane.getChildren().remove(wall1_values[i]);
						//System.out.println("hello");
						
						positionBurst(snake_display.getLayoutX()-300, snake_display.getLayoutY()-400);
					}
				}
			}
		}
		
		if(coin_display!=null) {
			if((snake_edge + coin_edge) >= calcDist(snake_display.getLayoutX()+10, snake_display.getLayoutY()+10, coin_display.getLayoutX()+coin_edge, coin_display.getLayoutY()+coin_edge) ) {
				//setPosition(coin_display);
				coin_display.setLayoutY(900);
				gamePane.getChildren().remove(coin_display);
				
				//System.out.println("adding");
				updatePoints(1);
				positionBurst(snake_display.getLayoutX()-300, snake_display.getLayoutY()-400);
				
			}
		}
		if(magnet_display!=null) {
			if((snake_edge + 20) >= calcDist(snake_display.getLayoutX()+10, snake_display.getLayoutY()+10, magnet_display.getLayoutX()+20, magnet_display.getLayoutY()+20) ) {
				//setPosition(magnet_display);
				gamePane.getChildren().remove(magnet_display);
				has_magnet=true;
				magnet_time = gridPane1.getLayoutY();
				positionBurst(snake_display.getLayoutX()-300, snake_display.getLayoutY()-400);
			}
		}
		if(shield_display!=null) {
			if((snake_edge + 20) >= calcDist(snake_display.getLayoutX()+10, snake_display.getLayoutY()+10, shield_display.getLayoutX()+20, shield_display.getLayoutY()+20) ) {
				//setPosition(shield_display);
				gamePane.getChildren().remove(shield_display);
				has_shield =true;
				shield_time = gridPane1.getLayoutY();
				positionBurst(snake_display.getLayoutX()-300, snake_display.getLayoutY()-400);
				// has has_shield boolean value to check if shield is there
			}
		}
		if(destroy_blocks_display!=null) {
			if((snake_edge + 20) >= calcDist(snake_display.getLayoutX()+10, snake_display.getLayoutY()+10, destroy_blocks_display.getLayoutX()+20, destroy_blocks_display.getLayoutY()+20) ) {
				//setPosition(destroy_blocks_display);
				gamePane.getChildren().remove(destroy_blocks_display);
				for(int i=0;i<wall1_display.length;i++) {
					if(wall1_display[i].getLayoutY() > 0 && wall1_display[i].getLayoutY() < 800  ) {
						updatePoints(Integer.parseInt(wall1_values[i].getText()));
						wall1_display[i].setLayoutY(wall1_display[i].getLayoutY() - 800);
						wall1_values[i].setLayoutY(wall1_values[i].getLayoutY() - 800);
					}	
				}
				for(int i=0;i<wall2_display.length;i++) {
					if(wall2_display[i].getLayoutY() > 0 && wall2_display[i].getLayoutY() < 800  ) {
						updatePoints(Integer.parseInt(wall2_values[i].getText()));
						//positionBurst(wall2_display[i].getLayoutX(), wall2_display[i].getLayoutY());
						wall2_display[i].setLayoutY(wall2_display[i].getLayoutY() - 800);
						wall2_values[i].setLayoutY(wall2_values[i].getLayoutY() -  800);
					}
				}
				positionBurst(snake_display.getLayoutX()-300, snake_display.getLayoutY()-400);
			}
		}
		if(ball_display!=null) {
			if((snake_edge + 20) >= calcDist(snake_display.getLayoutX()+10, snake_display.getLayoutY()+10, ball_display.getLayoutX()+20, ball_display.getLayoutY()+20) ) {
				//setPosition(ball_display);
				//get ball number;
				ball_display.setLayoutY(900);
				ball_value.setLayoutY(900);
				snake.setlen(Integer.parseInt(ball_value.getText()));
				gamePane.getChildren().remove(ball_value);
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
	
	private void Animation(int x, int y) {
		burst = new Burst_Anim(x, y);
		
		gamePane.getChildren().add(burst.c1);
		gamePane.getChildren().add(burst.c2);
		gamePane.getChildren().add(burst.c3);
		gamePane.getChildren().add(burst.c4);
		gamePane.getChildren().add(burst.c5);
		gamePane.getChildren().add(burst.c6);
		gamePane.getChildren().add(burst.c7);
	}
	
	private void moveCircles() {
		burst.c1.setLayoutY(burst.c1.getLayoutY() - 15);
		
		burst.c2.setLayoutX(burst.c2.getLayoutX() - 12);
		
		burst.c3.setLayoutY(burst.c3.getLayoutY() + 13);
		
		burst.c4.setLayoutX(burst.c4.getLayoutX() - 10);
		burst.c4.setLayoutY(burst.c4.getLayoutY() + 8);
		
		burst.c5.setLayoutX(burst.c5.getLayoutX() + 7);
		burst.c5.setLayoutY(burst.c5.getLayoutY() + 10);
		
		burst.c6.setLayoutX(burst.c6.getLayoutX() - 10);
		burst.c6.setLayoutY(burst.c6.getLayoutY() - 7);
		
		burst.c7.setLayoutX(burst.c7.getLayoutX() + 8);
		burst.c7.setLayoutY(burst.c7.getLayoutY() - 10);
	}
	
	private void positionBurst(double x, double y) {
		burst.c1.setLayoutX(x);
		burst.c1.setLayoutY(y);
		
		burst.c2.setLayoutX(x);
		burst.c2.setLayoutY(y);
		
		burst.c3.setLayoutX(x);
		burst.c3.setLayoutY(y);
		
		burst.c4.setLayoutX(x);
		burst.c4.setLayoutY(y);
		
		burst.c5.setLayoutX(x);
		burst.c5.setLayoutY(y);
		
		burst.c6.setLayoutX(x);
		burst.c6.setLayoutY(y);
		
		burst.c7.setLayoutX(x);
		burst.c7.setLayoutY(y);
	}
	
	
	public static void serialize_topplayer(TopPlayers root)throws IOException{
		ObjectOutputStream out = null;
        if (root==null){
            root = new TopPlayers();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream("database_topplayer.txt"));
            out.writeObject(root);
        }
        finally {
            out.close();
        }
    }
    public static TopPlayers deserialize_topplayer() throws IOException,ClassNotFoundException{
        ObjectInputStream in = null;
        TopPlayers newroot = null;
        try{
            in = new ObjectInputStream(new FileInputStream("database_topplayer.txt"));
            newroot = (TopPlayers) in.readObject();
        }
        finally {
            in.close();
            return newroot;
        }
    }
	
	private void checkEnd() throws IOException, ClassNotFoundException {
		if(snake.getlen()<0) {
			player.setScore(Integer.parseInt(points_label.getText()));
			
			DateTimeFormatter dtm = DateTimeFormatter.BASIC_ISO_DATE;
			LocalDateTime ldt = LocalDateTime.now();
			player.setDate(ldt);
			topplayers.addplayer(player);
			topplayers.setLastplayer(player);
			
			serialize_topplayer(topplayers);
			TopPlayers op = deserialize_topplayer();
			for(int i =0;i<op.getPlayers().size();i++) {
				int a = op.getPlayers().get(i).getScore();
				System.out.println(a + " " + op.getPlayers().get(i).getDate());
			}
			gameTimer.stop();
			backButton.setVisible(true);
			restartButton.setVisible(true);
		}
	}
	
}