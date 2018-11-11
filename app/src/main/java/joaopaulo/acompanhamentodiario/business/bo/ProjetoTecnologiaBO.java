package joaopaulo.acompanhamentodiario.business.bo;


import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.ProjetoTecnologiaDAO;
import joaopaulo.acompanhamentodiario.persistence.model.ProjetoTecnologia;

public abstract class ProjetoTecnologiaBO extends BO<ProjetoTecnologia,ProjetoTecnologiaDAO> {
    public abstract List<ProjetoTecnologia> selectTecnologiasFromProjeto(Integer id) throws QueryModelException;

    // ----- Specific CategoriaCientifica Methods ----- //
	
}
