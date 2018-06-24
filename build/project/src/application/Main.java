package application;

import java.io.IOException;

import application.pessoa.RegisterEditPersonController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.database.ConnectionManager;
import main.person.Person;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	public static Stage primaryStage;

	public static ConnectionManager connectionManager;

	@Override
	public void start(Stage stage) {
		try {
			/* Default
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			Parent root = FXMLLoader.load(getClass().getResource("/application/SceneMain.fxml"));
			 */
			primaryStage = stage;
			Parent root = FXMLLoader.load(Main.class.getClass().getResource("/application/login/LoginScene.fxml"));
			primaryStage.setTitle("Login to Marota Social Network");
			Scene scene = new Scene(root);
			// Carrega o css AquaTheme
			scene.getStylesheets().add(Main.class.getClass().getResource("/application/themes/AquaTheme.css").toExternalForm());
			//Seta o ícone da aplicação
			/*https://www.iconfinder.com/icons/3151579/cup_game_geek_minecraft_mug_video_icon#size=256*/
			primaryStage.getIcons().add(new Image("/application/icons/cup_game_geek_minecraft_mug_video_icon.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void loadMainScene() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getClass().getResource("/application/MainScene.fxml")));
			scene.getStylesheets().add(Main.class.getClass().getResource("/application/themes/AquaTheme.css").toExternalForm());
			primaryStage.setTitle("Marota Social Network");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// Fechar a conexão com o banco de dados
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					System.out.println("Conexão fechada.");
					connectionManager.close();
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

	public static void showAlert(String messageTitle, String messageHeader, String messageContent, AlertType tipoDeAlerta) {
	    Alert alert = new Alert(tipoDeAlerta);
	    alert.setTitle(messageTitle);
	    alert.setHeaderText(messageHeader);
	    alert.setContentText(messageContent);
	    alert.showAndWait();
	}
}
