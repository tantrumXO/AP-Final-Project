package view;

import java.util.Random;

import assets.Barricade;
import assets.Coin;
import assets.Magnet;
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
	
	private ImageView[] coins;
	private Coin coin;
	private ImageView magnets;
	private Magnet magnet;
	private ImageView[] barricades;
	private Barricade barricade;
	Random randomPosGen;
//	private Coin coin;
//	private ImageView coin_display;
//	private Magnet magnet;
//	private ImageView magnet_display;
//	private Shield shield;
//	private ImageView shield_display;
//	private Destroy_Blocks destroy_blocks;
//	private ImageView destroy_blocks_display;
//	private Ball ball;
//	private ImageView ball_display;
//	private Barricade barricade;
//	private ImageView barricade_display;
	private Label points_label;
	private int points = 0;
	private SnakeButton backButton;
	
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
		createGameElements();
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
	
	private void createGameElements() {
		points_label = new Label();
		points_label.setText(Integer.toString(points));
		points_label.setFont(Font.font("Verdana", 30));
		points_label.setLayoutX(game_width/2-15);
		points_label.setLayoutY(10);
		points_label.setTextFill(Color.web("#ffffff"));
		gamePane.getChildren().add(points_label);
		
		coin = new Coin();
		coins = new ImageView[2];
		for(int i=0; i<coins.length; i++) {
			coins[i] = new ImageView(coin.block_path);
			
			setPosition(coins[i]);
			
			gamePane.getChildren().add(coins[i]);
		}
		
		barricades = new ImageView[10];
		for(int i=0; i<barricades.length; i++) {
			barricade = new Barricade();
			barricades[i] = new ImageView(barricade.block_path);
			int x = randomPosGen.nextInt(9);
			barricades[i].setLayoutX(75+75*x);
			barricades[i].setLayoutY(-(225+randomPosGen.nextInt(game_height)));
			gamePane.getChildren().add(barricades[i]);
		}
		
		createWall(wall1, wall1_display, -(game_height/2));
		createWall(wall2, wall2_display, -(game_height + game_height/2));
	}
	
	private void moveGameElements() {
		for(int i=0; i<coins.length; i++) {
			coins[i].setLayoutY(coins[i].getLayoutY()+5);
		}
		
		for(int i=0; i<coins.length; i++) {
			if(coins[i].getLayoutY()>800) {
				setPosition(coins[i]);
			}
		}
		
		for(int i=0; i<barricades.length; i++) {
			barricades[i].setLayoutY(barricades[i].getLayoutY()+5);
		}
		
		for(int i=0; i<barricades.length; i++) {
			if(barricades[i].getLayoutY()>800) {
				barricades[i].setLayoutY(-(225+randomPosGen.nextInt(game_height)));
			}
		}
		
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
		
		if(wall1_y > 800) {
			createWall(wall1, wall1_display, -(game_height + game_height/2));
		}
		
		if(wall2_y > 800) {
			createWall(wall2, wall2_display, -(game_height + game_height/2));
		}
	}
	
	private void setPosition(ImageView image) {
		image.setLayoutX(randomPosGen.nextInt(game_width-50-50+1)+50);
		image.setLayoutY(-(randomPosGen.nextInt(game_height)));
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
				moveGameElements();
				moveSnake();
				collisionHandling();
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
	
	private void collisionHandling() {
		for(int i=0; i<coins.length; i++) {
			if((snake_edge + coin_edge) >= calcDist(Snake.getLayoutX()+snake_edge, Snake.getLayoutY()+snake_edge, coins[i].getLayoutX()+coin_edge, coins[i].getLayoutY()+coin_edge) ) {
				setPosition(coins[i]);
				updatePoints(1);
			}
		}
		
		for(int i=0; i<wall2_display.length; i++) {
			if(wall2_display[i]!=null) {
				if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, wall2_display[i].getLayoutX()+75/2, wall2_display[i].getLayoutY()+75/2) ) {
					//setPosition(wall2_display[i]);
					gamePane.getChildren().remove(wall2_display[i]);
				}
			}
		}
		
		for(int i=0; i<wall1_display.length; i++) {
			if(wall1_display[i]!=null) {
				if((snake_edge + wall_edge) >= calcDist(Snake.getLayoutX()+10, Snake.getLayoutY()+10, wall1_display[i].getLayoutX()+75/2, wall1_display[i].getLayoutY()+75/2) ) {
					//setPosition(wall1_display[i]);
					gamePane.getChildren().remove(wall1_display[i]);
				}
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
