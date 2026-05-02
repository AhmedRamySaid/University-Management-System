package ASU.CAIE.GUI.Dashboards;

import ASU.CAIE.model.User;
import ASU.CAIE.model.Course;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class InstructorDashboardView {

    public Node build() {
        VBox content = new VBox(36);
        content.setPadding(new Insets(44, 48, 44, 48));
        content.setStyle("-fx-background-color: #f4f5f7;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        int          pending = 0;
        List<Course> courses = new ArrayList<>();

        // ── Header ──────────────────────────────────────────────────────────
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 8, 0));

        Circle avatar = new Circle(26, Color.web("#7c3aed"));
        String initial = user.GetName() != null && !user.GetName().isEmpty()
                ? String.valueOf(user.GetName().charAt(0)).toUpperCase() : "P";
        Label initLbl = new Label(initial);
        initLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 17px;");
        StackPane avatarPane = new StackPane(avatar, initLbl);

        VBox headerText = new VBox(5);
        Label welcome = new Label("Professor Portal");
        welcome.setStyle("-fx-text-fill: #111827; -fx-font-size: 24px; -fx-font-weight: bold;");
        Label sub = new Label("Manage your courses and student performance.");
        sub.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");
        headerText.getChildren().addAll(welcome, sub);
        header.getChildren().addAll(avatarPane, headerText);

        // ── Stats Cards ──────────────────────────────────────────────────────
        HBox stats = new HBox(20);
        VBox pendingCard = buildStatCard("Pending Grading",  String.valueOf(pending),       "#d97706", "#fffbeb", "#fde68a", "⏳");
        VBox courseCard  = buildStatCard("Active Courses",   String.valueOf(courses.size()), "#2563eb", "#eff6ff", "#bfdbfe", "📖");
        VBox studentCard = buildStatCard("Total Students",   "—",                            "#7c3aed", "#f5f3ff", "#ddd6fe", "👥");
        HBox.setHgrow(pendingCard, Priority.ALWAYS);
        HBox.setHgrow(courseCard,  Priority.ALWAYS);
        HBox.setHgrow(studentCard, Priority.ALWAYS);
        stats.getChildren().addAll(pendingCard, courseCard, studentCard);

        // ── Courses Table ────────────────────────────────────────────────────
        VBox tableSection = new VBox(14);

        VBox tableHeader = new VBox(4);
        Label tableTitle = new Label("Assigned Courses");
        tableTitle.setStyle("-fx-text-fill: #111827; -fx-font-size: 17px; -fx-font-weight: bold;");
        Label tableSub = new Label("Courses you are currently teaching");
        tableSub.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
        tableHeader.getChildren().addAll(tableTitle, tableSub);

        TableView<Course> table = buildCourseTable(courses);
        tableSection.getChildren().addAll(tableHeader, table);

        content.getChildren().addAll(header, stats, tableSection);
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

    @SuppressWarnings("unchecked")
    private TableView<Course> buildCourseTable(List<Course> courses) {
        TableView<Course> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(courses.isEmpty() ? 110 : Math.min(52 * courses.size() + 52, 320));
        table.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 2);"
        );

        TableColumn<Course, String>  colName    = new TableColumn<>("Course Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, String>  colSched   = new TableColumn<>("Schedule");
        colSched.setCellValueFactory(new PropertyValueFactory<>("schedule"));

        TableColumn<Course, Integer> colCredits = new TableColumn<>("Credits");
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colCredits.setMaxWidth(100);

        table.getColumns().addAll(colName, colSched, colCredits);
        table.getItems().setAll(courses);
        return table;
    }
}