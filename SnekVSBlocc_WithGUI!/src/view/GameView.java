package view;

import assets.Wall;
import javafx.animation.AnimationTimer;
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
	private AnimationTimer gameTimer;
	
	private GridPane gridPane1;
	private GridPane gridPane2;
	private final static String background_url = "resources/game_background.png";
	private Wall wall1;
	private Wall wall2;
	private ImageView[] wall1_display;
	private ImageView[] wall2_display;
	
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
	
	private void createInitWall() {
		createWall(wall1, wall1_display, -(game_height/2));
		createWall(wall2, wall2_display, -(game_height + game_height/2));
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
		createGameLoop();
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
