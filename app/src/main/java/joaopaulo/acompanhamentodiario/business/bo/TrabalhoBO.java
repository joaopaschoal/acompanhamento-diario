package joaopaulo.acompanhamentodiario.business.bo;


import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.TrabalhoDAO;
import joaopaulo.acompanhamentodiario.persistence.model.Trabalho;

public abstract class TrabalhoBO extends BO<Trabalho,TrabalhoDAO> {
    public abstract List<Trabalho> selectTrabalhosFromAcompanhamentoId(Integer id) throws QueryModelException;

    // ----- Specific CategoriaCientifica Methods ----- //
	
}
