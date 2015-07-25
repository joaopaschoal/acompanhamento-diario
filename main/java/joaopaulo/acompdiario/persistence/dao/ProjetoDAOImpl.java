package joaopaulo.acompdiario.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.Projeto;

public class ProjetoDAOImpl extends SQLiteDAO<Projeto> implements ProjetoDAO {

	public static final String TBL_NAME = "projeto";
	public static final String COL_ID = "id_projeto";
	public static final String COL_NOME = "nome";
	public static final String COL_DATA_CRIACAO = "data_criacao";
	public static final String COL_ID_NATUREZA_PROJETO = "id_natureza_projeto";
	public static final String COL_ID_DONO_PRODUTO = "id_dono_produto";
	
	private static ProjetoDAOImpl instance;
	
	private ProjetoDAOImpl(Context context) {
		super(context);
	}
	
	public static ProjetoDAO getInstance(Context context) {
		if (instance == null) {
			instance = new ProjetoDAOImpl(context); 
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
		String[] columns = new String[] { COL_ID, COL_NOME, COL_DATA_CRIACAO, COL_ID_NATUREZA_PROJETO, COL_ID_DONO_PRODUTO };
		return columns;
	}

	@Override
	public Projeto fillByCursor(Cursor cursor, Projeto model) {
		if (model == null) {
			model = new Projeto();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setNome(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)));
		model.setDataCriacao(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATA_CRIACAO))));
		model.getNaturezaProjeto().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_NATUREZA_PROJETO)));
		model.getDonoProduto().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_DONO_PRODUTO)));
		return model;
	}

	@Override
	public Projeto insert(Projeto model) throws InsertModelException {
		ContentValues cv = new ContentValues();
		cv.put(COL_NOME, model.getNome());
		cv.put(COL_DATA_CRIACAO, model.getDataCriacao().getTime());
		cv.put(COL_ID_NATUREZA_PROJETO, model.getNaturezaProjeto().getId());
		cv.put(COL_ID_DONO_PRODUTO, model.getDonoProduto().getId());
		Long insertId = database.insert(TBL_NAME, null, cv);
		if (insertId <= 0) {
			throw new InsertModelException();
		}
		model.setId(insertId.intValue());
		return model;
	}

	@Override
	public Projeto update(Projeto model) throws UpdateModelException {
		ContentValues cvUpd = new ContentValues();
		ContentValues cvWhr = new ContentValues();
		cvUpd.put(COL_NOME, model.getNome());
		cvUpd.put(COL_DATA_CRIACAO, model.getDataCriacao().getTime());
		cvUpd.put(COL_ID_NATUREZA_PROJETO, model.getNaturezaProjeto().getId());
		cvUpd.put(COL_ID_DONO_PRODUTO, model.getDonoProduto().getId());
		cvWhr.put(COL_ID, model.getId());
		UtilDAO utilDAO = new UtilDAOImpl();
		int affectedRows = database.update(TBL_NAME, cvUpd, utilDAO.buildWhereClauseByCV(cvWhr), utilDAO.buildWhereParamsByCV(cvWhr));
		if (affectedRows == 0) {
			throw new UpdateModelException();
		}
		return model;
	}
	
}