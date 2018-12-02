package view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import assets.SnakeButton;
import assets.TopPlayers;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LeaderBoard {
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private TopPlayers topplayers;
	private static final int game_height = 800;
	private static final int game_width = 600;
	
	private Stage menuStage;
	private AnimationTimer gameTimer;
	
	private GridPane gridPane1;
	private GridPane gridPane2;
	private final static String background_url = "resources/game_background.png";
	private final static String title_url = "resources/leaderboard_title.png";
	private final static String grid_url = "resources/grid.png";
	
	public LeaderBoard() {
		initializeStage();
		try {
			topplayers = deserialize_topplayer();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, game_width, game_height);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
		gameStage.setResizable(false);
		gameStage.initStyle(StageStyle.UNDECORATED);
		gameStage.setTitle("Snake VS Blocks");
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
	private void createBackground() {
		gridPane1 = new GridPane();
		gridPane2 = new GridPane();
		
		ImageView backgroundImage1 = new ImageView(background_url);
		ImageView backgroundImage2 = new ImageView(background_url);
		ImageView title = new ImageView(title_url);
		ImageView grid = new ImageView(grid_url);
		GridPane.setConstraints(backgroundImage1, 1, 1);
		GridPane.setConstraints(backgroundImage2, 1, 1);
		gridPane1.getChildren().add(backgroundImage1);
		gridPane2.getChildren().add(backgroundImage2);
		gridPane2.setLayoutY(800);
		
		SnakeButton backButton = new SnakeButton("BACK");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuStage.show();
				gameStage.close();
			}
		});
		backButton.setLayoutX(30);
		backButton.setLayoutY(45);
		
		gamePane.getChildren().addAll(gridPane1, gridPane2, title, grid, backButton);
	}
	
	private void moveBackground() {
		gridPane1.setLayoutY(gridPane1.getLayoutY() - 0.5);
		gridPane2.setLayoutY(gridPane2.getLayoutY() - 0.5);
		
		if(gridPane1.getLayoutY() <= -800) {
			gridPane1.setLayoutY(800);
		}
		if(gridPane2.getLayoutY() <= -800) {
			gridPane2.setLayoutY(800);
		}
	}
	
	public void createNewWindow(Stage menuStage) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground();
		createGameLoop();
		gameStage.show();
	}
	
	private void createGameLoop() {
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				moveBackground();
			}
		};
		
		gameTimer.start();
	}
	
}