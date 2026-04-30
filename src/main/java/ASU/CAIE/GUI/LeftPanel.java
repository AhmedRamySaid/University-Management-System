package ASU.CAIE.GUI;

import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

import static ASU.CAIE.GUI.ComponentFactory.styledLabel;
import static ASU.CAIE.GUI.ThemeManager.*;

public class LeftPanel {

    public static StackPane build() {
        boolean dark = ThemeManager.isDark();

        StackPane pane = new StackPane();
        pane.setPrefWidth(240);
        pane.setMinWidth(240);
        pane.setStyle(
                "-fx-background-color: " + (dark ? "#0a0a0a" : "#111111") + ";" +
                        "-fx-background-radius: 12 0 0 12;"
        );

        // Decorative circle
        Circle circle = new Circle(130);
        circle.setFill(Color.web(dark ? "#161616" : "#1e1e1e"));
        StackPane.setAlignment(circle, Pos.BOTTOM_LEFT);
        circle.setTranslateX(-65);
        circle.setTranslateY(65);

        // Content
        VBox content = new VBox();
        content.setPadding(new Insets(28, 24, 28, 24));
        content.setSpacing(0);

        // Top dot row
        HBox dotRow = new HBox(8);
        dotRow.setAlignment(Pos.CENTER_LEFT);
        Circle dot = new Circle(3.5, Color.web("#888888"));
        dotRow.getChildren().addAll(dot, styledLabel("UMS Portal", 11, "#888888"));

        Region spacer1 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);

        // Headline
        Text line1 = new Text("Access your\n");
        line1.setFont(Font.font("Georgia", FontWeight.NORMAL, 26));
        line1.setFill(Color.WHITE);

        Text line2 = new Text("university\n");
        line2.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        line2.setFill(Color.WHITE);

        Text line3 = new Text("account.");
        line3.setFont(Font.font("Georgia", FontWeight.NORMAL, 26));
        line3.setFill(Color.WHITE);

        TextFlow headline = new TextFlow(line1, line2, line3);
        headline.setLineSpacing(2);

        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        content.getChildren().addAll(
                dotRow, spacer1, headline, spacer2,
                styledLabel("Ain Shams University · 2026", 11, "#555555")
        );

        pane.getChildren().addAll(circle, content);
        StackPane.setAlignment(content, Pos.CENTER_LEFT);
        return pane;
    }
}