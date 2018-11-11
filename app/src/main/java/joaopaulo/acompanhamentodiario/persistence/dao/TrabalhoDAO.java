package joaopaulo.acompanhamentodiario.persistence.dao;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.model.Trabalho;

public interface TrabalhoDAO extends DAO<Trabalho> {

    List<Trabalho> selectTrabalhosFromAcompanhamentoId(Integer idAcompanhamento) throws QueryModelException;
}
