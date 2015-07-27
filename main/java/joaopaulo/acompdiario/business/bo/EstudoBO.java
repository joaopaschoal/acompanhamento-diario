package joaopaulo.acompdiario.business.bo;

import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.dao.DisciplinaDAO;
import joaopaulo.acompdiario.persistence.dao.EstudoDAO;
import joaopaulo.acompdiario.persistence.model.Disciplina;
import joaopaulo.acompdiario.persistence.model.Estudo;

public abstract class EstudoBO extends BO<Estudo,EstudoDAO> {
    public abstract List<Estudo> selectEstudosFromAcompanhamentoId(int idAcompanhamento) throws QueryModelException;

    // ----- Specific CategoriaCientifica Methods ----- //
	
}
