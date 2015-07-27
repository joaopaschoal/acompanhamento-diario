package joaopaulo.acompdiario.view.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.AssuntoBO;
import joaopaulo.acompdiario.business.bo.AssuntoBOImpl;
import joaopaulo.acompdiario.business.bo.EstudoBO;
import joaopaulo.acompdiario.business.bo.EstudoBOImpl;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.Acompanhamento;
import joaopaulo.acompdiario.persistence.model.Assunto;
import joaopaulo.acompdiario.persistence.model.Estudo;
import joaopaulo.acompdiario.tools.UtilDate;
import joaopaulo.acompdiario.view.activity.AcompanhamentoEstudosActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class AcompanhamentoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Acompanhamento> items = new ArrayList<Acompanhamento>();
	
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
		
		holder.txvDataAcompanhamento = (TextView)convertView.findViewById(R.id.gdv_acompanhamento_txv_data_acompanhamento);
		holder.txvPeriodoAerobica = (TextView)convertView.findViewById(R.id.gdv_acompanhamento_txv_tempo_aerobica);
		holder.txvPeriodoMusculacao = (TextView)convertView.findViewById(R.id.gdv_acompanhamento_txv_tempo_musculacao);
		holder.txvPeriodoTotalEstudo = (TextView)convertView.findViewById(R.id.gdv_acompanhamento_txv_tempo_total_estudo);
        holder.txvNomesAssuntos = (TextView)convertView.findViewById(R.id.gdv_acompanhamento_txv_nomes_assuntos);
		holder.chkExcUr = (CheckBox)convertView.findViewById(R.id.gdv_acompanhamento_chk_exc_ur);

		Acompanhamento acompanhamento = items.get(position);
        int tempoTotalEstudo = 0;
        String nomesAssuntos = "";
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
            estudoBO.open();
            assuntoBO.open();
            List<Estudo> estudos = estudoBO.selectEstudosFromAcompanhamentoId(idAcomp);

            boolean flgPossuiEstudos = false;
            for (Estudo estudo : estudos) {
                tempoTotalEstudo += estudo.getTempoMins();
                Assunto assunto = assuntoBO.selectOneById(estudo.getAssunto().getId());
                nomesAssuntos += assunto.getNomeCustomLength(14) + ", ";
                flgPossuiEstudos = true;
            }
            if (flgPossuiEstudos) {
                nomesAssuntos = nomesAssuntos.substring(0, nomesAssuntos.length() - 2).toLowerCase(Locale.getDefault());
            }
            estudoBO.close();
            assuntoBO.close();
        } catch (QueryModelException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.failed_loading_model_list, Estudo.ACTUAL_NAME), Toast.LENGTH_LONG).show();
        }
        holder.txvDataAcompanhamento.setText(UtilDate.dateToShortBrStringGmtTime(acompanhamento.getDataAcompanhamento().getTime(), true));
		holder.txvPeriodoAerobica.setText(String.valueOf(acompanhamento.getPeriodoAerobica()));
        holder.txvPeriodoMusculacao.setText(String.valueOf(acompanhamento.getPeriodoMusculacao()));
        holder.txvPeriodoTotalEstudo.setText(String.valueOf(tempoTotalEstudo));
        holder.txvNomesAssuntos.setText(nomesAssuntos);
        holder.chkExcUr.setChecked(acompanhamento.getFlgExUr());
		
		convertView.setTag(holder);
		return convertView;
	}

	static class RecordHolder {
		TextView txvDataAcompanhamento;
		TextView txvPeriodoAerobica;
		TextView txvPeriodoMusculacao;
		TextView txvPeriodoTotalEstudo;
        TextView txvNomesAssuntos;
		CheckBox chkExcUr;
	}
	
}
