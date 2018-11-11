package joaopaulo.acompanhamentodiario.persistence.dao;

import joaopaulo.acompanhamentodiario.business.exception.InsertModelException;
import joaopaulo.acompanhamentodiario.business.exception.UpdateModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.CategoriaCientifica;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CategoriaCientificaDAOImpl extends SQLiteDAO<CategoriaCientifica> implements CategoriaCientificaDAO {

	public static final String TBL_NAME = "categoria_cientifica";
	public static final String COL_ID = "id_categoria_cientifica";
	public static final String COL_NOME = "nome";
	
	private static CategoriaCientificaDAOImpl instance;
	
	private CategoriaCientificaDAOImpl(Context context) {
		super(context);
	}
	
	public static CategoriaCientificaDAO getInstance(Context context) {
		if (instance == null) {
			instance = new CategoriaCientificaDAOImpl(context); 
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
		return new String[] { COL_ID, COL_NOME };
	}

	@Override
	public CategoriaCientifica fillByCursor(Cursor cursor, CategoriaCientifica model) {
		if (model == null) {
			model = new CategoriaCientifica();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setNome(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)));
		return model;
	}

	@Override
	public CategoriaCientifica insert(CategoriaCientifica model) throws InsertModelException {
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
	public CategoriaCientifica update(CategoriaCientifica model) throws UpdateModelException {
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