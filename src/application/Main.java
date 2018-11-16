package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.MenuView;


public class Main extends Application {
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
	
	public static void main(String[] args) {
		launch(args);
	}
}
