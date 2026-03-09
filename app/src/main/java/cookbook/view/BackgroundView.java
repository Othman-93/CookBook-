package cookbook.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Manages the animated background.
 */

public class BackgroundView  {
  /**
 * initiate the animated background.
 */

 public ImageView createAnimatedBackground(String imagePath,
                                            Stage primaryStage,
                                            double animationDuration,
                                            double startX,
                                            double endX) {

    Image backgroundImage = new Image(imagePath);
    ImageView backgroundImageView = new ImageView(backgroundImage);
    backgroundImageView.setPreserveRatio(true);
    backgroundImageView.setFitHeight(primaryStage.getHeight());
    //for testing
    // backgroundImageView.setTranslateX(startX);

    // Update scene height
    primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number oldValue, Number newValue) {
        backgroundImageView.setFitHeight(newValue.doubleValue());
        }
        });

    // Animation
    Timeline timeline = new Timeline(new
        KeyFrame(Duration.ZERO, new KeyValue(backgroundImageView.translateXProperty(), startX)),
                                     new KeyFrame(Duration.seconds(animationDuration),
                                     new KeyValue(backgroundImageView.translateXProperty(), endX))
        );

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    return backgroundImageView;
  }

  /**
     * The start of the method.
     */

  public void stageOneSetUpBackground(StackPane root, Stage primaryStage) {

    root.getChildren().add(createAnimatedBackground(
            "./assets/background/stageOne/Background.png", primaryStage, 0, 2044, -1646));
    root.getChildren().add(createAnimatedBackground(
            "./assets/background/stageOne/Layer5.png", primaryStage, 100, 2044, -1646));
    root.getChildren().add(createAnimatedBackground(
            "./assets/background/stageOne/Layer4.png", primaryStage, 260, 2044, -1646));
    root.getChildren().add(createAnimatedBackground(
                    "./assets/background/stageOne/Layer3.png", primaryStage, 80, 2044, -1646));
    root.getChildren().add(createAnimatedBackground(
                    "./assets/background/stageOne/Layer2.png", primaryStage, 50, 2044, -1646));
  }

  public Parent stageTwoSetUpBackground(StackPane root) {

    root.getChildren().clear();
    ImageView[] frames = new ImageView[3];
    for (int i = 0; i < frames.length; i++) {
      Image image = new Image("./assets/background/stageTwo/frame" + (i + 1) + ".png", true);
      frames[i] = new ImageView(image);
      frames[i].fitWidthProperty().bind(root.widthProperty());
      frames[i].fitHeightProperty().bind(root.heightProperty());
      frames[i].setPreserveRatio(false);
      root.getChildren().add(frames[i]);
    }

    // Frame animation starts after moving to the center
    Timeline animationBackgroundTimeline = new Timeline(new KeyFrame(Duration.millis(200), ev -> {
      for (int i = 0; i < frames.length; i++) {
        if (frames[i].isVisible()) {
          frames[i].setVisible(false);
          frames[(i + 1) % frames.length].setVisible(true);
          break;
        }
      }
    }));
    animationBackgroundTimeline.setCycleCount(Timeline.INDEFINITE);
    animationBackgroundTimeline.play();

    return root;

  }

  
  public ImageView createBackground(String imagePath) {

    Image backgroundImage = new Image(imagePath);
    ImageView backgroundImageView = new ImageView(backgroundImage);
    backgroundImageView.setPreserveRatio(true);
    backgroundImageView.setFitHeight(600);

    return backgroundImageView;
  }
}
