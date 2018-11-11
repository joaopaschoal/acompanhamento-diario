package joaopaulo.acompanhamentodiario.persistence.dao;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.model.Assunto;

public interface AssuntoDAO extends DAO<Assunto> {

    List<Assunto> selectByAssuntoPaiId(int idAssunto) throws QueryModelException;

    List<Assunto> selectByDisciplinaId(Integer idDisciplina) throws QueryModelException;

    List<Assunto> selectByDisciplinaIdWhereIdAssuntoIsNull(Integer idDisciplina) throws QueryModelException;

}
