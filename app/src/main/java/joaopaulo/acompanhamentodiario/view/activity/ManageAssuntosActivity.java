package joaopaulo.acompanhamentodiario.view.activity;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.AssuntoBO;
import joaopaulo.acompanhamentodiario.business.bo.AssuntoBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.DisciplinaBO;
import joaopaulo.acompanhamentodiario.business.bo.DisciplinaBOImpl;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.SaveModelException;
import joaopaulo.acompanhamentodiario.business.exception.ValidationException;
import joaopaulo.acompanhamentodiario.persistence.model.Assunto;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class ManageAssuntosActivity extends BaseActivity {

	// ----- Attributes ----- //
	private ArrayAdapter<Assunto> adapter;
	private ArrayAdapter<Disciplina> disciplinasAdapter;
	private ArrayAdapter<Assunto> assuntosAdapter;
	private Assunto assunto;
	private AssuntoBO bo;

	// ----- Events ----- //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_assuntos);

		bo = AssuntoBOImpl.getInstance(this);

		bindEvents();
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
		View txtEmpty = findViewById(R.id.manage_assuntos_txt_empty_assuntos);
		ListView ltvItens = findViewById(R.id.manage_assuntos_ltv_itens_assuntos);
		ltvItens.setEmptyView(txtEmpty);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.manage_assuntos_ltv_itens_assuntos) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Assunto assunto = adapter.getItem(info.position);
			menu.setHeaderTitle(assunto.getNomeCustomLength());
			menu.add(Menu.NONE, ParametersView.MENU_EDIT, 1, R.string.edit);
			menu.add(Menu.NONE, ParametersView.MENU_DELETE, 2, R.string.delete);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int idItem = item.getItemId();

		switch (idItem) {
		case ParametersView.MENU_EDIT:
			this.assunto = adapter.getItem(info.position);
			fillScreenFromObject(assunto);
			break;

		case ParametersView.MENU_DELETE:
			this.assunto = adapter.getItem(info.position);
			new AlertDialog.Builder(this)
					.setTitle(R.string.title_confirm_delete)
					.setMessage(R.string.msg_confirm_delete)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									try {
										bo.delete(ManageAssuntosActivity.this.assunto);
										intializeScreen();
										Toast.makeText(
												ManageAssuntosActivity.this,
												getString(
														R.string.model_successful_deleted,
														Assunto.ACTUAL_NAME),
												Toast.LENGTH_LONG).show();
									} catch (SQLException ex) {
										Toast.makeText(
												ManageAssuntosActivity.this,
												ex.getMessage(),
												Toast.LENGTH_LONG).show();
										ex.printStackTrace();
									} catch (Exception ex) {
										Toast.makeText(
												ManageAssuntosActivity.this,
												getString(
														R.string.failed_deleting_model,
														Assunto.ACTUAL_NAME),
												Toast.LENGTH_LONG).show();
										ex.printStackTrace();
									}
								}
							}).setNegativeButton(R.string.no, null).show();
			break;
		}

		return true;
	}

	// ----- Public Methods ----- //

	public void intializeScreen() {
		clearScreen();
		loadListView();
		loadSpinnerDisciplinas();
		loadSpinnerAssuntos();
	}

	public void clearScreen() {
		this.assunto = null;
		EditText edtNome = findViewById(R.id.manage_assuntos_edt_name_assunto);
        edtNome.getText().clear();
        EditText edtNomeAbreviado = findViewById(R.id.manage_assuntos_edt_nome_assunto_abreviado);
        edtNomeAbreviado.getText().clear();
	}

	// ----- Private Methods ----- //

    private void bindEvents() {
        Button btnSalvar = findViewById(R.id.manage_assuntos_btn_save_assunto);
        btnSalvar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                assunto = fillObjectFromScreen(assunto);

                try {
                    bo.save(assunto);
                } catch (SaveModelException ex) {
                    new AlertDialog.Builder(ManageAssuntosActivity.this)
                            .setTitle("Falha ao Excluir Registro")
                            .setMessage(ex.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert).show();
                }
                intializeScreen();
                Toast.makeText(
                        ManageAssuntosActivity.this,
                        getString(R.string.model_successful_saved,
                                Assunto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
            }
        });

        EditText edtNome = findViewById(R.id.manage_assuntos_edt_name_assunto);
        edtNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText edtNomeAbreviado = findViewById(R.id.manage_assuntos_edt_nome_assunto_abreviado);
                    edtNomeAbreviado.setText(((EditText)v).getText().length() <= Assunto.DEFAULT_LENGTH ? ((EditText)v).getText() : ((EditText)v).getText().subSequence(0, Assunto.DEFAULT_LENGTH) );
                }
            }
        });

        ScrollView parentScrollView = findViewById(R.id.manage_assuntos_parent_scrlvw);
        parentScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.manage_assuntos_ltv_itens_assuntos).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        ListView listAssuntos = (ListView)findViewById(R.id.manage_assuntos_ltv_itens_assuntos);
        listAssuntos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

	protected Assunto fillObjectFromScreen(Assunto assunto) {
		EditText edtNome = findViewById(R.id.manage_assuntos_edt_name_assunto);
        EditText edtNomeAbreviado = findViewById(R.id.manage_assuntos_edt_nome_assunto_abreviado);
		Spinner spnDisciplinasSpinner = findViewById(R.id.manage_assuntos_spn_disciplinas);
		Spinner spnAssuntosSpinner = findViewById(R.id.manage_assuntos_spn_assuntos);

		if (assunto == null) {
			assunto = new Assunto();
		}
		assunto.setNome(edtNome.getText().toString().toUpperCase(Locale.getDefault()));
        assunto.setNomeAbreviado(edtNomeAbreviado.getText().toString().toUpperCase(Locale.getDefault()));
        assunto.setDisciplina((Disciplina) spnDisciplinasSpinner.getSelectedItem());
		Assunto assuntoPai = (Assunto) spnAssuntosSpinner.getSelectedItem();
		if (assuntoPai != null && assuntoPai.getId() == -1) {
			assuntoPai = null;
		}
		assunto.setAssuntoPai(assuntoPai);
		return assunto;
	}

	protected void fillScreenFromObject(Assunto assunto) {
		EditText edtNome = findViewById(R.id.manage_assuntos_edt_name_assunto);
        EditText edtNomeAbreviado = findViewById(R.id.manage_assuntos_edt_nome_assunto_abreviado);
		Spinner spnDisciplinas = findViewById(R.id.manage_assuntos_spn_disciplinas);
		Spinner spnAssuntos = findViewById(R.id.manage_assuntos_spn_assuntos);

		edtNome.setText(assunto.getNome());
        edtNomeAbreviado.setText(assunto.getNomeAbreviado());
		spnDisciplinas.setSelection(disciplinasAdapter.getPosition(assunto.getDisciplina()));
		Assunto assuntoPai = assunto.getAssuntoPai();
		if (assuntoPai == null || assuntoPai.getId() <= 0) {
			assuntoPai = new Assunto();
			assuntoPai.setId(-1);
			assuntoPai.setNome("...");
		}
		spnAssuntos.setSelection(assuntosAdapter.getPosition(assuntoPai));
	}

	protected void loadListView() {
		List<Assunto> list;
		try {
			list = bo.selectAll();
			for (Assunto assunto : list) {
				try {
					assunto.setNomeCompleto(bo.obterNomeCompleto(assunto));
				} catch (Exception e) {
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
				}
			}
			
			adapter = new ArrayAdapter<Assunto>(this,
					android.R.layout.simple_list_item_1, list);
			
			adapter.sort(new Comparator<Assunto>() {
			    @Override
			    public int compare(Assunto a, Assunto b) {
			        return b.getNomeCompleto().compareTo(a.getNomeCompleto());
			    }
			});
			
			ListView ltvItens = findViewById(R.id.manage_assuntos_ltv_itens_assuntos);
			ltvItens.setAdapter(adapter);
			registerForContextMenu(ltvItens);
		} catch (QueryModelException ex) {
			ex.printStackTrace();
			Toast.makeText(
					this,
					getString(R.string.failed_loading_model_list,
							Assunto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
		}
	}

	protected void loadSpinnerDisciplinas() {
		DisciplinaBO disciplinaBO = DisciplinaBOImpl.getInstance(this);
		List<Disciplina> disciplinas = null;
		try {
			disciplinaBO.open();
			disciplinas = disciplinaBO.selectAll();
			disciplinasAdapter = new ArrayAdapter<Disciplina>(this,
					android.R.layout.simple_spinner_item, disciplinas);
			disciplinasAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spnDisciplinas = findViewById(R.id.manage_assuntos_spn_disciplinas);
			spnDisciplinas.setAdapter(disciplinasAdapter);
		} catch (QueryModelException ex) {
			ex.printStackTrace();
			Toast.makeText(
					this,
					getString(R.string.failed_loading_model_list,
							CategoriaCientifica.ACTUAL_NAME), Toast.LENGTH_LONG)
					.show();
		}
	}

	public void loadSpinnerAssuntos() {
		List<Assunto> assuntos = null;
		try {
			assuntos = bo.selectAll();

			Assunto emptyAssunto = new Assunto();
			emptyAssunto.setId(-1);
			emptyAssunto.setNome("...");
			assuntos.add(emptyAssunto);

			assuntosAdapter = new ArrayAdapter<Assunto>(this,
					android.R.layout.simple_spinner_item, assuntos);
			assuntosAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spnAssuntos = findViewById(R.id.manage_assuntos_spn_assuntos);
			spnAssuntos.setAdapter(assuntosAdapter);
			spnAssuntos.setSelection(assuntosAdapter.getPosition(emptyAssunto));
		} catch (QueryModelException ex) {
			ex.printStackTrace();
			Toast.makeText(
					this,
					getString(R.string.failed_loading_model_list,
							Assunto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
		}
	}

}
