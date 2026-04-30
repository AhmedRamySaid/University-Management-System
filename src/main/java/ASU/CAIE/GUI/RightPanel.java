package ASU.CAIE.GUI;

import ASU.CAIE.Users.Role;
import ASU.CAIE.Users.User;
import ASU.CAIE.Database.UserDao;
import ASU.CAIE.util.SessionManager;
import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import static ASU.CAIE.GUI.ComponentFactory.*;
import static ASU.CAIE.GUI.ThemeManager.*;

public class RightPanel {

    private final ToastManager toast;

    // Tab buttons
    private Button tabLogin, tabSignup;

    // Forms
    private LoginForm  loginForm;
    private SignupForm signupForm;

    // Dark-toggle refs (rebuilt on theme change)
    private Label     dmLabel;
    private Circle    trackThumb;

    private final VBox panel;

    // Callback so UMSPortal can trigger a full theme rebuild
    private Runnable onThemeToggle;

    public RightPanel(ToastManager toast) {
        this.toast = toast;
        panel = new VBox(0);
        panel.setPadding(new Insets(28, 36, 28, 36));
        panel.setStyle("-fx-background-color: " + bg() + "; -fx-background-radius: 0 12 12 0;");
        panel.setAlignment(Pos.TOP_LEFT);

        HBox dmRow = buildDarkToggleRow();
        dmRow.setPadding(new Insets(0, 0, 16, 0));

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

        panel.getChildren().addAll(dmRow, tabs, formStack);
    }

    public VBox getNode() { return panel; }
    public void setOnThemeToggle(Runnable r) { onThemeToggle = r; }

    // ── Dark toggle ───────────────────────────────────────────────────────────

    private HBox buildDarkToggleRow() {
        HBox row = new HBox(7);
        row.setAlignment(Pos.CENTER_RIGHT);
        row.setCursor(Cursor.HAND);

        dmLabel = styledLabel(isDark() ? "Dark" : "Light", 12, text2());

        Rectangle track = new Rectangle(30, 17);
        track.setArcWidth(17); track.setArcHeight(17);
        track.setFill(Color.web(isDark() ? "#444444" : "#b0b0b0"));

        trackThumb = new Circle(6.5, Color.WHITE);
        trackThumb.setTranslateX(isDark() ? 6.5 : -6.5);

        StackPane toggle = new StackPane(track, trackThumb);
        row.getChildren().addAll(dmLabel, toggle);
        row.setOnMouseClicked(e -> toggleDark());
        return row;
    }

    private void toggleDark() {
        // Animate thumb
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), trackThumb);
        tt.setToX(isDark() ? -6.5 : 6.5);   // before toggle flip
        tt.play();

        ThemeManager.toggle();

        dmLabel.setText(isDark() ? "Dark" : "Light");
        dmLabel.setStyle("-fx-text-fill: " + text2() + "; -fx-font-size: 12px;");

        if (onThemeToggle != null) onThemeToggle.run();
    }

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

        // Background auth task
        Task<User> task = new Task<>() {
            @Override
            protected User call() {
                UserDao userDao = new UserDao();
                if (userDao.VerifyUserPassword(email, pw)) {
                    return userDao.GetUser(email).orElse(null);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            User user = task.getValue();
            if (user != null) {
                toast.show("Login successful", true);
                SessionManager.getInstance().login(user);
                DashboardLauncher.launch(user);
            } else {
                toast.show("Invalid credentials", false);
            }
        });
        task.setOnFailed(e -> toast.show("Connection error", false));
        runTask(task);
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
            toast.show("Please accept the terms to continue", false);
            ok = false;
        }
        if (!ok) return;

        User user = new User(fn + " " + ln, email, pw, Role.STUDENT);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return new UserDao().createUser(user);
            }
        };

        task.setOnSucceeded(e -> {
            if (task.getValue()) {
                toast.show("Registration successful", true);
                SessionManager.getInstance().login(user);
                DashboardLauncher.launch(user);
            } else {
                toast.show("Registration failed (email might already exist)", false);
            }
        });
        runTask(task);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────



    private void runTask(Task<?> task) {
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private static void setError(Label hint, String msg) {
        hint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
        hint.setText(msg);
    }
}