package joaopaulo.acompdiario.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.DonoProdutoBO;
import joaopaulo.acompdiario.business.bo.DonoProdutoBOImpl;
import joaopaulo.acompdiario.business.bo.NaturezaProjetoBO;
import joaopaulo.acompdiario.business.bo.NaturezaProjetoBOImpl;
import joaopaulo.acompdiario.business.bo.ProjetoBO;
import joaopaulo.acompdiario.business.bo.ProjetoBOImpl;
import joaopaulo.acompdiario.business.bo.ProjetoTecnologiaBO;
import joaopaulo.acompdiario.business.bo.ProjetoTecnologiaBOImpl;
import joaopaulo.acompdiario.business.bo.TecnologiaBO;
import joaopaulo.acompdiario.business.bo.TecnologiaBOImpl;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.SaveModelException;
import joaopaulo.acompdiario.persistence.model.DonoProduto;
import joaopaulo.acompdiario.persistence.model.NaturezaProjeto;
import joaopaulo.acompdiario.persistence.model.Projeto;
import joaopaulo.acompdiario.persistence.model.ProjetoTecnologia;
import joaopaulo.acompdiario.persistence.model.Tecnologia;
import joaopaulo.acompdiario.view.adapter.ProjetoTecnologiaAdapter;

public class NewProjetoActivity extends BaseActivity {

	// ----- Attributes ----- //
	private ArrayAdapter<NaturezaProjeto> naturezasProjetoAdapter;
    private ArrayAdapter<DonoProduto> donosProdutoAdapter;
    private ProjetoTecnologiaAdapter projetoTecnologiaAdapter;
    private ArrayAdapter<Tecnologia> tecnologiasAdapter;
	private Projeto projeto;
	private ProjetoBO bo;
    private ProjetoTecnologiaBO projetoTecnologiaBO;


    // ----- Events ----- //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_projeto);
		
		bo = ProjetoBOImpl.getInstance(this);
        projetoTecnologiaBO = ProjetoTecnologiaBOImpl.getInstance(this);

        bo.open();
        projetoTecnologiaBO.open();

