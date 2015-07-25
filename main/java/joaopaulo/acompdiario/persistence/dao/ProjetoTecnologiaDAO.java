package joaopaulo.acompdiario.persistence.dao;

import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.ProjetoTecnologia;

public interface ProjetoTecnologiaDAO extends DAO<ProjetoTecnologia> {

    List<ProjetoTecnologia> selectProjetosTecnologiaFromProjetoId(Integer idProjeto) throws QueryModelException;

}
