module UMS {
	requires io.github.cdimascio.dotenv.java;
	requires java.net.http;
	requires java.sql;
	requires javafx.controls;
	requires javafx.graphics;
	requires jbcrypt;

	opens ASU.CAIE to javafx.fxml, javafx.graphics;
	exports ASU.CAIE;
	exports ASU.CAIE.GUI;
	exports ASU.CAIE.GUI.Forms;
	exports ASU.CAIE.GUI.Panels;
	exports ASU.CAIE.GUI.Helpers;
}