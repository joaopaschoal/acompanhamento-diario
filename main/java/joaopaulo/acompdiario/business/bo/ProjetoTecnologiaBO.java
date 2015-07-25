package joaopaulo.acompdiario.business.bo;


import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.dao.ProjetoTecnologiaDAO;
import joaopaulo.acompdiario.persistence.model.ProjetoTecnologia;

public abstract class ProjetoTecnologiaBO extends BO<ProjetoTecnologia,ProjetoTecnologiaDAO> {
    public abstract List<ProjetoTecnologia> selectTecnologiasFromProjeto(Integer id) throws QueryModelException;

    // ----- Specific CategoriaCientifica Methods ----- //
	
}
