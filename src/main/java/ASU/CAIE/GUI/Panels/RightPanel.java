package ASU.CAIE.GUI.Panels;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.GUI.Dashboards.DashboardLauncher;
import ASU.CAIE.GUI.Forms.LoginForm;
import ASU.CAIE.GUI.Forms.SignupForm;
import ASU.CAIE.GUI.Helpers.Validators;
import ASU.CAIE.model.Role;
import ASU.CAIE.model.User;
import ASU.CAIE.util.SessionManager;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import static ASU.CAIE.GUI.Helpers.ThemeManager.*;

public class RightPanel {

	private Button tabLogin, tabSignup;

	private LoginForm loginForm;
	private SignupForm signupForm;

	private final VBox panel;

	private Runnable onThemeToggle;

	public RightPanel() {
		panel = new VBox(0);
		panel.setPadding(new Insets(32, 40, 32, 40));
		panel.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 0 12 12 0;");
		panel.setAlignment(Pos.TOP_LEFT);

		HBox tabs = buildTabBar();
		tabs.setPadding(new Insets(0, 0, 24, 0));

		loginForm = new LoginForm();
		signupForm = new SignupForm();

		loginForm.setOnSwitchToSignup(() -> switchTab(false));
		loginForm.setOnSubmit(this::doLogin);
		signupForm.setOnSubmit(this::doSignup);

		Node loginNode = loginForm.getNode();
		Node signupNode = signupForm.getNode();

		signupNode.setVisible(false);
		signupNode.setManaged(false);

		StackPane formStack = new StackPane(loginNode, signupNode);

		panel.getChildren().addAll(tabs, formStack);
	}

	public VBox getNode() {
		return panel;
	}

	public void setOnThemeToggle(Runnable r) {
		onThemeToggle = r;
	}

	private HBox buildTabBar() {
		HBox bar = new HBox(0);
		bar.setStyle(
				"-fx-background-color: " + bg() + ";" +
						"-fx-border-color: " + border2() + ";" +
						"-fx-border-width: 0.5;" +
						"-fx-border-radius: 10;" +
						"-fx-background-radius: 10;"
		);
		bar.setMaxWidth(Region.USE_PREF_SIZE);

		tabLogin = tabButton("Sign in", true);
		tabSignup = tabButton("Register", false);

		tabLogin.setOnAction(e -> switchTab(true));
		tabSignup.setOnAction(e -> switchTab(false));

		bar.getChildren().addAll(tabLogin, tabSignup);
		return bar;
	}

	private Button tabButton(String text, boolean active) {
		Button b = new Button(text);
		b.setPrefWidth(120);
		b.setPrefHeight(40);
		styleTab(b, active);
		return b;
	}

	private void styleTab(Button b, boolean active) {
		String base =
				"-fx-background-color: " + bg() + ";" +
						"-fx-background-radius: 10;" +
						"-fx-font-size: 13px;";

		if (active) {
			b.setStyle(base +
					"-fx-text-fill: " + text() + ";" +
					"-fx-font-weight: bold;" +
					"-fx-border-color: transparent transparent " + text() + " transparent;" +
					"-fx-border-width: 0 0 2 0;");
		} else {
			b.setStyle(base +
					"-fx-text-fill: " + text2() + ";" +
					"-fx-font-weight: normal;" +
					"-fx-border-color: transparent;");
		}
	}

	private void switchTab(boolean toLogin) {
		styleTab(tabLogin, toLogin);
		styleTab(tabSignup, !toLogin);

		Node out = toLogin ? signupForm.getNode() : loginForm.getNode();
		Node in = toLogin ? loginForm.getNode() : signupForm.getNode();

		FadeTransition ftOut = new FadeTransition(Duration.millis(120), out);
		ftOut.setFromValue(1);
		ftOut.setToValue(0);

		ftOut.setOnFinished(e -> {
			out.setVisible(false);
			out.setManaged(false);

			in.setVisible(true);
			in.setManaged(true);
			in.setOpacity(0);

			FadeTransition ftIn = new FadeTransition(Duration.millis(160), in);
			ftIn.setFromValue(0);
			ftIn.setToValue(1);
			ftIn.play();
		});

		ftOut.play();
	}

	private void doLogin() {
		String email = loginForm.getEmail();
		String pw = loginForm.getPassword();
		boolean ok = true;

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
			setError(loginForm.getPwHint(), "Invalid email or password");
			return;
		}

		User user = DatabaseManager.UserDaoInstance.GetUser(email).orElse(null);

		if (user != null) {
			SessionManager.getInstance().login(user);
			DashboardLauncher.launch(user);
		}
	}

	private void doSignup() {
		String fn = signupForm.getFirstName();
		String ln = signupForm.getLastName();
		String email = signupForm.getEmail();
		String pw = signupForm.getPassword();
		String pw2 = signupForm.getPassword2();
		boolean ok = true;

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
			setError(signupForm.getPw2Hint(), "Failed to create user");
			return;
		}

		// to make sure to get the new student ID
		user = DatabaseManager.UserDaoInstance.GetUser(email).orElse(null);

		if (user != null) {
			SessionManager.getInstance().login(user);
			DashboardLauncher.launch(user);
		}
	}

	private static void setError(Label hint, String msg) {
		hint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
		hint.setText(msg);
	}
}