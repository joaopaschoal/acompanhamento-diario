package joaopaulo.acompanhamentodiario.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.AssuntoBO;
import joaopaulo.acompanhamentodiario.business.bo.AssuntoBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.EstudoBO;
import joaopaulo.acompanhamentodiario.business.bo.EstudoBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.ProjetoBO;
import joaopaulo.acompanhamentodiario.business.bo.ProjetoBOImpl;
import joaopaulo.acompanhamentodiario.business.bo.TrabalhoBO;
import joaopaulo.acompanhamentodiario.business.bo.TrabalhoBOImpl;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;
import joaopaulo.acompanhamentodiario.persistence.model.Assunto;
import joaopaulo.acompanhamentodiario.persistence.model.Estudo;
import joaopaulo.acompanhamentodiario.persistence.model.Projeto;
import joaopaulo.acompanhamentodiario.persistence.model.Trabalho;
import joaopaulo.acompanhamentodiario.tools.UtilDate;
import joaopaulo.acompanhamentodiario.view.activity.AcompanhamentoEstudosActivity;

public class AcompanhamentoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Acompanhamento> items;
	
	public AcompanhamentoAdapter(Context context, List<Acompanhamento> items) {
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Acompanhamento getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ((Acompanhamento)items.get(position)).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gdv_acompanhamento_layout, null);			
		}
		RecordHolder holder = new RecordHolder();
		
		holder.txvDataAcompanhamento = convertView.findViewById(R.id.gdv_acompanhamento_txv_data_acompanhamento);
		holder.txvPeriodoAerobica = convertView.findViewById(R.id.gdv_acompanhamento_txv_tempo_aerobica);
		holder.txvPeriodoMusculacao = convertView.findViewById(R.id.gdv_acompanhamento_txv_tempo_musculacao);
		holder.txvPeriodoTotalEstudo = convertView.findViewById(R.id.gdv_acompanhamento_txv_tempo_total_estudo);
        holder.txvPeriodoTotalTrabalho = convertView.findViewById(R.id.gdv_acompanhamento_txv_tempo_total_trabalho);
        holder.txvAssuntosEstudados = convertView.findViewById(R.id.gdv_acompanhamento_txv_assuntos_estudados);
        holder.txvProjetosTrabalhados = convertView.findViewById(R.id.gdv_acompanhamento_txv_projetos_trabalhados);
		holder.chkExcUr = convertView.findViewById(R.id.gdv_acompanhamento_chk_exc_ur);

		Acompanhamento acompanhamento = items.get(position);
        int tempoTotalEstudo = 0;
        int tempoTotalTrabalho = 0;
        String assuntosEstudados = "";
        String projetosTrabalhados = "";
		final int idAcomp = acompanhamento.getId();

        holder.txvDataAcompanhamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, AcompanhamentoEstudosActivity.class);
                it.putExtra("load_acompanhamento", idAcomp);
                context.startActivity(it);
            }
        });

        try {
            EstudoBO estudoBO = EstudoBOImpl.getInstance(context);
            AssuntoBO assuntoBO = AssuntoBOImpl.getInstance(context);
            TrabalhoBO trabalhoBO = TrabalhoBOImpl.getInstance(context);
            ProjetoBO projetoBO = ProjetoBOImpl.getInstance(context);
            estudoBO.open();
            assuntoBO.open();
            trabalhoBO.open();
            projetoBO.open();
            List<Estudo> estudos = estudoBO.selectEstudosFromAcompanhamentoId(idAcomp);
            List<Trabalho> trabalhos = trabalhoBO.selectTrabalhosFromAcompanhamentoId(idAcomp);

            boolean flgPossuiEstudos = false;
            for (Estudo estudo : estudos) {
                tempoTotalEstudo += estudo.getTempoMins();
                Assunto assunto = assuntoBO.selectOneById(estudo.getAssunto().getId());
                assuntosEstudados += assunto.getNomeCustomLength(14) + ", ";
                flgPossuiEstudos = true;
            }
            if (flgPossuiEstudos) {
                assuntosEstudados = assuntosEstudados.substring(0, assuntosEstudados.length() - 2).toLowerCase(Locale.getDefault());
            }

            boolean flgPossuiTrabalhos = false;
            for (Trabalho trabalho : trabalhos) {
                tempoTotalTrabalho += trabalho.getTempoMins();
                Projeto projeto = projetoBO.selectOneById(trabalho.getProjeto().getId());
                int limiteAbrvNome = 15;
                String nomeProjetoAbreviado = projeto.getNome().length() > limiteAbrvNome ? projeto.getNome().substring(0, limiteAbrvNome-1) : projeto.getNome();
                projetosTrabalhados += nomeProjetoAbreviado + ", ";
                flgPossuiTrabalhos = true;
            }
            if (flgPossuiTrabalhos) {
                projetosTrabalhados = projetosTrabalhados.substring(0, projetosTrabalhados.length() - 2).toLowerCase(Locale.getDefault());
            }

            estudoBO.close();
            assuntoBO.close();
            trabalhoBO.close();
            projetoBO.close();
        } catch (QueryModelException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.failed_loading_model_list, Estudo.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
        holder.txvDataAcompanhamento.setText(UtilDate.dateToShortBrStringGmtTime(acompanhamento.getDataAcompanhamento().getTime(), true));
		holder.txvPeriodoAerobica.setText("a: " + String.valueOf(acompanhamento.getPeriodoAerobica()));
        holder.txvPeriodoMusculacao.setText("m: " + String.valueOf(acompanhamento.getPeriodoMusculacao()));
        holder.txvPeriodoTotalEstudo.setText("e: " + String.valueOf(tempoTotalEstudo));
        holder.txvPeriodoTotalTrabalho.setText("t: " + String.valueOf(tempoTotalTrabalho));
        holder.txvAssuntosEstudados.setText(assuntosEstudados);
        holder.txvProjetosTrabalhados.setText(projetosTrabalhados);
        holder.chkExcUr.setChecked(acompanhamento.getFlgExUr());

        holder.txvAssuntosEstudados.setTextSize(10);
        holder.txvProjetosTrabalhados.setTextSize(10);
		
		convertView.setTag(holder);
		return convertView;
	}

	static class RecordHolder {
		TextView txvDataAcompanhamento;
		TextView txvPeriodoAerobica;
		TextView txvPeriodoMusculacao;
		TextView txvPeriodoTotalEstudo;
		TextView txvPeriodoTotalTrabalho;
        TextView txvAssuntosEstudados;
		TextView txvProjetosTrabalhados;
		CheckBox chkExcUr;
	}
	
}
