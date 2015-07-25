package joaopaulo.acompdiario.persistence.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static DatabaseHandler instance;
	public static final int DATABASE_VERSION = 5;
	public static final String DATABASE_NAME = "bd_acompanhamento.db3";
	
	public static final String SQL_CREATE_TABLE_CATEGORIA_CIENTIFICA = 
				"CREATE TABLE categoria_cientifica ( " + 
				    CategoriaCientificaDAOImpl.COL_ID + " INTEGER        CONSTRAINT 'PK_CATEGORIA_CIENTIFICA' PRIMARY KEY AUTOINCREMENT, " +
				    CategoriaCientificaDAOImpl.COL_NOME + "                    VARCHAR( 50 )  NOT NULL CONSTRAINT 'UQ_categoria_cientifica_nome' UNIQUE " + 
				");";
	public static final String SQL_CREATE_TABLE_DISCIPLINA = "CREATE TABLE disciplina ( " + 
			    	DisciplinaDAOImpl.COL_ID + " INTEGER        CONSTRAINT 'PK_disciplina' PRIMARY KEY AUTOINCREMENT, " +
			    	DisciplinaDAOImpl.COL_NOME + " VARCHAR( 50 )  CONSTRAINT 'UQ_disciplina_nome' UNIQUE, " +
			    	DisciplinaDAOImpl.COL_ID_CATEGORIA_CIENTIFICA + " INTEGER        NOT NULL CONSTRAINT 'FK_disciplina_categoria_cientifica' REFERENCES categoria_cientifica ( id_categoria_cientifica ) " + 
				");";
	public static final String SQL_CREATE_TABLE_ASSUNTO = "CREATE TABLE assunto ( " + 
				    AssuntoDAOImpl.COL_ID + " INTEGER        CONSTRAINT 'PK_assunto' PRIMARY KEY AUTOINCREMENT, " +
				    AssuntoDAOImpl.COL_NOME + " VARCHAR( 50 )  NOT NULL CONSTRAINT 'UQ_assunto_nome' UNIQUE, " +
				    AssuntoDAOImpl.COL_ID_ASSUNTO_PAI + " INTEGER        CONSTRAINT 'FK_assunto_assunto' REFERENCES assunto ( id_assunto ), " +
				    AssuntoDAOImpl.COL_ID_DISCIPLINA + " INTEGER NOT NULL CONSTRAINT 'FK_assunto_disciplina' REFERENCES disciplina ( id_disciplina ) " + 
				");";
	public static final String SQL_CREATE_TABLE_ACOMPANHAMENTO = "CREATE TABLE acompanhamento ( " +
                    "    id_acompanhamento   INTEGER  CONSTRAINT 'PK_acompanhamento' PRIMARY KEY AUTOINCREMENT," +
                    "    data_acompanhamento DATE     NOT NULL" +
                    "                                 CONSTRAINT 'UQ_acompanhamento_data_acompanhamento' UNIQUE" +
                    "                                 DEFAULT ( CURRENT_DATE )," +
                    "    data_registro       DATETIME NOT NULL" +
                    "                                 DEFAULT ( CURRENT_TIME )," +
                    "    periodo_aerobica    INTEGER  NOT NULL" +
                    "                                 DEFAULT ( 0 )," +
                    "    periodo_musculacao  INTEGER  NOT NULL" +
                    "                                 DEFAULT ( 0 )," +
                    "    periodo_estudo      INTEGER  NOT NULL" +
                    "                                 DEFAULT ( 0 )," +
                    "    flg_ex_ur           BOOLEAN  NOT NULL," +
                    "    id_assunto          INTEGER  CONSTRAINT 'FK_acompanhamento_assunto' REFERENCES assunto ( id_assunto ) " +
                    ");";
	
	public static final String SQL_CREATE_TABLE_ESTUDO = "CREATE TABLE estudo ( " +
				    "id_estudo         INTEGER CONSTRAINT 'PK_estudo' PRIMARY KEY AUTOINCREMENT," +
				    "tempo_mins        INTEGER NOT NULL" +
				    "                          DEFAULT ( 0 )," +
				    "id_assunto        INTEGER NOT NULL" +
				    "                          CONSTRAINT 'FK_estudo_assunto_1' REFERENCES assunto ( id_assunto )," +
				    "id_acompanhamento INTEGER NOT NULL" +
				    "                           CONSTRAINT 'FK_estudo_acompanhamento_1' REFERENCES acompanhamento ( id_acompanhamento )" + 
				");";
	
	
	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static DatabaseHandler getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseHandler(context);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	protected void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS estudo;");
		db.execSQL("DROP TABLE IF EXISTS acompanhamento;");
		db.execSQL("DROP TABLE IF EXISTS assunto;");
		db.execSQL("DROP TABLE IF EXISTS disciplina;");
		db.execSQL("DROP TABLE IF EXISTS categoria_cientifica;");
	}
	
	protected void createTables(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_CATEGORIA_CIENTIFICA);
		db.execSQL(SQL_CREATE_TABLE_DISCIPLINA);
		db.execSQL(SQL_CREATE_TABLE_ASSUNTO);
        db.execSQL(SQL_CREATE_TABLE_ACOMPANHAMENTO);
        db.execSQL(SQL_CREATE_TABLE_ESTUDO);
	}
}
