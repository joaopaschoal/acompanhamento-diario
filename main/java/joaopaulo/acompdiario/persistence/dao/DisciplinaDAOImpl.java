package joaopaulo.acompdiario.persistence.dao;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.CategoriaCientifica;
import joaopaulo.acompdiario.persistence.model.Disciplina;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DisciplinaDAOImpl extends SQLiteDAO<Disciplina> implements DisciplinaDAO {
	
	public static final String TBL_NAME = "disciplina";
	public static final String COL_ID = "id_disciplina";
	public static final String COL_NOME = "nome";
	public static final String COL_ID_CATEGORIA_CIENTIFICA = "id_categoria_cientifica";

	private static DisciplinaDAOImpl instance;
	
	public static DisciplinaDAO getInstance(Context context) {
		if (instance == null) {
			instance = new DisciplinaDAOImpl(context);
		}
		return instance;
	}
	
	private DisciplinaDAOImpl(Context context) {
		super(context);
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
		String[] columns = {COL_ID, COL_NOME, COL_ID_CATEGORIA_CIENTIFICA};
		return columns;
	}

	@Override
	public Disciplina fillByCursor(Cursor cursor, Disciplina model) {
		if (model == null) {
			model = new Disciplina();
		}
		model.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
		model.setNome(cursor.getString(cursor.getColumnIndex(COL_NOME)));
		
		CategoriaCientifica categoriaCientifica = new CategoriaCientifica();
		categoriaCientifica.setId(cursor.getInt(cursor.getColumnIndex(COL_ID_CATEGORIA_CIENTIFICA)));
		
		model.setCategoriaCientifica(categoriaCientifica);
		return model;
	}

	@Override
	public Disciplina insert(Disciplina model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_NOME, model.getNome());
		cv.put(COL_ID_CATEGORIA_CIENTIFICA, model.getCategoriaCientifica().getId());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public Disciplina update(Disciplina model) throws UpdateModelException {
		ContentValues cvWhr = new ContentValues();
		ContentValues cvUpd = new ContentValues();
		
		cvWhr.put(COL_ID, model.getId());
		cvUpd.put(COL_NOME, model.getNome());
		cvUpd.put(COL_ID_CATEGORIA_CIENTIFICA, model.getCategoriaCientifica().getId());
		
		UtilDAO utilDao = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDao.buildWhereClauseByCV(cvWhr), utilDao.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}

	
}
