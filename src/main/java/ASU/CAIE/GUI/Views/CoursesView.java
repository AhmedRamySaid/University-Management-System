package ASU.CAIE.GUI.Views;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Role;
import ASU.CAIE.model.User;
import ASU.CAIE.model.Course;
import ASU.CAIE.util.SessionManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class CoursesView {
	private static final int NEW_ROW_SENTINEL = -1;

	// Cached view node — rebuilt only once, reused on tab switches
	private Node cachedView = null;

	public Node build() {
		if (cachedView != null) return cachedView;

		VBox content = new VBox(28);
		content.setPadding(new Insets(36, 40, 36, 40));
		content.setStyle("-fx-background-color: transparent;");

		User user = SessionManager.getInstance().getCurrentUser();
		if (user == null) return new Label("Not logged in");

		boolean isAdmin = user.GetRole() == Role.ADMIN;

		Label title = styledLabel("Courses", 26, text());
		title.setStyle(title.getStyle() + " -fx-font-weight: bold;");
		Label sub = styledLabel("All courses associated with your account.", 14, text2());

		HBox stats = new HBox(16);

		// Instructor name cache: instructorId -> display name
		// ConcurrentHashMap so the async loader can populate it safely
		Map<Integer, String> instructorNameCache = new ConcurrentHashMap<>();

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

		// --- Course ID (never editable) ---
		TableColumn<Course, Integer> colId = new TableColumn<>("Course ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
		colId.setEditable(false);
		colId.setCellFactory(col -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null || item == NEW_ROW_SENTINEL) setText("");
				else setText(item.toString());
			}
		});

		// --- Course Name ---
		TableColumn<Course, String> colName = new TableColumn<>("Course Name");
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// --- Credits ---
		TableColumn<Course, Integer> colCredits = new TableColumn<>("Credits");
		colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

		// --- Instructor Name: reads from cache only, zero DB calls on render ---
		TableColumn<Course, String> colInstructorName = new TableColumn<>("Instructor Name");
		colInstructorName.setEditable(false);
		colInstructorName.setCellValueFactory(cellData -> {
			int profId = cellData.getValue().getInstructorId();
			if (profId == NEW_ROW_SENTINEL)
				return new javafx.beans.property.SimpleStringProperty("");
			// Cache lookup only — no DB call here
			String name = instructorNameCache.getOrDefault(profId, "...");
			return new javafx.beans.property.SimpleStringProperty(name);
		});

		table.getColumns().addAll(colId, colName, colCredits, colInstructorName);

		VBox tableBox = new VBox(14);
		Label tableTitle = styledLabel("Course List", 17, text());
		tableTitle.setStyle(tableTitle.getStyle() + " -fx-font-weight: bold;");

		if (isAdmin) {
			table.setEditable(true);
			Set<Course> dirtyRows = new HashSet<>();

			// --- Editable: Course Name ---
			colName.setEditable(true);
			colName.setCellFactory(TextFieldTableCell.forTableColumn());
			colName.setOnEditCommit(event -> {
				Course course = event.getRowValue();
				String newName = event.getNewValue();
				String oldName = event.getOldValue();

				if (newName == null || newName.isBlank()) {
					course.setName(oldName);
					table.refresh();
					return;
				}

				course.setName(newName);
				dirtyRows.add(course);
				showConfirmBtn(content); // defined below
			});

			// --- Editable: Credits ---
			colCredits.setEditable(true);
			colCredits.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
				@Override
				public Integer fromString(String value) {
					try { return super.fromString(value); }
					catch (NumberFormatException e) { return null; }
				}
			}));
			colCredits.setOnEditCommit(event -> {
				Course course = event.getRowValue();
				Integer newCredits = event.getNewValue();
				int oldCredits = event.getOldValue();

				if (newCredits == null || newCredits <= 0) {
					course.setCredits(oldCredits);
					table.refresh();
					return;
				}

				course.setCredits(newCredits);
				dirtyRows.add(course);
				showConfirmBtn(content);
			});

			// --- Editable: Instructor ID ---
			TableColumn<Course, Integer> colInstructorId = new TableColumn<>("Instructor ID");
			colInstructorId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
			colInstructorId.setEditable(true);
			colInstructorId.setCellFactory(col -> new TextFieldTableCell<>(new IntegerStringConverter() {
				@Override
				public Integer fromString(String value) {
					try { return super.fromString(value); }
					catch (NumberFormatException e) { return null; }
				}
			}) {
				@Override
				public void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);
					if (!empty && item != null && item == NEW_ROW_SENTINEL) setText("");
				}
			});
			colInstructorId.setOnEditCommit(event -> {
				Course course = event.getRowValue();
				Integer newId = event.getNewValue();
				int oldId = event.getOldValue();

				if (newId == null) {
					course.setInstructorId(oldId);
					table.refresh();
					return;
				}

				// Validate async — no DB call on FX thread
				// Optimistically apply; revert in confirm if invalid
				DatabaseManager.Users.GetUser(newId)
						.thenAcceptAsync(optUser -> {
							if (optUser.isEmpty() || optUser.get().GetRole() != Role.PROFESSOR) {
								// Invalid: revert on FX thread
								course.setInstructorId(oldId);
								table.refresh();
							} else {
								// Valid: update name cache and mark dirty
								instructorNameCache.put(newId, optUser.get().GetName());
								course.setInstructorId(newId);
								dirtyRows.add(course);
								showConfirmBtn(content);
								table.refresh();
							}
						}, Platform::runLater);
				// No table.refresh() here — wait for the async result above
			});

			table.getColumns().add(colInstructorId);

			// --- Buttons ---
			Button addBtn = new Button("+ Add Course");
			addBtn.setStyle(
					"-fx-background-color: #3498db;" +
							"-fx-text-fill: white;" +
							"-fx-font-size: 13px;" +
							"-fx-padding: 8 18 8 18;" +
							"-fx-border-radius: 8;" +
							"-fx-background-radius: 8;" +
							"-fx-cursor: hand;"
			);

			Button confirmBtn = new Button("✓ Confirm");
			confirmBtn.setStyle(
					"-fx-background-color: #27ae60;" +
							"-fx-text-fill: white;" +
							"-fx-font-size: 13px;" +
							"-fx-padding: 8 18 8 18;" +
							"-fx-border-radius: 8;" +
							"-fx-background-radius: 8;" +
							"-fx-cursor: hand;"
			);
			confirmBtn.setVisible(false);
			confirmBtn.setManaged(false);

			// Store confirmBtn reference so showConfirmBtn() can reach it
			confirmBtn.setUserData("confirmBtn");
			content.setUserData(confirmBtn);

			addBtn.setOnAction(e -> {
				boolean alreadyHasPlaceholder = table.getItems().stream()
						.anyMatch(c -> c.getCourseId() == NEW_ROW_SENTINEL);
				if (alreadyHasPlaceholder) return;

				// Construct placeholder with sentinel in instructorId explicitly
				Course placeholder = new Course("", 0, NEW_ROW_SENTINEL);
				placeholder.setInstructorId(NEW_ROW_SENTINEL);

				table.getItems().add(placeholder);
				table.scrollTo(placeholder);
				table.getSelectionModel().select(placeholder);

				confirmBtn.setVisible(true);
				confirmBtn.setManaged(true);
			});

			confirmBtn.setOnAction(e -> {
				confirmBtn.setDisable(true);

				Course placeholder = table.getItems().stream()
						.filter(c -> c.getCourseId() == NEW_ROW_SENTINEL)
						.findFirst()
						.orElse(null);

				// Validate placeholder before firing any DB calls
				if (placeholder != null) {
					if (placeholder.getName() == null || placeholder.getName().isBlank()
							|| placeholder.getCredits() <= 0
							|| placeholder.getInstructorId() == NEW_ROW_SENTINEL) {
						showWarning("Incomplete Course",
								"Please fill in Name, Credits, and a valid Instructor ID before confirming.");
						confirmBtn.setDisable(false);
						return;
					}
				}

				List<CompletableFuture<?>> futures = new ArrayList<>();

				// Persist dirty existing rows
				for (Course dirty : dirtyRows) {
					if (dirty.getCourseId() != NEW_ROW_SENTINEL) {
						futures.add(
								DatabaseManager.runAsync(
										() -> DatabaseManager.CourseDaoInstance.updateCourse(dirty)
								)
						);
					}
				}

				// Insert new course if placeholder exists
				if (placeholder != null) {
					final Course newCourse = new Course(
							placeholder.getName(),
							placeholder.getCredits(),
							placeholder.getInstructorId()
					);
					futures.add(
							DatabaseManager.runAsync(
									() -> DatabaseManager.CourseDaoInstance.updateCourse(newCourse)
							).thenAcceptAsync(success -> {
								table.getItems().remove(placeholder);
								table.getItems().add(newCourse);
							}, Platform::runLater)
					);
				}

				CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
						.thenRunAsync(() -> {
							dirtyRows.clear();
							confirmBtn.setDisable(false);
							confirmBtn.setVisible(false);
							confirmBtn.setManaged(false);
							table.refresh();
						}, Platform::runLater);
			});

			HBox buttonRow = new HBox(10, addBtn, confirmBtn);
			buttonRow.setAlignment(Pos.CENTER_LEFT);
			tableBox.getChildren().addAll(tableTitle, buttonRow, table);

		} else {
			tableBox.getChildren().addAll(tableTitle, table);
		}

		content.getChildren().addAll(title, sub, stats, tableBox);

		// --- Async course + instructor name loading ---
		Label loadingLabel = styledLabel("Loading courses...", 14, text2());
		content.getChildren().add(loadingLabel);

		CompletableFuture<List<Course>> coursesFuture;

		if (user.GetRole() == Role.PROFESSOR) {
			// Professor course list comes from the user model; wrap in a future
			// so the rest of the load pipeline is uniform
			coursesFuture = CompletableFuture.completedFuture(user.GetTakenCourses());
		} else {
			coursesFuture = DatabaseManager.Courses.getAllCourses();
		}

		coursesFuture
				// Resolve all instructor names in bulk on the background thread
				.thenApplyAsync(courses -> {
					for (Course c : courses) {
						int id = c.getInstructorId();
						if (id != NEW_ROW_SENTINEL && !instructorNameCache.containsKey(id)) {
							DatabaseManager.UserDaoInstance.GetUser(id)
									.ifPresent(u -> instructorNameCache.put(id, u.GetName()));
						}
					}
					return courses;
				}, runnable -> DatabaseManager.runAsync(() -> { runnable.run(); return null; }))
				// Back to FX thread to update UI
				.thenAcceptAsync(courses -> {
					content.getChildren().remove(loadingLabel);
					table.getItems().setAll(courses);
					rebuildStatsCard(stats, courses.size());
				}, Platform::runLater)
				.exceptionally(ex -> {
					Platform.runLater(() -> {
						content.getChildren().remove(loadingLabel);
						content.getChildren().add(
								styledLabel("Failed to load courses.", 14, "#e74c3c"));
					});
					return null;
				});

		cachedView = content;
		return content;
	}

	/** Makes the confirm button visible; called from edit handlers. */
	private void showConfirmBtn(VBox content) {
		if (content.getUserData() instanceof Button btn) {
			btn.setVisible(true);
			btn.setManaged(true);
		}
	}

	private void rebuildStatsCard(HBox stats, int count) {
		stats.getChildren().clear();
		VBox card = dashboardCard("Total Courses", String.valueOf(count), "#3498db");
		HBox.setHgrow(card, Priority.ALWAYS);
		stats.getChildren().add(card);
	}

	private void showWarning(String title, String msg) {
		Platform.runLater(() -> {
			Alert a = new Alert(Alert.AlertType.WARNING);
			a.setTitle(title);
			a.setHeaderText(null);
			a.setContentText(msg);
			a.showAndWait();
		});
	}

	private void showError(String title, String msg) {
		Platform.runLater(() -> {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle(title);
			a.setHeaderText(null);
			a.setContentText(msg);
			a.showAndWait();
		});
	}
}