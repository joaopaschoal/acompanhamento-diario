package joaopaulo.acompanhamentodiario.persistence.dao;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.model.ProjetoTecnologia;

public interface ProjetoTecnologiaDAO extends DAO<ProjetoTecnologia> {

    List<ProjetoTecnologia> selectProjetosTecnologiaFromProjetoId(Integer idProjeto) throws QueryModelException;

}
