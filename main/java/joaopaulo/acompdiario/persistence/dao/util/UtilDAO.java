package joaopaulo.acompdiario.persistence.dao.util;

import android.content.ContentValues;

public interface UtilDAO {
	/**
	 * Obtém um ContentValues e a partir dele monta uma String de cláusula
	 * WHERE com todos os campos contidos no seu keySet.
	 * @param cv O ContentValues preenchido com as colunas e valores.
	 * @return A String da cláusula where, ex: nome = ? and idade = ?
	 */
	public String buildWhereClauseByCV(ContentValues cv);
	
	/**
	 * Obtém um ContentValues e a partir dele monta um Array de Strings relativo
	 * aos valores dos parâmetros da cláusula WHERE.
	 * @param cv O ContentValues preenchido com as colunas e valores.
	 * @return A String da cláusula where, ex: nome = ? and idade = ?
	 */
	public String[] buildWhereParamsByCV(ContentValues cv);
}
