package ASU.CAIE.GUI.Views;

import ASU.CAIE.model.User;
import ASU.CAIE.model.Grade;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class PerformanceDashboardView {
    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        // Header
        VBox header = new VBox(4);
        Label title = styledLabel("Performance Overview", 24, text());
        title.setStyle(title.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("Track your academic performance over time.", 14, text2());
        header.getChildren().addAll(title, sub);

        List<Grade> grades = new ArrayList<Grade>();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis   yAxis = new NumberAxis(0, 100, 10);
        xAxis.setLabel("Course");
        yAxis.setLabel("Score");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Scores by Course");
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(280);
        barChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Grade g : grades) {
            series.getData().add(
                    new XYChart.Data<>("Course " + g.getCourseId(), g.getScore())
            );
        }
        barChart.getData().add(series);

        // Summary Cards
        HBox stats = new HBox(20);

        double avg = grades.stream()
                .mapToDouble(Grade::getScore)
                .average()
                .orElse(0.0);

        long aCount = grades.stream().filter(g -> "A".equals(g.getLetterGrade())).count();
        long fCount = grades.stream().filter(g -> "F".equals(g.getLetterGrade())).count();

        VBox avgCard  = dashboardCard("Average Score", String.format("%.1f", avg), "#3498db");
        VBox aCard    = dashboardCard("A Grades",      String.valueOf(aCount),     "#2ecc71");
        VBox failCard = dashboardCard("Failed Courses", String.valueOf(fCount),    "#e74c3c");
        stats.getChildren().addAll(avgCard, aCard, failCard);

        content.getChildren().addAll(header, stats, barChart);
        return content;
    }
}