package model.entites;

import java.io.Serializable;
import java.util.Objects;

public class Produtos implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nome;
	private Double preco;
	private String descricao;
	
	
	public Produtos() {
		
	}


	public Produtos(Integer id, String nome, Double preco, String descricao) {
		super();
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.descricao = descricao;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Double getPreco() {
		return preco;
	}


	public void setPreco(Double preco) {
		this.preco = preco;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produtos other = (Produtos) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public String toString() {
		return "Produtos [id=" + id + ", nome=" + nome + ", preco=" + preco + ", descricao=" + descricao + "]";
	}


	

}
