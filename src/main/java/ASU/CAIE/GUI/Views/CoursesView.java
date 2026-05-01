package ASU.CAIE.GUI.Views;

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class CoursesView {
    public Node build() {
        VBox content = new VBox(28);
        content.setPadding(new Insets(36, 40, 36, 40));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        Label title = styledLabel("Courses", 26, text());
        title.setStyle(title.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("All courses associated with your account.", 14, text2());

		//todo: implement
        List<Course> courses = switch (user.GetRole()) {
            case PROFESSOR -> new ArrayList<Course>();
            default        -> new ArrayList<Course>();
        };

        // Stats
        HBox stats = new HBox(16);
        VBox totalCard = dashboardCard("Total Courses", String.valueOf(courses.size()), "#3498db");
        HBox.setHgrow(totalCard, Priority.ALWAYS);
        stats.getChildren().add(totalCard);

        // Table
        TableView<Course> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(400);
        table.setStyle(
                "-fx-background-color: " + bg() + ";" +
                        "-fx-border-color: " + border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;"
        );

        TableColumn<Course, Integer> colId      = new TableColumn<>("Course ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Course, String>  colName    = new TableColumn<>("Course Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, String>  colSched   = new TableColumn<>("Schedule");
        colSched.setCellValueFactory(new PropertyValueFactory<>("schedule"));

        TableColumn<Course, Integer> colCredits = new TableColumn<>("Credits");
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

        table.getColumns().addAll(colId, colName, colSched, colCredits);
        table.getItems().setAll(courses);

        VBox tableBox = new VBox(14);
        Label tableTitle = styledLabel("Course List", 17, text());
        tableTitle.setStyle(tableTitle.getStyle() + " -fx-font-weight: bold;");
        tableBox.getChildren().addAll(tableTitle, table);

        content.getChildren().addAll(title, sub, stats, tableBox);
        return content;
    }
}