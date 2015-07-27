package joaopaulo.acompdiario.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.AcompanhamentoBO;
import joaopaulo.acompdiario.business.bo.AcompanhamentoBOImpl;
import joaopaulo.acompdiario.business.bo.ProjetoBO;
import joaopaulo.acompdiario.business.bo.ProjetoBOImpl;
import joaopaulo.acompdiario.business.bo.TrabalhoBO;
import joaopaulo.acompdiario.business.bo.TrabalhoBOImpl;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.Acompanhamento;
import joaopaulo.acompdiario.persistence.model.Projeto;
import joaopaulo.acompdiario.persistence.model.Trabalho;
import joaopaulo.acompdiario.tools.UtilLanguage;
import joaopaulo.acompdiario.view.adapter.TrabalhoAdapter;

public class AcompanhamentoTrabalhosActivity extends BaseActivity {

	// ----- Attributes ----- //
	protected Acompanhamento acompanhamento;
    protected AcompanhamentoBO acompanhamentoBO;
    protected TrabalhoBO trabalhoBO;
    protected ProjetoBO projetoBO;

    protected ArrayAdapter<Projeto> projetosAdapter;
    protected TrabalhoAdapter trabalhosAdapter;

	// ----- Events ----- //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acompanhamento_trabalhos);

		acompanhamentoBO = AcompanhamentoBOImpl.getInstance(this);
        trabalhoBO = TrabalhoBOImpl.getInstance(this);
        projetoBO = ProjetoBOImpl.getInstance(this);

        acompanhamentoBO.open();
        trabalhoBO.open();
        projetoBO.open();

        projetosAdapter = new ArrayAdapter<Projeto>(this,android.R.layout.simple_spinner_item, new ArrayList<Projeto>());
        projetosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

    @Override
    protected void onStart() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Testa se foi passado um acomp da tela anterior:
            acompanhamento = (Acompanhamento) getIntent().getSerializableExtra("acompanhamento");
            if (acompanhamento != null) {
                fillScreenFromObject();
            }
        }
        super.onStart();

        intializeScreen();
        bindEvents();
    }

    @Override
	protected void onResume() {
		super.onResume();
        acompanhamentoBO.open();
        trabalhoBO.open();
        projetoBO.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        acompanhamentoBO.close();
        trabalhoBO.close();
        projetoBO.close();
    }

    // ----- Public Methods ----- //

	public void intializeScreen() {
		clearScreen();
		//load spinners:
        loadProjetosSpinner();
        loadGridView();
	}

	public void clearScreen() {
        projetosAdapter.clear();
        //TODO: limpar gridview de projetos trabalhados
	}

    public void loadProjetosSpinner() {
        List<Projeto> projetos = null;
        try {
            projetos = projetoBO.selectAll();
            projetosAdapter = new ArrayAdapter<Projeto>(this,android.R.layout.simple_spinner_item, projetos);
            projetosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spnProjetos = getProjetosSpinner();
            spnProjetos.setAdapter(projetosAdapter);
        } catch (QueryModelException ex) {
            ex.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list,Projeto.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
    }

    protected void loadGridView() {
        //binda a coleção de trabalhos do acomp ao adapter do gridview:
        trabalhosAdapter = new TrabalhoAdapter(acompanhamento.getTrabalhos(), this);
        GridView gdvItens = getGridViewProjetosTrabalhados();
        gdvItens.setAdapter(trabalhosAdapter);
        registerForContextMenu(gdvItens);
        if (acompanhamento != null) {
            loadTrabalhosIntoGridViewFromAcompanhamento(acompanhamento);
        }
        trabalhosAdapter.calcGridViewHeight(gdvItens);
    }

    private void loadTrabalhosIntoGridViewFromAcompanhamento(Acompanhamento acompanhamento) {
        try {
            //O acompanhamento já esteja persistido no BD e sua lista de estudos esteja vazia?
            if (acompanhamento.getTrabalhos().size() == 0 && acompanhamento.getId() != null && acompanhamento.getId() > 0) {
                //sim -> pesquisa por possíveis trabalhos persistidos no BD deste acomp e se houver os inclui na coleção de trabalhos do acomp:
                List<Trabalho> trabalhosJaExistentes = trabalhoBO.selectTrabalhosFromAcompanhamentoId(acompanhamento.getId());
                for (Trabalho trabalho : trabalhosJaExistentes) {
                    Projeto projeto = projetoBO.selectOneById(trabalho.getProjeto().getId());
                    trabalho.setProjeto(projeto);
                    trabalhosAdapter.add(trabalho);
                }
            }
        } catch (QueryModelException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list, Trabalho.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
    }

    // ----- View Getters ----- //


    public Spinner getProjetosSpinner() {
        return (Spinner)findViewById(R.id.acompanhamento_trabalhos_spn_projetos);
    }

    public EditText getEditTextTempoTrabalhado() {
        return (EditText)findViewById(R.id.acompanhamento_trabalhos_edt_tempo_trabalhado);
    }

    public GridView getGridViewProjetosTrabalhados() {
        return (GridView)findViewById(R.id.acompanhamento_trabalhos_gdv_projetos_trabalhados);
    }

	// ----- Private Methods ----- //
    private void bindEvents() {
        //Btn Avançar Click
        Button btnAvancarParaExercicios = (Button)findViewById(R.id.acompanhamento_trabalhos_btn_avancar);
        btnAvancarParaExercicios.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itNext = new Intent(AcompanhamentoTrabalhosActivity.this, AcompanhamentoExerciciosActivity.class);
                itNext.putExtra("acompanhamento", acompanhamento);
                startActivity(itNext);
            }
        });

        //Btn Voltar Click
        Button btnVoltarParaEstudos = (Button)findViewById(R.id.acompanhamento_trabalhos_btn_voltar);
        btnVoltarParaEstudos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itNext = new Intent(AcompanhamentoTrabalhosActivity.this, AcompanhamentoEstudosActivity.class);
                itNext.putExtra("acompanhamento", acompanhamento);
                startActivity(itNext);
            }
        });

        //ImageButton adicionar Trabalho:
        ImageButton btnAdicionarTrabalho = (ImageButton)findViewById(R.id.acompanhamento_trabalhos_btn_adicionar_trabalho);
        btnAdicionarTrabalho.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Trabalho trabalho = new Trabalho();
                EditText edtTempoTrabalhado = getEditTextTempoTrabalhado();

                trabalho.setTempoMins(UtilLanguage.strToInt(edtTempoTrabalhado.getText().toString()));
                trabalho.setProjeto((Projeto) getProjetosSpinner().getSelectedItem());
                trabalho.setAcompanhamento(acompanhamento);

                trabalhosAdapter.add(trabalho);

                edtTempoTrabalhado.setText("");
            }
        });
    }
	
	private void fillScreenFromObject() {
        loadGridView();
	}

}
