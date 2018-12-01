package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import assets.SnakeButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuView {
	
	private static final int height = 800;
	private static final int width = 600;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private final static int button_start_X = 200;
	private final static int button_start_Y = 350;
	
	List<SnakeButton> menuButtons;
	
	public MenuView() throws ClassNotFoundException, IOException {
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, width, height);
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		mainStage.setResizable(false);
		mainStage.initStyle(StageStyle.UNDECORATED);
		mainStage.setTitle("Snake VS Blocks");
		createPlayButton();
		createLoadSaveButton() ;
		createLeaderboardButton();
		createExitButton();
		createBackground();
	}
	
	public Stage getMainStage() {
		return mainStage;
	}
	
	private void addMenuButton(SnakeButton button) {
		button.setLayoutX(button_start_X);
		button.setLayoutY(button_start_Y + menuButtons.size()*100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}
	
	private void createPlayButton() {
		SnakeButton playButton = new SnakeButton("PLAY");
		addMenuButton(playButton);
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GameView gameManager = new GameView();
				gameManager.createNewGame(mainStage);
			}
		});
	}
	private void createLoadSaveButton() throws IOException,ClassNotFoundException{
		SnakeButton playButton = new SnakeButton("LOAD");
		addMenuButton(playButton);
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GameView gameManager = new GameView();
				try {
					gameManager.LoadSavedGame(mainStage);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	private void createLeaderboardButton() {
		SnakeButton leaderboardButton = new SnakeButton("LEADERBOARD");
		addMenuButton(leaderboardButton);
		
		leaderboardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LeaderBoard leaderBoard = new LeaderBoard();
				leaderBoard.createNewWindow(mainStage);
			}
		});
	}
	
	private void createExitButton() {
		SnakeButton exitButton = new SnakeButton("EXIT");
		addMenuButton(exitButton);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainStage.close();
			}
		});
	}
	
	private void createBackground() {
		Image backgroundImage = new Image("resources/background.png", 600, 800, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}
	
}