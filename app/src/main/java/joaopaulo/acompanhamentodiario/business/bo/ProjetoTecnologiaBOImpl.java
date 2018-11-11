package joaopaulo.acompanhamentodiario.business.bo;

import android.content.Context;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.ProjetoTecnologiaDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.ProjetoTecnologiaDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.ProjetoTecnologia;

public class ProjetoTecnologiaBOImpl extends ProjetoTecnologiaBO {

	private static ProjetoTecnologiaBOImpl instance;
	
	
	private ProjetoTecnologiaBOImpl(ProjetoTecnologiaDAO dao) {
		this.dao = dao;
	}
	
	
	public static ProjetoTecnologiaBO getInstance(Context context) {
		if (instance == null) {
			instance = new ProjetoTecnologiaBOImpl(ProjetoTecnologiaDAOImpl.getInstance(context));
		}
		return instance;
	}
	
	@Override
	public void validate(ProjetoTecnologia model) throws ValidationException {
		if (model.getProjeto() == null || model.getProjeto().getId() == null || model.getProjeto().getId() <= 0) {
			throw new ValidationException("O Projeto não foi especificado");
		}

		if (model.getTecnologia() == null || model.getTecnologia().getId() == null || model.getTecnologia().getId() <= 0) {
			throw new ValidationException("A Tecnologia não foi especificada");
		}
	}

	@Override
	public List<ProjetoTecnologia> selectTecnologiasFromProjeto(Integer idProjeto) throws QueryModelException {
		return dao.selectProjetosTecnologiaFromProjetoId(idProjeto);
	}
}
