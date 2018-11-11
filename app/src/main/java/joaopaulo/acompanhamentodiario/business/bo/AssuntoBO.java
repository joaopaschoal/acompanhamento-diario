package joaopaulo.acompanhamentodiario.business.bo;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.dao.AssuntoDAO;
import joaopaulo.acompanhamentodiario.persistence.model.Assunto;

public abstract class AssuntoBO extends BO<Assunto,AssuntoDAO> {

	// ----- Specific CategoriaCientifica Methods ----- //
	public abstract String obterNomeCompleto(Assunto assunto) throws QueryModelException, ValidationException;

    public abstract List<Assunto> selectByDisciplinaId(Integer idDisciplina) throws QueryModelException;

    public abstract List<Assunto>selectRootAssuntosByDisciplinaId(Integer idDisciplina) throws QueryModelException;

    public abstract List<Assunto> selectSubAssuntos(int idAssuntoPai) throws QueryModelException;
}
