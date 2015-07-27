package joaopaulo.acompdiario.persistence.dao;

import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.Trabalho;

public interface TrabalhoDAO extends DAO<Trabalho> {

    List<Trabalho> selectTrabalhosFromAcompanhamentoId(Integer idAcompanhamento) throws QueryModelException;
}
