package joaopaulo.acompanhamentodiario.persistence.dao;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.model.Estudo;

public interface EstudoDAO extends DAO<Estudo> {
    List<Estudo> selectEstudosByAcompanhamentoId(int idAcompanhamento) throws QueryModelException;
}
