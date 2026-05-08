package ASU.CAIE.GUI.Dashboards;

import ASU.CAIE.model.Student;
import ASU.CAIE.model.User;
import ASU.CAIE.model.Course;
import ASU.CAIE.model.Grade;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardView {

    public Node build() {
        VBox content = new VBox(36);
        content.setPadding(new Insets(44, 48, 44, 48));
        content.setStyle("-fx-background-color: #f4f5f7;");

		User user = SessionManager.getInstance().getCurrentUser();
		if (user == null) return new Label("Not logged in");
		if (!(user instanceof Student student)) return new Label("Incorrect View");

		double       gpa     = student.CalculateGPA();
        List<Course> courses = student.GetTakenCourses();
        List<Grade>  grades  = student.GetCourseGrades();

        // ── Header ──────────────────────────────────────────────────────────
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 8, 0));

        Circle avatar = new Circle(26, Color.web("#3b82f6"));
        String initial = user.GetName() != null && !user.GetName().isEmpty()
                ? String.valueOf(user.GetName().charAt(0)).toUpperCase() : "S";
        Label initLbl = new Label(initial);
        initLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 17px;");
        StackPane avatarPane = new StackPane(avatar, initLbl);

        VBox headerText = new VBox(5);
        Label welcome = new Label("Welcome back, " + user.GetName() + "!");
        welcome.setStyle("-fx-text-fill: #111827; -fx-font-size: 24px; -fx-font-weight: bold;");
        Label sub = new Label("Here's your academic summary for this semester.");
        sub.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");
        headerText.getChildren().addAll(welcome, sub);
        header.getChildren().addAll(avatarPane, headerText);

        // ── Stats Cards ──────────────────────────────────────────────────────
        String gpaColor  = gpa >= 3.0 ? "#059669" : gpa >= 2.0 ? "#d97706" : "#dc2626";
        String gpaBg     = gpa >= 3.0 ? "#ecfdf5" : gpa >= 2.0 ? "#fffbeb" : "#fef2f2";
        String gpaBorder = gpa >= 3.0 ? "#a7f3d0" : gpa >= 2.0 ? "#fde68a" : "#fecaca";

        HBox stats = new HBox(20);
        VBox gpaCard    = buildStatCard("Current GPA",      String.format("%.2f", gpa),  gpaColor, gpaBg, gpaBorder, "📈");
        VBox courseCard = buildStatCard("Enrolled Courses",  String.valueOf(courses.size()), "#2563eb", "#eff6ff", "#bfdbfe", "📚");
        VBox gradeCard  = buildStatCard("Completed Grades",  String.valueOf(grades.size()),  "#7c3aed", "#f5f3ff", "#ddd6fe", "✅");
        HBox.setHgrow(gpaCard,    Priority.ALWAYS);
        HBox.setHgrow(courseCard, Priority.ALWAYS);
        HBox.setHgrow(gradeCard,  Priority.ALWAYS);
        stats.getChildren().addAll(gpaCard, courseCard, gradeCard);

        // ── Grades Section ───────────────────────────────────────────────────
        VBox gradesSection = new VBox(14);
        gradesSection.getChildren().addAll(buildSectionHeader("Recent Grades", "Academic performance overview"), buildGradesTable(grades));

        content.getChildren().addAll(header, stats, gradesSection);
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

    private VBox buildSectionHeader(String title, String subtitle) {
        VBox box = new VBox(4);
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: #111827; -fx-font-size: 17px; -fx-font-weight: bold;");
        Label sub = new Label(subtitle);
        sub.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
        box.getChildren().addAll(lbl, sub);
        return box;
    }

    @SuppressWarnings("unchecked")
    private TableView<Course> buildCourseTable(List<Course> courses) {
        TableView<Course> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(courses.isEmpty() ? 110 : Math.min(52 * courses.size() + 52, 280));
        styleTable(table);

        TableColumn<Course, String>  colName    = new TableColumn<>("Course Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, Integer> colCredits = new TableColumn<>("Credits");
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colCredits.setMaxWidth(100);

        table.getColumns().addAll(colName, colCredits);
        table.getItems().setAll(courses);
        return table;
    }

    @SuppressWarnings("unchecked")
    private TableView<Grade> buildGradesTable(List<Grade> grades) {
        TableView<Grade> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(grades.isEmpty() ? 110 : Math.min(52 * grades.size() + 52, 280));
        styleTable(table);

        TableColumn<Grade, Integer> colCourse = new TableColumn<>("Course ID");
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourse.setMaxWidth(120);

        TableColumn<Grade, Double>  colScore  = new TableColumn<>("Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        colScore.setMaxWidth(100);

        TableColumn<Grade, String>  colLetter = new TableColumn<>("Grade");
        colLetter.setCellValueFactory(new PropertyValueFactory<>("letterGrade"));
        colLetter.setMaxWidth(90);
        colLetter.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                String[] c = switch (item) {
                    case "A" -> new String[]{"#065f46", "#d1fae5"};
                    case "B" -> new String[]{"#1e40af", "#dbeafe"};
                    case "C" -> new String[]{"#92400e", "#fef3c7"};
                    case "D" -> new String[]{"#9a3412", "#ffedd5"};
                    default  -> new String[]{"#991b1b", "#fee2e2"};
                };
                setStyle(
                        "-fx-text-fill: " + c[0] + ";" +
                                "-fx-font-weight: bold;" +
                                "-fx-alignment: CENTER;" +
                                "-fx-background-color: " + c[1] + ";" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 4 12 4 12;"
                );
            }
        });

        TableColumn<Grade, String> colSem = new TableColumn<>("Semester");
        colSem.setCellValueFactory(new PropertyValueFactory<>("semester"));

        table.getColumns().addAll(colCourse, colScore, colLetter, colSem);
        table.getItems().setAll(grades);
        return table;
    }

    private void styleTable(TableView<?> table) {
        table.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 2);"
        );
    }
}