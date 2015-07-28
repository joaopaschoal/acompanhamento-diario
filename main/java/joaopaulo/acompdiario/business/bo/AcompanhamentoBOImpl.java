package joaopaulo.acompdiario.business.bo;

import joaopaulo.acompdiario.business.exception.SaveModelException;
import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.AcompanhamentoDAO;
import joaopaulo.acompdiario.persistence.dao.AcompanhamentoDAOImpl;
import joaopaulo.acompdiario.persistence.model.Acompanhamento;
import android.content.Context;

public class AcompanhamentoBOImpl extends AcompanhamentoBO {

	private static AcompanhamentoBOImpl instance;
	
	
	private AcompanhamentoBOImpl(AcompanhamentoDAO dao) {
		this.dao = dao;
	}
	
	
	public static AcompanhamentoBOImpl getInstance(Context context) {
		if (instance == null) {
			instance = new AcompanhamentoBOImpl(AcompanhamentoDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(Acompanhamento model) throws ValidationException {
		if (model.getDataRegistro() == null) {
			throw new ValidationException("A data de registro do acompanhamento é obrigatória");
		}
		if (model.getDataAcompanhamento() == null) {
			throw new ValidationException("A data do acompanhamento é obrigatória");
		}
	}

}
