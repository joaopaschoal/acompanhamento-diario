package joaopaulo.acompdiario.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.exception.ModelNotPersistedException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.Model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public abstract class SQLiteDAO<M extends Model> implements DAO<M> {

	protected SQLiteDatabase database;
	protected DatabaseHandler dbHandler;
	
	
	public SQLiteDAO(Context context) {
		dbHandler = DatabaseHandler.getInstance(context);
	}
	
	@Override
    public void open() {
    	database = dbHandler.getWritableDatabase();
    }
    
	@Override
    public void close() {
    	dbHandler.close();
    }
    
    @Override
    public abstract String getTableName();
    
    @Override
    public abstract String getTableIdName();
    
    @Override
    public abstract String getDefaultSortField();
    
    @Override
    public abstract String[] getColumns();
    
    @Override
    public abstract M fillByCursor(Cursor cursor, M model);

	@Override
	public abstract M insert(M model) throws InsertModelException;


	@Override
	public abstract M update(M model) throws UpdateModelException;
    
    @Override
    public void delete(M model) throws ModelNotPersistedException {
    	if (model == null || model.getId() == 0) {
    		throw new ModelNotPersistedException("O objeto está vazio ou não foi persistido");
    	}
    	database.delete(getTableName(), getTableIdName() + " = ?", new String[] { String.valueOf(model.getId()) });
    }

    @Override
    public List<M> selectAll() throws QueryModelException {
    	return select(null);
    }
    
    @Override
    public List<M> select(ContentValues cv) throws QueryModelException {
    	return select(cv, getDefaultSortField());
    }
    
    @Override
    public List<M> select(ContentValues cv, String sortField) throws QueryModelException {
    	UtilDAO utilDAO = new UtilDAOImpl();
    	List<M> resultList = new ArrayList<M>();
    	Cursor cursor = null;
    	
    	try {
    		cursor = database.query(getTableName(), getColumns(), utilDAO.buildWhereClauseByCV(cv), utilDAO.buildWhereParamsByCV(cv), null, null, sortField);
    	} catch(SQLiteException ex) {
    		throw new QueryModelException(ex);
    	}
    	cursor.moveToFirst();
    	while (!cursor.isAfterLast()) {
    		M model = fillByCursor(cursor, null);
    		resultList.add(model);
    		cursor.moveToNext();
    	}
    	
    	return resultList;
    }
    
    @Override
    public M selectOneById(Integer id) throws QueryModelException {
    	ContentValues cv = new ContentValues();
    	cv.put(getTableIdName(), id);
    	List<M> resultList = select(cv);
    	return resultList.size() > 0 ? resultList.get(0) : null;
    }
    
    @Override
    public int selectCountRows(ContentValues cv) {
    	UtilDAO utilDAO = new UtilDAOImpl();
    	Cursor cursor = database.rawQuery("select count(*) from "+getTableName()+" "+utilDAO.buildWhereClauseByCV(cv), null);
    	return cursor.getInt(0);
    }
    
    
}
