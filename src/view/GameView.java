package view;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
	
	public GameView() {
		initializeStage();
		createKeyListeners();
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
		coin_display = new ImageView();
		paused = false;
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
	
	private void createInitWall() {
		createWall(wall1, wall1_display, -(game_height/2));
		createWall(wall2, wall2_display, -(game_height + game_height/2));
	}
	
	private void createWall(Wall wall, ImageView[] display, int Y) {
		wall = new Wall();
		for(int i=0; i<8; i++) {
			if(wall.block_appear[i]==true) {
				display[i] = new ImageView(wall.block_path[i]);
				display[i].setLayoutY(Y);
				display[i].setLayoutX(75*(i));
				gamePane.getChildren().add(display[i]);
			}
		}
	}
	
	private void moveWall() {
		
		double wall1_y = 0;
		double wall2_y = 0;
		
		for(int i=0; i<8; i++) {
			if(wall1_display[i]!=null) {
				wall1_display[i].setLayoutY(wall1_display[i].getLayoutY() + 5);
				wall1_y = wall1_display[i].getLayoutY();
			}
		}
		
		for(int i=0; i<8; i++) {
			if(wall2_display[i]!=null) {
				wall2_display[i].setLayoutY(wall2_display[i].getLayoutY() + 5);
				wall2_y = wall2_display[i].getLayoutY();
			}
		}
		
		if(wall1_y >= 800) {
			createWall(wall1, wall1_display, -(game_height + game_height/2));
		}
		
		if(wall2_y >= 800) {
			createWall(wall2, wall2_display, -(game_height + game_height/2));
		}
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
	private void createBarricade(Barricade barricade, int y) {
		barricade = new Barricade();
		Random rand = new Random();
		//int nosofwall = rand.nextInt(4);
		if(barricade.block_appear==true) {
			barricade_display = new ImageView(barricade.block_path);
			
			barricade_display.setLayoutX(60*rand.nextInt(10));
			barricade_display.setLayoutY(y);
			gamePane.getChildren().add(barricade_display);	
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
	private void createBall(Ball ball, int y ) {
		ball = new Ball();
		if(ball.block_appear==true) {
			ball_display = new ImageView(ball.block_path);
			ball_display.setLayoutY(y);
			Random rand = new Random();
			ball_display.setLayoutX(60*rand.nextInt(10));
			gamePane.getChildren().add(ball_display);
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
	private void moveBarricade() {
		
		double barricade_y = 0;
	
		if(barricade_display!=null) {
			barricade_display.setLayoutY(barricade_display.getLayoutY() + 5);
			barricade_y =barricade_display.getLayoutY();
		}
	
		
		if(barricade_y >= 800) {
			createBarricade(barricade, -100);
		}	
	}
	private void moveMagnet() {
		
		double magnet_y = 0;
	
		if(magnet_display!=null) {
			magnet_display.setLayoutY(magnet_display.getLayoutY() + 5);
			magnet_y =magnet_display.getLayoutY();
		}
	
		
		if(magnet_y >= 800) {
			createMagnet(magnet, -2400);
		}	
	}	
	
	private void moveShield() {
		
		double shield_y = 0;
	
		if(shield_display!=null) {
			shield_display.setLayoutY(shield_display.getLayoutY() + 5);
			shield_y =shield_display.getLayoutY();
		}
	
		
		if(shield_y >= 800) {
			createShield(shield, -3000);
		}	
	}
	private void moveBall() {
		
		double ball_y = 0;
	
		if(ball_display!=null) {
			ball_display.setLayoutY(ball_display.getLayoutY() + 5);
			ball_y =ball_display.getLayoutY();
		}
	
		
		if(ball_y >= 800) {
			createBall(ball, -3000);
		}	
	}
	private void movedestroy_blocks() {
		
		double destroy_y = 0;
	
		if(destroy_blocks_display!=null) {
			destroy_blocks_display.setLayoutY(destroy_blocks_display.getLayoutY() + 5);
			destroy_y =destroy_blocks_display.getLayoutY();
		}
	
		
		if(destroy_y >= 800) {
			createdestroy_block(destroy_blocks, -1800);
		}	
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
		createBall(ball,-3200);
		createBarricade(barricade,-100);
		createGameLoop();
		
		backButton = new SnakeButton("QUIT");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuStage.show();
				gameStage.close();
			}
		});
		backButton.setLayoutX(30);
		backButton.setLayoutY(45);
		gamePane.getChildren().add(backButton);
		backButton.setVisible(false);
		
		gameStage.show();
	}
	
	private void createSnake() {
		Snake = new ImageView("resources/ball.png");
		Snake.setLayoutX(game_width/2);
		Snake.setLayoutY(game_height - 200);
		gamePane.getChildren().add(Snake);
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
				//System.out.println(barricade_display.getLayoutY() + " " + barricade_display.getLayoutX());
			}
		};
		
		gameTimer.start();
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
}
