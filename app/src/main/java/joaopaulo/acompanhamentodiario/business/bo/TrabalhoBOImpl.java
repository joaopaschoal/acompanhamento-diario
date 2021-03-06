package joaopaulo.acompanhamentodiario.business.bo;

import android.content.Context;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.TrabalhoDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.TrabalhoDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.Trabalho;

public class TrabalhoBOImpl extends TrabalhoBO {

	private static TrabalhoBOImpl instance;
	
	
	private TrabalhoBOImpl(TrabalhoDAO dao) {
		this.dao = dao;
	}
	
	
	public static TrabalhoBO getInstance(Context context) {
		if (instance == null) {
			instance = new TrabalhoBOImpl(TrabalhoDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(Trabalho model) throws ValidationException {
		if (model.getTempoMins() < 0) {
			throw new ValidationException("O tempo não pode ser negativo");
		}

		if (model.getProjeto() == null || model.getProjeto().getId() == null || model.getProjeto().getId() <= 0) {
			throw new ValidationException("O Projeto não foi especificado");
		}

		if (model.getAcompanhamento() == null || model.getAcompanhamento().getId() == null || model.getAcompanhamento().getId() <= 0) {
			throw new ValidationException("O Acompanhamento não foi especificado");
		}
	}

	@Override
	public List<Trabalho> selectTrabalhosFromAcompanhamentoId(Integer idAcompanhamento) throws QueryModelException {
        return dao.selectTrabalhosFromAcompanhamentoId(idAcompanhamento);
	}
}
