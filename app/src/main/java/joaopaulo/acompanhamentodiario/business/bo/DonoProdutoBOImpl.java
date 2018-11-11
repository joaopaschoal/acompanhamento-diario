package joaopaulo.acompanhamentodiario.business.bo;

import android.content.Context;

import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.DonoProdutoDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.DonoProdutoDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.DonoProduto;

public class DonoProdutoBOImpl extends DonoProdutoBO {

	public static final int NOME_MAX_LENGTH = 50;
	private static DonoProdutoBOImpl instance;
	
	
	private DonoProdutoBOImpl(DonoProdutoDAO dao) {
		this.dao = dao;
	}
	
	
	public static DonoProdutoBO getInstance(Context context) {
		if (instance == null) {
			instance = new DonoProdutoBOImpl(DonoProdutoDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(DonoProduto model) throws ValidationException {
		if (model.getNome().isEmpty()) {
			throw new ValidationException("O nome não pode ser vazio");
		}
		
		if (model.getNome().length() > NOME_MAX_LENGTH) {
			throw new ValidationException("O comprimento do nome não pode maior do que 50 caracters");
		}
	}
	
}
