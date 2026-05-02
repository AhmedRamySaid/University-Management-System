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

    public Node getNode() { return root; }

    public void rebuild() {
        root.setStyle("-fx-background-color: #f4f5f7;");
        root.setLeft(buildSidebar());
        root.setTop(null);

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
                case PROFESSOR             -> new InstructorDashboardView().build();
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
        sidebar.setPrefWidth(264);
        sidebar.setStyle("-fx-background-color: #1a2332; -fx-border-width: 0;");

        // ── Brand ────────────────────────────────────────────────────────────
        VBox brandBox = new VBox(0);
        brandBox.setPadding(new Insets(28, 22, 22, 22));
        brandBox.setStyle(
                "-fx-border-color: transparent transparent rgba(255,255,255,0.08) transparent;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        HBox brand = new HBox(12);
        brand.setAlignment(Pos.CENTER_LEFT);

        Rectangle logoRect = new Rectangle(38, 38);
        logoRect.setArcWidth(10);
        logoRect.setArcHeight(10);
        logoRect.setFill(Color.web("#3b82f6"));
        Label logoChar = new Label("U");
        logoChar.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 19px;");
        StackPane logoPane = new StackPane(logoRect, logoChar);

        VBox brandText = new VBox(3);
        Label appName = new Label("UMS Portal");
        appName.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 15px;");
        Label appSub = new Label("University Management");
        appSub.setStyle("-fx-text-fill: #64748b; -fx-font-size: 10px;");
        brandText.getChildren().addAll(appName, appSub);
        brand.getChildren().addAll(logoPane, brandText);
        brandBox.getChildren().add(brand);

        // ── Nav ──────────────────────────────────────────────────────────────
        String currentRole = "";
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) currentRole = currentUser.GetRole().name();

        VBox navSection = new VBox(3);
        navSection.setPadding(new Insets(20, 10, 20, 10));

        Label navLabel = new Label("MAIN MENU");
        navLabel.setStyle(
                "-fx-text-fill: #475569;" +
                        "-fx-font-size: 9px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 0 10 10 10;"
        );
        navSection.getChildren().add(navLabel);

        navSection.getChildren().add(
                navBtn("Overview", "⊞", "Overview".equals(activeLink), () -> switchContent("Overview"))
        );

        if ("STUDENT".equals(currentRole)) {
            navSection.getChildren().add(
                    navBtn("Grades", "◈", "Grades".equals(activeLink), () -> switchContent("Grades"))
            );
            navSection.getChildren().add(
                    navBtn("Performance", "↗", "Performance".equals(activeLink), () -> switchContent("Performance"))
            );
        }

        if ("PROFESSOR".equals(currentRole)) {
            navSection.getChildren().add(
                    navBtn("Submit Grade", "✎", "Submit Grade".equals(activeLink), () -> switchContent("Submit Grade"))
            );
        }

        if (!"PARENT".equals(currentRole)) {
            navSection.getChildren().add(
                    navBtn("Courses", "⊟", "Courses".equals(activeLink), () -> switchContent("Courses"))
            );
        }

        navSection.getChildren().add(
                navBtn("Profile", "◯", "Profile".equals(activeLink), () -> switchContent("Profile"))
        );

        // ── Bottom ───────────────────────────────────────────────────────────
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(14, 10, 24, 10));
        bottomSection.setStyle(
                "-fx-border-color: rgba(255,255,255,0.07) transparent transparent transparent;" +
                        "-fx-border-width: 1 0 0 0;"
        );

        if (currentUser != null) {
            HBox userRow = new HBox(12);
            userRow.setAlignment(Pos.CENTER_LEFT);
            userRow.setPadding(new Insets(10, 14, 10, 14));
            userRow.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.05);" +
                            "-fx-background-radius: 10;"
            );

            Circle avCircle = new Circle(20, Color.web("#3b82f6"));
            String ini = currentUser.GetName() != null && !currentUser.GetName().isEmpty()
                    ? String.valueOf(currentUser.GetName().charAt(0)).toUpperCase() : "?";
            Label iniLbl = new Label(ini);
            iniLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
            StackPane avPane = new StackPane(avCircle, iniLbl);

            VBox userText = new VBox(3);
            Label uName = new Label(currentUser.GetName());
            uName.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 12px; -fx-font-weight: bold;");
            Label uRole = new Label(currentUser.GetRole().name());
            uRole.setStyle("-fx-text-fill: #64748b; -fx-font-size: 10px;");
            userText.getChildren().addAll(uName, uRole);
            userRow.getChildren().addAll(avPane, userText);

            bottomSection.getChildren().add(userRow);
        }

        Button logoutBtn = new Button("⎋   Sign Out");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setPadding(new Insets(11, 16, 11, 16));

        String logoutBase =
                "-fx-background-color: rgba(239,68,68,0.12);" +
                        "-fx-text-fill: #f87171;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;";
        String logoutHover =
                "-fx-background-color: rgba(239,68,68,0.22);" +
                        "-fx-text-fill: #fca5a5;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;";

        logoutBtn.setStyle(logoutBase);
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(logoutHover));
        logoutBtn.setOnMouseExited(e  -> logoutBtn.setStyle(logoutBase));
        logoutBtn.setOnAction(e -> {
            SessionManager.getInstance().logout();
            SceneManager.switchToPortal();
        });

        bottomSection.getChildren().add(logoutBtn);

        sidebar.getChildren().addAll(brandBox, navSection, spacer, bottomSection);
        return sidebar;
    }

    private Button navBtn(String text, String icon, boolean active, Runnable onAction) {
        Button b = new Button(icon + "   " + text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(11, 14, 11, 14));

        String activeStyle =
                "-fx-background-color: rgba(59,130,246,0.2);" +
                        "-fx-text-fill: #60a5fa;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #3b82f6;" +
                        "-fx-border-width: 0 0 0 3;" +
                        "-fx-border-radius: 0 9 9 0;";

        String idleStyle =
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #94a3b8;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: normal;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: rgba(255,255,255,0.06);" +
                        "-fx-text-fill: #cbd5e1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 9;" +
                        "-fx-cursor: hand;";

        b.setStyle(active ? activeStyle : idleStyle);

        if (!active) {
            b.setOnMouseEntered(e -> b.setStyle(hoverStyle));
            b.setOnMouseExited(e  -> b.setStyle(idleStyle));
        }

        b.setOnAction(e -> { if (onAction != null) onAction.run(); });
        return b;
    }
}