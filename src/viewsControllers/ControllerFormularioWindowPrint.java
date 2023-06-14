package viewsControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entites.Budget;
import model.entites.Clientes;
import model.entites.Produtos;
import model.exception.ValidaException;
import model.services.BudgetService;
import model.services.ClienteService;
import model.services.ProdutoService;
import utils.Alerts;
import utils.Utils;

public class ControllerFormularioWindowPrint implements Initializable {
	@FXML
	private TextField textNome;
	@FXML
	private TextField textTelefone;
	@FXML
	private TextField textEmail;
	@FXML
	private Label labelTotal;
	
	@FXML
	TableView<Budget>tableWindow;
    private ObservableList<Clientes>obsClientes;
    
    private ObservableList<Produtos>obsProdutos;

    @FXML
    private TextField txtQuantidade;
  
    @FXML
    private Label labelErrorId;

    @FXML
    private Label labelErroClienteNome;

    @FXML
    private Label labelErroProdutoNome;

    @FXML
    private Label labelErroQuantidade;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private Budget entidade;
  

    private BudgetService service;
    private ProdutoService produtoService;
    private ClienteService clienteService;
    private List<DataChangeListener> listener = new ArrayList<>();

    public BudgetService getService() {
        return service;
    }

    public void setServices(BudgetService service, ProdutoService produtoService, ClienteService clienteService) {
        this.service = service;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
    }

    public Budget getBudget() {
        return entidade;
    }

    public void setBudget(Budget entidade) {
        this.entidade = entidade;
    }

    public void sobrescreveDataChangeList(DataChangeListener data) {
        listener.add(data);
    }

    @FXML
    public void btnSalva(ActionEvent evento) {
        if (entidade == null) {
            throw new IllegalStateException("Entidade está nula");
        }

        if (service == null) {
            throw new IllegalStateException("Service está nulo");
        }

        try {
            entidade = getDadosForm();
            service.salvaAtualizarForm(entidade);
            notificaListener();
            Utils.palcoAtual(evento).close();
        } catch (ValidaException e) {
            setaErros(e.pegaErros());
        } catch (DbException e) {
            Alerts.showAlerts("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
        }
    }

    private void notificaListener() {
        for (DataChangeListener data : listener) {
            data.onDataChanged();
        }
    }

    private Budget getDadosForm() {
        Locale.setDefault(Locale.US);
        Budget obj = new Budget();
        ValidaException exception = new ValidaException("Erro no campo");

        obj.setId(Utils.converteParaInt(txtId.getText()));
        
       obj.setClientes(comboBoxClientes.getValue());
       obj.setProdutos(comboBoxProdutos.getValue());
        if (txtQuantidade.getText() == null) {
            exception.adicionaErros("quantidade", "Campo não pode estar vazio");
        } else {
            obj.setQuantidade(Utils.converteParaInt(txtQuantidade.getText()));
        }

        if (exception.pegaErros().size() > 0) {
            throw exception;
        }
        return obj;
    }

    @FXML
    public void btnCancela(ActionEvent evento) {
        Utils.palcoAtual(evento).close();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        iniciaNodes();
        initializaComboBoxClientes();
        initializaComboBoxProdutos();
    }

    private void iniciaNodes() {
        utils.Constrains.Constraints.setTextFieldInteger(txtId);
        utils.Constrains.Constraints.setTextFieldMaxLength(comboBoxClientes.getEditor(), 30);
        utils.Constrains.Constraints.setTextFieldMaxLength(comboBoxProdutos.getEditor(), 40);
        utils.Constrains.Constraints.setTextFieldInteger(txtQuantidade);
        
    }

    public void updateFormulario() {
        if (entidade == null) {
            throw new IllegalStateException("Entidade está nula");
        }
        txtId.setText(String.valueOf(entidade.getId()));

        if (entidade.getProdutos() != null) {
            comboBoxProdutos.getSelectionModel().selectFirst();
        } else {
            comboBoxProdutos.setValue(entidade.getProdutos());
        }

        if (entidade.getClientes() != null) {
          
            comboBoxClientes.getSelectionModel().selectFirst();
        } else {
            comboBoxClientes.setValue(entidade.getClientes());
        }

        txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
    }
    public void carregaClientes() {
    	List<Clientes> list = clienteService.buscarTodos();
    	obsClientes = FXCollections.observableArrayList(list);
    	comboBoxClientes.setItems(obsClientes);
		
	}
    
    public void carregarProdutos() {
    	List<Produtos> lista = produtoService.buscarTodos();
		obsProdutos = FXCollections.observableArrayList(lista);
		comboBoxProdutos.setItems(obsProdutos);
    }
    private void initializaComboBoxClientes (){
		Callback<ListView<Clientes>, ListCell<Clientes>> factory = lv -> new ListCell<Clientes>() {
			@Override
			protected void updateItem(Clientes item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}

		};
		comboBoxClientes.setCellFactory(factory);
		comboBoxClientes.setButtonCell(factory.call(null));
	}
    private void initializaComboBoxProdutos (){
		Callback<ListView<Produtos>, ListCell<Produtos>> factory = lv -> new ListCell<Produtos>() {
			@Override
			protected void updateItem(Produtos item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome() + " " +item.getPreco());
		
			}

		};
		comboBoxProdutos.setCellFactory(factory);
		comboBoxProdutos.setButtonCell(factory.call(null));
	}

    private void setaErros(Map<String, String> erros) {
        Set<String> campos = erros.keySet();

        if (campos.contains("cliente_nome")) {
            labelErroClienteNome.setText(erros.get("cliente_nome"));
        }
        if (campos.contains("produto_nome")) {
            labelErroProdutoNome.setText(erros.get("produto_nome"));
        }
        if (campos.contains("quantidade")) {
            labelErroQuantidade.setText(erros.get("quantidade"));
        }
    }

    
}
