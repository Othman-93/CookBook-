package cookbook.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * This class is for the welcome scene UI.
*/

public class BookAnimation {

  // Attributes.
  private StackPane root;

  /**
   * Constructor.
   *
   * @param root the root StackPane.
   */
  public  BookAnimation(StackPane root) {
    this.root = root;
  }

  /**
   * This method will make animation for the  Book to open when login been passed.
   */
  public void openAnimationBook(String path, Boolean move, int frame, String format, int position, int size) {


    // Image frames in the array
    ImageView[] frames = new ImageView[frame];
    for (int i = 0; i < frames.length; i++) {
      Image image = new Image(path + "/frame" + (i + 1) + format);
      frames[i] = new ImageView(image);
      frames[i].setVisible(i == 0);
      frames[i].setFitHeight(size);
      frames[i].setTranslateX(position);
      frames[i].setId("DeleteAnimation");
      frames[i].setTranslateY(-18);
      frames[i].setPreserveRatio(true);
      root.getChildren().add(frames[i]);
    }

    // Transition to move the first frame to the center initially
    TranslateTransition moveRight = new TranslateTransition(Duration.seconds(2), frames[0]);
    moveRight.setFromX(-123);
    moveRight.setToX(0);
    moveRight.setOnFinished(e -> {
      // Frame animation starts after moving to the center
      Timeline animationLoginBookTimeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
        for (int i = 0; i < frames.length; i++) {
          if (frames[i].isVisible()) {
            if (!frames[i].isVisible()) {
              String currentFrameId = frames[i].getId();
              System.out.println("Iam Invisible here:" + currentFrameId);
              // root.getChildren().removeIf(node -> currentFrameId.equals(node.getId()));
            }
            frames[i].setVisible(false);
            frames[(i + 1) % frames.length].setVisible(true);
            break;
          }
        }
      }));
      animationLoginBookTimeline.setCycleCount(frames.length - 1);
      animationLoginBookTimeline.play();
    });
    if (move){
      moveRight.play();
    }else{
            // Frame animation starts after moving to the center
            Timeline animationLoginBookTimeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
              for (int i = 0; i < frames.length; i++) {
                if (frames[i].isVisible()) {
                  frames[i].setVisible(false);
                  frames[(i + 1) % frames.length].setVisible(true);
                  break;
                }
              }
            }));
            animationLoginBookTimeline.setCycleCount(frames.length - 1);
            animationLoginBookTimeline.play();
    }
    

  }

  /**
   * Makes reverse animation (Closing a book).
   *
   * @param path something
   */
  public void closeAnimationBook(String path) {

    // Image frames in the array
    ImageView[] frames = new ImageView[17];
    for (int i = 0; i < frames.length; i++) {
      Image image = new Image(path + "/frame" + (i + 1) + ".png");
      frames[i] = new ImageView(image);
      frames[i].setVisible(false);
      frames[i].setFitHeight(422);
      frames[i].setTranslateX(0);
      frames[i].setTranslateY(-18);
      frames[i].setPreserveRatio(true);
      root.getChildren().add(frames[i]);
    }

    frames[frames.length - 1].setVisible(true);

    Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
      for (int i = frames.length - 1; i >= 0; i--) {
        if (frames[i].isVisible()) {
          frames[i].setVisible(false);
          if (i > 0) {
            frames[i - 1].setVisible(true);
          }
          break;
        }
      }
    }));
    animationTimeline.setCycleCount(frames.length - 1);
    animationTimeline.setOnFinished(e -> {
      TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(2), frames[0]);
      moveLeft.setToX(-123);
      moveLeft.play();
    });
    animationTimeline.play();

  }

  /**
   * Todo.
   *
   * @param path todo,
   */
  public void openPageAnimation(String path, int size) {

    // Image frames in the array
    ImageView[] frames = new ImageView[14];
    for (int i = 0; i < frames.length; i++) {
      Image image = new Image(path + "/frame" + (i + 1) + ".gif");
      frames[i] = new ImageView(image);
      frames[i].setVisible(false);
      frames[i].setFitHeight(size);
      frames[i].setId("DeleteAnimation");
      frames[i].setTranslateX(0);
      frames[i].setTranslateY(-18);
      frames[i].setPreserveRatio(true);
      root.getChildren().add(frames[i]);
    }

    frames[frames.length - 1].setVisible(true);

    Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
      for (int i = 0; i < frames.length; i++) {
        if (frames[i].isVisible()) {
          frames[i].setVisible(false);
          frames[(i + 1) % frames.length].setVisible(true);
          if (i + 1 < frames.length) {
            frames[i + 1].setVisible(true);
          }
          break;
        }
      }
    }));
    animationTimeline.setCycleCount(frames.length + 1);
    animationTimeline.play();

  }
}
