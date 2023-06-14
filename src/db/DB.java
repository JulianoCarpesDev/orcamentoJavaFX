package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conn = null;

	public static Connection getConnection() {
		if (conn == null) {

			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
				if (conn != null) {
					System.out.println("Connection success!");
				}
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}

		return conn;
	}

	private static Properties loadProperties() {
		Properties props = new Properties();
		try (FileInputStream fs = new FileInputStream("db.properties")) {

			props.load(fs);

		} catch (IOException e) {
			System.out.println("Erro: " + e.getLocalizedMessage());
		}
		return props;

	}

	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getLocalizedMessage());
			}
		}
	}

	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getLocalizedMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getLocalizedMessage());
			}
		}

	}

	public static void prepareStament(PreparedStatement rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getLocalizedMessage());
			}
		}
	}

}
