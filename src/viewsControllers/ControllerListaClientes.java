package viewsControllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityExeception;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entites.Clientes;
import model.services.ClienteService;
import model.services.ClienteService;
import utils.Alerts;
import utils.Utils;

public class ControllerListaClientes implements Initializable, DataChangeListener {

	private ClienteService service;
	@FXML
	private TableView<Clientes> tableViewClientes;

	@FXML
	private TableColumn<Clientes, Integer> tableCollumnId;

	@FXML
	private TableColumn<Clientes, String> tableCollumnName;

	@FXML
	private TableColumn<Clientes, Double> tableCollumnEndereco;
	@FXML
	private TableColumn<Clientes, Double> tableCollumnTelefone;
	@FXML
	private TableColumn<Clientes, Clientes> tableCollumnEditar;
	@FXML
	private TableColumn<Clientes, Clientes> tableCollumnRemove;


	@FXML
	private Button btnNovo;

	private ObservableList<Clientes> obsList;

	@FXML
	public void onBtNovo(ActionEvent evento) {
		Clientes obj = new Clientes();
		Stage pai = Utils.palcoAtual(evento);
		criaDialogoForm(obj, "/viewsControllers/FormularioClientes.fxml", pai);
	}

	public void setClientesService(ClienteService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciaNo();
	}

	private void iniciaNo() {
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableCollumnEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
		tableCollumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

		Stage palco = (Stage) Main.getMainScene().getWindow();

		tableViewClientes.prefHeightProperty().bind(palco.heightProperty());

	}

	public void atualizaTableView() {
		if (service == null) {
			throw new IllegalStateException("Service esta nulo");
		}
		List<Clientes> list = service.buscarTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewClientes.setItems(obsList);
		iniciaButtonEditar();
		iniciaRemoveButtons();
	}

	private void criaDialogoForm(Clientes obj, String caminho, Stage palco) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane novaTela = loader.load();

			ControllerFormularioClientes formControler = loader.getController();

			formControler.setClientes(obj);
			formControler.setService(new ClienteService());
			formControler.sobrescreveDataChangeList(this);
			formControler.updateFormulario();

			Stage novoPalco = new Stage();
			novoPalco.setTitle("Entre com os dados do Cliente");
			novoPalco.setScene(new Scene(novaTela));
			novoPalco.setResizable(false);
			novoPalco.initOwner(palco);
			novoPalco.initModality(Modality.WINDOW_MODAL);
			novoPalco.showAndWait();
		} catch (IOException e) {
			Alerts.showAlerts("IO Exception", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		atualizaTableView();

	}

	private void iniciaButtonEditar() {
		tableCollumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableCollumnEditar.setCellFactory(param -> new TableCell<Clientes, Clientes>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Clientes obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> criaDialogoForm(obj, "/viewsControllers/FormularioClientes.fxml", Utils.palcoAtual(event)));
			}

		});
	}

	private void iniciaRemoveButtons() {
		tableCollumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableCollumnRemove.setCellFactory(param -> new TableCell<Clientes, Clientes>() {
			private final Button button = new Button("deletar");

			@Override
			protected void updateItem(Clientes obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> deletaCliente(obj));

			}
		});
	}

	private void deletaCliente(Clientes obj) {
		
		Optional<ButtonType>res = Alerts.showConfirmation("Confirmação","Tem certeza que deseja deletar");
		if(res.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service esta nulo");
			}
			try {
				service.remove(obj);
				atualizaTableView();
			} catch (DbIntegrityExeception e) {
				Alerts.showAlerts("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}

}
