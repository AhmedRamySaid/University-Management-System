package ASU.CAIE.GUI.Dashboards;

import ASU.CAIE.model.User;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class AdminDashboardView {

    public Node build() {
        VBox content = new VBox(36);
        content.setPadding(new Insets(44, 48, 44, 48));
        content.setStyle("-fx-background-color: #f4f5f7;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        // ── Header ──────────────────────────────────────────────────────────
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 8, 0));

        Circle avatar = new Circle(26, Color.web("#dc2626"));
        String initial = user.GetName() != null && !user.GetName().isEmpty()
                ? String.valueOf(user.GetName().charAt(0)).toUpperCase() : "A";
        Label initLbl = new Label(initial);
        initLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 17px;");
        StackPane avatarPane = new StackPane(avatar, initLbl);

        VBox headerText = new VBox(5);
        Label welcome = new Label("System Administration");
        welcome.setStyle("-fx-text-fill: #111827; -fx-font-size: 24px; -fx-font-weight: bold;");
        Label sub = new Label("Monitor system health and manage user accounts.");
        sub.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");
        headerText.getChildren().addAll(welcome, sub);
        header.getChildren().addAll(avatarPane, headerText);

        // ── Stats Cards ──────────────────────────────────────────────────────
        HBox stats = new HBox(20);
        VBox usersCard  = buildStatCard("Total Users",    "1,245",   "#2563eb", "#eff6ff", "#bfdbfe", "👤");
        VBox statusCard = buildStatCard("System Status",  "OPTIMAL", "#059669", "#ecfdf5", "#a7f3d0", "✓");
        VBox loadCard   = buildStatCard("Server Load",    "12%",     "#dc2626", "#fef2f2", "#fecaca", "⚡");
        HBox.setHgrow(usersCard,  Priority.ALWAYS);
        HBox.setHgrow(statusCard, Priority.ALWAYS);
        HBox.setHgrow(loadCard,   Priority.ALWAYS);
        stats.getChildren().addAll(usersCard, statusCard, loadCard);

        // ── Actions Section ──────────────────────────────────────────────────
        VBox actionsSection = new VBox(20);

        VBox actionsHeader = new VBox(4);
        Label actionsTitle = new Label("Administrative Actions");
        actionsTitle.setStyle("-fx-text-fill: #111827; -fx-font-size: 17px; -fx-font-weight: bold;");
        Label actionsSub = new Label("System management and configuration tools");
        actionsSub.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
        actionsHeader.getChildren().addAll(actionsTitle, actionsSub);

        HBox actionCards = new HBox(16);
        VBox manageCard  = buildActionCard("Manage Users",      "Add, edit or remove user accounts.",   "#3b82f6", "👤");
        VBox logsCard    = buildActionCard("View System Logs",  "Monitor activity and error reports.",  "#8b5cf6", "📋");
        VBox dbCard      = buildActionCard("Database Settings", "Configure database connections.",      "#059669", "🗄");
        HBox.setHgrow(manageCard, Priority.ALWAYS);
        HBox.setHgrow(logsCard,   Priority.ALWAYS);
        HBox.setHgrow(dbCard,     Priority.ALWAYS);
        actionCards.getChildren().addAll(manageCard, logsCard, dbCard);

        actionsSection.getChildren().addAll(actionsHeader, actionCards);
        content.getChildren().addAll(header, stats, actionsSection);
        return content;
    }

    private VBox buildStatCard(String title, String value,
                               String textColor, String bgColor,
                               String borderColor, String icon) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(24, 28, 24, 28));
        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 12, 0, 0, 3);"
        );

        HBox topRow = new HBox(8);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label iconLbl  = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 15px;");
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px; -fx-font-weight: bold;");
        topRow.getChildren().addAll(iconLbl, titleLbl);

        Label valueLbl = new Label(value);
        valueLbl.setStyle(
                "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-size: 38px;" +
                        "-fx-font-weight: bold;"
        );

        card.getChildren().addAll(topRow, valueLbl);
        return card;
    }

    private VBox buildActionCard(String title, String desc, String color, String icon) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(24, 24, 24, 24));
        card.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 2);" +
                        "-fx-cursor: hand;"
        );

        Label iconLbl = new Label(icon);
        iconLbl.setStyle(
                "-fx-background-color: " + color + "18;" +
                        "-fx-text-fill: " + color + ";" +
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 10 12 10 12;" +
                        "-fx-background-radius: 10;"
        );

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #111827; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label descLbl = new Label(desc);
        descLbl.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
        descLbl.setWrapText(true);

        card.getChildren().addAll(iconLbl, titleLbl, descLbl);

        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: " + color + "08;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 14, 0, 0, 4);" +
                        "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 2);" +
                        "-fx-cursor: hand;"
        ));

        return card;
    }
}