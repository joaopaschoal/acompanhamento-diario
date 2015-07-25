package joaopaulo.acompdiario.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.Trabalho;

public class TrabalhoDAOImpl extends SQLiteDAO<Trabalho> implements TrabalhoDAO {

	public static final String TBL_NAME = "trabalho";
	public static final String COL_ID = "id_trabalho";
	public static final String COL_TEMPO_MINS = "tempo_mins";
	public static final String COL_ID_PROJETO = "id_projeto";
	public static final String COL_ID_ACOMPANHAMENTO = "id_acompanhamento";
	
	private static TrabalhoDAOImpl instance;
	
	private TrabalhoDAOImpl(Context context) {
		super(context);
	}
	
	public static TrabalhoDAO getInstance(Context context) {
		if (instance == null) {
			instance = new TrabalhoDAOImpl(context); 
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
		return COL_ID;
	}

	@Override
	public String[] getColumns() {
		String[] columns = new String[] { COL_ID, COL_TEMPO_MINS, COL_ID_PROJETO, COL_ID_ACOMPANHAMENTO };
		return columns;
	}

	@Override
	public Trabalho fillByCursor(Cursor cursor, Trabalho model) {
		if (model == null) {
			model = new Trabalho();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setTempoMins(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TEMPO_MINS)));
        model.getProjeto().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_PROJETO)));
        model.getAcompanhamento().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_ACOMPANHAMENTO)));
		return model;
	}

	@Override
	public Trabalho insert(Trabalho model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_TEMPO_MINS, model.getTempoMins());
        cv.put(COL_ID_PROJETO, model.getProjeto().getId());
        cv.put(COL_ID_ACOMPANHAMENTO, model.getAcompanhamento().getId());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public Trabalho update(Trabalho model) throws UpdateModelException {
		ContentValues cvUpd = new ContentValues();
		ContentValues cvWhr = new ContentValues();
        cvUpd.put(COL_TEMPO_MINS, model.getTempoMins());
        cvUpd.put(COL_ID_PROJETO, model.getProjeto().getId());
        cvUpd.put(COL_ID_ACOMPANHAMENTO, model.getAcompanhamento().getId());
		cvWhr.put(COL_ID, model.getId());
		UtilDAO utilDAO = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDAO.buildWhereClauseByCV(cvWhr), utilDAO.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}
	
}