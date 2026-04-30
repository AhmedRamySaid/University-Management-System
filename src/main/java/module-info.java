module UMS {
	requires io.github.cdimascio.dotenv.java;
	requires java.net.http;
	requires java.sql;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires jbcrypt;

	opens ASU.CAIE to javafx.fxml, javafx.graphics;

	opens ASU.CAIE.model to javafx.base;

	exports ASU.CAIE;
	exports ASU.CAIE.GUI;

	exports ASU.CAIE.Users;
	exports ASU.CAIE.model;
}