package application;

import java.io.IOException;
//import java.awt.event.ActionEvent; //IMPORT ERRADO
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.pessoa.RegisterEditPersonController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.person.Person;
import main.person.PersonService;
import main.util.DataUtil;

public class MainController implements Initializable {

	@FXML private TableView<Person> tvListaPessoas;
    
	@FXML private TableColumn<Person, Long> tcCodigo;
    @FXML private TableColumn<Person, String> tcNome;
    @FXML private TableColumn<Person, String> tcEmail;
    
    @FXML private Label lbCodigo;
	@FXML private Label lbNome;
	@FXML private Label lbEmail;
	@FXML private Label lbDataNascimento;
	@FXML private Label lbCidadeNascimento;
	@FXML private Label lbCidadeResidencia;

	@FXML private Button btInserir;
	@FXML private Button btEditar;
	@FXML private Button btExcluir;
	
	@FXML private TableView<Person> tvListaAmigos;
    
	@FXML private TableColumn<Person, Long> tcAmigoCodigo;
    @FXML private TableColumn<Person, String> tcAmigoNome;
    @FXML private TableColumn<Person, String> tcAmigoEmail;
	
	@FXML
	public void inserir(ActionEvent event) {
		System.out.println("inserir");
		showRegisterEditPersonDialog(null, "Novo...");
		setListPersons();
	}

	@FXML
	public void editar(ActionEvent event) {
		System.out.println("editar");
		String codigo = lbCodigo.getText();
		if(!codigo.equals("")) {
			Person pessoa = PersonService.instance.getPessoa(codigo);
			pessoa = showRegisterEditPersonDialog(pessoa, "Editar");
			setListPersons();
			showPersonDetails(pessoa);
		} else {
			Main.showAlert("Editar", "Ops!", "Por favor, selecione uma pessoa para editar.", AlertType.INFORMATION);
		}
	}

	@FXML
	public void excluir(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Excluir");
		alert.setHeaderText("Atenção!");
		alert.setContentText("Você deseja excluir a pessoa selecionada?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // Usuario escolhe OK
			System.out.println("excluir");
			String codigo = lbCodigo.getText();
			Person pessoa = PersonService.instance.getPessoa(codigo);
			PersonService.instance.deletePessoa(pessoa);
			setListPersons();
		} else {
		    // Usuario escolhe CANCEL ou fecha a dialog
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		tcCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		setListPersons();
		
		showPersonDetails(null);
		
		tvListaPessoas.getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<Person>() {
				@Override
				public void changed(ObservableValue observable, Person oldValue, Person newValue) {
				// TODO Auto-generated method stub
				showPersonDetails(newValue);
			};
		});
	}
	
	public void setListPersons() {
		if(PersonService.instance.getTodasAsPessoasDoSistema() != null) {
			ObservableList<Person> listaDePessoas = FXCollections.observableArrayList(PersonService.instance.getTodasAsPessoasDoSistema());
			tvListaPessoas.setItems(listaDePessoas);			
		}
	}
	
	public void showPersonDetails(Person pessoa) {
		if(pessoa != null) {
			lbCodigo.setText(pessoa.getCodigo().toString());
			lbNome.setText(pessoa.getNome());
			lbEmail.setText(pessoa.getEmail());
			try {
				lbDataNascimento.setText(pessoa.getDataNascimento());
			} catch (Exception e) {
				e.printStackTrace();
				lbDataNascimento.setText("");
			}
			lbCidadeNascimento.setText(pessoa.getCidadeNascimento());
			lbCidadeResidencia.setText(pessoa.getCidadeResidencia());
			
			tcAmigoCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
			tcAmigoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
			tcAmigoEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
			
			ObservableList<Person> listaDePessoas = FXCollections.observableArrayList(pessoa.getAmigos());
			
			tvListaAmigos.setItems(FXCollections.observableArrayList(listaDePessoas));
		} else {
			lbCodigo.setText("");
			lbNome.setText("");
			lbEmail.setText("");
			lbDataNascimento.setText("");
			lbCidadeNascimento.setText("");
			lbCidadeResidencia.setText("");
			
			tcAmigoCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
			tcAmigoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
			tcAmigoEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
			
			tvListaAmigos.setItems(null);
		}
	}
	
	public Person showRegisterEditPersonDialog(Person pessoa, String title) {
		try {
	        // Carrega o arquivo fxml e cria um novo stage para a janela popup.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource("/application/pessoa/RegisterEditPersonDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Cria o palco dialogStage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle(title);
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(Main.primaryStage);
	        Scene scene = new Scene(page);
	        scene.getStylesheets().add(Main.class.getClass().getResource("/application/themes/AquaTheme.css").toExternalForm());
	        dialogStage.setScene(scene);

	        RegisterEditPersonController controller = loader.getController();
	        if(pessoa == null) {
	        	pessoa = new Person();
	        }
	        controller.setPerson(pessoa);
	        
	        if(title.equals("Novo...")) {
	        	controller.isRegister();
	        } else {
	        	controller.isEdit(pessoa);
	        }
	        
	        dialogStage.showAndWait(); //dialogStage.show();
	        
	        return controller.getPerson();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
