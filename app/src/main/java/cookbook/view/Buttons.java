package cookbook.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class Buttons {
    private StackPane root;
    private Button settingButton;
    private Button infoButton;

    public Buttons(StackPane root) {
        this.root = root;
        initializeButtons();
    }

    private void initializeButtons() {
        settingButton = createSettingButton();
        infoButton = createInfoButton();
        root.getChildren().addAll(settingButton, infoButton);
    }

    private Button createSettingButton() {
        Button button = new Button();
        // Style for the button
        Image settingButtonImage = new Image(BackgroundView.class.getResourceAsStream(
                "/assets/buttons/settingButton.png"));
        ImageView settingButtonImageView = new ImageView(settingButtonImage);
        settingButtonImageView.setFitHeight(40);
        settingButtonImageView.setPreserveRatio(true);

        // Applying to the button
        button.setGraphic(settingButtonImageView);
        button.getStyleClass().add("clearButton");

        // For hovering logic
        Image settingHoverImage = new Image(BackgroundView.class.getResourceAsStream(
                "/assets/buttons/settingButton.png"));
        ImageView settingHoverImageView = new ImageView(settingHoverImage);
        settingHoverImageView.setFitHeight(42);
        settingHoverImageView.setPreserveRatio(true);

        // Hovering logic
        button.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setGraphic(settingHoverImageView);
            } else {
                button.setGraphic(settingButtonImageView);
            }
        });

        // Margins
        StackPane.setMargin(button, new Insets(-530, 730, 0, 0));

        return button;
    }

    private Button createInfoButton() {
        Button button = new Button();
        // Style for the button
        Image infoButtonImage = new Image(BackgroundView.class.getResourceAsStream(
                "/assets/buttons/infoButton.png"));
        ImageView infoButtonImageView = new ImageView(infoButtonImage);
        infoButtonImageView.setFitHeight(40);
        infoButtonImageView.setPreserveRatio(true);

        // Applying to the button
        button.setGraphic(infoButtonImageView);
        button.getStyleClass().add("clearButton");

        // For hovering logic
        Image settingHoverImage = new Image(BackgroundView.class.getResourceAsStream(
                "/assets/buttons/infoButton.png"));
        ImageView settingHoverImageView = new ImageView(settingHoverImage);
        settingHoverImageView.setFitHeight(42);
        settingHoverImageView.setPreserveRatio(true);

        // Hovering logic
        button.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setGraphic(settingHoverImageView);
            } else {
                button.setGraphic(infoButtonImageView);
            }
        });

        // Margins
        StackPane.setMargin(button, new Insets(-530, 630, 0, 0));

        return button;
    }

    public void setSettingButtonAction(EventHandler<ActionEvent> eventHandler) {
        settingButton.setOnAction(eventHandler);
    }

    public void setInfoButtonAction(EventHandler<ActionEvent> eventHandler) {
        infoButton.setOnAction(eventHandler);
    }
}
