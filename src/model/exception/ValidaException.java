package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	Map<String, String> erros = new HashMap<>();
	
	public ValidaException(String msg) {
		super(msg);
	}
	
	public Map<String, String> pegaErros(){
		return erros;
	}
	public void adicionaErros(String campo, String mensagemErro) {
		erros.put(campo, mensagemErro);
	}

}
