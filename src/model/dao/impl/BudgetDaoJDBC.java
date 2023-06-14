package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.BudgetDao;
import model.entites.Budget;

public class BudgetDaoJDBC implements BudgetDao {

	private Connection conn;
	
	public BudgetDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Budget obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO Orcamento "
					+ "(cliente_nome,produto_nome,quantidade,valor_unitario,valor_total) "
					+ "VALUES "
					+ "(?,?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getClientes().getNome());
			st.setString(2, obj.getProdutos().getNome());
			st.setInt(3, obj.getQuantidade());
			st.setDouble(4, obj.getProdutos().getPreco()+ (obj.getProdutos().getPreco() * 0.60));
			st.setDouble(5, obj.getProdutos().getPreco() * obj.getQuantidade() + (obj.getProdutos().getPreco() * obj.getQuantidade() * 0.60));
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Budget obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE Orcamento "
					+ "SET cliente_nome = ?, produto_nome = ?, quantidade = ?, valor_unitario = ?, valor_total = ? "
					+ "WHERE ID = ?");
			

			st.setString(1, obj.getClientes().getNome());
			st.setString(2, obj.getProdutos().getNome());
			st.setInt(3, obj.getQuantidade());
			st.setDouble(4, obj.getProdutos().getPreco() + (obj.getProdutos().getPreco() * 0.60));
			st.setDouble(5, obj.getProdutos().getPreco() * obj.getQuantidade() + (obj.getProdutos().getPreco() * obj.getQuantidade() * 0.60));
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM Orcamento WHERE ID = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	@Override
	public Budget findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM Orcamento WHERE ID = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Budget budget = instantiateBudget(rs);
				return budget;
			}
			
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Budget instantiateBudget(ResultSet rs) throws SQLException {
	    Budget obj = new Budget();
	    obj.setId(rs.getInt("id"));
	    obj.setCliente_nome(rs.getString("cliente_nome"));
	    obj.setProduto_nome(rs.getString("produto_nome"));
	    obj.setQuantidade(rs.getInt("quantidade"));
	    obj.setValor_unitario(rs.getDouble("valor_unitario"));
	    obj.setValor_total(rs.getDouble("valor_total"));
	  
	    return obj;
	}


	@Override
	public List<Budget> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM Orcamento ORDER BY ID");
			
			rs = st.executeQuery();
			
			List<Budget> list = new ArrayList<>();
			
			while (rs.next()) {
				Budget obj = instantiateBudget(rs);
				list.add(obj);
			}
			
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Budget> findByBudget(Budget budget) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM Orcamento WHERE ID = ? ORDER BY ID");
			
			st.setInt(1, budget.getId());
			
			rs = st.executeQuery();
			
			List<Budget> list = new ArrayList<>();
			
			while (rs.next()) {
				Budget obj = instantiateBudget(rs);
				list.add(obj);
			}
			
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}


}
