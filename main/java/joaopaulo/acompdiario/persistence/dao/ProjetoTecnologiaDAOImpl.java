package joaopaulo.acompdiario.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.ProjetoTecnologia;

public class ProjetoTecnologiaDAOImpl extends SQLiteDAO<ProjetoTecnologia> implements ProjetoTecnologiaDAO {

	public static final String TBL_NAME = "projeto_tecnologia";
	public static final String COL_ID = "id_projeto_tecnologia";
	public static final String COL_FLG_TECNOLOGIA_PRINCIPAL = "flg_tecnologia_principal";
	public static final String COL_ID_PROJETO = "id_projeto";
    public static final String COL_ID_TECNOLOGIA = "id_tecnologia";
	
	private static ProjetoTecnologiaDAOImpl instance;
	
	private ProjetoTecnologiaDAOImpl(Context context) {
		super(context);
	}
	
	public static ProjetoTecnologiaDAO getInstance(Context context) {
		if (instance == null) {
			instance = new ProjetoTecnologiaDAOImpl(context); 
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
		String[] columns = new String[] { COL_ID, COL_FLG_TECNOLOGIA_PRINCIPAL, COL_ID_PROJETO, COL_ID_TECNOLOGIA };
		return columns;
	}

	@Override
	public ProjetoTecnologia fillByCursor(Cursor cursor, ProjetoTecnologia model) {
		if (model == null) {
			model = new ProjetoTecnologia();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setFlgTecnologiaPrincipal(cursor.getInt(cursor.getColumnIndexOrThrow(COL_FLG_TECNOLOGIA_PRINCIPAL)) != 0);
        model.getProjeto().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_PROJETO)));
        model.getTecnologia().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_TECNOLOGIA)));
		return model;
	}

	@Override
	public ProjetoTecnologia insert(ProjetoTecnologia model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_FLG_TECNOLOGIA_PRINCIPAL, model.isFlgTecnologiaPrincipal());
        cv.put(COL_ID_PROJETO, model.getProjeto().getId());
        cv.put(COL_ID_TECNOLOGIA, model.getTecnologia().getId());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public ProjetoTecnologia update(ProjetoTecnologia model) throws UpdateModelException {
		ContentValues cvUpd = new ContentValues();
		ContentValues cvWhr = new ContentValues();
        cvUpd.put(COL_FLG_TECNOLOGIA_PRINCIPAL, model.isFlgTecnologiaPrincipal());
        cvUpd.put(COL_ID_PROJETO, model.getProjeto().getId());
        cvUpd.put(COL_ID_TECNOLOGIA, model.getTecnologia().getId());
		cvWhr.put(COL_ID, model.getId());
		UtilDAO utilDAO = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDAO.buildWhereClauseByCV(cvWhr), utilDAO.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}

	@Override
	public List<ProjetoTecnologia> selectProjetosTecnologiaFromProjetoId(Integer idProjeto) throws QueryModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID_PROJETO, idProjeto);
		return select(cv);
	}
}