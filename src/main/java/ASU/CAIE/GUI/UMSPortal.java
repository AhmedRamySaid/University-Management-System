package ASU.CAIE.GUI;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

import static ASU.CAIE.GUI.ThemeManager.*;

public class UMSPortal extends Application {

	private Stage      primaryStage;
	private BorderPane root;
	private HBox       outerCard;

	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;
		buildScene();

		stage.setTitle("ASU CAIE · UMS Portal");

		// ── Resizable: start maximized, allow minimize / resize ───────────────
		stage.setResizable(true);
		stage.setMaximized(true);           // opens full-screen (can minimize / restore)
		stage.setMinWidth(640);             // guards against squishing the layout
		stage.setMinHeight(480);

		stage.show();
	}

	// ── Scene ─────────────────────────────────────────────────────────────────

	private void buildScene() {
		root = new BorderPane();
		root.setStyle("-fx-background-color: " + bg2() + ";");
		root.setPadding(new Insets(32));

		outerCard = buildOuterCard();
		root.setCenter(outerCard);

		// Keep card centred and not stretching to fill the whole window
		BorderPane.setAlignment(outerCard, Pos.CENTER);

		Scene scene = new Scene(root, 820, 580);
		primaryStage.setScene(scene);

		// Fade-in
		FadeTransition ft = new FadeTransition(Duration.millis(350), outerCard);
		ft.setFromValue(0); ft.setToValue(1);
		ft.play();
	}

	// ── Outer card ────────────────────────────────────────────────────────────

	private HBox buildOuterCard() {
		ToastManager toast      = new ToastManager(primaryStage);
		RightPanel   rightPanel = new RightPanel(toast);

		// Wire theme-rebuild callback
		rightPanel.setOnThemeToggle(this::rebuildTheme);

		HBox card = new HBox();
		card.setStyle(cardStyle());
		card.setMinHeight(520);
		card.setMaxWidth(820);           // card never wider than 820 px

		VBox rightNode = rightPanel.getNode();
		HBox.setHgrow(rightNode, Priority.ALWAYS);

		card.getChildren().addAll(LeftPanel.build(), rightNode);
		return card;
	}

	// ── Theme rebuild ─────────────────────────────────────────────────────────

	private void rebuildTheme() {
		root.setStyle("-fx-background-color: " + bg2() + ";");

		outerCard.getChildren().clear();

		ToastManager toast      = new ToastManager(primaryStage);
		RightPanel   rightPanel = new RightPanel(toast);
		rightPanel.setOnThemeToggle(this::rebuildTheme);

		VBox rightNode = rightPanel.getNode();
		HBox.setHgrow(rightNode, Priority.ALWAYS);

		outerCard.getChildren().addAll(LeftPanel.build(), rightNode);
		outerCard.setStyle(cardStyle());
	}

	private static String cardStyle() {
		return  "-fx-background-color: " + bg() + ";" +
				"-fx-background-radius: 12;" +
				"-fx-border-color: "     + border2() + ";" +
				"-fx-border-width: 0.5;" +
				"-fx-border-radius: 12;" +
				"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 24, 0, 0, 4);";
	}

	// ── Entry point ───────────────────────────────────────────────────────────

	public static void main(String[] args) {
		launch(args);
	}
}