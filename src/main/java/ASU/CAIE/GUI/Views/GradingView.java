package ASU.CAIE.GUI.Views;

import ASU.CAIE.Users.User;
import ASU.CAIE.model.Grade;
import ASU.CAIE.service.GradingService;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import static ASU.CAIE.GUI.ComponentFactory.*;
import static ASU.CAIE.GUI.ThemeManager.*;

public class GradingView {

    private final GradingService service = new GradingService();

    public Node build() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        // Header
        VBox header = new VBox(4);
        Label title = styledLabel("Submit Grade", 24, text());
        title.setStyle(title.getStyle() + " -fx-font-weight: bold;");
        Label sub = styledLabel("Enter a student's grade for a course.", 14, text2());
        header.getChildren().addAll(title, sub);

        // Form Card
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

        // Fields
        Label lStudentId  = formLabel("Student ID");
        TextField fStudentId  = inputField("e.g. 1");

        Label lCourseId   = formLabel("Course ID");
        TextField fCourseId   = inputField("e.g. 101");

        Label lScore      = formLabel("Score (0–100)");
        TextField fScore      = inputField("e.g. 88.5");

        Label lSemester   = formLabel("Semester");
        TextField fSemester   = inputField("e.g. Fall 2025");

        Label lPreview    = formLabel("Letter Grade Preview");
        Label lGradeOut   = styledLabel("—", 20, "#3498db");
        lGradeOut.setStyle(lGradeOut.getStyle() + " -fx-font-weight: bold;");


        fScore.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double score = Double.parseDouble(newVal.trim());
                lGradeOut.setText(service.calculateLetterGrade(score));
            } catch (NumberFormatException e) {
                lGradeOut.setText("—");
            }
        });

        // Result Label
        Label resultLabel = styledLabel("", 13, "#2ecc71");

        // Submit Button
        Button submitBtn = primaryButton("Submit Grade");
        submitBtn.setMaxWidth(200);
        submitBtn.setOnAction(e -> {
            try {
                int    studentId   = Integer.parseInt(fStudentId.getText().trim());
                int    courseId    = Integer.parseInt(fCourseId.getText().trim());
                double score       = Double.parseDouble(fScore.getText().trim());
                String semester    = fSemester.getText().trim();

                // Validation
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


                Grade grade = new Grade();
                grade.setStudentId(studentId);
                grade.setCourseId(courseId);
                grade.setInstructorId(user.getId());
                grade.setScore(score);
                grade.setSemester(semester);

                boolean ok = service.submitGrade(grade);

                if (ok) {
                    resultLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 13px;");
                    resultLabel.setText("Grade submitted successfully! (" + grade.getLetterGrade() + ")");
                    // Clear form
                    fStudentId.clear(); fCourseId.clear();
                    fScore.clear(); fSemester.clear();
                    lGradeOut.setText("—");
                } else {
                    resultLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 13px;");
                    resultLabel.setText("Failed to submit grade. Please try again.");
                }

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