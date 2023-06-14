package model.entites;

import java.io.Serializable;
import java.util.Objects;

public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;
    private String cliente_nome;
    private String produto_nome;
    private Integer id;
    private Integer quantidade;
    private Double valor_total;
    private Double valor_unitario;
    private Clientes clientes;
    private Produtos produtos;

    
   
	public Budget() {
		super();
	}


	public Budget(String cliente_nome, String produto_nome, Integer id, Integer quantidade, Double valor_total,
			Double valor_unitario, Clientes clientes, Produtos produtos) {
		super();
		this.cliente_nome = cliente_nome;
		this.produto_nome = produto_nome;
		this.id = id;
		this.quantidade = quantidade;
		this.valor_total = valor_total;
		this.valor_unitario = valor_unitario;
		this.clientes = clientes;
		this.produtos = produtos;
	}


	public String getCliente_nome() {
		return cliente_nome;
	}


	public void setCliente_nome(String cliente_nome) {
		this.cliente_nome = cliente_nome;
	}


	public String getProduto_nome() {
		return produto_nome;
	}


	public void setProduto_nome(String produto_nome) {
		this.produto_nome = produto_nome;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}


	public Double getValor_total() {
		return valor_total;
	}


	public void setValor_total(Double valor_total) {
		this.valor_total = valor_total;
	}


	public Double getValor_unitario() {
		return valor_unitario;
	}


	public void setValor_unitario(Double valor_unitario) {
		this.valor_unitario = valor_unitario;
	}


	public Clientes getClientes() {
		return clientes;
	}


	public void setClientes(Clientes clientes) {
		this.clientes = clientes;
	}


	public Produtos getProdutos() {
		return produtos;
	}


	public void setProdutos(Produtos produtos) {
		this.produtos = produtos;
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
		Budget other = (Budget) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public String toString() {
		return "Budget [cliente_nome=" + cliente_nome + ", produto_nome=" + produto_nome + ", id=" + id
				+ ", quantidade=" + quantidade + ", valor_total=" + valor_total + ", valor_unitario=" + valor_unitario
				+ ", clientes=" + clientes + ", produtos=" + produtos + "]";
	}

}