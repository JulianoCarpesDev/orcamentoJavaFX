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
import model.entites.Budget;
import model.services.BudgetService;
import model.services.ClienteService;
import model.services.ProdutoService;
import utils.Alerts;
import utils.Utils;

public class ControllerListaBudget implements Initializable, DataChangeListener {

	private BudgetService service;
	@FXML
	private TableView<Budget> tableViewBudget;

	@FXML
	private TableColumn<Budget, Integer> tableCollumnId;

	@FXML
	private TableColumn<Budget, String> tableCollumnClienteNome;
	@FXML
	private TableColumn<Budget, String> tableCollumnProdutoNome;
	@FXML
	private TableColumn<Budget, Integer> tableCollumnQuantidade;
	@FXML
	private TableColumn<Budget, Double> tableCollumnValor_unitario;
	@FXML
	private TableColumn<Budget, Double> tableCollumnValor_Total;
	@FXML
	private TableColumn<Budget, Budget> tableCollumnEditar;
	@FXML
	private TableColumn<Budget, Budget> tableCollumnRemove;


	@FXML
	private Button btnNovo;

	private ObservableList<Budget> obsList;

	@FXML
	public void onBtNovo(ActionEvent evento) {
		Stage pai = Utils.palcoAtual(evento);
		Budget obj = new Budget();
		
		criaDialogoForm(obj, "/viewsControllers/BudgetFormulario.fxml", pai);
	}

	public void setBudgetService(BudgetService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciaNo();
	}

	private void iniciaNo() {
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableCollumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("cliente_nome"));
		tableCollumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("produto_nome"));
		tableCollumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableCollumnValor_unitario.setCellValueFactory(new PropertyValueFactory<>("valor_unitario"));
		tableCollumnValor_Total.setCellValueFactory(new PropertyValueFactory<>("valor_total"));
		Utils.formatTableColumnDouble(tableCollumnValor_unitario, 2);
		Utils.formatTableColumnDouble(tableCollumnValor_Total, 2);
		Stage palco = (Stage) Main.getMainScene().getWindow();

		tableViewBudget.prefHeightProperty().bind(palco.heightProperty());

	}

	public void atualizaTableView() {
		if (service == null) {
			throw new IllegalStateException("Service esta nulo");
		}
		List<Budget> list = service.buscarTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewBudget.setItems(obsList);
		iniciaButtonEditar();
		iniciaRemoveButtons();
	}

	private void criaDialogoForm(Budget obj, String caminho, Stage palco) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
	        Pane novaTela = loader.load();

	        ControllerFormularioBudget formController = loader.getController();

	        formController.setBudget(obj);
	        formController.setServices(new BudgetService(), new  ProdutoService(), new ClienteService());
	        formController.carregaClientes();
	        formController.carregarProdutos();
	        formController.sobrescreveDataChangeList(this);
	        formController.updateFormulario();

	        Stage novoPalco = new Stage();
	        novoPalco.setTitle("Entre com os dados do Orçamento");
	        novoPalco.setScene(new Scene(novaTela));
	        novoPalco.setResizable(false);
	        novoPalco.initOwner(palco);
	        novoPalco.initModality(Modality.WINDOW_MODAL);
	        novoPalco.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	        Alerts.showAlerts("IO Exception", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
	    }
	}

	@Override
	public void onDataChanged() {
		atualizaTableView();

	}

	private void iniciaButtonEditar() {
		tableCollumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableCollumnEditar.setCellFactory(param -> new TableCell<Budget, Budget>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Budget obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> criaDialogoForm(obj, "/viewsControllers/BudgetFormulario.fxml", Utils.palcoAtual(event)));
			}

		});
	}

	private void iniciaRemoveButtons() {
		tableCollumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableCollumnRemove.setCellFactory(param -> new TableCell<Budget, Budget>() {
			private final Button button = new Button("deletar");

			@Override
			protected void updateItem(Budget obj, boolean empty) {
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

	private void deletaCliente(Budget obj) {
		
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
