package joaopaulo.acompanhamentodiario.business.bo;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.DisciplinaDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.EstudoDAO;
import joaopaulo.acompanhamentodiario.persistence.model.Disciplina;
import joaopaulo.acompanhamentodiario.persistence.model.Estudo;

public abstract class EstudoBO extends BO<Estudo,EstudoDAO> {
    public abstract List<Estudo> selectEstudosFromAcompanhamentoId(int idAcompanhamento) throws QueryModelException;

    // ----- Specific CategoriaCientifica Methods ----- //
	
}
