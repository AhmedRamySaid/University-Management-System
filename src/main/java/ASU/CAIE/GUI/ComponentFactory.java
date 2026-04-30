package ASU.CAIE.GUI;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import static ASU.CAIE.GUI.ThemeManager.*;

public class ComponentFactory {

    // ── Labels ────────────────────────────────────────────────────────────────

    public static Label styledLabel(String text, int size, String color) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size + "px;");
        return l;
    }

    public static Label formLabel(String text) {
        Label l = styledLabel(text, 12, text2());
        l.setStyle(l.getStyle() + " -fx-font-weight: bold;");
        return l;
    }

    public static Label hintLabel(String text) {
        Label l = styledLabel(text, 11, text3());
        l.setMinHeight(14);
        return l;
    }

    // ── Input fields ──────────────────────────────────────────────────────────

    public static TextField inputField(String placeholder) {
        TextField f = new TextField();
        f.setPromptText(placeholder);
        f.setStyle(inputStyle());
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        f.focusedProperty().addListener((o, ov, nv) -> {
            String border = nv ? text() : border2();
            f.setStyle(f.getStyle().replaceAll("-fx-border-color: [^;]+;",
                    "-fx-border-color: " + border + ";"));
        });
        return f;
    }

    public static PasswordField passwordField(String placeholder) {
        PasswordField f = new PasswordField();
        f.setPromptText(placeholder);
        f.setStyle(
                "-fx-background-color: " + bg() + ";" +
                        "-fx-border-color: "      + border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: "         + text() + ";" +
                        "-fx-prompt-text-fill: "  + text3() + ";" +
                        "-fx-padding: 10 40 10 12;"
        );
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }

    public static HBox passwordRow(PasswordField pf) {
        Button eye = new Button("◉");
        eye.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + text3() + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0;"
        );
        eye.setPrefSize(34, 42);
        eye.setCursor(Cursor.HAND);

        TextField vis = new TextField();
        vis.setStyle(pf.getStyle());
        vis.setVisible(false);
        vis.setManaged(false);
        vis.setMaxWidth(Double.MAX_VALUE);
        vis.textProperty().bindBidirectional(pf.textProperty());

        eye.setOnAction(e -> {
            boolean showing = vis.isVisible();
            vis.setVisible(!showing);
            vis.setManaged(!showing);
            pf.setVisible(showing);
            pf.setManaged(showing);
            eye.setText(showing ? "◎" : "◉");
        });

        StackPane stack = new StackPane();
        stack.setMaxWidth(Double.MAX_VALUE);
        stack.getChildren().addAll(pf, vis, eye);
        StackPane.setAlignment(eye, Pos.CENTER_RIGHT);

        HBox row = new HBox(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
        row.setMaxWidth(Double.MAX_VALUE);
        return row;
    }

    public static StackPane inputWrap(TextField f) {
        return new StackPane(f);
    }

    // ── Buttons ───────────────────────────────────────────────────────────────

    public static Button primaryButton(String text) {
        boolean dark = isDark();
        Button b = new Button(text);
        b.setPrefHeight(44);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(Cursor.HAND);
        b.setStyle(
                "-fx-background-color: " + (dark ? "#f0f0f0" : "#111111") + ";" +
                        "-fx-text-fill: "        + (dark ? "#111111" : "#ffffff") + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: " + (dark ? "#d0d0d0" : "#2c2c2a") + ";")));
        b.setOnMouseExited(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: " + (dark ? "#f0f0f0" : "#111111") + ";")));
        return b;
    }

    public static Button ghostButton(String text) {
        Button b = new Button(text);
        b.setPrefHeight(42);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(Cursor.HAND);
        b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: "     + text2() + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: "  + border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: " + bg2() + ";")));
        b.setOnMouseExited(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: transparent;")));
        return b;
    }

    // ── Custom Components ─────────────────────────────────────────────────────

    /** 
     * The exact theme toggle from the login screen.
     */
    public static HBox themeToggle(Runnable onToggle) {
        boolean dark = isDark();
        HBox row = new HBox(7);
        row.setAlignment(Pos.CENTER_RIGHT);
        row.setCursor(Cursor.HAND);

        Label lbl = styledLabel(dark ? "Dark" : "Light", 12, text2());

        Rectangle track = new Rectangle(30, 17);
        track.setArcWidth(17); track.setArcHeight(17);
        track.setFill(Color.web(dark ? "#444444" : "#b0b0b0"));

        Circle thumb = new Circle(6.5, Color.WHITE);
        thumb.setTranslateX(dark ? 6.5 : -6.5);

        StackPane toggle = new StackPane(track, thumb);
        row.getChildren().addAll(lbl, toggle);

        row.setOnMouseClicked(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), thumb);
            tt.setToX(isDark() ? -6.5 : 6.5);
            tt.play();
            tt.setOnFinished(ev -> {
                ThemeManager.toggle();
                if (onToggle != null) onToggle.run();
            });
        });
        return row;
    }

    public static VBox dashboardCard(String title, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(24));
        card.setStyle(
                "-fx-background-color: " + bg() + ";" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 15, 0, 0, 4);"
        );
        HBox.setHgrow(card, Priority.ALWAYS);

        Label t = styledLabel(title, 14, text2());
        t.setStyle(t.getStyle() + " -fx-font-weight: bold;");
        
        Label v = styledLabel(value, 32, color);
        v.setStyle(v.getStyle() + " -fx-font-weight: bold;");

        card.getChildren().addAll(t, v);
        return card;
    }

    public static Button sidebarLink(String text, boolean active, Runnable onAction) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPadding(new Insets(12, 16, 12, 16));
        b.setCursor(Cursor.HAND);
        
        String baseStyle = 
            "-fx-background-color: " + (active ? (isDark() ? "#252525" : "#f5f5f5") : "transparent") + ";" +
            "-fx-text-fill: " + (active ? text() : text2()) + ";" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: " + (active ? "bold" : "normal") + ";" +
            "-fx-background-radius: 8;";
        
        b.setStyle(baseStyle);
        b.setOnAction(e -> { if (onAction != null) onAction.run(); });
        
        b.setOnMouseEntered(e -> {
            if (!active) b.setStyle(baseStyle + "-fx-background-color: " + (isDark() ? "#1a1a1a" : "#fafafa") + ";");
        });
        b.setOnMouseExited(e -> {
            if (!active) b.setStyle(baseStyle);
        });
        
        return b;
    }

    // ── Misc ──────────────────────────────────────────────────────────────────

    public static HBox divider() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);
        row.setMaxWidth(Double.MAX_VALUE);

        Separator left = new Separator();
        HBox.setHgrow(left, Priority.ALWAYS);
        Separator right = new Separator();
        HBox.setHgrow(right, Priority.ALWAYS);

        Label or = styledLabel("or", 12, text3());
        row.getChildren().addAll(left, or, right);
        return row;
    }

    public static Region vspace(int h) {
        Region r = new Region();
        r.setMinHeight(h);
        r.setPrefHeight(h);
        r.setMaxHeight(h);
        return r;
    }

    public static Region hspace(int w) {
        Region r = new Region();
        r.setMinWidth(w);
        r.setPrefWidth(w);
        r.setMaxWidth(w);
        return r;
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private static String inputStyle() {
        return  "-fx-background-color: " + bg() + ";" +
                "-fx-border-color: "     + border2() + ";" +
                "-fx-border-width: 0.5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: "        + text() + ";" +
                "-fx-prompt-text-fill: " + text3() + ";" +
                "-fx-padding: 10 12 10 12;";
    }
}