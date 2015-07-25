package joaopaulo.acompdiario.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.Tecnologia;

public class TecnologiaDAOImpl extends SQLiteDAO<Tecnologia> implements TecnologiaDAO {

	public static final String TBL_NAME = "tecnologia";
	public static final String COL_ID = "id_tecnologia";
	public static final String COL_NOME = "nome";
	
	private static TecnologiaDAOImpl instance;
	
	private TecnologiaDAOImpl(Context context) {
		super(context);
	}
	
	public static TecnologiaDAO getInstance(Context context) {
		if (instance == null) {
			instance = new TecnologiaDAOImpl(context); 
		}
		return instance;
	}
	

	@Override
	public String getTableName() {
		return TBL_NAME;
	}

	@Override
	public String getTableIdName() {
		return COL_ID;
	}

	@Override
	public String getDefaultSortField() {
		return COL_NOME;
	}

	@Override
	public String[] getColumns() {
		String[] columns = new String[] { COL_ID, COL_NOME };
		return columns;
	}

	@Override
	public Tecnologia fillByCursor(Cursor cursor, Tecnologia model) {
		if (model == null) {
			model = new Tecnologia();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setNome(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)));
		return model;
	}

	@Override
	public Tecnologia insert(Tecnologia model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_NOME, model.getNome());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public Tecnologia update(Tecnologia model) throws UpdateModelException {
		ContentValues cvUpd = new ContentValues();
		ContentValues cvWhr = new ContentValues();
		cvUpd.put(COL_NOME, model.getNome());
		cvWhr.put(COL_ID, model.getId());
		UtilDAO utilDAO = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDAO.buildWhereClauseByCV(cvWhr), utilDAO.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}
	
}