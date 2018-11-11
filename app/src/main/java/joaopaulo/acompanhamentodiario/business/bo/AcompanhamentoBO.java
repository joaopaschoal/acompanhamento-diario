package joaopaulo.acompanhamentodiario.business.bo;

import java.util.Date;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.AcompanhamentoDAO;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;

public abstract class AcompanhamentoBO extends BO<Acompanhamento,AcompanhamentoDAO> {

    // ----- Specific CategoriaCientifica Methods ----- //

    public abstract Date selectNextEmptyDate() throws QueryModelException;
	
}
