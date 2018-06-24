package application.login;

import java.io.IOException;
//import java.awt.TextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import main.database.ConnectionManager;
import main.person.Person;
import main.person.PersonService;
import main.util.PersonUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

	@FXML private TextField tfHost;
	@FXML private TextField tfPort;
	@FXML private TextField tfUser;
	@FXML private PasswordField pwfPassword;

	@FXML
	public void conectar(ActionEvent event) throws IOException {
		ConnectionManager connectionManager = new ConnectionManager(tfHost.getText(), tfPort.getText(),
				tfUser.getText(), pwfPassword.getText());
		
		String erro = "";
		try {
			System.out.println("Tentando estabelecer conexão...");
			connectionManager.open();
			System.out.println("Conexão aberta.");
			Main.connectionManager = connectionManager;
			Main.loadMainScene();
			
		} catch (Exception e) {
			// TODO: handle exception
			erro = e.getMessage();
			Main.showAlert("Connection failed", "Problemas ao estabelecer uma conexão com o banco de dados", erro, AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) { }
}