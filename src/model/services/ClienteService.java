package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ClientesDao;
import model.entites.Clientes;


public class ClienteService {

	private ClientesDao dao = DaoFactory.createClientesDao();
	
	public List<Clientes> buscarTodos(){
		
		return dao.findAll();
		
	}
	
	public void salvaAtualizarForm(Clientes obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	public void remove(Clientes obj) {
		dao.deleteById(obj.getId());
	}
	
}
