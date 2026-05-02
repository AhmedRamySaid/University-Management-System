package ASU.CAIE.GUI.Views;

import ASU.CAIE.model.Student;
import ASU.CAIE.model.User;
import ASU.CAIE.model.Grade;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class GradesView {
    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");
		if (!(user instanceof Student student)) return new Label("Incorrect View");

        List<Grade> grades = student.GetCourseGrades();
        double gpa = student.CalculateGPA();

        // Header
        VBox header = new VBox(4);
        Label title = styledLabel("My Grades", 24, text());
        title.setStyle(title.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("Full breakdown of your academic record.", 14, text2());
        header.getChildren().addAll(title, sub);

        // GPA Card
        HBox stats = new HBox(20);
        VBox gpaCard   = dashboardCard("Cumulative GPA", String.format("%.2f", gpa), "#3498db");
        VBox countCard = dashboardCard("Total Courses",  String.valueOf(grades.size()), "#9b59b6");
        stats.getChildren().addAll(gpaCard, countCard);

        // Grades Table
        TableView<Grade> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle(
                "-fx-background-color: " + bg() + ";" +
                        "-fx-border-color: " + border2() + ";" +
                        "-fx-border-radius: 8; -fx-background-radius: 8;"
        );

        TableColumn<Grade, Integer> colCourse   = new TableColumn<>("Course ID");
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Grade, Double>  colScore    = new TableColumn<>("Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<Grade, String>  colLetter   = new TableColumn<>("Grade");
        colLetter.setCellValueFactory(new PropertyValueFactory<>("letterGrade"));

        TableColumn<Grade, String>  colSemester = new TableColumn<>("Semester");
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));

        // Color the grade cell
        colLetter.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                String color = switch (item) {
                    case "A"  -> "#2ecc71";
                    case "B"  -> "#3498db";
                    case "C"  -> "#f39c12";
                    case "D"  -> "#e67e22";
                    default   -> "#e74c3c";
                };
                setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
            }
        });

        table.getColumns().addAll(colCourse, colScore, colLetter, colSemester);
        table.getItems().setAll(grades);
        table.setPrefHeight(350);

        VBox tableBox = new VBox(16);
        Label tableTitle = styledLabel("Grade Details", 18, text());
        tableTitle.setStyle(tableTitle.getStyle() + " -fx-font-weight: bold;");
        tableBox.getChildren().addAll(tableTitle, table);

        content.getChildren().addAll(header, stats, tableBox);
        return content;
    }
}