package ASU.CAIE.util;

import ASU.CAIE.GUI.UMSPortal;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;
    private static String cssPath;

    public static void init(Stage stage) {
        primaryStage = stage;
        // Cache the CSS path so every scene transition gets the same stylesheet
        if (SceneManager.class.getResource("/ASU/CAIE/css/styles.css") != null) {
            cssPath = SceneManager.class.getResource("/ASU/CAIE/css/styles.css").toExternalForm();
        }
    }

    /**
     * Seamlessly swap the root content with a programmatic node.
     */
    public static void setRoot(Node node) {
        if (!(node instanceof Parent)) {
            // Should not happen for roots, but safety first
            return;
        }
        Parent newRoot = (Parent) node;
        
        Scene existingScene = primaryStage.getScene();
        if (existingScene == null) {
            Scene newScene = new Scene(newRoot, 1024, 768);
            if (cssPath != null) newScene.getStylesheets().add(cssPath);
            primaryStage.setScene(newScene);
            primaryStage.show();
        } else {
            Parent oldRoot = existingScene.getRoot();
            if (cssPath != null && !existingScene.getStylesheets().contains(cssPath)) {
                existingScene.getStylesheets().add(cssPath);
            }

            StackPane overlay = new StackPane(oldRoot, newRoot);
            newRoot.setOpacity(0);
            existingScene.setRoot(overlay);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), newRoot);
            fadeIn.setFromValue(0); fadeIn.setToValue(1);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), oldRoot);
            fadeOut.setFromValue(1); fadeOut.setToValue(0);

            fadeOut.setOnFinished(e -> existingScene.setRoot(newRoot));

            fadeOut.play();
            fadeIn.play();
        }
    }

    /**
     * Seamlessly swap the root content of the current scene using an FXML.
     */
    public static void switchTo(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/ASU/CAIE/fxml/" + fxmlName));
            Parent newRoot = loader.load();
            setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML: " + fxmlName);
        }
    }

    public static void switchTo(String fxmlName, Object data) {
        switchTo(fxmlName);
    }

    public static void switchToPortal() {
        try {
            UMSPortal portal = new UMSPortal();
            portal.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to switch to Portal");
        }
    }
}
