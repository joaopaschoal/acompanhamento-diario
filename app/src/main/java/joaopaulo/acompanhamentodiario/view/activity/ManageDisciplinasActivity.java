package joaopaulo.acompanhamentodiario.view.activity;

import java.util.List;
import java.util.Locale;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.CategoriaCientificaBO;
import joaopaulo.acompanhamentodiario.business.bo.CategoriaCientificaBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.DisciplinaBO;
import joaopaulo.acompanhamentodiario.business.bo.DisciplinaBOImpl;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.SaveModelException;
import joaopaulo.acompanhamentodiario.persistence.model.CategoriaCientifica;
import joaopaulo.acompanhamentodiario.persistence.model.Disciplina;
import joaopaulo.acompanhamentodiario.view.util.ParametersView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ManageDisciplinasActivity extends BaseActivity {

	// ----- Attributes ----- //
	private ArrayAdapter<Disciplina> adapter;
	private ArrayAdapter<CategoriaCientifica> categsCientAdapter;
	private Disciplina disciplina;
	private DisciplinaBO bo;
	
	
	// ----- Events ----- //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_disciplinas);
		
		bo = DisciplinaBOImpl.getInstance(this);
		
		Button btnSalvar = findViewById(R.id.manage_disciplinas_btn_save_disciplina);
		btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				disciplina = fillObjectFromScreen(disciplina);
				
				try {
					bo.save(disciplina);
				} catch (SaveModelException ex) {
					new AlertDialog.Builder(ManageDisciplinasActivity.this)
					.setTitle("Falha ao Salvar Registro")
					.setMessage(ex.getMessage())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
				}
				intializeScreen();
				Toast.makeText(ManageDisciplinasActivity.this, getString(R.string.model_successful_saved, Disciplina.ACTUAL_NAME), Toast.LENGTH_LONG).show();
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
		View txtEmpty = findViewById(R.id.manage_disciplinas_txt_empty_disciplina);
		ListView ltvItens = findViewById(R.id.manage_disciplinas_ltv_itens_disciplina);
		ltvItens.setEmptyView(txtEmpty);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		 if (v.getId() == R.id.manage_disciplinas_ltv_itens_disciplina) {
			 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			 Disciplina disciplina = adapter.getItem(info.position);
			 menu.setHeaderTitle(disciplina.getNome());
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
				this.disciplina = adapter.getItem(info.position);
				fillScreenFromObject(disciplina);
				break;
	
			case ParametersView.MENU_DELETE:
				this.disciplina = adapter.getItem(info.position);
				new AlertDialog.Builder(this)
					.setTitle(R.string.title_confirm_delete)
					.setMessage(R.string.msg_confirm_delete)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							try {
								bo.delete(ManageDisciplinasActivity.this.disciplina);
								intializeScreen();
								Toast.makeText(ManageDisciplinasActivity.this, getString(R.string.model_successful_deleted, Disciplina.ACTUAL_NAME), Toast.LENGTH_LONG).show();
							} catch (SQLException ex) {
								Toast.makeText(ManageDisciplinasActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
								ex.printStackTrace();
							} catch (Exception ex) {
								Toast.makeText(ManageDisciplinasActivity.this, getString(R.string.failed_deleting_model,Disciplina.ACTUAL_NAME), Toast.LENGTH_LONG).show();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	
	// ----- Public Methods ----- //

	public void intializeScreen() {
		clearScreen();
		loadListView();
		loadSpinner();
	}
	
	public void clearScreen() {
		this.disciplina = null;
		EditText edtName = findViewById(R.id.manage_disciplinas_edt_name_disciplina);
		edtName.getText().clear();
	}


	// ----- Private Methods ----- //

	protected Disciplina fillObjectFromScreen(Disciplina disciplina) {
		EditText edtNome = findViewById(R.id.manage_disciplinas_edt_name_disciplina);
		Spinner spnCategoriasCientificas = findViewById(R.id.manage_disciplinas_spn_categorias_cientificas);
		
		if (disciplina == null) {
			disciplina = new Disciplina();
		}
		disciplina.setNome(edtNome.getText().toString().toUpperCase(Locale.getDefault()));
		disciplina.setCategoriaCientifica((CategoriaCientifica)spnCategoriasCientificas.getSelectedItem());
		return disciplina;
	}
	
	protected void fillScreenFromObject(Disciplina disciplina) {
		EditText edtNome = findViewById(R.id.manage_disciplinas_edt_name_disciplina);
		Spinner spnCategoriasCientificas = findViewById(R.id.manage_disciplinas_spn_categorias_cientificas);
		
		edtNome.setText(disciplina.getNome());
		spnCategoriasCientificas.setSelection(categsCientAdapter.getPosition(disciplina.getCategoriaCientifica()));
	}
	
	protected void loadListView() {
		List<Disciplina> list;
		try {
			list = bo.selectAll();
			adapter = new ArrayAdapter<Disciplina>(this,android.R.layout.simple_list_item_1, list);
			ListView ltvItens = findViewById(R.id.manage_disciplinas_ltv_itens_disciplina);
			ltvItens.setAdapter(adapter);
			registerForContextMenu(ltvItens);
		} catch (QueryModelException ex) {
			ex.printStackTrace();
			Toast.makeText(this, getString(R.string.failed_loading_model_list,Disciplina.ACTUAL_NAME), Toast.LENGTH_LONG).show();
		}
	}
	
	protected void loadSpinner() {
		CategoriaCientificaBO categCientBO = CategoriaCientificaBOImpl.getInstance(this);
		List<CategoriaCientifica> categoriasCientificas = null;
		try {
			categCientBO.open();
			categoriasCientificas = categCientBO.selectAll();
			categsCientAdapter = new ArrayAdapter<CategoriaCientifica>(this,android.R.layout.simple_spinner_item, categoriasCientificas);
			categsCientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spnCategoriasCientificas = findViewById(R.id.manage_disciplinas_spn_categorias_cientificas);
			spnCategoriasCientificas.setAdapter(categsCientAdapter);
		} catch (QueryModelException ex) {
			ex.printStackTrace();
			Toast.makeText(this, getString(R.string.failed_loading_model_list,CategoriaCientifica.ACTUAL_NAME), Toast.LENGTH_LONG).show();
		}
	}

}
