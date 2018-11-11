package joaopaulo.acompanhamentodiario.persistence.dao;

import java.util.Date;

import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;

public interface AcompanhamentoDAO extends DAO<Acompanhamento> {
    Date selectDateNextAcompanhamento() throws QueryModelException;
}
