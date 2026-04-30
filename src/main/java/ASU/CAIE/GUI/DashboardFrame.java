package ASU.CAIE.GUI;

import ASU.CAIE.Users.User;
import ASU.CAIE.util.SessionManager;
import ASU.CAIE.util.SceneManager;
import ASU.CAIE.config.AppConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.function.Supplier;

import static ASU.CAIE.GUI.ComponentFactory.*;
import static ASU.CAIE.GUI.ThemeManager.*;

public class DashboardFrame {

    private final BorderPane root;
    private final Supplier<Node> contentSupplier;
    private final String activeLink;

    public DashboardFrame(String activeLink, Supplier<Node> contentSupplier) {
        this.activeLink = activeLink;
        this.contentSupplier = contentSupplier;
        this.root = new BorderPane();
        rebuild();
    }

    public Node getNode() {
        return root;
    }

    public void rebuild() {
        root.setStyle("-fx-background-color: " + bg2() + ";");
        
        root.setLeft(buildSidebar());
        root.setTop(buildTopBar());
        
        Node content = contentSupplier.get();
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-width: 0;");
        
        root.setCenter(sp);
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: " + bg() + "; -fx-border-color: " + border2() + "; -fx-border-width: 0 0.5 0 0;");
        sidebar.setPadding(new Insets(24, 16, 24, 16));

        // Branding
        HBox brand = new HBox(8);
        brand.setAlignment(Pos.CENTER_LEFT);
        brand.setPadding(new Insets(0, 0, 32, 8));
        Circle dot = new Circle(4, Color.web("#3498db"));
        Label title = styledLabel("UMS DASHBOARD", 14, text());
        title.setStyle(title.getStyle() + " -fx-font-weight: bold; -fx-letter-spacing: 1px;");
        brand.getChildren().addAll(dot, title);

        VBox links = new VBox(4);
        links.getChildren().addAll(
            sidebarLink("Overview", "Overview".equals(activeLink), () -> {}),
            sidebarLink("Courses", "Courses".equals(activeLink), () -> {}),
            sidebarLink("Grades", "Grades".equals(activeLink), () -> {}),
            sidebarLink("Schedule", "Schedule".equals(activeLink), () -> {}),
            sidebarLink("Profile", "Profile".equals(activeLink), () -> {})
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logout = sidebarLink("Logout", false, () -> {
            SessionManager.getInstance().logout();
            SceneManager.switchToPortal();
        });
        logout.setStyle(logout.getStyle() + " -fx-text-fill: #e24b4a;");

        sidebar.getChildren().addAll(brand, links, spacer, logout);
        return sidebar;
    }

    private HBox buildTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 32, 16, 32));
        topBar.setStyle("-fx-background-color: " + bg() + "; -fx-border-color: " + border2() + "; -fx-border-width: 0 0 0.5 0;");

        User user = SessionManager.getInstance().getCurrentUser();
        String name = (user != null) ? user.getName() : "Guest User";
        String role = (user != null) ? user.getRole().name() : "NONE";

        VBox userProfile = new VBox(2);
        Label nameLbl = styledLabel(name, 14, text());
        nameLbl.setStyle(nameLbl.getStyle() + " -fx-font-weight: bold;");
        Label roleLbl = styledLabel(role, 11, text2());
        userProfile.getChildren().addAll(nameLbl, roleLbl);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Mock Mode Toggle (Styled like a primary button but smaller)
        Button mockBtn = new Button(AppConfig.USE_MOCK_DATA ? "Mock: ON" : "Mock: OFF");
        mockBtn.setPrefHeight(30);
        mockBtn.setStyle(
            "-fx-background-color: " + (AppConfig.USE_MOCK_DATA ? "#2ecc71" : border2()) + ";" +
            "-fx-text-fill: " + (AppConfig.USE_MOCK_DATA ? "white" : text()) + ";" +
            "-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;"
        );
        mockBtn.setOnAction(e -> {
            AppConfig.USE_MOCK_DATA = !AppConfig.USE_MOCK_DATA;
            rebuild();
        });

        // Theme Toggle (Exact same as Login)
        HBox themeToggle = themeToggle(this::rebuild);

        topBar.getChildren().addAll(userProfile, spacer, mockBtn, themeToggle);
        return topBar;
    }
}
