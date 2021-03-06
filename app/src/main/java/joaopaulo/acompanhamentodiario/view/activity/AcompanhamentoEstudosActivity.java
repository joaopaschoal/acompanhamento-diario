package joaopaulo.acompanhamentodiario.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.AcompanhamentoBO;
import joaopaulo.acompanhamentodiario.business.bo.AcompanhamentoBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.AssuntoBO;
import joaopaulo.acompanhamentodiario.business.bo.AssuntoBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.DisciplinaBO;
import joaopaulo.acompanhamentodiario.business.bo.DisciplinaBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.EstudoBO;
import joaopaulo.acompanhamentodiario.business.bo.EstudoBOImpl;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.util.ParametersBusn;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;
import joaopaulo.acompanhamentodiario.persistence.model.Assunto;
import joaopaulo.acompanhamentodiario.persistence.model.Disciplina;
import joaopaulo.acompanhamentodiario.persistence.model.Estudo;
import joaopaulo.acompanhamentodiario.tools.UtilDate;
import joaopaulo.acompanhamentodiario.tools.UtilLanguage;
import joaopaulo.acompanhamentodiario.view.adapter.EstudoAdapter;

public class AcompanhamentoEstudosActivity extends BaseActivity {

	// ----- Attributes ----- //
	protected Acompanhamento acompanhamento;
    protected AcompanhamentoBO acompanhamentoBO;
    protected EstudoBO estudoBO;
    protected AssuntoBO assuntoBO;
    protected DisciplinaBO disciplinaBO;
    protected Calendar calendar;
	final SimpleDateFormat sdfUTC = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    protected ArrayAdapter<Disciplina> disciplinasAdapter;
    protected ArrayAdapter<Assunto> assuntosAdapter;
    protected EstudoAdapter estudosAdapter;
    protected Map<Integer, SubAssunto> dicIdxAndIdSubAssuntos;
    static final int DATE_DIALOG_ID = 9001;

	// ----- Events ----- //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acompanhamento_estudos);

		acompanhamentoBO = AcompanhamentoBOImpl.getInstance(this);
        estudoBO = EstudoBOImpl.getInstance(this);
        assuntoBO = AssuntoBOImpl.getInstance(this);
        disciplinaBO = DisciplinaBOImpl.getInstance(this);
        assuntosAdapter = new ArrayAdapter<Assunto>(this,android.R.layout.simple_spinner_item, new ArrayList<Assunto>());
        assuntosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dicIdxAndIdSubAssuntos = new HashMap<Integer, SubAssunto>();
        sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

