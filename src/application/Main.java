package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.MenuView;


public class Main extends Application {
	/**
	 * Start method to initialize game 
	 */
	@Override
	public void start(Stage primaryStage) {
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
