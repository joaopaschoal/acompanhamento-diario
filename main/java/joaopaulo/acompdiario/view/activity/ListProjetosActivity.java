package joaopaulo.acompdiario.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.ProjetoBO;
import joaopaulo.acompdiario.business.bo.ProjetoBOImpl;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.Projeto;
import joaopaulo.acompdiario.view.util.ParametersView;

public class ListProjetosActivity extends BaseActivity {

		// ----- Attributes ----- //
		private ArrayAdapter<Projeto> adapter;
		private ProjetoBO bo;


		// ----- Events ----- //
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_list_projetos);

			bo = ProjetoBOImpl.getInstance(this);

			Button btnNovoProjeto = (Button)findViewById(R.id.list_projetos_btn_novo_projeto);
			btnNovoProjeto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itNovoProjeto = new Intent(ListProjetosActivity.this, NewProjetoActivity.class);
                    startActivity(itNovoProjeto);
                }
            });
		}

		@Override
		protected void onResume() {
			bo.open();
			super.onResume();

			intializeScreen();
		}

		@Override
		protected void onPause() {
			bo.close();
			super.onPause();
		}

		@Override
		public void onContentChanged() {
			super.onContentChanged();
			View txtEmpty = findViewById(R.id.list_projetos_txv_empty_items);
			ListView ltvItens = (ListView)findViewById(R.id.list_projetos_ltv_items);
			ltvItens.setEmptyView(txtEmpty);
		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
			if (v.getId() == R.id.list_projetos_ltv_items) {
				AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
				Projeto projeto = adapter.getItem(info.position);
				menu.setHeaderTitle(projeto.getNome());
				menu.add(Menu.NONE, ParametersView.MENU_EDIT, 1, R.string.edit);
				menu.add(Menu.NONE, ParametersView.MENU_DELETE, 2, R.string.delete);
			}
		}

		@Override
		public boolean onContextItemSelected(MenuItem item) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			int idItem = item.getItemId();

			switch (idItem) {
				case ParametersView.MENU_EDIT:
					Projeto projeto = adapter.getItem(info.position);
                    loadProjetoOntoNewProjetoActivity(projeto);
					break;

				case ParametersView.MENU_DELETE:
					final Projeto projetoToDelete  = adapter.getItem(info.position);
					new AlertDialog.Builder(this)
							.setTitle(R.string.title_confirm_delete)
							.setMessage(R.string.msg_confirm_delete)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									try {
										bo.delete(projetoToDelete);
										intializeScreen();
										Toast.makeText(ListProjetosActivity.this, getString(R.string.model_successful_deleted, Projeto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
									} catch (SQLException ex) {
										Toast.makeText(ListProjetosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
										ex.printStackTrace();
									} catch (Exception ex) {
										Toast.makeText(ListProjetosActivity.this, getString(R.string.failed_deleting_model,Projeto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
										ex.printStackTrace();
									}
								}
							})
							.setNegativeButton(R.string.no, null)
							.show();
					break;
			}

			return true;
		}


		// ----- Public Methods ----- //

		public void intializeScreen() {
			loadListView();
		}


		// ----- Private Methods ----- //

		protected void loadListView() {
			List<Projeto> list;
			try {
				list = bo.selectAll();
				adapter = new ArrayAdapter<Projeto>(this,android.R.layout.simple_list_item_1, list);
				ListView ltvItens = (ListView)findViewById(R.id.list_projetos_ltv_items);
				ltvItens.setAdapter(adapter);
				registerForContextMenu(ltvItens);
			} catch (QueryModelException ex) {
				ex.printStackTrace();
				Toast.makeText(this, getString(R.string.failed_loading_model_list, Projeto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
			}
		}

        protected void loadProjetoOntoNewProjetoActivity(Projeto projeto) {
            Intent itEditProjeto = new Intent(this, NewProjetoActivity.class);
            itEditProjeto.putExtra("id_projeto_to_edit", projeto.getId());
            startActivity(itEditProjeto);
        }

}
