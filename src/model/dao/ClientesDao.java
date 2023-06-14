package model.dao;

import java.util.List;

import model.entites.Clientes;
import model.entites.Produtos;


public interface ClientesDao {

	void insert(Clientes obj);
	void update(Clientes obj);
	void deleteById(Integer id);
	Clientes findById(Integer id);
	List<Clientes> findAll();
	List<Clientes> findByClientes(Clientes cliente);

		
	
}
