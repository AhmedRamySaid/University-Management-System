package ASU.CAIE.GUI.Views;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Role;
import ASU.CAIE.model.User;
import ASU.CAIE.model.Course;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;
import java.util.Optional;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class CoursesView {
	public Node build() {
		VBox content = new VBox(28);
		content.setPadding(new Insets(36, 40, 36, 40));
		content.setStyle("-fx-background-color: transparent;");

		User user = SessionManager.getInstance().getCurrentUser();
		if (user == null) return new Label("Not logged in");

		boolean isAdmin = user.GetRole() == Role.ADMIN;

		List<Course> courses;
		if (user.GetRole() == Role.PROFESSOR) {
			courses = user.GetTakenCourses();
		} else {
			if (Course.courseList != null) courses = Course.courseList;
			else {
				courses = DatabaseManager.CourseDaoInstance.getAllCourses();
				Course.courseList = courses;
			}
		}

		Label title = styledLabel("Courses", 26, text());
		title.setStyle(title.getStyle() + " -fx-font-weight: bold;");
		Label sub = styledLabel("All courses associated with your account.", 14, text2());

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

		TableColumn<Course, Integer> colId = new TableColumn<>("Course ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("courseId"));

		TableColumn<Course, String> colName = new TableColumn<>("Course Name");
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Course, Integer> colCredits = new TableColumn<>("Credits");
		colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

		// Instructor name column — always visible, derived from professorId
		TableColumn<Course, String> colInstructorName = new TableColumn<>("Instructor Name");
		colInstructorName.setCellValueFactory(cellData -> {
			int profId = cellData.getValue().getInstructorId();
			Optional<User> prof = DatabaseManager.UserDaoInstance.GetUser(profId);
			String name = prof.map(User::GetName).orElse("Unknown");
			return new javafx.beans.property.SimpleStringProperty(name);
		});

		table.getColumns().addAll(colId, colName, colCredits, colInstructorName);

		// Admin-only: editable Instructor ID column
		if (isAdmin) {
			table.setEditable(true);

			TableColumn<Course, Integer> colInstructorId = new TableColumn<>("Instructor ID");
			colInstructorId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
			colInstructorId.setEditable(true);

			// Use a safe IntegerStringConverter that reverts on non-numeric input
			colInstructorId.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
				@Override
				public Integer fromString(String value) {
					try {
						return super.fromString(value);
					} catch (NumberFormatException e) {
						return null; // signals invalid input
					}
				}
			}));

			colInstructorId.setOnEditCommit(event -> {
				Course course = event.getRowValue();
				Integer newId = event.getNewValue();
				int oldId = event.getOldValue(); // capture before any changes

				// Revert if input was non-numeric (null) or ID doesn't resolve to a professor
				if (newId == null) {
					course.setInstructorId(oldId);
					table.refresh();
					return;
				}

				Optional<User> newProf = DatabaseManager.UserDaoInstance.GetUser(newId);
				if (newProf.isEmpty() || newProf.get().GetRole() != Role.PROFESSOR) {
					course.setInstructorId(oldId);
					table.refresh();
					return;
				}

				// Commit: update model + persist
				course.setInstructorId(newId);
				DatabaseManager.CourseDaoInstance.updateCourse(course);
				table.refresh();
			});

			table.getColumns().add(colInstructorId);
		}

		table.getItems().setAll(courses);

		VBox tableBox = new VBox(14);
		Label tableTitle = styledLabel("Course List", 17, text());
		tableTitle.setStyle(tableTitle.getStyle() + " -fx-font-weight: bold;");
		tableBox.getChildren().addAll(tableTitle, table);

		content.getChildren().addAll(title, sub, stats, tableBox);

		return content;
	}
}