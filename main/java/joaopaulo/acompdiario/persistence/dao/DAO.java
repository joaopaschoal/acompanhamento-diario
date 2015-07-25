package joaopaulo.acompdiario.persistence.dao;

import java.util.List;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.exception.ModelNotPersistedException;
import joaopaulo.acompdiario.persistence.model.Model;
import android.content.ContentValues;
import android.database.Cursor;

public interface DAO<M extends Model> {
	void open();
	void close();
	
    //M save(M model) throws InsertModelException, UpdateModelException;
    abstract M insert(M model) throws InsertModelException;
    abstract M update(M model) throws UpdateModelException;
    void delete(M model) throws ModelNotPersistedException;
    List<M> selectAll() throws QueryModelException;
    List<M> select(ContentValues cv) throws QueryModelException;
    List<M> select(ContentValues cv, String sortField) throws QueryModelException;
    M selectOneById(Integer id) throws QueryModelException;
    int selectCountRows(ContentValues cv);
    M fillByCursor(Cursor cursor, M model);
    
    abstract String getTableName();
	abstract String getTableIdName();
	abstract String getDefaultSortField();
	abstract String[] getColumns();
}
