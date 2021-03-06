package joaopaulo.acompanhamentodiario.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import joaopaulo.acompanhamentodiario.business.exception.InsertModelException;
import joaopaulo.acompanhamentodiario.business.exception.UpdateModelException;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompanhamentodiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.DonoProduto;

public class DonoProdutoDAOImpl extends SQLiteDAO<DonoProduto> implements DonoProdutoDAO {

	public static final String TBL_NAME = "dono_produto";
	public static final String COL_ID = "id_dono_produto";
	public static final String COL_NOME = "nome";
	
	private static DonoProdutoDAOImpl instance;
	
	private DonoProdutoDAOImpl(Context context) {
		super(context);
	}
	
	public static DonoProdutoDAO getInstance(Context context) {
		if (instance == null) {
			instance = new DonoProdutoDAOImpl(context); 
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
	public DonoProduto fillByCursor(Cursor cursor, DonoProduto model) {
		if (model == null) {
			model = new DonoProduto();
		}
		model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
		model.setNome(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)));
		return model;
	}

	@Override
	public DonoProduto insert(DonoProduto model) throws InsertModelException {
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
	public DonoProduto update(DonoProduto model) throws UpdateModelException {
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