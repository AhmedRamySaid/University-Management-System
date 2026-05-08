package ASU.CAIE.GUI.Dashboards;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Role;
import ASU.CAIE.model.User;
import ASU.CAIE.util.SceneManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ManageUsersView {

    public Node build() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f4f6f9;");

        // ================= BACK BUTTON =================
        Button backBtn = new Button("← Back to Dashboard");

        backBtn.setStyle(
                "-fx-background-color: #111827;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 8 14 8 14;" +
                "-fx-background-radius: 8;"
        );

        backBtn.setOnAction(e -> {
            DashboardFrame dashboardFrame = new DashboardFrame(
                    "Overview",
                    () -> new AdminDashboardView().build()
            );
            SceneManager.setRoot(dashboardFrame.getNode());
        });

        // ================= TITLE =================
        Label title = new Label("Manage Users");

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #111827;"
        );

        VBox top = new VBox(10, backBtn, title);
        root.setTop(top);

        // ================= TABLE =================
        TableView<User> table = new TableView<>();

        table.setMaxWidth(1000);
        table.setPrefHeight(600);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        table.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 18;" +
                "-fx-border-radius: 18;" +
                "-fx-border-color: #e5e7eb;" +
                "-fx-border-width: 1;" +
                "-fx-padding: 14;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 18, 0, 0, 4);"
        );

        // ================= COLUMNS =================
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().GetID()));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().GetName()));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().GetEmail()));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().GetRole().toString()));

        // ================= ROLE EDITOR =================
        roleCol.setCellFactory(col -> new TableCell<>() {

            private final ComboBox<String> combo = new ComboBox<>();

            {
                // FIXED LOGIC: PROFESSOR instead of INSTRUCTOR
                combo.getItems().addAll("STUDENT", "PROFESSOR");

                combo.setStyle(
                        "-fx-background-color: #eff6ff;" +
                        "-fx-text-fill: #2563eb;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: #bfdbfe;" +
                        "-fx-padding: 2 8 2 8;"
                );

                combo.setPrefWidth(140);

                combo.setOnAction(e -> {

                    if (getIndex() < 0 || getIndex() >= getTableView().getItems().size())
                        return;

                    User user = getTableView().getItems().get(getIndex());

                    Role newRole = Role.valueOf(combo.getValue());

                    user.SetRole(newRole);

                    Task<Void> task = new Task<>() {
                        @Override
                        protected Void call() {
                            DatabaseManager.UserDaoInstance.UpdateUserRole(
                                    user.GetID(),
                                    newRole
                            );
                            return null;
                        }
                    };

                    new Thread(task).start();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    combo.setValue(item);
                    setGraphic(new HBox(combo));
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, emailCol, roleCol);

        idCol.setStyle("-fx-alignment: CENTER;");
        nameCol.setStyle("-fx-alignment: CENTER;");
        emailCol.setStyle("-fx-alignment: CENTER;");
        roleCol.setStyle("-fx-alignment: CENTER;");

        // ================= ROW STYLE =================
        table.setRowFactory(tv -> {

            TableRow<User> row = new TableRow<>();
            row.setPrefHeight(56);

            row.hoverProperty().addListener((obs, oldVal, newVal) -> {

                if (!row.isEmpty()) {

                    if (newVal) {
                        row.setStyle(
                                "-fx-background-color: #f8fafc;" +
                                "-fx-border-color: transparent transparent #eef2f7 transparent;" +
                                "-fx-border-width: 0 0 1 0;"
                        );
                    } else {
                        row.setStyle(
                                "-fx-background-color: white;" +
                                "-fx-border-color: transparent transparent #f1f5f9 transparent;" +
                                "-fx-border-width: 0 0 1 0;"
                        );
                    }
                }
            });

            return row;
        });

        // ================= LOADING =================
        VBox loadingBox = new VBox(15);
        loadingBox.setAlignment(Pos.CENTER);

        ProgressIndicator loading = new ProgressIndicator();
        Label loadingLabel = new Label("Loading users...");
        loadingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6b7280;");

        loadingBox.getChildren().addAll(loading, loadingLabel);

        root.setCenter(loadingBox);

        // ================= LOAD USERS =================
        Task<List<User>> loadTask = new Task<>() {
            @Override
            protected List<User> call() {
                return DatabaseManager.UserDaoInstance.GetAllUsers();
            }
        };

        loadTask.setOnSucceeded(e -> {
            table.getItems().setAll(loadTask.getValue());
            root.setCenter(table);
        });

        loadTask.setOnFailed(e -> {
            Label error = new Label("Failed to load users.");
            error.setStyle("-fx-text-fill: red;");
            root.setCenter(error);
        });

        new Thread(loadTask).start();

        return root;
    }
}