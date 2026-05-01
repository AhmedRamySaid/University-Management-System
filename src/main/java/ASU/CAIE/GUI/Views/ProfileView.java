package ASU.CAIE.GUI.Views;

import ASU.CAIE.Users.User;
import ASU.CAIE.util.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static ASU.CAIE.GUI.ComponentFactory.*;
import static ASU.CAIE.GUI.ComponentFactory.styledLabel;
import static ASU.CAIE.GUI.ThemeManager.*;
import static ASU.CAIE.GUI.ThemeManager.bg;
import static ASU.CAIE.GUI.ThemeManager.bg2;
import static ASU.CAIE.GUI.ThemeManager.border2;
import static ASU.CAIE.GUI.ThemeManager.text;
import static ASU.CAIE.GUI.ThemeManager.text2;

public class ProfileView {

    public Node build() {
        VBox content = new VBox(28);
        content.setPadding(new Insets(36, 40, 36, 40));
        content.setStyle("-fx-background-color: transparent;");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return new Label("Not logged in");

        // Header
        Label title = styledLabel("My Profile", 26, text());
        title.setStyle(title.getStyle() + " -fx-font-weight: bold;");

        // Avatar + Name Card
        VBox profileCard = new VBox(16);
        profileCard.setPadding(new Insets(32));
        profileCard.setAlignment(Pos.CENTER);
        profileCard.setStyle(
                "-fx-background-color: " + bg() + ";" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 16;"
        );


        StackPane avatar = new StackPane();
        Circle circle = new Circle(40, Color.web("#3498db"));
        String initial = user.getName() != null && !user.getName().isEmpty()
                ? String.valueOf(user.getName().charAt(0)).toUpperCase() : "?";
        Label initialLbl = styledLabel(initial, 28, "#ffffff");
        initialLbl.setStyle(initialLbl.getStyle() + " -fx-font-weight: bold;");
        avatar.getChildren().addAll(circle, initialLbl);

        Label nameLbl  = styledLabel(user.getName(), 22, text());
        nameLbl.setStyle(nameLbl.getStyle() + " -fx-font-weight: bold;");
        Label emailLbl = styledLabel(user.getEmail() != null ? user.getEmail() : "—", 14, text2());

        // Role Badge
        String roleColor = switch (user.getRole()) {
            case STUDENT   -> "#3498db";
            case PROFESSOR -> "#9b59b6";
            case ADMIN     -> "#e74c3c";
            case PARENT    -> "#2ecc71";
            default        -> "#95a5a6";
        };
        Label roleBadge = new Label(user.getRole().name());
        roleBadge.setStyle(
                "-fx-background-color: " + roleColor + "22;" +
                        "-fx-text-fill: " + roleColor + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 4 12 4 12;" +
                        "-fx-background-radius: 20;"
        );

        profileCard.getChildren().addAll(avatar, nameLbl, emailLbl, roleBadge);

        // Info Grid
        VBox infoCard = new VBox(0);
        infoCard.setStyle(
                "-fx-background-color: " + bg() + ";" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: " + border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 16;"
        );

        infoCard.getChildren().addAll(
                buildInfoRow("Full Name",  user.getName()  != null ? user.getName()  : "—", true),
                buildInfoRow("Email",      user.getEmail() != null ? user.getEmail() : "—", false),
                buildInfoRow("Role",       user.getRole().name(), true),
                buildInfoRow("User ID",    String.valueOf(user.getId()), false),
                buildInfoRow("Status",     "Active", true)
        );

        content.getChildren().addAll(title, profileCard, infoCard);
        return content;
    }

    private HBox buildInfoRow(String label, String value, boolean shaded) {
        HBox row = new HBox();
        row.setPadding(new Insets(14, 24, 14, 24));
        row.setAlignment(Pos.CENTER_LEFT);
        if (shaded) {
            row.setStyle("-fx-background-color: " + bg2() + ";");
        }

        Label lbl = styledLabel(label, 13, text2());
        lbl.setMinWidth(140);

        Label val = styledLabel(value, 13, text());
        val.setStyle(val.getStyle() + " -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(lbl, spacer, val);
        return row;
    }
}