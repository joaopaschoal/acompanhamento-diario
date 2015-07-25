package joaopaulo.acompdiario.business.bo;

import android.content.Context;

import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.SaveModelException;
import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.EstudoDAO;
import joaopaulo.acompdiario.persistence.dao.EstudoDAOImpl;
import joaopaulo.acompdiario.persistence.model.Estudo;

public class EstudoBOImpl extends EstudoBO {

	private static EstudoBOImpl instance;
	
	
	private EstudoBOImpl(EstudoDAO dao) {
		this.dao = dao;
	}
	
	
	public static EstudoBO getInstance(Context context) {
		if (instance == null) {
			instance = new EstudoBOImpl(EstudoDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(Estudo model) throws ValidationException {
		if (model.getTempoMins() < 0) {
			throw new ValidationException("O tempo de estudo não pode ser nulo nem negativo");
		}
		
		if (model.getAssunto() == null || model.getAssunto().getId() == null || model.getAssunto().getId() <= 0) {
			throw new ValidationException("O Assunto é obrigatório");
		}
	}

    @Override
    public Estudo save(Estudo model) throws SaveModelException {
        adjustments(model);
        return super.save(model);
    }

    private void adjustments(Estudo model) {
		if (model.getAssunto() == null || model.getAssunto().getId() == null || model.getAssunto().getId() <= 0) {
			model.getAssunto().setId(null);
		}
	}

    @Override
    public List<Estudo> selectEstudosFromAcompanhamento(int idAcompanhamento) throws QueryModelException {
        return dao.selectEstudosByAcompanhamentoId(idAcompanhamento);
    }
}
