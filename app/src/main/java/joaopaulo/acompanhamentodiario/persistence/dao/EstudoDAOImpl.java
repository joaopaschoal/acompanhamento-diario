package joaopaulo.acompanhamentodiario.persistence.dao;

import joaopaulo.acompanhamentodiario.business.exception.InsertModelException;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.UpdateModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;
import joaopaulo.acompanhamentodiario.persistence.model.Assunto;
import joaopaulo.acompanhamentodiario.persistence.model.Estudo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

public class EstudoDAOImpl extends SQLiteDAO<Estudo> implements EstudoDAO {
	
	public static final String TBL_NAME = "estudo";
	public static final String COL_ID = "id_estudo";
	public static final String COL_TEMPO_MINS = "tempo_mins";
	public static final String COL_ID_ASSUNTO = "id_assunto";
	public static final String COL_ID_ACOMPANHAMENTO = "id_acompanhamento";

	private static EstudoDAOImpl instance;
	
	public static EstudoDAO getInstance(Context context) {
		if (instance == null) {
			instance = new EstudoDAOImpl(context);
		}
		return instance;
	}
	
	private EstudoDAOImpl(Context context) {
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
		return COL_ID;
	}

	@Override
	public String[] getColumns() {
		String[] columns = {COL_ID, COL_TEMPO_MINS, COL_ID_ASSUNTO, COL_ID_ACOMPANHAMENTO};
		return columns;
	}

	@Override
	public Estudo fillByCursor(Cursor cursor, Estudo model) {
		if (model == null) {
			model = new Estudo();
		}
		model.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
		model.setTempoMins(cursor.getInt(cursor.getColumnIndex(COL_TEMPO_MINS)));
		
		Assunto assunto = new Assunto();
		assunto.setId(cursor.getInt(cursor.getColumnIndex(COL_ID_ASSUNTO)));
		
		Acompanhamento acompanhamento = new Acompanhamento();
		acompanhamento.setId(cursor.getInt(cursor.getColumnIndex(COL_ID_ACOMPANHAMENTO)));
		
		model.setAssunto(assunto);
		model.setAcompanhamento(acompanhamento);
		return model;
	}

	@Override
	public Estudo insert(Estudo model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_TEMPO_MINS, model.getTempoMins());
		cv.put(COL_ID_ASSUNTO, model.getAssunto().getId());
		cv.put(COL_ID_ACOMPANHAMENTO, model.getAcompanhamento().getId());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public Estudo update(Estudo model) throws UpdateModelException {
		ContentValues cvWhr = new ContentValues();
		ContentValues cvUpd = new ContentValues();
		
		cvWhr.put(COL_ID, model.getId());
		cvUpd.put(COL_TEMPO_MINS, model.getTempoMins());
		cvUpd.put(COL_ID_ASSUNTO, model.getAssunto().getId());
		cvUpd.put(COL_ID_ACOMPANHAMENTO, model.getAcompanhamento().getId());
		
		UtilDAO utilDao = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDao.buildWhereClauseByCV(cvWhr), utilDao.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}

    @Override
    public List<Estudo> selectEstudosByAcompanhamentoId(int idAcompanhamento) throws QueryModelException {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_ACOMPANHAMENTO, idAcompanhamento);
        return select(cv);
    }
}
