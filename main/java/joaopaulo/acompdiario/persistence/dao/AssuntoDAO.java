package joaopaulo.acompdiario.persistence.dao;

import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.Assunto;

public interface AssuntoDAO extends DAO<Assunto> {

    public List<Assunto> selectByAssuntoPaiId(int idAssunto) throws QueryModelException;

    public List<Assunto> selectByDisciplinaId(Integer idDisciplina) throws QueryModelException;

    public List<Assunto> selectByDisciplinaIdWhereIdAssuntoIsNull(Integer idDisciplina) throws QueryModelException;

}
