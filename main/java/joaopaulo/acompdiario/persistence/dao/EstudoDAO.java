package joaopaulo.acompdiario.persistence.dao;

import java.util.List;

import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.Estudo;

public interface EstudoDAO extends DAO<Estudo> {
    List<Estudo> selectEstudosByAcompanhamentoId(int idAcompanhamento) throws QueryModelException;
}
