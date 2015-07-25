package joaopaulo.acompdiario.business.bo;

import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.DisciplinaDAO;
import joaopaulo.acompdiario.persistence.dao.DisciplinaDAOImpl;
import joaopaulo.acompdiario.persistence.model.Disciplina;
import android.content.Context;

public class DisciplinaBOImpl extends DisciplinaBO {

	private static DisciplinaBOImpl instance;
	
	
	private DisciplinaBOImpl(DisciplinaDAO dao) {
		this.dao = dao;
	}
	
	
	public static DisciplinaBO getInstance(Context context) {
		if (instance == null) {
			instance = new DisciplinaBOImpl(DisciplinaDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(Disciplina model) throws ValidationException {
		if (model.getNome().isEmpty()) {
			throw new ValidationException("O nome não pode ser vazio");
		}
		
		if (model.getNome().length() > 50) {
			throw new ValidationException("O comprimento do nome não pode maior do que 50 caracters");
		}
		
		if (model.getCategoriaCientifica() == null || model.getCategoriaCientifica().getId() == null || model.getCategoriaCientifica().getId() <= 0) {
			throw new ValidationException("A Categoria Científica da disciplina é obrigatória");
		}
	}
	
}
