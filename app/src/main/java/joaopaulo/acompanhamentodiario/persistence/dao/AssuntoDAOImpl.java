package joaopaulo.acompanhamentodiario.persistence.dao;

import joaopaulo.acompanhamentodiario.business.exception.InsertModelException;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.UpdateModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.Assunto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

public class AssuntoDAOImpl extends SQLiteDAO<Assunto> implements AssuntoDAO {

	public static final String TBL_NAME = "assunto";
	public static final String COL_ID = "id_assunto";
	public static final String COL_NOME = "nome";
    public static final String COL_NOME_ABREVIADO = "nome_abreviado";
	public static final String COL_ID_ASSUNTO_PAI = "id_assunto_pai";
	public static final String COL_ID_DISCIPLINA = "id_disciplina";

	
	private static AssuntoDAOImpl instance;
	
	private AssuntoDAOImpl(Context context) {
		super(context);
	}
	
	public static AssuntoDAO getInstance(Context context) {
		if (instance == null) {
			instance = new AssuntoDAOImpl(context); 
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
		return new String[] { COL_ID, COL_NOME, COL_NOME_ABREVIADO, COL_ID_ASSUNTO_PAI, COL_ID_DISCIPLINA };
	}

	@Override
	public Assunto fillByCursor(Cursor cursor, Assunto model) {
		if (model == null) {
			model = new Assunto();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setNome(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)));
        model.setNomeAbreviado(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME_ABREVIADO)));
		model.getAssuntoPai().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_ASSUNTO_PAI)));
		model.getDisciplina().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_DISCIPLINA)));
		return model;
	}

	@Override
	public Assunto insert(Assunto model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_NOME, model.getNome());
        cv.put(COL_NOME_ABREVIADO, model.getNomeAbreviado() != null && model.getNomeAbreviado().trim() != "" ? model.getNomeAbreviado().trim() : null);
		Integer idAssuntoPai = (model.getAssuntoPai() != null && model.getAssuntoPai().getId() != null && model.getAssuntoPai().getId() > 0) ? model.getAssuntoPai().getId() : null;
		cv.put(COL_ID_ASSUNTO_PAI, idAssuntoPai);
		cv.put(COL_ID_DISCIPLINA, model.getDisciplina().getId());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public Assunto update(Assunto model) throws UpdateModelException {
		ContentValues cvUpd = new ContentValues();
		ContentValues cvWhr = new ContentValues();
		cvUpd.put(COL_NOME, model.getNome());
        cvUpd.put(COL_NOME_ABREVIADO, model.getNomeAbreviado() != null && model.getNomeAbreviado().trim() != "" ? model.getNomeAbreviado().trim() : null);
		Integer idAssuntoPai = (model.getAssuntoPai() != null && model.getAssuntoPai().getId() != null && model.getAssuntoPai().getId() > 0) ? model.getAssuntoPai().getId() : null;
		cvUpd.put(COL_ID_ASSUNTO_PAI, idAssuntoPai);
		cvUpd.put(COL_ID_DISCIPLINA, model.getDisciplina().getId());
		cvWhr.put(COL_ID, model.getId());
		UtilDAO utilDAO = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDAO.buildWhereClauseByCV(cvWhr), utilDAO.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}

    @Override
    public List<Assunto> selectByAssuntoPaiId(int idAssunto) throws QueryModelException {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_ASSUNTO_PAI, idAssunto);
        return select(cv);
    }

    @Override
    public List<Assunto> selectByDisciplinaId(Integer idDisciplina) throws QueryModelException {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_DISCIPLINA, idDisciplina);
        return select(cv);
    }

    @Override
    public List<Assunto> selectByDisciplinaIdWhereIdAssuntoIsNull (Integer idDisciplina) throws QueryModelException {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_DISCIPLINA, idDisciplina);
        cv.putNull(COL_ID_ASSUNTO_PAI);
        return select(cv);
    }
}