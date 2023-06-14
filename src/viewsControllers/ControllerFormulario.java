package viewsControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entites.Produtos;
import model.exception.ValidaException;
import model.services.ProdutoService;
import utils.Alerts;
import utils.Constrains.Constraints;
import utils.Utils;

public class ControllerFormulario implements Initializable{

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtPreco;
	
	@FXML
	private TextField txtDecricao;
	
	@FXML
	private Label labelErrorId;
	@FXML
	private Label labelErroNome;
	@FXML
	private Label labelErroPreco;
	@FXML
	private Label labelErroDecricao;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	private Produtos entidade;
	
	private ProdutoService service;
	
	private List<DataChangeListener> listener = new ArrayList<>();

	public ProdutoService getService() {
		return service;
	}

	public void setService(ProdutoService service) {
		this.service = service;
	}
	
	public Produtos getProdutos() {
		return entidade;
	}

	public void setProdutos(Produtos entidade) {
		this.entidade = entidade;
	}
	
	public void sobrescreveDataChangeList(DataChangeListener data) {
		listener.add(data);
	}
	@FXML
	public void btnSalva(ActionEvent evento) {
		if(entidade == null) {
			throw new IllegalStateException("Entidade esta nula");
			
		}
		if(service == null ) {
			throw new IllegalStateException("Service esta nulo");
			
		}
		try {
			entidade = getDadosForm();
			service.salvaAtualizarForm(entidade);
			nofiticaListener();
			Utils.palcoAtual(evento).close();
		} 
		catch (ValidaException e) {
			SetaErros(e.pegaErros());
		}
		catch (DbException e) {
			Alerts.showAlerts("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}
	
		
	}
	
	
	private void nofiticaListener() {
		for (DataChangeListener data : listener) {
			data.onDataChanged();
			
		}
		
	}

	private Produtos getDadosForm() {
	
		Locale.setDefault(Locale.US);
		Produtos obj = new Produtos();
		ValidaException exception = new ValidaException("Erro no campo");
		
		obj.setId(Utils.converteParaInt(txtId.getText()));
		if(txtNome.getText()== null || txtNome.getText().trim().equals("")) {
			exception.adicionaErros("Nome","Campo não pode ser vazio" );
		}
		obj.setNome(txtNome.getText());
		
		if(txtPreco.getText() == null || txtPreco.getText().trim().equals("")) {
			exception.adicionaErros("Preco", "Campo não pode estar vazio");
		}
		
		obj.setPreco(Utils.converteParaDouble(txtPreco.getText()));
		
		if(txtDecricao.getText() == null) {
			exception.adicionaErros("Descricao", "Campo não pode estar vazio");
		}
		obj.setDescricao(txtDecricao.getText());
		if(exception.pegaErros().size()>0) {
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
		
	}
	private void iniciaNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldDouble(txtPreco);
		Constraints.setTextFieldMaxLength(txtDecricao, 60);
	}

	public void updateFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("entidade esta nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
		txtPreco.setText(Utils.converteParaString(entidade.getPreco()));
		txtDecricao.setText(entidade.getDescricao());
	}	
	
	private void SetaErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		if (campos.contains("Nome")){
			labelErroNome.setText(erros.get("Nome"));	
		}
		if (campos.contains("Preco")){
			labelErroPreco.setText(erros.get("Preco"));	
		}
		if (campos.contains("Descricao")){
			labelErroDecricao.setText(erros.get("Descricao"));	
		}
	}

}
