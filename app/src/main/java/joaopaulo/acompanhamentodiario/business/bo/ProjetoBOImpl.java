package joaopaulo.acompanhamentodiario.business.bo;

import android.content.Context;

import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.ProjetoDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.ProjetoDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.Projeto;

public class ProjetoBOImpl extends ProjetoBO {

	public static final int NOME_MAX_LENGTH = 50;
	private static ProjetoBOImpl instance;
	
	
	private ProjetoBOImpl(ProjetoDAO dao) {
		this.dao = dao;
	}
	
	
	public static ProjetoBO getInstance(Context context) {
		if (instance == null) {
			instance = new ProjetoBOImpl(ProjetoDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(Projeto model) throws ValidationException {
		if (model.getNome().isEmpty()) {
			throw new ValidationException("O nome não pode ser vazio");
		}
		
		if (model.getNome().length() > NOME_MAX_LENGTH) {
			throw new ValidationException("O comprimento do nome não pode maior do que 50 caracters");
		}

		if (model.getDataCriacao() == null) {
			throw new ValidationException("A data de criação não pode ser nula");
		}

		if (model.getNaturezaProjeto() == null || model.getNaturezaProjeto().getId() == null || model.getNaturezaProjeto().getId() <= 0) {
			throw new ValidationException("A natureza não foi especificada");
		}

		if (model.getDonoProduto() == null || model.getDonoProduto().getId() == null || model.getDonoProduto().getId() <= 0) {
			throw new ValidationException("O dono do produto não foi especificado");
		}

	}
	
}
