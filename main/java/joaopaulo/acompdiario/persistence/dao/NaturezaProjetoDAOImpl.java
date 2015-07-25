package joaopaulo.acompdiario.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.NaturezaProjeto;

public class NaturezaProjetoDAOImpl extends SQLiteDAO<NaturezaProjeto> implements NaturezaProjetoDAO {

	public static final String TBL_NAME = "natureza_projeto";
	public static final String COL_ID = "id_natureza_projeto";
	public static final String COL_DESCRICAO = "descricao";
	
	private static NaturezaProjetoDAOImpl instance;
	
	private NaturezaProjetoDAOImpl(Context context) {
		super(context);
	}
	
	public static NaturezaProjetoDAO getInstance(Context context) {
		if (instance == null) {
			instance = new NaturezaProjetoDAOImpl(context); 
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
		return COL_DESCRICAO;
	}

	@Override
	public String[] getColumns() {
		String[] columns = new String[] { COL_ID, COL_DESCRICAO };
		return columns;
	}

	@Override
	public NaturezaProjeto fillByCursor(Cursor cursor, NaturezaProjeto model) {
		if (model == null) {
			model = new NaturezaProjeto();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRICAO)));
		return model;
	}

	@Override
	public NaturezaProjeto insert(NaturezaProjeto model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_DESCRICAO, model.getDescricao());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public NaturezaProjeto update(NaturezaProjeto model) throws UpdateModelException {
		ContentValues cvUpd = new ContentValues();
		ContentValues cvWhr = new ContentValues();
		cvUpd.put(COL_DESCRICAO, model.getDescricao());
		cvWhr.put(COL_ID, model.getId());
		UtilDAO utilDAO = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDAO.buildWhereClauseByCV(cvWhr), utilDAO.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}
	
}