package joaopaulo.acompanhamentodiario.view.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.persistence.dao.DatabaseHandler;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ExportarBDActivity extends BaseActivity {
	//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exportar_bd);
		
		final EditText edtSql = findViewById(R.id.exportar_bd_edt_sql);
		final EditText edtDbPath = findViewById(R.id.exportar_bd_edt_path_db);
		Button btnExecSql = findViewById(R.id.exportar_bd_btn_exec_sql);
		Button btnExportarBD = findViewById(R.id.exportar_bd_btn_exportar);
		Button btnImportarBD = findViewById(R.id.exportar_bd_btn_importar);
		
		btnExecSql.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sql = edtSql.getText().toString().trim();
				if (sql != "") {
					DatabaseHandler dbHandler = DatabaseHandler.getInstance(ExportarBDActivity.this);
					SQLiteDatabase database = dbHandler.getWritableDatabase();
					database.execSQL(sql);
					
//					final Cursor cursor = database.rawQuery("SELECT changes()", null);
//					//final Cursor cursor = database.rawQuery("SELECT * from acompanhamento where id_acompanhamento = (select max(id_acompanhamento) from acompanhamento)", null);
//					if (cursor != null) {
//					    try {
//					        if (cursor.moveToFirst()) {
//					        	int qtdeRows = cursor.getInt(0);
//					            //int id = cursor.getLong(cursor.getColumnIndex("data_acompanhamento"));
//					        }
//					    } finally {
//					        cursor.close();
//					    }
//					}
					dbHandler.close();
					Toast.makeText(ExportarBDActivity.this, "Sql Executado com Sucesso!!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		btnExportarBD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				File data = Environment.getDataDirectory();
				FileChannel source;
				FileChannel destination;
				String currentDBPath = "/data/" + "joaopaulo.acompanhamentodiario" +"/databases/"+DatabaseHandler.DATABASE_NAME;
				String backupDBPath = DatabaseHandler.DATABASE_NAME;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
				try {
					source = new FileInputStream(currentDB).getChannel();
					destination = new FileOutputStream(backupDB).getChannel();
					destination.transferFrom(source, 0, source.size());
					source.close();
					destination.close();
					
					ExportarBDActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))));
					
					Toast.makeText(ExportarBDActivity.this, "DB Exportado!", Toast.LENGTH_LONG).show();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		btnImportarBD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File sd = Environment.getExternalStorageDirectory();
				File data = Environment.getDataDirectory();
				String pathToBackupDB = edtDbPath.getText().toString();

			    if (sd.canWrite()) {
			    	String currentDBPath = "/data/" + "joaopaulo.acompanhamentodiario" +"/databases/"+DatabaseHandler.DATABASE_NAME;
			        //String backupDBPath = DatabaseHandler.DATABASE_NAME;
			        File currentDB = new File(data, currentDBPath);
			        File backupDB = new File(sd, pathToBackupDB);

			        if (currentDB.exists()) {
						try {
							FileChannel src;
							src = new FileInputStream(backupDB).getChannel();
							FileChannel dst = new FileOutputStream(currentDB).getChannel();
							dst.transferFrom(src, 0, src.size());
				            src.close();
				            dst.close();
						} catch (FileNotFoundException e) {
							Toast.makeText(ExportarBDActivity.this, "Erro: arquivo não encontrado.", Toast.LENGTH_LONG).show();
						} catch (IOException e) {
							Toast.makeText(ExportarBDActivity.this, "Erro: erro de permissão de leitura / escrita.", Toast.LENGTH_LONG).show();
						}
			            
			            
			            Toast.makeText(getApplicationContext(), "Database Restored successfully", Toast.LENGTH_SHORT).show();
			        }
			    }
			}
		});
	}
	
}
