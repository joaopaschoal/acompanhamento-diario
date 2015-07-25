package joaopaulo.acompdiario.view.activity;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.AcompanhamentoBO;
import joaopaulo.acompdiario.business.bo.AcompanhamentoBOImpl;
import joaopaulo.acompdiario.business.bo.EstudoBO;
import joaopaulo.acompdiario.business.bo.EstudoBOImpl;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.SaveModelException;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAO;
import joaopaulo.acompdiario.persistence.dao.util.UtilDAOImpl;
import joaopaulo.acompdiario.persistence.model.Acompanhamento;
import joaopaulo.acompdiario.persistence.model.Estudo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class AcompanhamentoExerciciosActivity extends BaseActivity {

	// ----- Attributes ----- //
	private Acompanhamento acompanhamento;
	private AcompanhamentoBO acompanhamentoBO;
    private EstudoBO estudoBO;
	
	// ----- Events ----- //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acompanhamento_exercicios);

		acompanhamentoBO = AcompanhamentoBOImpl.getInstance(this);
        estudoBO = EstudoBOImpl.getInstance(this);

		intializeScreen();
		
		Button btnSaveAcompanhamento = (Button)findViewById(R.id.acompanhamento_btn_save);
		btnSaveAcompanhamento.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                acompanhamento = fillObjectFromScreen(acompanhamento);

                try {
                    //TODO: Implementar operações dentro de uma transação
                    acompanhamentoBO.open();
                    estudoBO.open();

                    //Trate-se de um acompanhamento previamente salvo?
                    if (acompanhamento.getId() != null && acompanhamento.getId() > 0) {
                        //Sim -> obtém e remove estudos antigos para registrar os novos:
                        List<Estudo> estudosAnteriores = estudoBO.selectEstudosFromAcompanhamento(acompanhamento.getId());
                        for (Estudo estudoAnterior : estudosAnteriores) {
                            estudoBO.delete(estudoAnterior);
                        }
                    }

                    acompanhamentoBO.save(acompanhamento);

                    for (Estudo estudo : acompanhamento.getEstudos()) {
                        estudoBO.save(estudo);
                    }
                } catch (SaveModelException ex) {
                    new AlertDialog.Builder(AcompanhamentoExerciciosActivity.this)
                            .setTitle("Falha ao Salvar Acompanhamento")
                            .setMessage(ex.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (QueryModelException ex) {
                    new AlertDialog.Builder(AcompanhamentoExerciciosActivity.this)
                            .setTitle("Falha ao Verificar Existência de Estudos anteriores para este acompanhamento")
                            .setMessage(ex.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } finally {
                    acompanhamentoBO.close();
                    estudoBO.close();
                }
                Toast.makeText(AcompanhamentoExerciciosActivity.this, getString(R.string.model_successful_saved, "Acompanhamento"), Toast.LENGTH_LONG).show();
                Intent itPagLista = new Intent(AcompanhamentoExerciciosActivity.this, ListarAcompanhamentosActivity.class);
                startActivity(itPagLista);
			}
		});

        Button btnVoltar = (Button)findViewById(R.id.acompanhamento_exercicios_btn_voltar);
        btnVoltar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itNext = new Intent(AcompanhamentoExerciciosActivity.this, AcompanhamentoEstudosActivity.class);
                itNext.putExtra("acompanhamento", acompanhamento);
                startActivity(itNext);
            }
        });
	}

    @Override
    protected void onStart() {
        super.onStart();
//        acompanhamentoBO.open();
//        estudoBO.open();

        //Testa se foi passado um acomp para edição:
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            acompanhamento = (Acompanhamento)getIntent().getSerializableExtra("acompanhamento");
            if (acompanhamento == null) { //Testar se neste ponto o acomp.getEstudos está vazio
                Toast.makeText(AcompanhamentoExerciciosActivity.this, "Falha ao receber os dados preenchidos na tela anterior.", Toast.LENGTH_LONG).show();
            }
            fillScreenFromObject(acompanhamento);
        }
    }

    // ----- Public Methods ----- //
	
	public void intializeScreen() {
		clearScreen();
	}
	
	public void clearScreen() {
		EditText edtTempoAerobica = getEditTextTempoAerobica();
		EditText edtTempoMusculacao = getEditTextTempoMusculacao();
		CheckBox chkFlgExcUr = getCheckBoxExUr();
		
		edtTempoAerobica.getText().clear();
		edtTempoMusculacao.getText().clear();
		chkFlgExcUr.setChecked(false);
	}
	
	
	// ----- Private Methods ----- //
	
	protected Acompanhamento fillObjectFromScreen(Acompanhamento acompanhamento) {
		EditText edtTempoAerobica = getEditTextTempoAerobica();
		EditText edtTempoMusculacao = getEditTextTempoMusculacao();
		CheckBox chkFlgExcUr = getCheckBoxExUr();
		
		if (acompanhamento == null) {
			acompanhamento = new Acompanhamento();
		}
		
		int periodoAerobica = Integer.valueOf(edtTempoAerobica.getText().toString().trim().length() > 0 ? edtTempoAerobica.getText().toString().trim() : "0");
		int periodoMusculacao = Integer.valueOf(edtTempoMusculacao.getText().toString().trim().length() > 0 ? edtTempoMusculacao.getText().toString().trim() : "0");
		
		acompanhamento.setPeriodoAerobica(periodoAerobica);
		acompanhamento.setPeriodoMusculacao(periodoMusculacao);
		acompanhamento.setFlgExUr(chkFlgExcUr.isChecked());
		return acompanhamento;
	}
	
	protected void fillScreenFromObject(Acompanhamento acompanhamento) {
		EditText edtTempoAerobica = getEditTextTempoAerobica();
		EditText edtTempoMusculacao = getEditTextTempoMusculacao();
		CheckBox chkFlgExcUr = getCheckBoxExUr();

		edtTempoAerobica.setText(String.valueOf(acompanhamento.getPeriodoAerobica()));
		edtTempoMusculacao.setText(String.valueOf(acompanhamento.getPeriodoMusculacao()));
		chkFlgExcUr.setChecked(acompanhamento.getFlgExUr());
	}

    // ----- View Getters ----- //
    public EditText getEditTextTempoAerobica() {
        return (EditText)findViewById(R.id.acompanhamento_exercicios_edt_tempo_aerobica);
    }

    public EditText getEditTextTempoMusculacao() {
        return (EditText)findViewById(R.id.acompanhamento_exercicios_edt_tempo_musculacao);
    }

    public CheckBox getCheckBoxExUr() {
        return (CheckBox)findViewById(R.id.acompanhamento_exercicios_chk_flg_ex_ur);
    }
}
