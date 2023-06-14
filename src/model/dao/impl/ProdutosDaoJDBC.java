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
import db.DbIntegrityExeception;
import model.dao.ProdutosDao;
import model.entites.Produtos;


public class ProdutosDaoJDBC implements ProdutosDao {

	private Connection conn;
	
	public ProdutosDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Produtos findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Produtos WHERE ID = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Produtos obj = new Produtos();
				obj.setId(rs.getInt("ID"));
				obj.setNome(rs.getString("Nome"));
				obj.setPreco(rs.getDouble("Preco"));
				obj.setDescricao(rs.getString("Descricao"));
				return obj;
			}
			return null;
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
	public List<Produtos> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Produtos ORDER BY Id");
			rs = st.executeQuery();

			List<Produtos> list = new ArrayList<>();

			while (rs.next()) {
				Produtos obj = new Produtos();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setPreco(rs.getDouble("Preco"));
				obj.setDescricao(rs.getString("Descricao"));
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
	public void insert(Produtos obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO Produtos " +
				"(Nome, Preco,Descricao) " +
				"VALUES " +
				"(?,?,?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setDouble(2, obj.getPreco());
			st.setString(3, obj.getDescricao());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
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
	public void update(Produtos obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE Produtos " +
				"SET Nome = ?  , Preco = ?,Descricao = ? " +
				"WHERE Id = ?");

			st.setString(1, obj.getNome());
			st.setDouble(2, obj.getPreco());
			st.setString(3, obj.getDescricao());
			st.setInt(4, obj.getId());
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
			st = conn.prepareStatement(
				"DELETE FROM Produtos WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityExeception(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	

}
