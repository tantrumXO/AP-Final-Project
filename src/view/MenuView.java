package view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import assets.SnakeButton;
import assets.TopPlayers;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuView {
	
	private static final int height = 800;
	private static final int width = 600;
	private AnchorPane mainPane;
	private Scene mainScene;
	public Stage mainStage;
	private TopPlayers topplayers;
	
	private final static int button_start_X = 200;
	private final static int button_start_Y = 350;
	
	List<SnakeButton> menuButtons;
	/**
	 * Constructor of the class
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public MenuView() throws ClassNotFoundException, IOException {
		
		try {
			topplayers = deserialize_topplayer();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		createScore();
		
//		Media media = new Media("resources/music.mp3");
//		MediaPlayer player = new MediaPlayer(media);
//		player.play();
	}
	/**
	 * returns main stage
	 * @return
	 */
	public Stage getMainStage() {
		return mainStage;
	}
	/**
	 * deserialize top players classes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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
	/**
	 * Adds a menu button
	 * @param button
	 */
	private void addMenuButton(SnakeButton button) {
		button.setLayoutX(button_start_X);
		button.setLayoutY(button_start_Y + menuButtons.size()*100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}
	/**
	 * creates a play button
	 */
	private void createPlayButton() {
		SnakeButton playButton = new SnakeButton("NEW GAME");
		addMenuButton(playButton);
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GameView gameManager;
				try {
					gameManager = new GameView(mainStage);
					gameManager.createNewGame(mainStage);

				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * creates a load button
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void createLoadSaveButton() throws IOException,ClassNotFoundException{
		SnakeButton playButton = new SnakeButton("LOAD");
		addMenuButton(playButton);
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				try {
					GameView gameManager = new GameView(mainStage);
					gameManager.LoadSavedGame(mainStage);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * creates a Leader board Button
	 */
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
	/**
	 * Creates a Exit button
	 */
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
	/**
	 * Creates a background
	 */
	private void createBackground() {
		Image backgroundImage = new Image("resources/background.png", 600, 800, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}
	/**
	 * Creates prev score
	 */
	private void createScore() {
		Text prevScore = new Text();
		prevScore.setText(Integer.toString(topplayers.getLastplayer().getScore()));
		prevScore.setLayoutX(280);
		prevScore.setLayoutY(320);
		prevScore.setFill(Color.WHITE);
		mainPane.getChildren().add(prevScore);
	}
	
}
