package application.pessoa;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import application.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.person.Person;
import main.person.PersonService;
import main.util.DataUtil;

public class RegisterEditPersonController implements Initializable {

	@FXML private Label lbCodigo;
	
	@FXML private TextField tfCodigo;
	@FXML private TextField tfNome;
	@FXML private TextField tfEmail;
	@FXML private TextField tfDataNascimento;
	@FXML private TextField tfCidadeNascimento;
	@FXML private TextField tfCidadeResidencia;

	@FXML private TableView<Person> tvAmigos;
	@FXML private TableColumn<Person, Long> tdAmigoCodigo;
	@FXML private TableColumn<Person, String> tdAmigoNome;
	@FXML private TableColumn<Person, String> tdAmigoEmail;
	
	@FXML private ComboBox<Person> cbPersons;
	
	@FXML private Button btAddFriend;
	@FXML private Button btRemoveFriend;
	
	@FXML private Label lbCadastrarPessoa;
	@FXML private Button btRegister;

	@FXML private Label lbEditarPessoa;
	@FXML private Button btEdit;

	private Person person;

	@FXML
	public void register(ActionEvent event) {
		System.out.println("register");
		
		String erro = "";
		erro = validateTextFields();
		if(erro == null || erro.equals("")) {
			try {
				Person novaPessoa = new Person();
				novaPessoa.setNome(tfNome.getText());
				novaPessoa.setEmail(tfEmail.getText());
				novaPessoa.setCidadeNascimento(tfCidadeNascimento.getText());
				novaPessoa.setCidadeResidencia(tfCidadeResidencia.getText());
				
				GregorianCalendar date = DataUtil.instance.stringToGregorianCalendar(tfDataNascimento.getText());
				novaPessoa.setDataNascimento(tfDataNascimento.getText());

				PersonService.instance.insertPerson(novaPessoa);
				Main.showAlert("Cadastrar nova pessoa", "Sucesso!", "O cadastro da pessoa foi registrado no banco de dados.", AlertType.INFORMATION);
				setPerson(novaPessoa);
			} catch (Exception e) {
				e.printStackTrace();
				erro = e.getMessage();
				Main.showAlert("Insert failed", "Problemas na inserção da nova pessoa.", erro, AlertType.ERROR);
				setPerson(null);
			}
		} else {
			Main.showAlert("Insert failed", "Problemas na validação dos campos.", erro, AlertType.ERROR);
			setPerson(null);
		}
	}

	@FXML
	public void edit(ActionEvent event) {
		System.out.println("edit");
		
		String erro = "";
		erro = validateTextFields();
		if(erro == null || erro.equals("")) {
			try { 
				Person editedPessoa = getPerson();
				editedPessoa.setCodigo(Long.parseLong(tfCodigo.getText()));
				editedPessoa.setNome(tfNome.getText());
				editedPessoa.setEmail(tfEmail.getText());
				editedPessoa.setCidadeNascimento(tfCidadeNascimento.getText());
				editedPessoa.setCidadeResidencia(tfCidadeResidencia.getText());
				
				GregorianCalendar date = DataUtil.instance.stringToGregorianCalendar(tfDataNascimento.getText());
				editedPessoa.setDataNascimento(tfDataNascimento.getText());
	
				PersonService.instance.updatePerson(editedPessoa);
				Main.showAlert("Editar pessoa", "Sucesso!", "O cadastro da pessoa foi atualizado.", AlertType.INFORMATION);
				setPerson(editedPessoa);
			} catch (Exception e) {
				e.printStackTrace();
				erro = e.getMessage();
				Main.showAlert("Update failed", "Problemas na atualização da pessoa.", erro, AlertType.ERROR);
				setPerson(null);
			}
		} else {
			Main.showAlert("Update failed", "Problemas na validação dos campos.", erro, AlertType.ERROR);
			setPerson(null);
		}
		
	}
	
	@FXML
	public void addFriend(ActionEvent event) {
		System.out.println("addFriend");
		
		Person amigo = cbPersons.getValue();
		if(amigo != null) {
			String erro = "";
			try {
				if(getPerson().getAmigos() == null) {
					getPerson().setAmigos(new ArrayList<>());
				}
				getPerson().getAmigos().add(amigo);
				setFriendList();
				setFriendComboBox();
			} catch (Exception e) {
				e.printStackTrace();
				erro = e.getMessage();
				Main.showAlert("Insert friend failed", "Problemas na inserção do amigo.", erro, AlertType.ERROR);
			}
		} else {
			Main.showAlert("Adicionar amigo", "Ops!", "Por favor, selecione uma pessoa.", AlertType.INFORMATION);
		}
		
	}
	
