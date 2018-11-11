package joaopaulo.acompanhamentodiario.persistence.dao;

import java.util.List;

import joaopaulo.acompanhamentodiario.business.exception.InsertModelException;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.UpdateModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.exception.ModelNotPersistedException;
import joaopaulo.acompanhamentodiario.persistence.model.Model;
import android.content.ContentValues;
import android.database.Cursor;

public interface DAO<M extends Model> {
	void open();
	void close();
	
    //M save(M model) throws InsertModelException, UpdateModelException;
    M insert(M model) throws InsertModelException;
    M update(M model) throws UpdateModelException;
    void delete(M model) throws ModelNotPersistedException;
    List<M> selectAll() throws QueryModelException;
    List<M> select(ContentValues cv) throws QueryModelException;
    List<M> select(ContentValues cv, String sortField) throws QueryModelException;
    M selectOneById(Integer id) throws QueryModelException;
    int selectCountRows(ContentValues cv);
    M fillByCursor(Cursor cursor, M model);
    
    String getTableName();
	String getTableIdName();
	String getDefaultSortField();
	String[] getColumns();
}
