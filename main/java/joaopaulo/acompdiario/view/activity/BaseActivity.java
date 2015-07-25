package joaopaulo.acompdiario.view.activity;

import joaopaulo.acompdiario.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_acompanhamento:
			intent = new Intent(this, AcompanhamentoEstudosActivity.class);
			startActivity(intent);
			break;
			
		case R.id.action_listar_acompanhamentos:
			intent = new Intent(this, ListarAcompanhamentosActivity.class);
			startActivity(intent);
			break;

		case R.id.action_index_estudo:
			intent = new Intent(this, IndexEstudoActivity.class);
			startActivity(intent);
			break;

		case R.id.action_index_trabalho:
			intent = new Intent(this, IndexTrabalhoActivity.class);
			startActivity(intent);
			break;

		case R.id.action_index_ferramentas:
			intent = new Intent(this, IndexFerramentasActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
