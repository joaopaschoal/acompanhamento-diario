package joaopaulo.acompanhamentodiario.business.bo;

import android.content.Context;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.SaveModelException;
import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.EstudoDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.EstudoDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.Estudo;

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
    public List<Estudo> selectEstudosFromAcompanhamentoId(int idAcompanhamento) throws QueryModelException {
        return dao.selectEstudosByAcompanhamentoId(idAcompanhamento);
    }
}
