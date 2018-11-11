package joaopaulo.acompanhamentodiario.business.bo;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.AcompanhamentoDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.AcompanhamentoDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;
import android.content.Context;

import java.util.Date;

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

	@Override
	public Date selectNextEmptyDate() throws QueryModelException {
		return dao.selectDateNextAcompanhamento();
	}
}
