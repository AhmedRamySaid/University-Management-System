package ASU.CAIE.GUI.Dashboards;

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
        VBox content = new VBox(32);
        content.setPadding(new Insets(40, 44, 40, 44));
        content.setStyle("-fx-background-color: #f4f5f7;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        double      gpa     = 3.3;
        List<Course> courses = new ArrayList<Course>();
        List<Grade>  grades  = new ArrayList<Grade>();

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 4, 0));

        Circle avatar = new Circle(24, Color.web("#3b82f6"));
        String initial = user.GetName() != null && !user.GetName().isEmpty()
                ? String.valueOf(user.GetName().charAt(0)).toUpperCase() : "S";
        Label initLbl = new Label(initial);
        initLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        StackPane avatarPane = new StackPane(avatar, initLbl);

        VBox headerText = new VBox(3);
        Label welcome = new Label("Welcome back, " + user.GetName() + "!");
        welcome.setStyle("-fx-text-fill: #111827; -fx-font-size: 22px; -fx-font-weight: bold;");
        Label sub = new Label("Here's your academic summary for this semester.");
        sub.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");
        headerText.getChildren().addAll(welcome, sub);
        header.getChildren().addAll(avatarPane, headerText);

        // Stats Cards
        HBox stats = new HBox(16);

        String gpaColor = gpa >= 3.0 ? "#10b981" : gpa >= 2.0 ? "#f59e0b" : "#ef4444";
        String gpaBg    = gpa >= 3.0 ? "#ecfdf5" : gpa >= 2.0 ? "#fffbeb" : "#fef2f2";
        String gpaBorder= gpa >= 3.0 ? "#6ee7b7" : gpa >= 2.0 ? "#fcd34d" : "#fca5a5";

        VBox gpaCard    = buildStatCard("Current GPA",       String.format("%.2f", gpa),
                gpaColor, gpaBg, gpaBorder, "GPA");
        VBox courseCard = buildStatCard("Enrolled Courses",  String.valueOf(courses.size()),
                "#3b82f6", "#eff6ff", "#bfdbfe", "CRS");
        VBox gradeCard  = buildStatCard("Completed Grades",  String.valueOf(grades.size()),
                "#8b5cf6", "#f5f3ff", "#ddd6fe", "GRD");

        HBox.setHgrow(gpaCard,    Priority.ALWAYS);
        HBox.setHgrow(courseCard, Priority.ALWAYS);
        HBox.setHgrow(gradeCard,  Priority.ALWAYS);
        stats.getChildren().addAll(gpaCard, courseCard, gradeCard);

        // Schedule Section
        VBox scheduleSection = buildSection("Your Schedule", "Courses registered this semester");
        scheduleSection.getChildren().add(buildCourseTable(courses));

        // Grades Section
        VBox gradesSection = buildSection("Recent Grades", "Academic performance overview");
        gradesSection.getChildren().add(buildGradesTable(grades));

        content.getChildren().addAll(header, stats, scheduleSection, gradesSection);
        return content;
    }

    private VBox buildStatCard(String title, String value,
                               String textColor, String bgColor,
                               String borderColor, String tag) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(22, 24, 22, 24));
        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;"
        );

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tagLbl = new Label(tag);
        tagLbl.setStyle(
                "-fx-background-color: " + textColor + "22;" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-size: 9px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 3 7 3 7;" +
                        "-fx-background-radius: 10;"
        );
        topRow.getChildren().addAll(titleLbl, spacer, tagLbl);

        Label valueLbl = new Label(value);
        valueLbl.setStyle(
                "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;"
        );

        card.getChildren().addAll(topRow, valueLbl);
        return card;
    }

    private VBox buildSection(String title, String subtitle) {
        VBox box = new VBox(16);

        VBox titleBox = new VBox(3);
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: #111827; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label sub = new Label(subtitle);
        sub.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
        titleBox.getChildren().addAll(lbl, sub);

        box.getChildren().add(titleBox);
        return box;
    }

    @SuppressWarnings("unchecked")
    private TableView<Course> buildCourseTable(List<Course> courses) {
        TableView<Course> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(courses.isEmpty() ? 100 : Math.min(48 * courses.size() + 50, 260));
        styleTable(table);

        TableColumn<Course, String>  colName    = new TableColumn<>("Course Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, String>  colSched   = new TableColumn<>("Schedule");
        colSched.setCellValueFactory(new PropertyValueFactory<>("schedule"));

        TableColumn<Course, Integer> colCredits = new TableColumn<>("Credits");
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

        table.getColumns().addAll(colName, colSched, colCredits);
        table.getItems().setAll(courses);
        return table;
    }

    @SuppressWarnings("unchecked")
    private TableView<Grade> buildGradesTable(List<Grade> grades) {
        TableView<Grade> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(grades.isEmpty() ? 100 : Math.min(48 * grades.size() + 50, 260));
        styleTable(table);

        TableColumn<Grade, Integer> colCourse = new TableColumn<>("Course ID");
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Grade, Double>  colScore  = new TableColumn<>("Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<Grade, String>  colLetter = new TableColumn<>("Grade");
        colLetter.setCellValueFactory(new PropertyValueFactory<>("letterGrade"));
        colLetter.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
				String[] colors = switch (item) {
					case "A" -> new String[]{"#065f46", "#d1fae5", "#6ee7b7"};
					case "B" -> new String[]{"#1e40af", "#dbeafe", "#93c5fd"};
					case "C" -> new String[]{"#92400e", "#fef3c7", "#fcd34d"};
					case "D" -> new String[]{"#9a3412", "#ffedd5", "#fdba74"};
					default -> new String[]{"#991b1b", "#fee2e2", "#fca5a5"};
				};
				setStyle(
                        "-fx-text-fill: " + colors[0] + ";" +
                                "-fx-font-weight: bold;" +
                                "-fx-alignment: CENTER;" +
                                "-fx-background-color: " + colors[1] + ";" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 3 10 3 10;"
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
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;"
        );
    }
}