		bindEvents();
        intializeScreen();
	}

    @Override
    protected void onStart() {
        //intializeScreen();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Testa se foi passado um acomp para edicao:
            int idProjeto = getIntent().getExtras().getInt("id_projeto_to_edit");
            if (idProjeto > 0) {
                try {
                    this.projeto = bo.selectOneById(idProjeto);
                    fillScreenFromObject();
                } catch (QueryModelException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onStart();
    }

    @Override
	protected void onResume() {
		bo.open();
        projetoTecnologiaBO.open();
        super.onResume();
	}

	@Override
	protected void onPause() {
		bo.close();
        projetoTecnologiaBO.close();
		super.onPause();
	}

	// ----- Public Methods ----- //

	public void intializeScreen() {
		clearScreen();
		loadNaturezasProjetoSpinner();
        loadDonosProdutoSpinner();
        loadTecnologiasSpinner();
        loadGridView();
	}

	public void clearScreen() {
		this.projeto = new Projeto();
		EditText edtName = (EditText)findViewById(R.id.new_projeto_edt_name_projeto);
		edtName.getText().clear();
        CheckBox chkTecnologiaPrincipal = (CheckBox)findViewById(R.id.new_projeto_chk_principal_tecnologia);
        chkTecnologiaPrincipal.setChecked(true);

	}

    public GridView getGridViewTecnologiasProjeto() {
        return (GridView)findViewById(R.id.new_projeto_gdv_tecnologias_projeto);
    }

	// ----- Private Methods ----- //

    protected void bindEvents() {

        ImageButton btnIncluirTecnologia = (ImageButton)findViewById(R.id.new_projeto_btn_inlcuir_tecnologia);
        btnIncluirTecnologia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjetoTecnologia projetoTecnologia = new ProjetoTecnologia();

                Spinner spnTecnologias = (Spinner) findViewById(R.id.new_projeto_spn_tecnologias);
                CheckBox chkFlgTecnologiaPrincipal = (CheckBox) findViewById(R.id.new_projeto_chk_principal_tecnologia);

                projetoTecnologia.setProjeto(NewProjetoActivity.this.projeto);
                projetoTecnologia.setTecnologia((Tecnologia) spnTecnologias.getSelectedItem());
                projetoTecnologia.setFlgTecnologiaPrincipal(chkFlgTecnologiaPrincipal.isChecked());

                projetoTecnologiaAdapter.add(projetoTecnologia);

                spnTecnologias.setSelection(0);
                chkFlgTecnologiaPrincipal.setChecked(false);
            }
        });

        Button btnSalvar = (Button)findViewById(R.id.new_projeto_btn_save_projeto);
        btnSalvar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                projeto = fillObjectFromScreen(projeto);

                try {
                    if (projeto.getId() != null && projeto.getId() > 0) {
                        List<ProjetoTecnologia> tecnologiasProjeto = projetoTecnologiaBO.selectTecnologiasFromProjeto(projeto.getId());
                        for (ProjetoTecnologia projetoTecnologia : tecnologiasProjeto) {
                            projetoTecnologiaBO.delete(projetoTecnologia);
                        }
                    }

                    bo.save(projeto);

                    for (ProjetoTecnologia projetoTecnologia : projeto.getTecnologiasProjeto()) {
                        projetoTecnologia.setId(null);
                        projetoTecnologiaBO.save(projetoTecnologia);
                    }

                    Toast.makeText(NewProjetoActivity.this, getString(R.string.model_successful_saved, Projeto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
                    navigateToListProjects();
                } catch (SaveModelException ex) {
                    new AlertDialog.Builder(NewProjetoActivity.this)
                            .setTitle("Falha ao Salvar Registro")
                            .setMessage(ex.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (QueryModelException ex) {
                    new AlertDialog.Builder(NewProjetoActivity.this)
                            .setTitle("Falha ao Atualizar Tecnologias do Projeto")
                            .setMessage(ex.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        Button btnVoltar = (Button)findViewById(R.id.new_projeto_btn_voltar);
        btnVoltar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToListProjects();
            }
        });
    }

    private void saveTecnologiasProjeto(Projeto projeto) throws SaveModelException, QueryModelException {



    }

    private void navigateToListProjects() {
        Intent itListProjetos = new Intent(NewProjetoActivity.this, ListProjetosActivity.class);
        startActivity(itListProjetos);
    }

    protected void loadGridView() {
        //binda a colecao de ProjetoTecnologia do projeto ao adapter do gridview:
        projetoTecnologiaAdapter = new ProjetoTecnologiaAdapter(projeto.getTecnologiasProjeto(), this);
        GridView gdvItens = getGridViewTecnologiasProjeto();
        gdvItens.setAdapter(projetoTecnologiaAdapter);
        registerForContextMenu(gdvItens);
        loadTecnologiasIntoGridViewFromProjeto(projeto);
        projetoTecnologiaAdapter.calcGridViewHeight(gdvItens);
    }

    protected Projeto fillObjectFromScreen(Projeto projeto) {
        if (projeto == null) {
            projeto = new Projeto();
        }
        Timestamp utcTimeNow = new Timestamp(new Date().getTime());
        projeto.setDataCriacao(utcTimeNow);

		EditText edtNome = (EditText)findViewById(R.id.new_projeto_edt_name_projeto);
        Spinner spnNaturezasProjeto = (Spinner)findViewById(R.id.new_projeto_spn_naturezas_projeto);
        Spinner spnDonosProduto = (Spinner)findViewById(R.id.new_projeto_spn_dono_do_produto);

		if (projeto == null) {
			projeto = new Projeto();
		}
		projeto.setNome(edtNome.getText().toString().toUpperCase(Locale.getDefault()));
		projeto.setNaturezaProjeto((NaturezaProjeto) spnNaturezasProjeto.getSelectedItem());
        projeto.setDonoProduto((DonoProduto) spnDonosProduto.getSelectedItem());
		return projeto;
	}

	protected void fillScreenFromObject() {
		EditText edtNome = (EditText)findViewById(R.id.new_projeto_edt_name_projeto);
		Spinner spnNaturezasProjeto = (Spinner)findViewById(R.id.new_projeto_spn_naturezas_projeto);
        Spinner spnDonosProduto = (Spinner)findViewById(R.id.new_projeto_spn_dono_do_produto);

		edtNome.setText(this.projeto.getNome());
		spnNaturezasProjeto.setSelection(naturezasProjetoAdapter.getPosition(this.projeto.getNaturezaProjeto()));
        spnDonosProduto.setSelection(donosProdutoAdapter.getPosition(this.projeto.getDonoProduto()));
        loadGridView();
	}


	protected void loadNaturezasProjetoSpinner() {
		NaturezaProjetoBO naturezaProjetoBO = NaturezaProjetoBOImpl.getInstance(this);
		List<NaturezaProjeto> naturezasProjeto = null;
		try {
            naturezaProjetoBO.open();
            naturezasProjeto = naturezaProjetoBO.selectAll();
			naturezasProjetoAdapter = new ArrayAdapter<NaturezaProjeto>(this,android.R.layout.simple_spinner_item, naturezasProjeto);
            naturezasProjetoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spnNaturezasProjeto = (Spinner)findViewById(R.id.new_projeto_spn_naturezas_projeto);
            spnNaturezasProjeto.setAdapter(naturezasProjetoAdapter);
		} catch (QueryModelException ex) {
			ex.printStackTrace();
			Toast.makeText(this, getString(R.string.failed_loading_model_list,NaturezaProjeto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
		}
	}

    protected void loadDonosProdutoSpinner() {
        DonoProdutoBO donoProdutoBO = DonoProdutoBOImpl.getInstance(this);
        List<DonoProduto> donosProduto = null;
        try {
            donoProdutoBO.open();
            donosProduto = donoProdutoBO.selectAll();
            donosProdutoAdapter = new ArrayAdapter<DonoProduto>(this,android.R.layout.simple_spinner_item, donosProduto);
            donosProdutoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spnDonosProduto = (Spinner)findViewById(R.id.new_projeto_spn_dono_do_produto);
            spnDonosProduto.setAdapter(donosProdutoAdapter);
        } catch (QueryModelException ex) {
            ex.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list,DonoProduto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
    }

    protected void loadTecnologiasSpinner() {
        TecnologiaBO tecnologiaBO = TecnologiaBOImpl.getInstance(this);
        List<Tecnologia> tecnologias = null;
        try {
            tecnologiaBO.open();
            tecnologias = tecnologiaBO.selectAll();
            tecnologiasAdapter = new ArrayAdapter<Tecnologia>(this,android.R.layout.simple_spinner_item, tecnologias);
            tecnologiasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spnTecnologias = (Spinner)findViewById(R.id.new_projeto_spn_tecnologias);
            spnTecnologias.setAdapter(tecnologiasAdapter);
        } catch (QueryModelException ex) {
            ex.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list,Tecnologia.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
    }

    protected void loadTecnologiasIntoGridViewFromProjeto(Projeto projeto) {
        try {
            //O projeto ja esteja persistido no BD e sua lista de estudos esteja vazia?
            if (projeto.getTecnologiasProjeto().size() == 0 && projeto.getId() != null && projeto.getId() > 0) {
                //sim -> pesquisa por possiveis tecnologias persistidas no BD deste projeto e se houver os inclui na colecao:
                List<ProjetoTecnologia> tecnologiasDoProjetoJaExistentes = projetoTecnologiaBO.selectTecnologiasFromProjeto(projeto.getId());
                for (ProjetoTecnologia projetoTecnologia : tecnologiasDoProjetoJaExistentes) {
                    projetoTecnologiaAdapter.add(projetoTecnologia);
                }
            }
        } catch (QueryModelException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list, Tecnologia.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
    }

}