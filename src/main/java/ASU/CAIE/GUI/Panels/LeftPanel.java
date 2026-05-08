package ASU.CAIE.GUI.Panels;

import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.styledLabel;

public class LeftPanel {

    public static StackPane build() {
        StackPane pane = new StackPane();
        pane.setPrefWidth(260);
        pane.setMinWidth(260);
        pane.setStyle(
                "-fx-background-color: #0f172a;" +
                        "-fx-background-radius: 12 0 0 12;"
        );

        HBox dotRow = new HBox(8);
        dotRow.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(3.5, Color.web("#64748b"));
        dotRow.getChildren().addAll(dot, styledLabel("UMS Portal", 11, "#64748b"));

        Region spacer1 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);

        Text line1 = new Text("Access your\n");
        line1.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 26));
        line1.setFill(Color.WHITE);

        Text line2 = new Text("university\n");
        line2.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        line2.setFill(Color.WHITE);

        Text line3 = new Text("account.");
        line3.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 26));
        line3.setFill(Color.WHITE);

        TextFlow headline = new TextFlow(line1, line2, line3);
        headline.setLineSpacing(4);

        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        VBox content = new VBox(20, dotRow, spacer1, headline, spacer2);
        content.setPadding(new Insets(28));

        pane.getChildren().add(content);

        return pane;
    }
}