	@FXML
	public void removeFriend(ActionEvent event) {
		System.out.println("removeFriend");
		
		Person amigoSelecionado = (Person) tvAmigos.getSelectionModel().getSelectedItem();
		
		Person amigoRemovido = null;
		for(Person amigo : getPerson().getAmigos()) {
			if(amigo.getCodigo() == amigoSelecionado.getCodigo()) {
				amigoRemovido = amigo;
			}
		}
		getPerson().getAmigos().remove(amigoRemovido);
		
		setFriendList();
		setFriendComboBox();
	}

	public void isRegister() {
		lbEditarPessoa.setVisible(false);
		btEdit.setVisible(false);
		tfCodigo.setDisable(true);
	}

	public void isEdit(Person pessoa) {
		// Botões cadastro desabilitados/invisíveis
		lbCadastrarPessoa.setVisible(false);
		btRegister.setVisible(false);
		
		// Trazer os valores em tela
		tfCodigo.setText(pessoa.getCodigo().toString());
		tfCodigo.setDisable(true);
		tfNome.setText(pessoa.getNome());
		tfEmail.setText(pessoa.getEmail());
		try {
			tfDataNascimento.setText(pessoa.getDataNascimento());
		} catch (Exception e) {
			e.printStackTrace();
		}
		tfCidadeNascimento.setText(pessoa.getCidadeNascimento());
		tfCidadeResidencia.setText(pessoa.getCidadeResidencia());
		
		setFriendList();
		setFriendComboBox();
	}

	public String validateTextFields() {
		StringBuilder erro = new StringBuilder();

		System.out.println("validateTextFields");

		if(tfNome.getText() == null || tfNome.getText().trim().equals(""))
			erro.append("Campo \"Nome\" é obrigatório\n");
		if(tfEmail.getText() == null || tfEmail.getText().trim().equals(""))
			erro.append("Campo \"E-mail\" é obrigatório\n");

		if(tfDataNascimento.getText() == null || tfDataNascimento.getText().trim().equals("")) {
			erro.append("Campo \"Data de Nascimento\" é obrigatório\n");
		} else {
			try {
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				Date data = formato.parse(tfDataNascimento.getText());
			} catch (Exception e) {
				e.printStackTrace();
				erro.append(e.getMessage());
			}
		}

		if(tfCidadeNascimento.getText() == null || tfCidadeNascimento.getText().trim().equals(""))
			erro.append("Campo \"Cidade de Nascimento\" é obrigatório\n");
		if(tfCidadeResidencia.getText() == null || tfCidadeResidencia.getText().trim().equals(""))
			erro.append("Campo \"Cidade de Residencia\" é obrigatório\n");

		return erro.toString();
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person pessoa) {
		this.person = pessoa;
	}
	
	public void setFriendList() {
		if(getPerson().getAmigos() != null) {
			ObservableList<Person> listaDeAmigos = FXCollections.observableArrayList(getPerson().getAmigos());
			tvAmigos.setItems(listaDeAmigos);			
		}
	}
	
	public void setFriendComboBox() {
		if(PersonService.instance.getTodasAsPessoasDoSistema() != null) {
			List<Person> listaDeAmigos = new ArrayList<>();
			listaDeAmigos = PersonService.instance.getTodasAsPessoasDoSistema();
			if(getPerson() != null && getPerson().getCodigo() != null) {
				List<Person> listaDeAmigosInvalidos = new ArrayList<>();
				
				for(Person amigo : listaDeAmigos) {
					if(amigo.getCodigo().toString().equals(getPerson().getCodigo().toString())) {
						listaDeAmigosInvalidos.add(amigo);
					}
				}
				
				List<Person> todosOsAmigosDaPessoa = getPerson().getAmigos();
				for(Person amigoDaPessoa : todosOsAmigosDaPessoa) {
					for(Person amigoDaLista : listaDeAmigos) {
						if(amigoDaPessoa.getCodigo().toString().equals(amigoDaLista.getCodigo().toString())) {
							listaDeAmigosInvalidos.add(amigoDaLista);
						}
					}
				}
				
				for(Person amigo : listaDeAmigosInvalidos) {
					listaDeAmigos.remove(amigo);
				}
			}
			ObservableList<Person> listaDeAmigosCombo = FXCollections.observableArrayList(listaDeAmigos);
			cbPersons.setItems(listaDeAmigosCombo);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		// Popular a lista de amigos
		tdAmigoCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		tdAmigoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tdAmigoEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		setFriendComboBox();
	}

}
