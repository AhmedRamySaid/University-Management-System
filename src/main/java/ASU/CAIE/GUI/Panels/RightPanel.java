package ASU.CAIE.GUI.Panels;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.GUI.Forms.LoginForm;
import ASU.CAIE.GUI.Forms.SignupForm;
import ASU.CAIE.GUI.Helpers.Validators;
import ASU.CAIE.Users.Role;
import ASU.CAIE.Users.User;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class RightPanel {

    // Tab buttons
    private Button tabLogin, tabSignup;

    // Forms
    private LoginForm loginForm;
    private SignupForm signupForm;

    // Dark-toggle refs (rebuilt on theme change)
    private Label     dmLabel;
    private Circle    trackThumb;

    private final VBox panel;

    // Callback so UMSPortal can trigger a full theme rebuild
    private Runnable onThemeToggle;

    public RightPanel() {
        panel = new VBox(0);
        panel.setPadding(new Insets(28, 36, 28, 36));
        panel.setStyle("-fx-background-color: " + bg() + "; -fx-background-radius: 0 12 12 0;");
        panel.setAlignment(Pos.TOP_LEFT);

        HBox tabs = buildTabBar();
        tabs.setPadding(new Insets(0, 0, 20, 0));

        loginForm  = new LoginForm();
        signupForm = new SignupForm();

        // Wire callbacks
        loginForm.setOnSwitchToSignup(() -> switchTab(false));
        loginForm.setOnSubmit(this::doLogin);
        signupForm.setOnSubmit(this::doSignup);

        Node loginNode  = loginForm.getNode();
        Node signupNode = signupForm.getNode();
        signupNode.setVisible(false);
        signupNode.setManaged(false);

        StackPane formStack = new StackPane(loginNode, signupNode);

        panel.getChildren().addAll(tabs, formStack);
    }

    public VBox getNode() { return panel; }
    public void setOnThemeToggle(Runnable r) { onThemeToggle = r; }

    // ── Tab bar ───────────────────────────────────────────────────────────────

    private HBox buildTabBar() {
        HBox bar = new HBox(0);
        bar.setStyle(
                "-fx-background-color: " + bg() + ";" +
                        "-fx-border-color: "     + border2() + ";" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        bar.setMaxWidth(Region.USE_PREF_SIZE);

        tabLogin  = tabButton("Sign in",  true);
        tabSignup = tabButton("Register", false);
        tabLogin.setOnAction(e  -> switchTab(true));
        tabSignup.setOnAction(e -> switchTab(false));
        bar.getChildren().addAll(tabLogin, tabSignup);
        return bar;
    }

    private Button tabButton(String text, boolean active) {
        Button b = new Button(text);
        b.setPrefWidth(110); b.setPrefHeight(36);
        b.setCursor(Cursor.HAND);
        styleTab(b, active);
        return b;
    }

    private void styleTab(Button b, boolean active) {
        String base =
                "-fx-background-color: " + bg() + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;";
        if (active) {
            b.setStyle(base +
                    "-fx-text-fill: "    + text() + ";" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-color: transparent transparent " + text() + " transparent;" +
                    "-fx-border-width: 0 0 2 0;");
        } else {
            b.setStyle(base +
                    "-fx-text-fill: "    + text2() + ";" +
                    "-fx-font-weight: normal;" +
                    "-fx-border-color: transparent;" +
                    "-fx-border-width: 0;");
        }
    }

    private void switchTab(boolean toLogin) {
        styleTab(tabLogin,  toLogin);
        styleTab(tabSignup, !toLogin);

        Node out = toLogin ? signupForm.getNode() : loginForm.getNode();
        Node in  = toLogin ? loginForm.getNode()  : signupForm.getNode();

        FadeTransition ftOut = new FadeTransition(Duration.millis(120), out);
        ftOut.setFromValue(1); ftOut.setToValue(0);
        ftOut.setOnFinished(e -> {
            out.setVisible(false); out.setManaged(false);
            in.setVisible(true);   in.setManaged(true);
            in.setOpacity(0);
            FadeTransition ftIn = new FadeTransition(Duration.millis(160), in);
            ftIn.setFromValue(0); ftIn.setToValue(1);
            ftIn.play();
        });
        ftOut.play();
    }

    // ── API calls ─────────────────────────────────────────────────────────────

    private void doLogin() {
        String email = loginForm.getEmail();
        String pw    = loginForm.getPassword();
        boolean ok   = true;

        if (email.isEmpty() || !Validators.isValidEmail(email)) {
            setError(loginForm.getEmailHint(), "Enter a valid email address");
            ok = false;
        }
        if (pw.isEmpty()) {
            setError(loginForm.getPwHint(), "Password is required");
            ok = false;
        }
        if (!ok) return;

		boolean success = DatabaseManager.UserDaoInstance.VerifyUserPassword(email, pw);
		if (!success) {
			setError(loginForm.getPwHint(),"Invalid email or password");
			return;
		}

		DatabaseManager.CurrentUser =
				DatabaseManager.UserDaoInstance.GetUser(email).orElse(null);
    }

    private void doSignup() {
        String fn    = signupForm.getFirstName();
        String ln    = signupForm.getLastName();
        String email = signupForm.getEmail();
        String pw    = signupForm.getPassword();
        String pw2   = signupForm.getPassword2();
        boolean ok   = true;

        if (fn.isEmpty() || ln.isEmpty()) {
            setError(signupForm.getNameHint(), "Please fill in both first and last name");
            ok = false;
        }
        if (!Validators.isValidEmail(email)) {
            setError(signupForm.getEmailHint(), "Enter a valid email address");
            ok = false;
        }
        if (pw.length() < 8) {
            setError(signupForm.getPwHint(), "Minimum 8 characters required");
            ok = false;
        }
        if (!pw.equals(pw2)) {
            setError(signupForm.getPw2Hint(), "Passwords do not match");
            ok = false;
        }
        if (!signupForm.isTermsAccepted()) {
            setError(signupForm.getTermsHint(), "Please accept the terms to continue");
            ok = false;
        }
        if (!ok) return;

		User user = new User(fn + " " + ln, email, Role.STUDENT);
		boolean success = DatabaseManager.UserDaoInstance.createUser(user, pw);

		if (!success) {
			setError(signupForm.getPw2Hint(), "Failed to create user. Try again later");
			return;
		}

		DatabaseManager.CurrentUser = user;
    }

    private static void setError(Label hint, String msg) {
        hint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
        hint.setText(msg);
    }
}