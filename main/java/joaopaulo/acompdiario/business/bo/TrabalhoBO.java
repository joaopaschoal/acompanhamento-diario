package joaopaulo.acompdiario.business.bo;


import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.dao.TrabalhoDAO;
import joaopaulo.acompdiario.persistence.model.Trabalho;

public abstract class TrabalhoBO extends BO<Trabalho,TrabalhoDAO> {
    public abstract List<Trabalho> selectTrabalhosFromAcompanhamentoId(Integer id) throws QueryModelException;

    // ----- Specific CategoriaCientifica Methods ----- //
	
}
