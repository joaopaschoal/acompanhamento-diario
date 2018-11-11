package joaopaulo.acompanhamentodiario.business.bo;

import android.content.Context;

import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.NaturezaProjetoDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.NaturezaProjetoDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.NaturezaProjeto;

public class NaturezaProjetoBOImpl extends NaturezaProjetoBO {

	public static final int DESCRICAO_MAX_LENGTH = 50;
	private static NaturezaProjetoBOImpl instance;
	
	
	private NaturezaProjetoBOImpl(NaturezaProjetoDAO dao) {
		this.dao = dao;
	}
	
	
	public static NaturezaProjetoBO getInstance(Context context) {
		if (instance == null) {
			instance = new NaturezaProjetoBOImpl(NaturezaProjetoDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(NaturezaProjeto model) throws ValidationException {
		if (model.getDescricao().isEmpty()) {
			throw new ValidationException("O nome não pode ser vazio");
		}
		
		if (model.getDescricao().length() > DESCRICAO_MAX_LENGTH) {
			throw new ValidationException("O comprimento do nome não pode maior do que 50 caracters");
		}
	}
	
}
