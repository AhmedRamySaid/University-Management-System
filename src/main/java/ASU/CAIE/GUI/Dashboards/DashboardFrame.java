package ASU.CAIE.GUI.Dashboards;

import ASU.CAIE.GUI.Views.*;
import ASU.CAIE.model.User;
import ASU.CAIE.util.SceneManager;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.function.Supplier;

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
        root.setStyle("-fx-background-color: #f4f5f7;");
        root.setLeft(buildSidebar());
        root.setTop(buildTopBar());

        Node content = contentSupplier.get();
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-width: 0;"
        );
        root.setCenter(sp);
    }

    private void switchContent(String section) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) return;

        Supplier<Node> newSupplier = () -> switch (section) {
            case "Grades"       -> new GradesView().build();
            case "Performance"  -> new PerformanceDashboardView().build();
            case "Submit Grade" -> new GradingView().build();
            case "Courses"      -> new CoursesView().build();
            case "Profile"      -> new ProfileView().build();
            case "Overview"     -> switch (currentUser.GetRole()) {
                case STUDENT               -> new StudentDashboardView().build();
                case PROFESSOR -> new InstructorDashboardView().build();
                case ADMIN, STAFF          -> new AdminDashboardView().build();
                default                    -> new StudentDashboardView().build();
            };
            default -> new Label("Coming soon...");
        };

        DashboardFrame newFrame = new DashboardFrame(section, newSupplier);
        SceneManager.setRoot(newFrame.getNode());
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(260);
        sidebar.setStyle(
                "-fx-background-color: #1e2a3a;" +
                        "-fx-border-width: 0;"
        );
        sidebar.setPadding(new Insets(0));

        // Branding
        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(28, 24, 24, 24));
        brandBox.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.08) transparent; -fx-border-width: 0 0 1 0;");

        HBox brand = new HBox(10);
        brand.setAlignment(Pos.CENTER_LEFT);

        Rectangle logoRect = new Rectangle(32, 32);
        logoRect.setArcWidth(8);
        logoRect.setArcHeight(8);
        logoRect.setFill(Color.web("#3b82f6"));

        Label logoText = new Label("U");
        logoText.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        StackPane logoPane = new StackPane(logoRect, logoText);

        VBox brandText = new VBox(2);
        Label appName = new Label("UMS Portal");
        appName.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label appSub = new Label("University Management");
        appSub.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 10px;");
        brandText.getChildren().addAll(appName, appSub);

        brand.getChildren().addAll(logoPane, brandText);
        brandBox.getChildren().add(brand);

        String currentRole = "";
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentRole = currentUser.GetRole().name();
        }

        VBox navSection = new VBox(2);
        navSection.setPadding(new Insets(20, 12, 20, 12));

        Label navLabel = new Label("NAVIGATION");
        navLabel.setStyle(
                "-fx-text-fill: rgba(255,255,255,0.3);" +
                        "-fx-font-size: 10px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 0 12 8 12;"
        );
        navSection.getChildren().add(navLabel);

        navSection.getChildren().add(
                sidebarNavLink("Overview", "⊞", "Overview".equals(activeLink), () -> switchContent("Overview"))
        );

        if ("STUDENT".equals(currentRole)) {
            navSection.getChildren().add(
                    sidebarNavLink("Grades", "◈", "Grades".equals(activeLink), () -> switchContent("Grades"))
            );
            navSection.getChildren().add(
                    sidebarNavLink("Performance", "↗", "Performance".equals(activeLink), () -> switchContent("Performance"))
            );
        }

        if ("PROFESSOR".equals(currentRole) || "INSTRUCTOR".equals(currentRole)) {
            navSection.getChildren().add(
                    sidebarNavLink("Submit Grade", "✎", "Submit Grade".equals(activeLink), () -> switchContent("Submit Grade"))
            );
        }

        if (!"PARENT".equals(currentRole)) {
            navSection.getChildren().add(
                    sidebarNavLink("Courses", "⊟", "Courses".equals(activeLink), () -> switchContent("Courses"))
            );
        }

        navSection.getChildren().add(
                sidebarNavLink("Profile", "◯", "Profile".equals(activeLink), () -> switchContent("Profile"))
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox bottomSection = new VBox(8);
        bottomSection.setPadding(new Insets(12, 12, 24, 12));
        bottomSection.setStyle("-fx-border-color: rgba(255,255,255,0.08) transparent transparent transparent; -fx-border-width: 1 0 0 0;");

        if (currentUser != null) {
            HBox userInfo = new HBox(10);
            userInfo.setAlignment(Pos.CENTER_LEFT);
            userInfo.setPadding(new Insets(10, 12, 10, 12));
            userInfo.setStyle("-fx-background-radius: 8;");

            Circle avatar = new Circle(18, Color.web("#3b82f6"));
            String initial = currentUser.GetName() != null && !currentUser.GetName().isEmpty()
                    ? String.valueOf(currentUser.GetName().charAt(0)).toUpperCase() : "?";
            Label initLbl = new Label(initial);
            initLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
            StackPane avatarPane = new StackPane(avatar, initLbl);

            VBox userText = new VBox(2);
            Label uName = new Label(currentUser.GetName());
            uName.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-weight: bold;");
            Label uRole = new Label(currentUser.GetRole().name());
            uRole.setStyle("-fx-text-fill: rgba(255,255,255,0.4); -fx-font-size: 10px;");
            userText.getChildren().addAll(uName, uRole);
            userInfo.getChildren().addAll(avatarPane, userText);
            bottomSection.getChildren().add(userInfo);
        }

        Button logout = new Button("Sign Out");
        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setStyle(
                "-fx-background-color: rgba(239,68,68,0.15);" +
                        "-fx-text-fill: #f87171;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 16 10 16;" +
                        "-fx-cursor: hand;"
        );
        logout.setOnMouseEntered(e -> logout.setStyle(
                "-fx-background-color: rgba(239,68,68,0.25);" +
                        "-fx-text-fill: #fca5a5;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 16 10 16;" +
                        "-fx-cursor: hand;"
        ));
        logout.setOnMouseExited(e -> logout.setStyle(
                "-fx-background-color: rgba(239,68,68,0.15);" +
                        "-fx-text-fill: #f87171;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 16 10 16;" +
                        "-fx-cursor: hand;"
        ));
        logout.setOnAction(e -> {
            SessionManager.getInstance().logout();
            SceneManager.switchToPortal();
        });
        bottomSection.getChildren().add(logout);

        sidebar.getChildren().addAll(brandBox, navSection, spacer, bottomSection);
        return sidebar;
    }

    private Button sidebarNavLink(String text, String icon, boolean active, Runnable onAction) {
        Button b = new Button(icon + "   " + text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(11, 16, 11, 16));

        if (active) {
            b.setStyle(
                    "-fx-background-color: rgba(59,130,246,0.2);" +
                            "-fx-text-fill: #93c5fd;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;" +
                            "-fx-border-color: rgba(59,130,246,0.4);" +
                            "-fx-border-width: 0 0 0 2;" +
                            "-fx-border-radius: 0 8 8 0;"
            );
        } else {
            b.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: rgba(255,255,255,0.55);" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: normal;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
            b.setOnMouseEntered(e -> b.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.07);" +
                            "-fx-text-fill: rgba(255,255,255,0.85);" +
                            "-fx-font-size: 13px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            ));
            b.setOnMouseExited(e -> b.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: rgba(255,255,255,0.55);" +
                            "-fx-font-size: 13px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            ));
        }

        b.setOnAction(e -> { if (onAction != null) onAction.run(); });
        return b;
    }

    private HBox buildTopBar() {
        HBox topBar = new HBox(16);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 32, 0, 32));
        topBar.setPrefHeight(64);
        topBar.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: transparent transparent #e5e7eb transparent;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label pageTitle = new Label(activeLink);
        pageTitle.setStyle(
                "-fx-text-fill: #111827;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(pageTitle, spacer);
        return topBar;
    }
}