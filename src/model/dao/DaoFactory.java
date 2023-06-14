package model.dao;

import db.DB;
import model.dao.impl.BudgetDaoJDBC;
import model.dao.impl.ClientesDaoJDBC;
import model.dao.impl.ProdutosDaoJDBC;

public class DaoFactory {

	public static ClientesDao createClientesDao() {
		return new ClientesDaoJDBC(DB.getConnection());
	}
	
	public static ProdutosDao createProdutosDao() {
		return new ProdutosDaoJDBC(DB.getConnection());
	}
	public static BudgetDao createBudgetDao() {
		return new BudgetDaoJDBC(DB.getConnection());
	}
}