		intializeScreen();
        bindEvents();

	}

    @Override
    protected void onStart() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Testa se foi passado um acomp para edição:
            int idAcomp = getIntent().getExtras().getInt("load_acompanhamento");
            if (idAcomp > 0) {
                try {
                    acompanhamentoBO.open();
                    this.acompanhamento = acompanhamentoBO.selectOneById(idAcomp);
                    acompanhamentoBO.close();
                } catch (QueryModelException e) {
                    e.printStackTrace();
                }
                fillScreenFromObject();
            } else {
                //Testa se foi passado um acomp da tela anterior:
                acompanhamento = (Acompanhamento) getIntent().getSerializableExtra("acompanhamento");
                if (acompanhamento != null) {
                    fillScreenFromObject();
                }
            }
        }
        super.onStart();
    }

    @Override
	protected void onResume() {
		super.onResume();
    }

    @Override
    protected Dialog onCreateDialog(int idDialog) {
        switch (idDialog) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {

                        // set selected date into textview
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        UtilDate.removeTimePart(calendar);
                        initializeEditDataReferenteToSpecificTime(calendar);
                    }
                }, year, month, day);
        }
        return null;
    }

    protected void selectDefaultDisciplina() {
        disciplinaBO.open();
        Disciplina disciplinaDefault = null;
        try {
            disciplinaDefault = disciplinaBO.selectOneById(ParametersBusn.ID_DISCIPLINA_DEFAULT);
        } catch (QueryModelException e) {
            e.printStackTrace();
        }
        if (disciplinaDefault != null) {
            Spinner spnDisciplinas = getDisciplinasSpinner();
            spnDisciplinas.setSelection(disciplinasAdapter.getPosition(disciplinaDefault));
        }
    }


	// ----- Public Methods ----- //

	public void intializeScreen() {
		clearScreen();
		initializeDate();
		//load spinners:
        loadDisciplinasSpinner();
        loadGridView();
	}

	public void clearScreen() {
		this.acompanhamento =  new Acompanhamento();
		EditText edtDataReferente = getEditTextDataReferente();
		edtDataReferente.getText().clear();
        clearSubAssuntos();
        assuntosAdapter.clear();
        EditText edtTempoEstudo = getEditTextTempoEstudo();
        edtTempoEstudo.getText().clear();
	}

    public LinearLayout inflateSubAssuntos() {
        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup container = this.findViewById(R.id.acompanhamento_estudos_linlyt_assuntos);
        return (LinearLayout) layoutInflater.inflate(R.layout.subassunto, container, false);
    }

    public void loadDisciplinasSpinner() {
        List<Disciplina> disciplinas = null;
        try {
            disciplinaBO.open();
            disciplinas = disciplinaBO.selectAll();
            disciplinasAdapter = new ArrayAdapter<Disciplina>(this,android.R.layout.simple_spinner_item, disciplinas);
            disciplinasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spnDisciplinas = getDisciplinasSpinner();
            spnDisciplinas.setAdapter(disciplinasAdapter);
            selectDefaultDisciplina();
        } catch (QueryModelException ex) {
            ex.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list,Disciplina.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
    }

    protected void loadGridView() {
        //binda a coleção de estudos do acomp ao adapter do gridview:
        estudosAdapter = new EstudoAdapter(this, acompanhamento.getEstudos());
        GridView gdvItens = getGridViewAssuntosEstudados();
        gdvItens.setAdapter(estudosAdapter);
        registerForContextMenu(gdvItens);
        if (acompanhamento != null) {
            loadEstudosIntoGridViewFromAcompanhamento(acompanhamento);
        }
        estudosAdapter.calcGridAssuntosEstudadosHeight(gdvItens);
    }

    private void loadEstudosIntoGridViewFromAcompanhamento(Acompanhamento acompanhamento) {
        try {
            estudoBO.open();
            assuntoBO.open();
            //O acompanhamento já esteja persistido no BD e sua lista de estudos esteja vazia?
            if (acompanhamento.getEstudos().size() == 0 && acompanhamento.getId() != null && acompanhamento.getId() > 0) {
                //sim -> pesquisa por possíveis estudos persistidos no BD deste acomp e se houver os inclui na coleção de estudos do acomp:
                List<Estudo> estudosJaExistentes = estudoBO.selectEstudosFromAcompanhamentoId(acompanhamento.getId());
                for (Estudo estudo : estudosJaExistentes) {
                    Assunto assunto = assuntoBO.selectOneById(estudo.getAssunto().getId());
                    estudo.setAssunto(assunto);
                    estudosAdapter.add(estudo);
                }
            }
        } catch (QueryModelException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list, Estudo.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        } finally {
            assuntoBO.close();
            estudoBO.close();
        }
    }

    public void fillSpnAssuntosFromIdDisciplina(long idDisciplina) {
        assuntosAdapter.clear();
        if (idDisciplina <= 0) {
            return;
        }

        List<Assunto> assuntos = null;
        try {
            assuntoBO.open();
            assuntos = assuntoBO.selectRootAssuntosByDisciplinaId((int)idDisciplina);
            assuntosAdapter.clear();
            assuntosAdapter.addAll(assuntos);
            Spinner spnAssuntos = getAssuntosSpinner();
            spnAssuntos.setAdapter(assuntosAdapter);
        } catch (QueryModelException ex) {
            ex.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list,Disciplina.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
    }

    // ----- View Getters ----- //
    public LinearLayout getRootLinearLayout() {
        return (LinearLayout)findViewById(R.id.acompanhamento_estudos_main_layout);
    }

    public LinearLayout getSubAssuntosLinearLayout() {
        return (LinearLayout)findViewById(R.id.acompanhamento_estudos_linlyt_subassuntos);
    }

    public Spinner getDisciplinasSpinner() {
        return (Spinner)findViewById(R.id.acompanhamento_estudos_spn_disciplinas);
    }

    public Spinner getAssuntosSpinner() {
        return (Spinner)findViewById(R.id.acompanhamento_estudos_spn_assuntos);
    }

    public EditText getEditTextDataReferente() {
        return (EditText)findViewById(R.id.acompanhamento_estudos_edt_data_referente);
    }

    public EditText getEditTextTempoEstudo() {
        return (EditText)findViewById(R.id.acompanhamento_estudos_edt_tempo_estudo);
    }

    public GridView getGridViewAssuntosEstudados() {
        return (GridView)findViewById(R.id.acompanhamento_estudos_gdv_assuntos_estudados);
    }

	// ----- Private Methods ----- //
    private void bindEvents() {
        //Btn Avançar Click
        Button btnAvancarParaTrabalhos = findViewById(R.id.acompanhamento_estudos_btn_avancar);
        btnAvancarParaTrabalhos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fillObjectFromScreen();

                Intent itNext = new Intent(AcompanhamentoEstudosActivity.this, AcompanhamentoTrabalhosActivity.class);
                itNext.putExtra("acompanhamento", acompanhamento);
                startActivity(itNext);
            }
        });

        //EdtText Data Focus
        EditText edtDataReferente = getEditTextDataReferente();
        edtDataReferente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(DATE_DIALOG_ID);
                }
            }
        });

        //Spinner Disciplinas Change:
        final Spinner spnDisciplinas = getDisciplinasSpinner();
        spnDisciplinas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                fillSpnAssuntosFromIdDisciplina(disciplinasAdapter.getItem(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                assuntosAdapter.clear();
            }
        });

        //Button expandirSubAssuntos Click
        ImageButton btnExpandirSubAssuntos = findViewById(R.id.acompanhamento_estudos_btn_expandir_sub_assuntos);
        btnExpandirSubAssuntos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createSubAssunto(assuntosAdapter.getItem((int) getAssuntosSpinner().getSelectedItemId()).getId());
            }
        });

        //ImageButton adicionar Estudo:
        ImageButton btnAdicionarEstudo = findViewById(R.id.acompanhamento_estudos_btn_adicionar_estudo);
        btnAdicionarEstudo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Estudo estudo = new Estudo();
                EditText edtTempoEstudo = getEditTextTempoEstudo();

                Assunto assunto = fillAssuntoFromScreen(null);
                estudo.setTempoMins(UtilLanguage.strToInt(edtTempoEstudo.getText().toString()));
                estudo.setAssunto(assunto);
                estudo.setAcompanhamento(acompanhamento);
                estudosAdapter.add(estudo);

                edtTempoEstudo.setText("");
            }
        });

        //Spinner Assuntos Change:
        final Spinner spnAssuntos = getAssuntosSpinner();
        spnAssuntos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                clearSubAssuntos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {  }
        });
    }

    private void createSubAssunto(int idAssunto) {
        SubAssunto subAssunto = new SubAssunto(idAssunto);
        ArrayAdapter<Assunto> adapter = getAssuntosAdapterByAssuntoPai(idAssunto);

        if (adapter.getCount() == 0) {
            Toast.makeText(this, R.string.acompanhamento_estudos_assunto_doesnt_contains_subassuntos, Toast.LENGTH_LONG).show();
            return;
        }

        LinearLayout subAssuntosLayout = inflateSubAssuntos();

        LinearLayout rootLinearLayout = findViewById(R.id.acompanhamento_estudos_linlyt_subassuntos);
        subAssunto.layoutContainer = subAssuntosLayout;
        rootLinearLayout.addView(subAssunto.layoutContainer);

        subAssunto.spnSubAssuntos = subAssuntosLayout.findViewById(R.id.acompanhamento_estudos_spn_assuntos_2);
        subAssunto.spnSubAssuntos.setAdapter(adapter);
        subAssunto.spnSubAssuntos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int startingIndexKey = calcularIndiceParaLimparSubAssuntos((Assunto) adapterView.getSelectedItem());
                clearSubAssuntos(startingIndexKey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        subAssunto.btnExpandirSubAssuntos = subAssuntosLayout.findViewById(R.id.acompanhamento_estudos_btn_expandir_sub_assuntos_2);
        final int dicPosAssuntoToExpand = dicIdxAndIdSubAssuntos.size();
        subAssunto.btnExpandirSubAssuntos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SubAssunto correspondingSubAssunto = dicIdxAndIdSubAssuntos.get(dicPosAssuntoToExpand);
                int selectedId = ((Assunto) correspondingSubAssunto.spnSubAssuntos.getSelectedItem()).getId();
                createSubAssunto(selectedId);
            }
        });
        dicIdxAndIdSubAssuntos.put(dicIdxAndIdSubAssuntos.size(), subAssunto);
    }

    private Assunto fillAssuntoFromScreen(Assunto assunto) {
        if (assunto == null) {
            assunto = new Assunto();
        }

        long selectedId;
        String nomeAssunto;
        if (dicIdxAndIdSubAssuntos.size() == 0) {
            //Caso não hajam subassuntos, obtém o assunto pai:
            selectedId = ((Assunto)getAssuntosSpinner().getSelectedItem()).getId();
            nomeAssunto = getAssuntosSpinner().getSelectedItem().toString();
        } else {
            //Do contrário, obtém o último subassunto expandido:
            selectedId = ((Assunto) dicIdxAndIdSubAssuntos.get(dicIdxAndIdSubAssuntos.size() - 1).spnSubAssuntos.getSelectedItem()).getId();
            nomeAssunto = dicIdxAndIdSubAssuntos.get(dicIdxAndIdSubAssuntos.size() - 1).spnSubAssuntos.getSelectedItem().toString();
        }

        assunto.setId((int)selectedId);
        assunto.setNome(nomeAssunto);
        return assunto;
    }


    private int calcularIndiceParaLimparSubAssuntos(Assunto assuntoSelecionado) {
        int idxSubAssuntoClear = 0;
        for (Map.Entry<Integer, SubAssunto> entry : dicIdxAndIdSubAssuntos.entrySet()) {
            if (entry.getValue().idAssuntoPai == assuntoSelecionado.getAssuntoPai().getId()) {
                idxSubAssuntoClear = entry.getKey()+1;
            }
        }
        return idxSubAssuntoClear;
    }

    private ArrayAdapter<Assunto> getAssuntosAdapterByAssuntoPai(int idAssuntoPai) {
        ArrayAdapter<Assunto> assuntoArrayAdapter = null;
        try {
            List<Assunto> subAssuntos = AssuntoBOImpl.getInstance(this).selectSubAssuntos(idAssuntoPai);
            assuntoArrayAdapter = new ArrayAdapter<Assunto>(this, android.R.layout.simple_spinner_item, subAssuntos);
            assuntoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } catch (QueryModelException e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle("Falha ao Buscar Sub-Assuntos")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Erro ao buscar os sub-assuntos: \""+ e.getMessage() +"\"")
                    .show();
        }
        return assuntoArrayAdapter;
    }

    private void clearSubAssuntos() {
        clearSubAssuntos(0);
    }

    private void clearSubAssuntos(int startingIndexKey) {
        for (int keyToRemove = startingIndexKey; keyToRemove < dicIdxAndIdSubAssuntos.size(); keyToRemove++) {
            SubAssunto subAssunto = dicIdxAndIdSubAssuntos.get(keyToRemove);

            LinearLayout linearLayout = getSubAssuntosLinearLayout();
            linearLayout.removeView(subAssunto.layoutContainer);
            dicIdxAndIdSubAssuntos.remove(subAssunto);
        }
    }

	private void fillObjectFromScreen() {
		EditText edtDataReferente = getEditTextDataReferente();

		if (acompanhamento == null) {
			acompanhamento = new Acompanhamento();
		}
        Date d = new Date();
        long t = d.getTime();
        Timestamp tsp = new Timestamp(t);

		acompanhamento.setDataRegistro(tsp);
		acompanhamento.setDataAcompanhamento(getDateFromDateEdit(edtDataReferente));

        //A colecao de estudos do acomp não precisa ser preenchida pois está bindada diretamente ao EstudosAdapter
	}
	
	private void fillScreenFromObject() {
		EditText edtDataReferente = getEditTextDataReferente();
		edtDataReferente.setText(UtilDate.dateToShortBrStringGmtTime(acompanhamento.getDataAcompanhamento().getTime(), false));
        loadGridView();
	}
	
	private void initializeDate() {
        Date nextEmptyDate = null;
        try {
            nextEmptyDate = acompanhamentoBO.selectNextEmptyDate();

            if (nextEmptyDate != null) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTime(nextEmptyDate);
            } else {
                initializeDateByCurrentDay();
            }
            initializeEditDataReferenteToSpecificTime(calendar);
        } catch (QueryModelException e) {
            e.printStackTrace();
        }
	}

    private void initializeDateByCurrentDay() {
        long MILLIS_PER_HOUR = 60 * 60 * 1000;
        long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
        long MILLIS_3AM_GMT = 3 * MILLIS_PER_HOUR;
        long MILLIS_8AM_GMT = 8 * MILLIS_PER_HOUR;

        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")); //Obtenho uma instância em UTC

        //Obtém-se a fração referente à hora do dia a partir do resto da divisão da qtde de milis num dia:
        long timePortionUTC = calendar.getTime().getTime() % MILLIS_PER_DAY;

        //Lembrando que java.util.Date está sempre em UTC (e portanto fuso GMT)...
        //Quero descobrir se o timePortion equivale a um período entre 0h e 5h no Brasil, portanto pergunto:
        //A fração de tempo é maior que 3h GMT e inferior a 8h GMT?
        if (timePortionUTC > MILLIS_3AM_GMT && timePortionUTC < MILLIS_8AM_GMT) {
            //Sim... sugere o dia anterior:
            calendar.add(Calendar.DATE, -1);
        }
    }
	
	private void initializeEditDataReferenteToSpecificTime(Calendar calendar) {
		EditText edtDataReferente = getEditTextDataReferente();
		edtDataReferente.setText(sdfUTC.format(calendar.getTime()));
	}
	
	private java.sql.Date getDateFromDateEdit(EditText dateEditText) {
		String[] strDataRefAux = dateEditText.getText().toString().split("/");
		//java.sql.Date dateFromEdit = new java.sql.Date(Integer.parseInt(strDataRefAux[2].toString()), Integer.parseInt(strDataRefAux[1].toString()), Integer.parseInt(strDataRefAux[0].toString()));
		Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.set(Integer.parseInt(strDataRefAux[2].toString()), Integer.parseInt(strDataRefAux[1].toString())-1, Integer.parseInt(strDataRefAux[0].toString()));
        UtilDate.removeTimePart(cal);
		java.sql.Date dateFromEdit = new java.sql.Date(cal.getTime().getTime());
		return dateFromEdit;
	}

    private class SubAssunto {
        public SubAssunto(int idAssuntoPai) {
            this.idAssuntoPai = idAssuntoPai;
        }
        public int idAssuntoPai;
        public LinearLayout layoutContainer;
        public Spinner spnSubAssuntos;
        public ImageButton btnExpandirSubAssuntos;
    }
}
