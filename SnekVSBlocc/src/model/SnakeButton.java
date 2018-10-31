package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class SnakeButton extends Button {
	private final String font_path = "src/resources/SIMPLIFICA_Typeface.ttf";
	private final String button_pressed_style = "-fx-background-color: transparent; -fx-text-fill: white; -fx-background-image: url('/resources/purple_button_pressed.png');";
	private final String button_style = "-fx-background-color: transparent; -fx-text-fill: white; -fx-background-image: url('/resources/purple_button.png');";

	public SnakeButton(String text) {
		setText(text);
		setButtonFont();
		setPrefWidth(188);
		setPrefHeight(70);
		setStyle(button_style);
		initializeButtonListeners();
	}
	
	private void setButtonFont() {
		try {
			setFont(Font.loadFont(new FileInputStream(font_path), 23));
		}
		catch (FileNotFoundException e) {
			setFont(Font.font("Verdana", 23));
		}
	}
	
	private void setButtonPressedStyle() {
		setStyle(button_pressed_style);
		setPrefHeight(70);
		setLayoutY(getLayoutY() - 2);
		setLayoutX(getLayoutX() - 2);
	}
	
	private void setButtonReleasedStyle() {
		setStyle(button_style);
		setPrefHeight(70);
		setLayoutY(getLayoutY() + 2);
		setLayoutX(getLayoutX() + 2);
	}
	
	private void initializeButtonListeners() {
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					setButtonPressedStyle();
				}
			}
		});
		
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					setButtonReleasedStyle();
				}
			}
		});
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setEffect(new DropShadow());
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setEffect(null);
			}
		});
	}
	
}
