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
import model.entites.Produtos;
import model.services.ProdutoService;
import utils.Alerts;
import utils.Utils;

public class ControllerListaProdutos implements Initializable, DataChangeListener {

	private ProdutoService service;
	@FXML
	private TableView<Produtos> tableViewProdutos;

	@FXML
	private TableColumn<Produtos, Integer> tableCollumnId;

	@FXML
	private TableColumn<Produtos, String> tableCollumnName;

	@FXML
	private TableColumn<Produtos, Double> tableCollumnPreco;
	@FXML
	private TableColumn<Produtos, Double> tableCollumnDescricao;
	@FXML
	private TableColumn<Produtos, Produtos> tableCollumnEditar;
	@FXML
	private TableColumn<Produtos, Produtos> tableCollumnRemove;


	@FXML
	private Button btnNovo;

	private ObservableList<Produtos> obsList;

	@FXML
	public void onBtNovo(ActionEvent evento) {
		Produtos obj = new Produtos();
		Stage pai = Utils.palcoAtual(evento);
		criaDialogoForm(obj, "/viewsControllers/FormularioProdutos.fxml", pai);
	}

	public void setProdutosService(ProdutoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciaNo();
	}

	private void iniciaNo() {
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableCollumnPreco.setCellValueFactory(new PropertyValueFactory<>("Preco"));
		tableCollumnDescricao.setCellValueFactory(new PropertyValueFactory<>("Descricao"));
		Utils.formatTableColumnDouble(tableCollumnPreco, 2);
		
		Stage palco = (Stage) Main.getMainScene().getWindow();

		tableViewProdutos.prefHeightProperty().bind(palco.heightProperty());

	}

	public void atualizaTableView() {
		if (service == null) {
			throw new IllegalStateException("Service esta nulo");
		}
		List<Produtos> list = service.buscarTodos();
		obsList = FXCollections.observableArrayList(list);
		tableViewProdutos.setItems(obsList);
		iniciaButtonEditar();
		iniciaRemoveButtons();
	}

	private void criaDialogoForm(Produtos obj, String caminho, Stage palco) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane novaTela = loader.load();

			ControllerFormulario formControler = loader.getController();

			formControler.setProdutos(obj);
			formControler.setService(new ProdutoService());
			formControler.sobrescreveDataChangeList(this);
			formControler.updateFormulario();

			Stage novoPalco = new Stage();
			novoPalco.setTitle("Entre com os dados do Produto");
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
		tableCollumnEditar.setCellFactory(param -> new TableCell<Produtos, Produtos>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Produtos obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> criaDialogoForm(obj, "/viewsControllers/FormularioProdutos.fxml", Utils.palcoAtual(event)));
			}

		});
	}

	private void iniciaRemoveButtons() {
		tableCollumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableCollumnRemove.setCellFactory(param -> new TableCell<Produtos, Produtos>() {
			private final Button button = new Button("deletar");

			@Override
			protected void updateItem(Produtos obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> deletaProduto(obj));

			}
		});
	}

	private void deletaProduto(Produtos obj) {
		
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
