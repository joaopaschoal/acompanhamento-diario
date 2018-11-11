package joaopaulo.acompanhamentodiario.persistence.dao;

import java.sql.Date;
import java.sql.Timestamp;

import joaopaulo.acompanhamentodiario.business.exception.InsertModelException;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.UpdateModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AcompanhamentoDAOImpl extends SQLiteDAO<Acompanhamento> implements AcompanhamentoDAO {

	public static final String TBL_NAME = "acompanhamento";
	public static final String COL_ID = "id_acompanhamento";
	public static final String COL_DATA_ACOMPANHAMENTO = "data_acompanhamento";
	public static final String COL_DATA_REGISTRO = "data_registro";
	public static final String COL_PERIODO_AEROBICA = "periodo_aerobica";
	public static final String COL_PERIODO_MUSCULACAO = "periodo_musculacao";
	public static final String COL_FLG_EX_UR = "flg_ex_ur";
	
	private static AcompanhamentoDAOImpl instance;
	
	public static AcompanhamentoDAO getInstance(Context context) {
		if (instance == null) {
			instance = new AcompanhamentoDAOImpl(context);
		}
		return instance;
	}
	
	private AcompanhamentoDAOImpl(Context context) {
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
		return COL_DATA_ACOMPANHAMENTO;
	}

	@Override
	public String[] getColumns() {
		return new String[] {COL_ID, COL_DATA_ACOMPANHAMENTO, COL_DATA_REGISTRO, COL_PERIODO_AEROBICA,
				COL_PERIODO_MUSCULACAO, COL_FLG_EX_UR};
	}

	@Override
	public Acompanhamento fillByCursor(Cursor cursor, Acompanhamento model) {
		if (model == null) {
			model = new Acompanhamento();
		}
		
		try {
			model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
			model.setDataAcompanhamento(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATA_ACOMPANHAMENTO))));
			model.setDataRegistro(new Timestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATA_REGISTRO))));
			
			model.setPeriodoAerobica(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PERIODO_AEROBICA)));
			model.setPeriodoMusculacao(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PERIODO_MUSCULACAO)));
			model.setFlgExUr(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FLG_EX_UR)) != 0);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}

	@Override
	public Acompanhamento insert(Acompanhamento model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_DATA_ACOMPANHAMENTO, model.getDataAcompanhamento().getTime());
		cv.put(COL_DATA_REGISTRO, new java.util.Date().getTime());
		cv.put(COL_PERIODO_AEROBICA, model.getPeriodoAerobica());
		cv.put(COL_PERIODO_MUSCULACAO, model.getPeriodoMusculacao());
		cv.put(COL_FLG_EX_UR, model.getFlgExUr());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public Acompanhamento update(Acompanhamento model) throws UpdateModelException {
		ContentValues cvWhr = new ContentValues();
		ContentValues cvUpd = new ContentValues();
		
		cvWhr.put(COL_ID, model.getId());
		
		cvUpd.put(COL_DATA_ACOMPANHAMENTO, model.getDataAcompanhamento().getTime());
		cvUpd.put(COL_PERIODO_AEROBICA, model.getPeriodoAerobica());
		cvUpd.put(COL_PERIODO_MUSCULACAO, model.getPeriodoMusculacao());
		cvUpd.put(COL_FLG_EX_UR, model.getFlgExUr());
		
		UtilDAO utilDao = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDao.buildWhereClauseByCV(cvWhr), utilDao.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}

	@Override
	public Date selectDateNextAcompanhamento() throws QueryModelException {
	    String query = "select " + COL_DATA_ACOMPANHAMENTO + " from " + TBL_NAME + " order by " + COL_DATA_ACOMPANHAMENTO + ";";


        Cursor cursor = null;
	    try {
            this.open();
            cursor = this.database.rawQuery(query, null);
            long timeCurrentRecord;
            long millisPerDay = 24 * 60 * 60 * 1000;

            if (!cursor.moveToNext()) return null;
            timeCurrentRecord = cursor.getLong(cursor.getColumnIndex(COL_DATA_ACOMPANHAMENTO));

            while (cursor.moveToNext()) {
                long timeNextRecord = cursor.getLong(cursor.getColumnIndex(COL_DATA_ACOMPANHAMENTO));
                if (timeCurrentRecord + millisPerDay != timeNextRecord) {
                    return new Date(timeCurrentRecord + millisPerDay);
                }
                timeCurrentRecord = timeNextRecord;
            }
        } catch(Exception e) {
	        System.err.println(e);
        } finally {
            cursor.close();
	        this.close();
        }

		return null;
	}
}
