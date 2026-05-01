package ASU.CAIE.GUI.Dashboards;

import ASU.CAIE.model.User;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class AdminDashboardView {

    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        // Header Section
        VBox header = new VBox(4);
        Label welcome = styledLabel("System Administration", 24, text());
        welcome.setStyle(welcome.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("Monitor system health and manage user accounts.", 14, text2());
        header.getChildren().addAll(welcome, sub);

        // Stats Cards
        HBox stats = new HBox(20);
        
        VBox usersCard = dashboardCard("Total Users", "1,245", "#3498db");
        VBox statusCard = dashboardCard("System Status", "OPTIMAL", "#2ecc71");
        VBox logsCard = dashboardCard("Server Load", "12%", "#e74c3c");
        
        stats.getChildren().addAll(usersCard, statusCard, logsCard);

        // Actions Section
        VBox actionsBox = new VBox(16);
        Label actionsTitle = styledLabel("Administrative Actions", 18, text());
        actionsTitle.setStyle(actionsTitle.getStyle() + " -fx-font-weight: bold;");

        FlowPane actions = new FlowPane(20, 20);
        
        Button userMgmt = primaryButton("Manage Users");
        userMgmt.setPrefWidth(200);
        
        Button systemLogs = ghostButton("View System Logs");
        systemLogs.setPrefWidth(200);
        
        Button dbSettings = ghostButton("Database Settings");
        dbSettings.setPrefWidth(200);
        
        actions.getChildren().addAll(userMgmt, systemLogs, dbSettings);

        actionsBox.getChildren().addAll(actionsTitle, actions);

        content.getChildren().addAll(header, stats, actionsBox);
        return content;
    }
}
