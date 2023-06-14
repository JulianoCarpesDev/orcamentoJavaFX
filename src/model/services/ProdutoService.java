package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProdutosDao;
import model.entites.Produtos;


public class ProdutoService {

	private ProdutosDao dao = DaoFactory.createProdutosDao();
	
	public List<Produtos> buscarTodos(){
		
		return dao.findAll();
		
	}
	
	public void salvaAtualizarForm(Produtos obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	public void remove(Produtos obj) {
		dao.deleteById(obj.getId());
	}
	
}
