package ASU.CAIE.GUI.Forms;

import ASU.CAIE.GUI.Helpers.Validators;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import static ASU.CAIE.GUI.Helpers.ComponentFactory.*;
import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class LoginForm {

    // ── Field refs (accessed by RightPanel for submit / tab-switch) ──────────
    private final TextField     lEmail;
    private final PasswordField lPw;
    private final Label         lEmailHint;
    private final Label         lPwHint;

    private final VBox form;

    // Callback wired by RightPanel
    private Runnable onSwitchToSignup;
    private Runnable onSubmit;

    public LoginForm() {
        form = new VBox(0);
        form.setAlignment(Pos.TOP_LEFT);

        Label title = styledLabel("Sign in", 22, text());
        title.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 22));
        Label sub = styledLabel("Enter your credentials below.", 13, text2());

        // Email
        Label emailLbl = formLabel("Email address");
        lEmail     = inputField("name@university.edu");
        lEmailHint = hintLabel("");
        lEmail.textProperty().addListener((o, ov, nv) -> validateEmail());
        lEmail.focusedProperty().addListener((o, ov, nv) -> { if (!nv) validateEmail(); });

        // Password
        HBox pwLblRow = new HBox();
        Label pwLbl   = formLabel("Password");
        HBox.setHgrow(pwLbl, Priority.ALWAYS);
        pwLblRow.getChildren().addAll(pwLbl);

        lPw     = passwordField("••••••••");
        lPwHint = hintLabel("");
        HBox lPwRow = passwordRow(lPw);

        // Buttons
        Button signInBtn = primaryButton("Sign in");
        signInBtn.setOnAction(e -> { if (onSubmit != null) onSubmit.run(); });

        HBox divider = divider();

        Button ghost = ghostButton("No account? Register ↗");
        ghost.setOnAction(e -> { if (onSwitchToSignup != null) onSwitchToSignup.run(); });

        addAll(form,
                title, vspace(4), sub, vspace(20),
                emailLbl, inputWrap(lEmail), lEmailHint, vspace(10),
                pwLblRow, lPwRow, lPwHint, vspace(14),
                signInBtn, vspace(8), divider, vspace(8), ghost
        );
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public VBox     getNode()       { return form; }
    public String   getEmail()      { return lEmail.getText().trim(); }
    public String   getPassword()   { return lPw.getText(); }
    public Label    getEmailHint()  { return lEmailHint; }
    public Label    getPwHint()     { return lPwHint; }

    // ── Callbacks ─────────────────────────────────────────────────────────────

    public void setOnSwitchToSignup(Runnable r) { onSwitchToSignup = r; }
    public void setOnSubmit(Runnable r)          { onSubmit = r; }

    // ── Validation ────────────────────────────────────────────────────────────

    private void validateEmail() {
        String v = lEmail.getText().trim();
        if (v.isEmpty()) { lEmailHint.setText(""); return; }
        if (Validators.isValidEmail(v)) {
            lEmailHint.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");
            lEmailHint.setText("Looks good");
        } else {
            lEmailHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
            lEmailHint.setText("Enter a valid email address");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static void addAll(VBox box, Node... nodes) {
        box.getChildren().addAll(nodes);
    }
}