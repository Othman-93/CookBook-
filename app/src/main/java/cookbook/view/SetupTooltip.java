package cookbook.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import java.io.InputStream;
import javafx.geometry.Side;

public class SetupTooltip{

public void Tooltip(Node node, String path, Side tooltipSide, double size) {
    //Load the image for the tooltip
    InputStream resourceStream = getClass().getResourceAsStream("/assets/tooltips/" + path);
    if (resourceStream == null) {
        System.err.println("Resource not found: /assets/tooltips/" + path);
        return;
    }

    Image tooltipImage = new Image(resourceStream);
    ImageView imageView = new ImageView(tooltipImage);
    imageView.setFitHeight(size);
    imageView.setPreserveRatio(true);

    //Create a tooltip and Display a Image
    Tooltip tooltip = new Tooltip();
    tooltip.setGraphic(imageView);
    tooltip.getStyleClass().add("arrowButton");
    tooltip.setShowDelay(Duration.millis(100));

    //Manually control tooltip position
    node.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
        double x = event.getScreenX();
        double y = event.getScreenY();

        switch (tooltipSide) {
            case TOP:
                x -= tooltip.getWidth() / 2;
                y -= tooltip.getHeight() + 20;
                break;
            case BOTTOM:
                x -= tooltip.getWidth() / 2;
                y += 20;
                break;
            case LEFT:
                x -= tooltip.getWidth() + 20;
                break;
            case RIGHT:
                x += 20;
                break;
        }

        tooltip.show(node, x, y);
    });

    node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> tooltip.hide());
}

    public void showTooltipDis(ListCell<String> cell, String description) {
        // Create a tooltip with the description text
        Tooltip tooltip = new Tooltip(description);
        tooltip.setShowDelay(Duration.millis(100));

        // Show tooltip when mouse enters the cell
        cell.setOnMouseEntered(event -> {
            if (cell.getItem() != null) {
                tooltip.show(cell, event.getScreenX(), event.getScreenY() + 10);
            }
        });

        // Hide tooltip when mouse exits the cell
        cell.setOnMouseExited(event -> tooltip.hide());

        // Hide tooltip if the cell becomes empty
        cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
            if (isNowEmpty) {
                tooltip.hide();
            }
        });

        // Hide tooltip when the cell is clicked
        cell.setOnMouseClicked(event -> tooltip.hide());

        // Ensure tooltip is hidden when cell loses focus
        cell.setOnMousePressed(event -> tooltip.hide());
    }
}