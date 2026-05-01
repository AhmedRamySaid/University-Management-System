package ASU.CAIE.util;

import ASU.CAIE.GUI.UMSPortal;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static String cssPath;

    public static void init(Stage stage) {
        primaryStage = stage;
        if (SceneManager.class.getResource("styles.css") != null) {
            cssPath = SceneManager.class.getResource("styles.css").toExternalForm();
        }
    }

    public static void setRoot(Node node) {
        if (!(node instanceof Parent)) return;

        Parent newRoot = (Parent) node;
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            Scene newScene = new Scene(newRoot, 1024, 768);
            if (cssPath != null) newScene.getStylesheets().add(cssPath);
            primaryStage.setScene(newScene);
            primaryStage.show();
        } else {
            if (cssPath != null && !scene.getStylesheets().contains(cssPath)) {
                scene.getStylesheets().add(cssPath);
            }
            scene.setRoot(newRoot);
        }
    }

    public static void switchToPortal() {
        try {
            UMSPortal portal = new UMSPortal();
            portal.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}