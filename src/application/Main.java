package application;
	
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import view.MenuView;


public class Main extends Application {
	/**
	 * Start method to initialize game 
	 */
	@Override
	public void start(Stage primaryStage) {
		final Task task = new Task() {
	        @Override
	        protected Object call() throws Exception {
	            AudioClip audio = new AudioClip(getClass().getResource("/resources/music.mp3").toExternalForm());
	            audio.setCycleCount(1000);
	            audio.play();
	            return null;
	        }
	    };
	    Thread thread = new Thread(task);
	    thread.start();
		
		try {
			MenuView manager = new MenuView();
			primaryStage = manager.getMainStage();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * the Main function
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
