package joaopaulo.acompdiario.business.bo;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.SaveModelException;
import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.AssuntoDAO;
import joaopaulo.acompdiario.persistence.dao.AssuntoDAOImpl;
import joaopaulo.acompdiario.persistence.model.Assunto;
import android.content.Context;

import java.util.List;

public class AssuntoBOImpl extends AssuntoBO {

	private static AssuntoBOImpl instance;
	
	
	private AssuntoBOImpl(AssuntoDAO dao) {
		this.dao = dao;
	}
	
	public static AssuntoBO getInstance(Context context) {
		if (instance == null) {
			instance = new AssuntoBOImpl(AssuntoDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(Assunto model) throws ValidationException {
		if (model.getNome().isEmpty()) {
			throw new ValidationException("O nome não pode ser vazio");
		}

		if (model.getNome().length() > 50) {
			throw new ValidationException("O comprimento do nome não pode ser maior do que 50 caracters");
		}

        if (model.getNomeAbreviado() != null && model.getNomeAbreviado().length() > 20) {
            throw new ValidationException("O comprimento do nome abreviado não pode ser maior do que 20 caracters");
        }
	}

    @Override
	public String obterNomeCompleto(Assunto assunto) throws QueryModelException, ValidationException {
		if (assunto == null) {
			throw new ValidationException("Um assunto nulo foi recebido para tentativa de gerar seu nome completo");
		}
		
		if (assunto.getAssuntoPai() == null || assunto.getAssuntoPai().getId() <= 0) {
			return assunto.getNome();
		}
		Assunto assuntoPai = dao.selectOneById(assunto.getAssuntoPai().getId());
		assunto.setAssuntoPai(assuntoPai);
		return obterNomeCompleto(assunto.getAssuntoPai()) + " - " + assunto.getNome();
	}

    @Override
    public List<Assunto> selectByDisciplinaId(Integer idDisciplina) throws QueryModelException {
        return this.dao.selectByDisciplinaId(idDisciplina);
    }

    @Override
    public List<Assunto> selectRootAssuntosByDisciplinaId(Integer idDisciplina) throws QueryModelException {
        return this.dao.selectByDisciplinaIdWhereIdAssuntoIsNull(idDisciplina);
    }

    @Override
    public List<Assunto> selectSubAssuntos(int idAssuntoPai) throws QueryModelException {
        return this.dao.selectByAssuntoPaiId(idAssuntoPai);
    }

    @Override
    public void beforeSave(Assunto model) {
        handleEmptyNomeAbreviado(model);
    }

    public void handleEmptyNomeAbreviado(Assunto model) {
        if (model.getNomeAbreviado() != null && model.getNomeAbreviado().trim() == "") {
            model.setNomeAbreviado(null);
        }
    }
}
