package ASU.CAIE.GUI;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.util.Duration;

public class ToastManager {

    private final Stage owner;
    private Stage toastStage;

    public ToastManager(Stage owner) {
        this.owner = owner;
    }

    public void show(String msg, boolean success) {
        Platform.runLater(() -> {
            if (toastStage != null) toastStage.close();

            toastStage = new Stage();
            toastStage.initOwner(owner);
            toastStage.initStyle(StageStyle.UNDECORATED);
            toastStage.setAlwaysOnTop(true);

            Label lbl = new Label(msg);
            lbl.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
            lbl.setTextFill(Color.web(success ? "#16a34a" : "#dc2626"));

            StackPane pane = new StackPane(lbl);
            pane.setPadding(new Insets(10, 16, 10, 16));
            pane.setStyle(
                    "-fx-background-color: " + (success ? "#f0fdf4" : "#fef2f2") + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: "      + (success ? "#bbf7d0" : "#fecaca") + ";" +
                            "-fx-border-width: 0.5;" +
                            "-fx-border-radius: 8;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 2);"
            );
            pane.setMinWidth(260);

            Scene sc = new Scene(pane);
            sc.setFill(Color.TRANSPARENT);
            toastStage.setScene(sc);
            toastStage.sizeToScene();

            // Position top-right of main window
            toastStage.setX(owner.getX() + owner.getWidth() - 280);
            toastStage.setY(owner.getY() + 20);
            toastStage.show();

            // Fade in
            FadeTransition ft = new FadeTransition(Duration.millis(200), pane);
            ft.setFromValue(0); ft.setToValue(1); ft.play();

            // Auto-dismiss after 5 s
            PauseTransition pt = new PauseTransition(Duration.seconds(5));
            pt.setOnFinished(e -> {
                FadeTransition out = new FadeTransition(Duration.millis(200), pane);
                out.setFromValue(1); out.setToValue(0);
                out.setOnFinished(ev -> toastStage.close());
                out.play();
            });
            pt.play();
        });
    }
}