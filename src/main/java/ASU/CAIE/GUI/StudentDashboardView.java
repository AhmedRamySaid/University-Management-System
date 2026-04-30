package ASU.CAIE.GUI;

import ASU.CAIE.Users.User;
import ASU.CAIE.model.Course;
import ASU.CAIE.service.DashboardService;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

import static ASU.CAIE.GUI.ComponentFactory.*;
import static ASU.CAIE.GUI.ThemeManager.*;

public class StudentDashboardView {

    private final DashboardService service = new DashboardService();

    @SuppressWarnings("unchecked")
    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        // Header Section
        VBox header = new VBox(4);
        Label welcome = styledLabel("Welcome back, " + user.getName() + "!", 24, text());
        welcome.setStyle(welcome.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("Here is what's happening with your courses today.", 14, text2());
        header.getChildren().addAll(welcome, sub);

        // Stats Cards
        HBox stats = new HBox(20);
        
        double gpa = service.calculateGPA(user.getId());
        VBox gpaCard = dashboardCard("Current GPA", String.format("%.2f", gpa), "#3498db");
        
        List<Course> courses = service.getStudentCourses(user.getId());
        VBox coursesCard = dashboardCard("Enrolled Courses", String.valueOf(courses.size()), "#2ecc71");
        
        VBox tasksCard = dashboardCard("Upcoming Tasks", "3", "#f1c40f");
        
        stats.getChildren().addAll(gpaCard, coursesCard, tasksCard);

        // Table Section
        VBox tableBox = new VBox(16);
        Label tableTitle = styledLabel("Your Schedule", 18, text());
        tableTitle.setStyle(tableTitle.getStyle() + " -fx-font-weight: bold;");

        TableView<Course> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle("-fx-background-color: " + bg() + "; -fx-border-color: " + border2() + "; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        TableColumn<Course, String> colName = new TableColumn<>("Course Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Course, String> colSchedule = new TableColumn<>("Schedule");
        colSchedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        
        table.getColumns().addAll(colName, colSchedule);
        table.getItems().setAll(courses);
        table.setPrefHeight(300);

        tableBox.getChildren().addAll(tableTitle, table);

        content.getChildren().addAll(header, stats, tableBox);
        return content;
    }
}
