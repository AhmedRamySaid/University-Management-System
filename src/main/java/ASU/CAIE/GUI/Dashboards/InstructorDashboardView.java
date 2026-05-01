package ASU.CAIE.GUI.Dashboards;

import ASU.CAIE.model.User;
import ASU.CAIE.model.Course;

import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class InstructorDashboardView {
    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        // Header Section
        VBox header = new VBox(4);
        Label welcome = styledLabel("Instructor Portal", 24, text());
        welcome.setStyle(welcome.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("Manage your courses and student performance.", 14, text2());
        header.getChildren().addAll(welcome, sub);

        // Stats Cards
        HBox stats = new HBox(20);

		//todo: implement
        int pending = 0;
        VBox pendingCard = dashboardCard("Pending Grading", String.valueOf(pending), "#e67e22");
        
        List<Course> courses = new ArrayList<Course>();
        VBox coursesCard = dashboardCard("Active Courses", String.valueOf(courses.size()), "#3498db");
        
        VBox studentsCard = dashboardCard("Total Students", "142", "#9b59b6");
        
        stats.getChildren().addAll(pendingCard, coursesCard, studentsCard);

        // Table Section
        VBox tableBox = new VBox(16);
        Label tableTitle = styledLabel("Assigned Courses", 18, text());
        tableTitle.setStyle(tableTitle.getStyle() + " -fx-font-weight: bold;");

        TableView<Course> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle("-fx-background-color: " + bg() + "; -fx-border-color: " + border2() + "; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        TableColumn<Course, String> colName = new TableColumn<>("Course Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Course, Integer> colCredits = new TableColumn<>("Credits");
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        
        table.getColumns().addAll(colName, colCredits);
        table.getItems().setAll(courses);
        table.setPrefHeight(300);

        tableBox.getChildren().addAll(tableTitle, table);

        content.getChildren().addAll(header, stats, tableBox);
        return content;
    }
}
