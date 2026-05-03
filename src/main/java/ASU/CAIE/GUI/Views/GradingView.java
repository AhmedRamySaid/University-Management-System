package ASU.CAIE.GUI.Views;

import ASU.CAIE.model.User;
import ASU.CAIE.model.Grade;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class GradingView {

    private String calculateLetterGrade(double score) {
        if (score >= 97) return "A+";
        if (score >= 93) return "A";
        if (score >= 90) return "A-";
        if (score >= 87) return "B+";
        if (score >= 83) return "B";
        if (score >= 80) return "B-";
        if (score >= 77) return "C+";
        if (score >= 73) return "C";
        if (score >= 70) return "C-";
        if (score >= 67) return "D+";
        if (score >= 63) return "D";
        if (score >= 60) return "D-";
        return "F";
    }
    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        VBox header = new VBox(4);
        Label title = styledLabel("Submit Grade", 24, text());
        title.setStyle(title.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("Enter a student's grade for a course.", 14, text2());
        header.getChildren().addAll(title, sub);

        VBox form = new VBox(14);
        form.setPadding(new Insets(24));
        form.setStyle(
                "-fx-background-color: " + bg() + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 12;"
        );
        form.setMaxWidth(500);

        Label lStudentId  = formLabel("Student ID");
        TextField fStudentId = inputField("e.g. 1");

        Label lCourseId   = formLabel("Course ID");
        TextField fCourseId  = inputField("e.g. 101");

        Label lScore      = formLabel("Score (0–100)");
        TextField fScore     = inputField("e.g. 88.5");

        Label lSemester   = formLabel("Semester");
        TextField fSemester  = inputField("e.g. Fall 2025");

        Label lPreview    = formLabel("Letter Grade Preview");
        Label lGradeOut   = styledLabel("—", 20, "#3498db");
        lGradeOut.setStyle(lGradeOut.getStyle() + " -fx-font-weight: bold;");

        fScore.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double score = Double.parseDouble(newVal.trim());
                if (score >= 0 && score <= 100) {
                    String letter = calculateLetterGrade(score);
                    lGradeOut.setText(letter);
                    String color = switch (letter) {
                        case "A" -> "#059669";
                        case "B" -> "#2563eb";
                        case "C" -> "#d97706";
                        case "D" -> "#ea580c";
                        default  -> "#dc2626";
                    };
                    lGradeOut.setStyle(
                            "-fx-text-fill: " + color + ";" +
                                    "-fx-font-size: 20px;" +
                                    "-fx-font-weight: bold;"
                    );
                } else {
                    lGradeOut.setText("—");
                    lGradeOut.setStyle("-fx-text-fill: #3498db; -fx-font-size: 20px; -fx-font-weight: bold;");
                }
            } catch (NumberFormatException e) {
                lGradeOut.setText("—");
                lGradeOut.setStyle("-fx-text-fill: #3498db; -fx-font-size: 20px; -fx-font-weight: bold;");
            }
        });

        Label resultLabel = styledLabel("", 13, "#2ecc71");

        Button submitBtn = primaryButton("Submit Grade");
        submitBtn.setMaxWidth(200);
        submitBtn.setOnAction(e -> {
            try {
                int    studentId = Integer.parseInt(fStudentId.getText().trim());
                int    courseId  = Integer.parseInt(fCourseId.getText().trim());
                double score     = Double.parseDouble(fScore.getText().trim());
                String semester  = fSemester.getText().trim();

                if (score < 0 || score > 100) {
                    resultLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px;");
                    resultLabel.setText("Score must be between 0 and 100.");
                    return;
                }
                if (semester.isEmpty()) {
                    resultLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px;");
                    resultLabel.setText("Please enter the semester.");
                    return;
                }

                String letterGrade = calculateLetterGrade(score);

                resultLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 13px;");
                resultLabel.setText("Grade submitted successfully! (" + letterGrade + ")");

                fStudentId.clear();
                fCourseId.clear();
                fScore.clear();
                fSemester.clear();
                lGradeOut.setText("—");
                lGradeOut.setStyle("-fx-text-fill: #3498db; -fx-font-size: 20px; -fx-font-weight: bold;");

            } catch (NumberFormatException ex) {
                resultLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px;");
                resultLabel.setText("Please enter valid numeric values for ID and Score.");
            }
        });

        form.getChildren().addAll(
                lStudentId,  fStudentId,
                lCourseId,   fCourseId,
                lScore,      fScore,
                lSemester,   fSemester,
                lPreview,    lGradeOut,
                vspace(8),   submitBtn,
                resultLabel
        );

        content.getChildren().addAll(header, form);
        return content;
    }
}