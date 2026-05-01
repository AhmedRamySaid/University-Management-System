package ASU.CAIE.GUI.Helpers;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ComponentFactory {

    // ── Labels ────────────────────────────────────────────────────────────────

    public static Label styledLabel(String text, int size, String color) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size + "px;");
        return l;
    }

    public static Label formLabel(String text) {
        Label l = styledLabel(text, 12, ThemeManager.text2());
        l.setStyle(l.getStyle() + " -fx-font-weight: bold;");
        return l;
    }

    public static Label hintLabel(String text) {
        Label l = styledLabel(text, 11, ThemeManager.text3());
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
            String border = nv ? ThemeManager.text() : ThemeManager.border2();
            f.setStyle(f.getStyle().replaceAll("-fx-border-color: [^;]+;",
                    "-fx-border-color: " + border + ";"));
        });
        return f;
    }

    public static PasswordField passwordField(String placeholder) {
        PasswordField f = new PasswordField();
        f.setPromptText(placeholder);
        f.setStyle(
                "-fx-background-color: " + ThemeManager.bg() + ";" +
                        "-fx-border-color: "      + ThemeManager.border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: "         + ThemeManager.text() + ";" +
                        "-fx-prompt-text-fill: "  + ThemeManager.text3() + ";" +
                        "-fx-padding: 10 40 10 12;"
        );
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }

    /** Password field + show/hide toggle button in an overlay. */
    public static HBox passwordRow(PasswordField pf) {
        Button eye = new Button("◉");
        eye.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + ThemeManager.text3() + ";" +
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
        StackPane sp = new StackPane(f);
        sp.setMaxWidth(Double.MAX_VALUE);
        return sp;
    }

    // ── Buttons ───────────────────────────────────────────────────────────────

    public static Button primaryButton(String text) {

        Button b = new Button(text);
        b.setPrefHeight(44);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(Cursor.HAND);
        b.setStyle(
                "-fx-background-color: " + ("#111111") + ";" +
                        "-fx-text-fill: "        + ( "#ffffff") + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: " + ( "#2c2c2a") + ";")));
        b.setOnMouseExited(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: " + ("#111111") + ";")));
        return b;
    }

    public static Button ghostButton(String text) {
        Button b = new Button(text);
        b.setPrefHeight(42);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(Cursor.HAND);
        b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: "     + ThemeManager.text2() + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: "  + ThemeManager.border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: " + ThemeManager.bg2() + ";")));
        b.setOnMouseExited(e -> b.setStyle(b.getStyle().replaceAll(
                "-fx-background-color: [^;]+;",
                "-fx-background-color: transparent;")));
        return b;
    }

    // ── Misc ──────────────────────────────────────────────────────────────────

    public static HBox divider() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);
        row.setMaxWidth(Double.MAX_VALUE);

        Separator left = new Separator();
        left.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(left, Priority.ALWAYS);

        Separator right = new Separator();
        right.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(right, Priority.ALWAYS);

        Label or = styledLabel("or", 12, ThemeManager.text3());
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

    // ── Internal helpers ──────────────────────────────────────────────────────

    private static String inputStyle() {
        return  "-fx-background-color: " + ThemeManager.bg() + ";" +
                "-fx-border-color: "     + ThemeManager.border2() + ";" +
                "-fx-border-width: 0.5;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: "        + ThemeManager.text() + ";" +
                "-fx-prompt-text-fill: " + ThemeManager.text3() + ";" +
                "-fx-padding: 10 12 10 12;";
    }
}