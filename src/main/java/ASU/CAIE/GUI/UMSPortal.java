package ASU.CAIE.GUI;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class UMSPortal extends Application {

	// ── Palette ───────────────────────────────────────────────────────────────
	private static boolean dark = false;

	// light
	private static final String L_BG       = "#ffffff";
	private static final String L_BG2      = "#f5f5f5";
	private static final String L_BORDER   = "rgba(0,0,0,0.15)";
	private static final String L_BORDER2  = "#b0b0b0";
	private static final String L_TEXT     = "#111111";
	private static final String L_TEXT2    = "#555555";
	private static final String L_TEXT3    = "#888888";

	// dark
	private static final String D_BG       = "#1a1a1a";
	private static final String D_BG2      = "#252525";
	private static final String D_BORDER2  = "#4a4a4a";
	private static final String D_TEXT     = "#f0f0f0";
	private static final String D_TEXT2    = "#aaaaaa";
	private static final String D_TEXT3    = "#666666";

	// ── State ─────────────────────────────────────────────────────────────────
	private String selectedRole = "STUDENT";
	private boolean termsAccepted = false;

	// ── Scene refs ────────────────────────────────────────────────────────────
	private Stage       primaryStage;
	private BorderPane  root;
	private HBox        outerCard;
	private StackPane   leftPanel;
	private VBox        rightPanel;
	private StackPane   formStack;
	private VBox        loginForm, signupForm;
	private Button      tabLogin, tabSignup;

	// Login fields
	private TextField     lEmail;
	private PasswordField lPw;
	private Label         lEmailHint, lPwHint;

	// Signup fields
	private TextField     sFn, sLn, sEmail;
	private PasswordField sPw, sPw2;
	private TextField     sPwVisible, sPw2Visible;
	private Label         sNameHint, sEmailHint, sPwHint, sPw2Hint;
	private Rectangle     pwFill;
	private Button[]      roleBtns;
	private Rectangle     checkMark;
	private Label         dmLabel;
	private Rectangle     trackFill;
	private Circle        trackThumb;

	// ── Toast ─────────────────────────────────────────────────────────────────
	private Stage toastStage;

	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;
		buildScene();
		stage.setTitle("ASU CAIE · UMS Portal");
		stage.setResizable(false);
		stage.show();
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  SCENE BUILD
	// ═══════════════════════════════════════════════════════════════════════════
	private void buildScene() {
		root = new BorderPane();
		root.setStyle("-fx-background-color: " + bg2() + ";");
		root.setPadding(new Insets(32));

		outerCard = buildOuterCard();
		root.setCenter(outerCard);

		Scene scene = new Scene(root, 820, 580);
		primaryStage.setScene(scene);

		// Fade-in
		FadeTransition ft = new FadeTransition(Duration.millis(350), outerCard);
		ft.setFromValue(0); ft.setToValue(1); ft.play();
	}

	private HBox buildOuterCard() {
		HBox card = new HBox();
		card.setStyle(
				"-fx-background-color: " + bg() + ";" +
						"-fx-background-radius: 12;" +
						"-fx-border-color: " + border2() + ";" +
						"-fx-border-width: 0.5;" +
						"-fx-border-radius: 12;" +
						"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 24, 0, 0, 4);"
		);
		card.setMinHeight(520);

		leftPanel  = buildLeftPanel();
		rightPanel = buildRightPanel();

		HBox.setHgrow(rightPanel, Priority.ALWAYS);
		card.getChildren().addAll(leftPanel, rightPanel);
		return card;
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  LEFT PANEL
	// ═══════════════════════════════════════════════════════════════════════════
	private StackPane buildLeftPanel() {
		StackPane pane = new StackPane();
		pane.setPrefWidth(240);
		pane.setMinWidth(240);
		pane.setStyle(
				"-fx-background-color: " + (dark ? "#0a0a0a" : "#111111") + ";" +
						"-fx-background-radius: 12 0 0 12;"
		);

		// Decorative circle
		Circle circle = new Circle(130);
		circle.setFill(Color.web(dark ? "#161616" : "#1e1e1e"));
		StackPane.setAlignment(circle, Pos.BOTTOM_LEFT);
		circle.setTranslateX(-65);
		circle.setTranslateY(65);

		// Content VBox
		VBox content = new VBox();
		content.setPadding(new Insets(28, 24, 28, 24));
		content.setSpacing(0);

		// Top dot row
		HBox dotRow = new HBox(8);
		dotRow.setAlignment(Pos.CENTER_LEFT);
		Circle dot = new Circle(3.5, Color.web("#888888"));
		Label dotLbl = styledLabel("UMS Portal", 11, "#888888");
		dotRow.getChildren().addAll(dot, dotLbl);

		// Spacer
		Region spacer1 = new Region();
		VBox.setVgrow(spacer1, Priority.ALWAYS);

		// Headline
		Text line1 = new Text("Access your\n");
		line1.setFont(Font.font("Georgia", FontWeight.NORMAL, 26));
		line1.setFill(Color.WHITE);
		Text line2 = new Text("university\n");
		line2.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
		line2.setFill(Color.WHITE);
		Text line3 = new Text("account.");
		line3.setFont(Font.font("Georgia", FontWeight.NORMAL, 26));
		line3.setFill(Color.WHITE);
		TextFlow headline = new TextFlow(line1, line2, line3);
		headline.setLineSpacing(2);

		Region spacer2 = new Region();
		VBox.setVgrow(spacer2, Priority.ALWAYS);

		// Footer
		Label foot = styledLabel("Ain Shams University · 2026", 11, "#555555");

		content.getChildren().addAll(dotRow, spacer1, headline, spacer2, foot);

		pane.getChildren().addAll(circle, content);
		StackPane.setAlignment(content, Pos.CENTER_LEFT);
		return pane;
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  RIGHT PANEL
	// ═══════════════════════════════════════════════════════════════════════════
	private VBox buildRightPanel() {
		VBox panel = new VBox(0);
		panel.setPadding(new Insets(28, 36, 28, 36));
		panel.setStyle("-fx-background-color: " + bg() + "; -fx-background-radius: 0 12 12 0;");
		panel.setAlignment(Pos.TOP_LEFT);

		// Dark mode row
		HBox dmRow = new HBox();
		dmRow.setAlignment(Pos.CENTER_RIGHT);
		dmRow.getChildren().add(buildDarkToggle());
		dmRow.setPadding(new Insets(0, 0, 16, 0));

		// Tab bar
		HBox tabs = buildTabBar();
		tabs.setPadding(new Insets(0, 0, 20, 0));

		// Form stack
		formStack = new StackPane();
		loginForm  = buildLoginForm();
		signupForm = buildSignupForm();
		signupForm.setVisible(false);
		signupForm.setManaged(false);
		formStack.getChildren().addAll(loginForm, signupForm);

		panel.getChildren().addAll(dmRow, tabs, formStack);
		return panel;
	}

	// ── Dark mode toggle ──────────────────────────────────────────────────────
	private HBox buildDarkToggle() {
		HBox row = new HBox(7);
		row.setAlignment(Pos.CENTER_RIGHT);
		row.setCursor(Cursor.HAND);

		dmLabel = styledLabel(dark ? "Dark" : "Light", 12, text2());

		// Track
		Rectangle track = new Rectangle(30, 17);
		track.setArcWidth(17); track.setArcHeight(17);
		track.setFill(Color.web(dark ? "#444444" : "#b0b0b0"));

		trackThumb = new Circle(6.5, Color.WHITE);

		StackPane toggle = new StackPane(track, trackThumb);
		trackThumb.setTranslateX(dark ? 6.5 : -6.5);

		row.getChildren().addAll(dmLabel, toggle);
		row.setOnMouseClicked(e -> toggleDark());
		return row;
	}

	// ── Tab bar ───────────────────────────────────────────────────────────────
	private HBox buildTabBar() {
		HBox bar = new HBox(0);
		bar.setStyle(
				"-fx-background-color: " + bg() + ";" +
						"-fx-border-color: " + border2() + ";" +
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
		b.setPrefWidth(110);
		b.setPrefHeight(36);
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
					"-fx-text-fill: " + text() + ";" +
					"-fx-font-weight: bold;" +
					"-fx-border-color: transparent transparent " + text() + " transparent;" +
					"-fx-border-width: 0 0 2 0;"
			);
		} else {
			b.setStyle(base +
					"-fx-text-fill: " + text2() + ";" +
					"-fx-font-weight: normal;" +
					"-fx-border-color: transparent;" +
					"-fx-border-width: 0;"
			);
		}
	}

	private void switchTab(boolean toLogin) {
		styleTab(tabLogin,  toLogin);
		styleTab(tabSignup, !toLogin);

		Node out = toLogin ? signupForm : loginForm;
		Node in  = toLogin ? loginForm  : signupForm;

		FadeTransition ftOut = new FadeTransition(Duration.millis(120), out);
		ftOut.setFromValue(1); ftOut.setToValue(0);
		ftOut.setOnFinished(e -> {
			out.setVisible(false); out.setManaged(false);
			in.setVisible(true);  in.setManaged(true);
			in.setOpacity(0);
			FadeTransition ftIn = new FadeTransition(Duration.millis(160), in);
			ftIn.setFromValue(0); ftIn.setToValue(1); ftIn.play();
		});
		ftOut.play();
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  LOGIN FORM
	// ═══════════════════════════════════════════════════════════════════════════
	private VBox buildLoginForm() {
		VBox form = new VBox(0);
		form.setAlignment(Pos.TOP_LEFT);

		Label title = styledLabel("Sign in", 22, text());
		title.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 22));
		Label sub = styledLabel("Enter your credentials below.", 13, text2());

		// Email field
		Label emailLbl = formLabel("Email address");
		lEmail     = inputField("name@university.edu");
		lEmailHint = hintLabel("");
		lEmail.textProperty().addListener((o, ov, nv) -> validateLoginEmail());
		lEmail.focusedProperty().addListener((o, ov, nv) -> { if (!nv) validateLoginEmail(); });

		// Password field
		HBox pwLblRow = new HBox();
		Label pwLbl  = formLabel("Password");
		Label forgot = styledLabel("Forgot password?", 12, text2());
		forgot.setCursor(Cursor.HAND);
		HBox.setHgrow(pwLbl, Priority.ALWAYS);
		pwLblRow.getChildren().addAll(pwLbl, forgot);

		lPw     = passwordField("••••••••");
		lPwHint = hintLabel("");
		HBox lPwRow = passwordRow(lPw);

		// Button
		Button signInBtn = primaryButton("Sign in");
		signInBtn.setOnAction(e -> doLogin());

		// Divider
		HBox divider = divider();

		// Ghost button
		Button ghost = ghostButton("No account? Register ↗");
		ghost.setOnAction(e -> switchTab(false));

		addSpaced(form,
				title, vspace(4), sub, vspace(20),
				emailLbl, inputWrap(lEmail), lEmailHint, vspace(10),
				pwLblRow, lPwRow, lPwHint, vspace(14),
				signInBtn, vspace(8), divider, vspace(8), ghost
		);
		return form;
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  SIGNUP FORM
	// ═══════════════════════════════════════════════════════════════════════════
	private VBox buildSignupForm() {
		VBox form = new VBox(0);
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
		sEmail.textProperty().addListener((o, ov, nv) -> validateSignupEmail());
		sEmail.focusedProperty().addListener((o, ov, nv) -> { if (!nv) validateSignupEmail(); });

		// Role grid
		GridPane roleGrid = buildRoleGrid();

		// Password
		sPw = passwordField("At least 8 characters");
		sPwHint = hintLabel("");
		HBox sPwRow = passwordRow(sPw);
		// Strength bar
		Rectangle pwTrack = new Rectangle(0, 3);
		pwTrack.setFill(Color.web(dark ? "#333333" : "#e0e0e0"));
		pwTrack.setArcWidth(3); pwTrack.setArcHeight(3);
		pwFill = new Rectangle(0, 3);
		pwFill.setFill(Color.web("#e24b4a"));
		pwFill.setArcWidth(3); pwFill.setArcHeight(3);
		StackPane pwBar = new StackPane();
		pwBar.setAlignment(Pos.CENTER_LEFT);
		pwBar.setMaxWidth(Double.MAX_VALUE);
		pwBar.getChildren().addAll(pwTrack, pwFill);
		pwBar.widthProperty().addListener((o, ov, nv) -> pwTrack.setWidth(nv.doubleValue()));

		sPw.textProperty().addListener((o, ov, nv) -> validateSignupPw(pwBar));

		// Confirm password
		sPw2 = passwordField("Repeat password");
		sPw2Hint = hintLabel("");
		HBox sPw2Row = passwordRow(sPw2);
		sPw2.textProperty().addListener((o, ov, nv) -> validateSignupPw2());

		// Terms
		HBox termsRow = buildTermsRow();

		// Button
		Button createBtn = primaryButton("Create account");
		createBtn.setOnAction(e -> doSignup());
		Label note = styledLabel("Account requires admin approval before activation", 11, text3());
		note.setAlignment(Pos.CENTER);
		note.setMaxWidth(Double.MAX_VALUE);

		addSpaced(form,
				title, vspace(4), sub, vspace(18),
				nameRow, sNameHint, vspace(8),
				formLabel("University email"), inputWrap(sEmail), sEmailHint, vspace(8),
				formLabel("Role"), roleGrid, vspace(8),
				formLabel("Password"), sPwRow, pwBar, sPwHint, vspace(8),
				formLabel("Confirm password"), sPw2Row, sPw2Hint, vspace(10),
				termsRow, vspace(6),
				createBtn, vspace(6), note
		);
		return form;
	}

	// ── Role grid ─────────────────────────────────────────────────────────────
	private GridPane buildRoleGrid() {
		GridPane grid = new GridPane();
		grid.setHgap(7); grid.setVgap(7);
		grid.setMaxWidth(Double.MAX_VALUE);

		String[] roles  = {"STUDENT","PROFESSOR","STAFF","ADMIN"};
		String[] labels = {"Student","Professor","Staff","Admin"};
		roleBtns = new Button[4];

		for (int i = 0; i < 4; i++) {
			final String role = roles[i];
			Button b = roleButton(labels[i], role.equals(selectedRole));
			b.setMaxWidth(Double.MAX_VALUE);
			GridPane.setHgrow(b, Priority.ALWAYS);
			b.setOnAction(e -> {
				selectedRole = role;
				for (int j = 0; j < roleBtns.length; j++)
					styleRoleBtn(roleBtns[j], roles[j].equals(role));
			});
			roleBtns[i] = b;
			grid.add(b, i % 2, i / 2);
		}
		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(50);
		grid.getColumnConstraints().addAll(cc, new ColumnConstraints() {{ setPercentWidth(50); }});
		return grid;
	}

	private Button roleButton(String text, boolean selected) {
		Button b = new Button(text);
		b.setPrefHeight(34);
		b.setCursor(Cursor.HAND);
		styleRoleBtn(b, selected);
		return b;
	}

	private void styleRoleBtn(Button b, boolean selected) {
		if (selected) {
			b.setStyle(
					"-fx-background-color: " + text() + ";" +
							"-fx-text-fill: " + bg() + ";" +
							"-fx-font-size: 12px;" +
							"-fx-background-radius: 8;" +
							"-fx-border-color: " + text() + ";" +
							"-fx-border-width: 0.5;" +
							"-fx-border-radius: 8;"
			);
		} else {
			b.setStyle(
					"-fx-background-color: " + bg() + ";" +
							"-fx-text-fill: " + text2() + ";" +
							"-fx-font-size: 12px;" +
							"-fx-background-radius: 8;" +
							"-fx-border-color: " + border2() + ";" +
							"-fx-border-width: 0.5;" +
							"-fx-border-radius: 8;"
			);
		}
	}

	// ── Terms row ─────────────────────────────────────────────────────────────
	private HBox buildTermsRow() {
		HBox row = new HBox(8);
		row.setAlignment(Pos.CENTER_LEFT);
		row.setCursor(Cursor.HAND);

		// Custom checkbox
		StackPane chkBox = new StackPane();
		Rectangle chkBg = new Rectangle(14, 14);
		chkBg.setArcWidth(3); chkBg.setArcHeight(3);
		chkBg.setFill(Color.web(bg()));
		chkBg.setStroke(Color.web(border2())); chkBg.setStrokeWidth(0.5);
		checkMark = new Rectangle(0, 0);  // placeholder — drawn as polyline
		Polyline check = new Polyline(3, 7, 6, 10, 11, 4);
		check.setStroke(Color.WHITE); check.setStrokeWidth(1.5);
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

	// ═══════════════════════════════════════════════════════════════════════════
	//  VALIDATION
	// ═══════════════════════════════════════════════════════════════════════════
	private void validateLoginEmail() {
		String v = lEmail.getText().trim();
		if (v.isEmpty()) { lEmailHint.setText(""); return; }
		if (isValidEmail(v)) {
			lEmailHint.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");
			lEmailHint.setText("Looks good");
		} else {
			lEmailHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			lEmailHint.setText("Enter a valid email address");
		}
	}

	private void validateSignupEmail() {
		String v = sEmail.getText().trim();
		if (v.isEmpty()) { sEmailHint.setText(""); return; }
		if (isValidEmail(v)) {
			sEmailHint.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");
			sEmailHint.setText("Looks good");
		} else {
			sEmailHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			sEmailHint.setText("Enter a valid email address");
		}
	}

	private void validateSignupPw(StackPane pwBar) {
		String pw = sPw.getText();
		if (pw.isEmpty()) {
			sPwHint.setText("");
			pwFill.setWidth(0);
			validateSignupPw2();
			return;
		}
		int score = pwScore(pw);
		double pct = Math.min(1.0, score / 5.0);

		// Animate fill width
		double targetW = pwBar.getWidth() * pct;
		Timeline tl = new Timeline(new KeyFrame(Duration.millis(300),
				new KeyValue(pwFill.widthProperty(), targetW, Interpolator.EASE_OUT)));
		tl.play();

		String[] colors = {"#e24b4a","#e24b4a","#ef9f27","#ef9f27","#444441","#444441"};
		pwFill.setFill(Color.web(colors[Math.min(score, 5)]));

		if (pw.length() < 8) {
			sPwHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			sPwHint.setText("Minimum 8 characters required");
		} else {
			String[] labels = {"Too short","Weak","Fair","Good","Strong","Very strong"};
			sPwHint.setStyle("-fx-text-fill: " + (score >= 3 ? "#555555" : "#888888") + "; -fx-font-size: 11px;");
			sPwHint.setText(labels[Math.min(score, 5)]);
		}
		validateSignupPw2();
	}

	private void validateSignupPw2() {
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

	// ═══════════════════════════════════════════════════════════════════════════
	//  API CALLS
	// ═══════════════════════════════════════════════════════════════════════════
	private void doLogin() {
		String email = lEmail.getText().trim();
		String pw    = lPw.getText();
		boolean ok   = true;

		if (email.isEmpty() || !isValidEmail(email)) {
			lEmailHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			lEmailHint.setText("Enter a valid email address");
			ok = false;
		}
		if (pw.isEmpty()) {
			lPwHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			lPwHint.setText("Password is required");
			ok = false;
		}
		if (!ok) return;

		String body = "{\"email\":\"" + esc(email) + "\",\"password\":\"" + esc(pw) + "\"}";
		callApi("http://localhost:8080/api/login", body);
	}

	private void doSignup() {
		String fn    = sFn.getText().trim();
		String ln    = sLn.getText().trim();
		String email = sEmail.getText().trim();
		String pw    = sPw.getText();
		String pw2   = sPw2.getText();
		boolean ok   = true;

		if (fn.isEmpty() || ln.isEmpty()) {
			sNameHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			sNameHint.setText("Please fill in both first and last name");
			ok = false;
		}
		if (!isValidEmail(email)) {
			sEmailHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			sEmailHint.setText("Enter a valid email address");
			ok = false;
		}
		if (pw.length() < 8) {
			sPwHint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			sPwHint.setText("Minimum 8 characters required");
			ok = false;
		}
		if (!pw.equals(pw2)) {
			sPw2Hint.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px;");
			sPw2Hint.setText("Passwords do not match");
			ok = false;
		}
		if (!termsAccepted) {
			showToast("Please accept the terms to continue", false);
			ok = false;
		}
		if (!ok) return;

		String name = fn + " " + ln;
		String body = "{\"name\":\"" + esc(name) + "\",\"email\":\"" + esc(email) +
				"\",\"password\":\"" + esc(pw) + "\",\"role\":\"" + selectedRole + "\"}";
		Task<String> task = apiTask("http://localhost:8080/api/register", body);
		task.setOnSucceeded(e -> {
			String msg = task.getValue();
			if (msg.startsWith("SUCCESS")) {
				showToast(msg.replace("SUCCESS: ", ""), true);
				PauseTransition pt = new PauseTransition(Duration.millis(1500));
				pt.setOnFinished(ev -> switchTab(true));
				pt.play();
			} else {
				showToast(msg.replace("ERROR: ", ""), false);
			}
		});
		task.setOnFailed(e -> showToast("Cannot reach the server. Is the Java app running?", false));
		new Thread(task).start();
	}

	private void callApi(String url, String body) {
		Task<String> task = apiTask(url, body);
		task.setOnSucceeded(e -> {
			String msg = task.getValue();
			if (msg.startsWith("SUCCESS")) showToast(msg.replace("SUCCESS: ",""), true);
			else                           showToast(msg.replace("ERROR: ",""),   false);
		});
		task.setOnFailed(e -> showToast("Cannot reach the server. Is the Java app running?", false));
		new Thread(task).start();
	}

	private Task<String> apiTask(String url, String body) {
		return new Task<>() {
			protected String call() throws Exception {
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest req = HttpRequest.newBuilder()
						.uri(URI.create(url))
						.header("Content-Type", "application/json")
						.POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
						.build();
				HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
				String r = resp.body();
				int i = r.indexOf("\"message\"");
				if (i < 0) return r;
				int s = r.indexOf('"', i + 10) + 1;
				int end = r.indexOf('"', s);
				return r.substring(s, end);
			}
		};
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  DARK MODE TOGGLE
	// ═══════════════════════════════════════════════════════════════════════════
	private void toggleDark() {
		dark = !dark;

		// Animate thumb
		TranslateTransition tt = new TranslateTransition(Duration.millis(200), trackThumb);
		tt.setToX(dark ? 6.5 : -6.5);
		tt.play();

		dmLabel.setText(dark ? "Dark" : "Light");
		dmLabel.setStyle("-fx-text-fill: " + text2() + "; -fx-font-size: 12px;");

		// Rebuild panels
		outerCard.getChildren().clear();

		leftPanel  = buildLeftPanel();
		rightPanel = buildRightPanel();
		HBox.setHgrow(rightPanel, Priority.ALWAYS);
		outerCard.getChildren().addAll(leftPanel, rightPanel);
		outerCard.setStyle(
				"-fx-background-color: " + bg() + ";" +
						"-fx-background-radius: 12;" +
						"-fx-border-color: " + border2() + ";" +
						"-fx-border-width: 0.5;" +
						"-fx-border-radius: 12;" +
						"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 24, 0, 0, 4);"
		);
		root.setStyle("-fx-background-color: " + bg2() + ";");
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  TOAST
	// ═══════════════════════════════════════════════════════════════════════════
	private void showToast(String msg, boolean success) {
		Platform.runLater(() -> {
			if (toastStage != null) toastStage.close();
			toastStage = new Stage();
			toastStage.initOwner(primaryStage);
			toastStage.initStyle(StageStyle.UNDECORATED);
			toastStage.setAlwaysOnTop(true);

			Label lbl = new Label(msg);
			lbl.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
			lbl.setTextFill(Color.web(success ? "#16a34a" : "#dc2626"));

			StackPane pane = new StackPane(lbl);
			pane.setPadding(new Insets(10, 16, 10, 16));
			pane.setStyle(
					"-fx-background-color: " + (success ? "#f0fdf4" : "#fef2f2") + ";" +
							"-fx-background-radius: 8;" +
							"-fx-border-color: " + (success ? "#bbf7d0" : "#fecaca") + ";" +
							"-fx-border-width: 0.5;" +
							"-fx-border-radius: 8;" +
							"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 2);"
			);
			pane.setMinWidth(260);

			Scene sc = new Scene(pane);
			sc.setFill(Color.TRANSPARENT);
			toastStage.setScene(sc);
			toastStage.sizeToScene();

			// Position top-right of main window
			toastStage.setX(primaryStage.getX() + primaryStage.getWidth() - 280);
			toastStage.setY(primaryStage.getY() + 20);
			toastStage.show();

			// Fade in
			FadeTransition ft = new FadeTransition(Duration.millis(200), pane);
			ft.setFromValue(0); ft.setToValue(1); ft.play();

			// Auto-dismiss
			PauseTransition pt = new PauseTransition(Duration.seconds(5));
			pt.setOnFinished(e -> {
				FadeTransition out = new FadeTransition(Duration.millis(200), pane);
				out.setFromValue(1); out.setToValue(0);
				out.setOnFinished(ev -> toastStage.close());
				out.play();
			});
			pt.play();
		});
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  COMPONENT FACTORY HELPERS
	// ═══════════════════════════════════════════════════════════════════════════

	private TextField inputField(String placeholder) {
		TextField f = new TextField();
		f.setPromptText(placeholder);
		f.setStyle(
				"-fx-background-color: " + bg() + ";" +
						"-fx-border-color: " + border2() + ";" +
						"-fx-border-width: 0.5;" +
						"-fx-border-radius: 8;" +
						"-fx-background-radius: 8;" +
						"-fx-font-size: 14px;" +
						"-fx-text-fill: " + text() + ";" +
						"-fx-prompt-text-fill: " + text3() + ";" +
						"-fx-padding: 10 12 10 12;"
		);
		f.setPrefHeight(42);
		f.setMaxWidth(Double.MAX_VALUE);
		// Focus glow
		f.focusedProperty().addListener((o, ov, nv) -> {
			String border = nv ? text() : border2();
			f.setStyle(f.getStyle().replaceAll("-fx-border-color: [^;]+;", "-fx-border-color: " + border + ";"));
		});
		return f;
	}

	private PasswordField passwordField(String placeholder) {
		PasswordField f = new PasswordField();
		f.setPromptText(placeholder);
		f.setStyle(
				"-fx-background-color: " + bg() + ";" +
						"-fx-border-color: " + border2() + ";" +
						"-fx-border-width: 0.5;" +
						"-fx-border-radius: 8;" +
						"-fx-background-radius: 8;" +
						"-fx-font-size: 14px;" +
						"-fx-text-fill: " + text() + ";" +
						"-fx-prompt-text-fill: " + text3() + ";" +
						"-fx-padding: 10 40 10 12;"
		);
		f.setPrefHeight(42);
		f.setMaxWidth(Double.MAX_VALUE);
		return f;
	}

	/** Password field + show/hide toggle button in an overlay */
	private HBox passwordRow(PasswordField pf) {
		// We overlay a toggle button using a StackPane
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

		// Use a TextField as the visible-password alternative
		TextField vis = new TextField();
		vis.setStyle(pf.getStyle());
		vis.setVisible(false);
		vis.setManaged(false);
		vis.setMaxWidth(Double.MAX_VALUE);
		vis.textProperty().bindBidirectional(pf.textProperty());

		eye.setOnAction(e -> {
			boolean showing = vis.isVisible();
			vis.setVisible(!showing); vis.setManaged(!showing);
			pf.setVisible(showing);  pf.setManaged(showing);
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

	private StackPane inputWrap(TextField f) {
		StackPane sp = new StackPane(f);
		sp.setMaxWidth(Double.MAX_VALUE);
		return sp;
	}

	private Button primaryButton(String text) {
		Button b = new Button(text);
		b.setPrefHeight(44);
		b.setMaxWidth(Double.MAX_VALUE);
		b.setCursor(Cursor.HAND);
		b.setStyle(
				"-fx-background-color: " + (dark ? "#f0f0f0" : "#111111") + ";" +
						"-fx-text-fill: " + (dark ? "#111111" : "#ffffff") + ";" +
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

	private Button ghostButton(String text) {
		Button b = new Button(text);
		b.setPrefHeight(42);
		b.setMaxWidth(Double.MAX_VALUE);
		b.setCursor(Cursor.HAND);
		b.setStyle(
				"-fx-background-color: transparent;" +
						"-fx-text-fill: " + text2() + ";" +
						"-fx-font-size: 13px;" +
						"-fx-background-radius: 8;" +
						"-fx-border-color: " + border2() + ";" +
						"-fx-border-width: 0.5;" +
						"-fx-border-radius: 8;" +
						"-fx-cursor: hand;"
		);
		b.setOnMouseEntered(e -> b.setStyle(b.getStyle().replaceAll(
				"-fx-background-color: [^;]+;", "-fx-background-color: " + bg2() + ";")));
		b.setOnMouseExited(e -> b.setStyle(b.getStyle().replaceAll(
				"-fx-background-color: [^;]+;", "-fx-background-color: transparent;")));
		return b;
	}

	private HBox divider() {
		HBox row = new HBox(10);
		row.setAlignment(Pos.CENTER);
		row.setMaxWidth(Double.MAX_VALUE);
		Separator l = new Separator(); l.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(l, Priority.ALWAYS);
		Separator r = new Separator(); r.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(r, Priority.ALWAYS);
		Label or = styledLabel("or", 12, text3());
		row.getChildren().addAll(l, or, r);
		return row;
	}

	private Label styledLabel(String text, int size, String color) {
		Label l = new Label(text);
		l.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size + "px;");
		return l;
	}

	private Label formLabel(String text) {
		Label l = styledLabel(text, 12, text2());
		l.setStyle(l.getStyle() + " -fx-font-weight: bold;");
		return l;
	}

	private Label hintLabel(String text) {
		Label l = styledLabel(text, 11, text3());
		l.setMinHeight(14);
		return l;
	}

	private Region vspace(int h) {
		Region r = new Region();
		r.setMinHeight(h); r.setPrefHeight(h); r.setMaxHeight(h);
		return r;
	}

	private void addSpaced(VBox box, Node... nodes) {
		box.getChildren().addAll(nodes);
	}

	// ═══════════════════════════════════════════════════════════════════════════
	//  UTILITIES
	// ═══════════════════════════════════════════════════════════════════════════
	private static boolean isValidEmail(String v) {
		return Pattern.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", v);
	}

	private static int pwScore(String pw) {
		int s = 0;
		if (pw.length() >= 8)               s++;
		if (pw.length() >= 12)              s++;
		if (pw.matches(".*[A-Z].*"))        s++;
		if (pw.matches(".*[0-9].*"))        s++;
		if (pw.matches(".*[^A-Za-z0-9].*")) s++;
		return s;
	}

	private static String esc(String s) {
		return s.replace("\\","\\\\").replace("\"","\\\"");
	}

	// ── Palette helpers (respect dark mode) ───────────────────────────────────
	private static String bg()      { return dark ? D_BG      : L_BG;      }
	private static String bg2()     { return dark ? D_BG2     : L_BG2;     }
	private static String border2() { return dark ? D_BORDER2 : L_BORDER2; }
	private static String text()    { return dark ? D_TEXT    : L_TEXT;    }
	private static String text2()   { return dark ? D_TEXT2   : L_TEXT2;   }
	private static String text3()   { return dark ? D_TEXT3   : L_TEXT3;   }

	// ── Entry point ───────────────────────────────────────────────────────────
	public static void main(String[] args) {
		launch(args);
	}
}
