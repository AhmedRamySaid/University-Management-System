package ASU.CAIE.GUI;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

import static ASU.CAIE.GUI.ComponentFactory.*;
import static ASU.CAIE.GUI.ThemeManager.*;

public class SignupForm {

    // ── Field refs ────────────────────────────────────────────────────────────
    private final TextField     sFn, sLn, sEmail;
    private final PasswordField sPw, sPw2;

    private final Label sNameHint, sEmailHint, sPwHint, sPw2Hint;

    private final Rectangle pwFill;

    private boolean termsAccepted = false;

    private final VBox form;

    // Callbacks
    private Runnable onSubmit;

    public SignupForm() {
        form = new VBox(0);
        form.setAlignment(Pos.TOP_LEFT);

        Label title = styledLabel("Create account", 22, text());
        title.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 22));
        Label sub = styledLabel("Fill in your details to get started.", 13, text2());

        // Name row
        sFn = inputField("First");
        sLn = inputField("Last");
        HBox nameRow = new HBox(10);
        VBox fnBox = new VBox(4, formLabel("First name"), inputWrap(sFn));
        VBox lnBox = new VBox(4, formLabel("Last name"),  inputWrap(sLn));
        HBox.setHgrow(fnBox, Priority.ALWAYS);
        HBox.setHgrow(lnBox, Priority.ALWAYS);
        nameRow.getChildren().addAll(fnBox, lnBox);
        sNameHint = hintLabel("");

        // Email
        sEmail     = inputField("name@university.edu");
        sEmailHint = hintLabel("");
        sEmail.textProperty().addListener((o, ov, nv) -> validateEmail());
        sEmail.focusedProperty().addListener((o, ov, nv) -> { if (!nv) validateEmail(); });

        // Password + strength bar
        sPw    = passwordField("At least 8 characters");
        sPwHint = hintLabel("");
        HBox sPwRow = passwordRow(sPw);

        Rectangle pwTrack = new Rectangle(0, 3);
        pwTrack.setFill(Color.web(isDark() ? "#333333" : "#e0e0e0"));
        pwTrack.setArcWidth(3); pwTrack.setArcHeight(3);

        pwFill = new Rectangle(0, 3);
        pwFill.setFill(Color.web("#e24b4a"));
        pwFill.setArcWidth(3); pwFill.setArcHeight(3);

        StackPane pwBar = new StackPane();
        pwBar.setAlignment(Pos.CENTER_LEFT);
        pwBar.setMaxWidth(Double.MAX_VALUE);
        pwBar.getChildren().addAll(pwTrack, pwFill);
        pwBar.widthProperty().addListener((o, ov, nv) -> pwTrack.setWidth(nv.doubleValue()));
        sPw.textProperty().addListener((o, ov, nv) -> validatePw(pwBar));

        // Confirm password
        sPw2     = passwordField("Repeat password");
        sPw2Hint = hintLabel("");
        HBox sPw2Row = passwordRow(sPw2);
        sPw2.textProperty().addListener((o, ov, nv) -> validatePw2());

        // Terms
        HBox termsRow = buildTermsRow();

        // Button
        Button createBtn = primaryButton("Create account");
        createBtn.setOnAction(e -> { if (onSubmit != null) onSubmit.run(); });


        addAll(form,
                title, vspace(4), sub, vspace(18),
                nameRow, sNameHint, vspace(8),
                formLabel("University email"), inputWrap(sEmail), sEmailHint, vspace(8),
                formLabel("Password"), sPwRow, pwBar, sPwHint, vspace(8),
                formLabel("Confirm password"), sPw2Row, sPw2Hint, vspace(10),
                termsRow, vspace(6), createBtn, vspace(6)
        );
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public VBox    getNode()         { return form; }
    public String  getFirstName()    { return sFn.getText().trim(); }
    public String  getLastName()     { return sLn.getText().trim(); }
    public String  getEmail()        { return sEmail.getText().trim(); }
    public String  getPassword()     { return sPw.getText(); }
    public String  getPassword2()    { return sPw2.getText(); }
    public boolean isTermsAccepted() { return termsAccepted; }

    public Label getNameHint()  { return sNameHint; }
    public Label getEmailHint() { return sEmailHint; }
    public Label getPwHint()    { return sPwHint; }
    public Label getPw2Hint()   { return sPw2Hint; }

    public void setOnSubmit(Runnable r) { onSubmit = r; }

    // ── Terms row ─────────────────────────────────────────────────────────────

    private HBox buildTermsRow() {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setCursor(Cursor.HAND);

        StackPane chkBox = new StackPane();
        Rectangle chkBg = new Rectangle(14, 14);
        chkBg.setArcWidth(3); chkBg.setArcHeight(3);
        chkBg.setFill(Color.web(bg()));
        chkBg.setStroke(Color.web(border2()));
        chkBg.setStrokeWidth(0.5);

        Polyline check = new Polyline(3, 7, 6, 10, 11, 4);
        check.setStroke(Color.WHITE);
        check.setStrokeWidth(1.5);
        check.setFill(Color.TRANSPARENT);
        check.setVisible(false);

        chkBox.getChildren().addAll(chkBg, check);

        Label lbl = styledLabel("I agree to the Terms of Service and Privacy Policy", 12, text2());
        row.getChildren().addAll(chkBox, lbl);

        row.setOnMouseClicked(e -> {
            termsAccepted = !termsAccepted;
            chkBg.setFill(termsAccepted ? Color.web(text()) : Color.web(bg()));
            chkBg.setStroke(termsAccepted ? Color.web(text()) : Color.web(border2()));
            check.setVisible(termsAccepted);
            ScaleTransition st = new ScaleTransition(Duration.millis(120), chkBox);
            st.setFromX(0.8); st.setToX(1.0);
            st.setFromY(0.8); st.setToY(1.0);
            st.play();
        });
        return row;
    }

    // ── Validation ────────────────────────────────────────────────────────────

    private void validateEmail() {
        String v = sEmail.getText().trim();
        if (v.isEmpty()) { sEmailHint.setText(""); return; }
        if (Validators.isValidEmail(v)) {
            sEmailHint.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");
            sEmailHint.setText("Looks good");
        } else {
            sEmailHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
            sEmailHint.setText("Enter a valid email address");
        }
    }

    private void validatePw(StackPane pwBar) {
        String pw = sPw.getText();
        if (pw.isEmpty()) { sPwHint.setText(""); pwFill.setWidth(0); validatePw2(); return; }

        int    score   = Validators.pwScore(pw);
        double targetW = pwBar.getWidth() * Math.min(1.0, score / 5.0);

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(pwFill.widthProperty(), targetW, Interpolator.EASE_OUT)));
        tl.play();

        String[] colors = {"#e24b4a","#e24b4a","#ef9f27","#ef9f27","#444441","#444441"};
        pwFill.setFill(Color.web(colors[Math.min(score, 5)]));

        if (pw.length() < 8) {
            sPwHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
            sPwHint.setText("Minimum 8 characters required");
        } else {
            String[] lbls = {"Too short","Weak","Fair","Good","Strong","Very strong"};
            sPwHint.setStyle("-fx-text-fill: " + (score >= 3 ? "#555555" : "#888888") + "; -fx-font-size: 11px;");
            sPwHint.setText(lbls[Math.min(score, 5)]);
        }
        validatePw2();
    }

    private void validatePw2() {
        String pw  = sPw.getText();
        String pw2 = sPw2.getText();
        if (pw2.isEmpty()) { sPw2Hint.setText(""); return; }
        if (pw.equals(pw2)) {
            sPw2Hint.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");
            sPw2Hint.setText("Passwords match");
        } else {
            sPw2Hint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
            sPw2Hint.setText("Passwords do not match");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static void addAll(VBox box, Node... nodes) {
        box.getChildren().addAll(nodes);
    }
}