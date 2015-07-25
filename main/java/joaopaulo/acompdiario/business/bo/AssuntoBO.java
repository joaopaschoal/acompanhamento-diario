package joaopaulo.acompdiario.business.bo;

import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.AssuntoDAO;
import joaopaulo.acompdiario.persistence.model.Assunto;

public abstract class AssuntoBO extends BO<Assunto,AssuntoDAO> {

	// ----- Specific CategoriaCientifica Methods ----- //
	public abstract String obterNomeCompleto(Assunto assunto) throws QueryModelException, ValidationException;

    public abstract List<Assunto> selectByDisciplinaId(Integer idDisciplina) throws QueryModelException;

    public abstract List<Assunto>selectRootAssuntosByDisciplinaId(Integer idDisciplina) throws QueryModelException;

    public abstract List<Assunto> selectSubAssuntos(int idAssuntoPai) throws QueryModelException;
}
