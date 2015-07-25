package joaopaulo.acompdiario.business.bo;

import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.CategoriaCientificaDAO;
import joaopaulo.acompdiario.persistence.dao.CategoriaCientificaDAOImpl;
import joaopaulo.acompdiario.persistence.model.CategoriaCientifica;
import android.content.Context;

public class CategoriaCientificaBOImpl extends CategoriaCientificaBO {

	private static CategoriaCientificaBOImpl instance;
	
	
	private CategoriaCientificaBOImpl(CategoriaCientificaDAO dao) {
		this.dao = dao;
	}
	
	
	public static CategoriaCientificaBO getInstance(Context context) {
		if (instance == null) {
			instance = new CategoriaCientificaBOImpl(CategoriaCientificaDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(CategoriaCientifica model) throws ValidationException {
		if (model.getNome().isEmpty()) {
			throw new ValidationException("O nome não pode ser vazio");
		}
		
		if (model.getNome().length() > 50) {
			throw new ValidationException("O comprimento do nome não pode maior do que 50 caracters");
		}
	}
	
}
