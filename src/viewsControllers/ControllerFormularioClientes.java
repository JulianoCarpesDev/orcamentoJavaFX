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
import model.entites.Clientes;
import model.exception.ValidaException;
import model.services.ClienteService;
import utils.Alerts;
import utils.Constrains.Constraints;
import utils.Utils;

public class ControllerFormularioClientes implements Initializable{

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEndereco;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private Label labelErrorId;
	@FXML
	private Label labelErroNome;
	@FXML
	private Label labelErroEndereco;
	@FXML
	private Label labelErroTelefone;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	private Clientes entidade;
	
	private ClienteService service;
	
	private List<DataChangeListener> listener = new ArrayList<>();

	public ClienteService getService() {
		return service;
	}

	public void setService(ClienteService service) {
		this.service = service;
	}
	
	public Clientes getClientes() {
		return entidade;
	}

	public void setClientes(Clientes entidade) {
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

	private Clientes getDadosForm() {
	
		Locale.setDefault(Locale.US);
		Clientes obj = new Clientes();
		ValidaException exception = new ValidaException("Erro no campo");
		
		obj.setId(Utils.converteParaInt(txtId.getText()));
		if(txtNome.getText()== null || txtNome.getText().trim().equals("")) {
			exception.adicionaErros("Nome","Campo não pode ser vazio" );
		}
		obj.setNome(txtNome.getText());
		
		if(txtEndereco.getText() == null) {
			exception.adicionaErros("Endereco", "Campo não pode estar vazio");
		}
		obj.setEndereco(txtEndereco.getText());
		
		if(txtTelefone.getText() == null) {
			exception.adicionaErros("Telefone", "Campo não pode estar vazio");
		}
		obj.setTelefone(txtTelefone.getText());
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
		Constraints.setTextFieldMaxLength(txtEndereco,40);
		Constraints.setTextFieldMaxLength(txtTelefone, 12);
	}

	public void updateFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("entidade esta nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
		txtEndereco.setText(entidade.getEndereco());
		txtTelefone.setText(entidade.getTelefone());
	}	
	
	private void SetaErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		if (campos.contains("Nome")){
			labelErroNome.setText(erros.get("Nome"));
		}
		if (campos.contains("Endereco")){
			labelErroEndereco.setText(erros.get("Endereco"));
		}
		if (campos.contains("Telefone")){
			labelErroTelefone.setText(erros.get("Telefone"));
		}
		
	}

}
