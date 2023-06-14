package viewsControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.BudgetService;
import model.services.ClienteService;
import model.services.ProdutoService;
import utils.Alerts;

public class MainController implements Initializable{
	@FXML
	private MenuItem MenuOrcamento;
	
	@FXML
	private MenuItem cadastroProduto; 
	
	@FXML
	private MenuItem cadastroCliente; 
	
	@FXML
	private MenuItem MenuAbout;

	
	public void aboutAction() {
		carregaViewAbout("/viewsControllers/Ajuda.fxml", x-> {});
		
	}
	public void cadastroProdutos() {
		carregaViewAbout("/viewsControllers/ListaDeProdutos.fxml",(ControllerListaProdutos controle)->{
			controle.setProdutosService(new ProdutoService());
			controle.atualizaTableView();
		});
	}
	public void cadastroOrcamento() {
		carregaViewAbout("/viewsControllers/BudgetList.fxml",(ControllerListaBudget controle)->{
			controle.setBudgetService(new BudgetService());
			controle.atualizaTableView();
		});
	}
	public void cadastroClientes() {
		carregaViewAbout("/viewsControllers/ListaDeClientes.fxml",(ControllerListaClientes controle)->{
			controle.setClientesService(new ClienteService());
			controle.atualizaTableView();
		});
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	private synchronized <T>void carregaViewAbout(String nomeTela, Consumer<T> iniciaAcao) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeTela));
	
		try {
			VBox novoVbox = loader.load();
			
			Scene novaCena = Main.getMainScene();
			
			VBox cenaPrincipal = (VBox)((ScrollPane) novaCena. getRoot()).getContent();
			
			Node menuTelaPrincipal = cenaPrincipal.getChildren().get(0);
			cenaPrincipal.getChildren().clear();
			cenaPrincipal.getChildren().add(menuTelaPrincipal);
			cenaPrincipal.getChildren().addAll(novoVbox.getChildren());
			
			T controle = loader.getController();
			iniciaAcao.accept(controle);
			
			
		} catch (IOException e) {
			Alerts.showAlerts("Erro", "Tela About", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
		
	}

}
