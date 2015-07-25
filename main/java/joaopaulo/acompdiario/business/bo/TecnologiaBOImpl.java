package joaopaulo.acompdiario.business.bo;

import android.content.Context;

import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.TecnologiaDAO;
import joaopaulo.acompdiario.persistence.dao.TecnologiaDAOImpl;
import joaopaulo.acompdiario.persistence.model.Tecnologia;

public class TecnologiaBOImpl extends TecnologiaBO {

	public static final int NOME_MAX_LENGTH = 50;
	private static TecnologiaBOImpl instance;
	
	
	private TecnologiaBOImpl(TecnologiaDAO dao) {
		this.dao = dao;
	}
	
	
	public static TecnologiaBO getInstance(Context context) {
		if (instance == null) {
			instance = new TecnologiaBOImpl(TecnologiaDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(Tecnologia model) throws ValidationException {
		if (model.getNome().isEmpty()) {
			throw new ValidationException("O nome não pode ser vazio");
		}
		
		if (model.getNome().length() > NOME_MAX_LENGTH) {
			throw new ValidationException("O comprimento do nome não pode maior do que 50 caracters");
		}
	}
	
}
