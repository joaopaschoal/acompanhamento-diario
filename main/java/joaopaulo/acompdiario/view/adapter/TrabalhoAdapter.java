package joaopaulo.acompdiario.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.persistence.model.Estudo;
import joaopaulo.acompdiario.persistence.model.Trabalho;
import joaopaulo.acompdiario.view.activity.AcompanhamentoTrabalhosActivity;

public class TrabalhoAdapter extends GridViewAdapter<Trabalho> {

	public TrabalhoAdapter(List<Trabalho> trabalhos, Context context) {
		super(trabalhos, context);
	}

	@Override
	protected GridView getGridView() {
		return ((AcompanhamentoTrabalhosActivity)context).getGridViewProjetosTrabalhados();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = inflater.inflate(R.layout.gdv_projetos_trabalhados, null);
		}
		RecordHolder holder = new RecordHolder();

		holder.txvNumSeq = (TextView)view.findViewById(R.id.gdv_projetos_trabalhados_txv_num_seq);
		holder.txvNomeProjeto = (TextView)view.findViewById(R.id.gdv_projetos_trabalhados_txv_nome_projeto);
		holder.txvTempoTrabalhado = (TextView)view.findViewById(R.id.gdv_projetos_trabalhados_txv_tempo_trabalhado);
		holder.btnRemover = (ImageButton)view.findViewById(R.id.gdv_projetos_trabalhados_btn_remover);


		final Trabalho trabalho = items.get(position);
		holder.txvNumSeq.setText(String.valueOf(position+1));
		holder.txvNomeProjeto.setText(trabalho.getProjeto().getNome());
		holder.txvTempoTrabalhado.setText(String.valueOf(trabalho.getTempoMins()));
		final GridView gridView = (GridView)viewGroup;
		holder.btnRemover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Trabalho trabalho = items.get(position);
				items.remove(trabalho);
				calcGridViewHeight(gridView);
				notifyDataSetChanged();
			}
		});

		view.setTag(holder);
		return view;
	}

	static class RecordHolder {
		TextView txvNumSeq;
		TextView txvNomeProjeto;
		TextView txvTempoTrabalhado;
		ImageButton btnRemover;
	}
}
