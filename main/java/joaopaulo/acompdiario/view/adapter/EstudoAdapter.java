package joaopaulo.acompdiario.view.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.persistence.model.Estudo;
import joaopaulo.acompdiario.view.activity.AcompanhamentoEstudosActivity;

public class EstudoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Estudo> estudos;

    public EstudoAdapter(Context context, List<Estudo> estudos) {
		this.context = context;
        this.estudos = estudos;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public int getCount() {
        return estudos.size();
	}

	@Override
	public Estudo getItem(int position) {
		return estudos.get(position);
	}

	@Override
	public long getItemId(int position) {
         return ((Estudo)estudos.get(position)).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gdv_assuntos_estudados, null);
		}
		RecordHolder holder = new RecordHolder();

		holder.txvNumSeq = (TextView)convertView.findViewById(R.id.gdv_assuntos_estudados_txv_num_seq);
        holder.txvNomeAssunto = (TextView)convertView.findViewById(R.id.gdv_assuntos_estudados_txv_nome_assunto);
        holder.txvTempoEstudo = (TextView)convertView.findViewById(R.id.gdv_assuntos_estudados_txv_tempo_estudo);
        holder.btnRemover = (ImageButton)convertView.findViewById(R.id.gdv_assuntos_estudados_btn_remover);


		final Estudo estudo = estudos.get(position);
		holder.txvNumSeq.setText(String.valueOf(position+1));
		holder.txvNomeAssunto.setText(estudo.getAssunto().getNome());
		holder.txvTempoEstudo.setText(String.valueOf(estudo.getTempoMins()));
        final GridView gridView = (GridView)parent;
		holder.btnRemover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Estudo estudo = estudos.get(position);
                estudos.remove(estudo);
                calcGridAssuntosEstudadosHeight(gridView);
                notifyDataSetChanged();
            }
        });

		convertView.setTag(holder);
		return convertView;
	}

    public void add(Estudo estudo){
        this.estudos.add(estudo);
        calcGridAssuntosEstudadosHeight(((AcompanhamentoEstudosActivity) context).getGridViewAssuntosEstudados());
        notifyDataSetChanged();
    }



    public void calcGridAssuntosEstudadosHeight(GridView gdvAssuntosEstudados) {
        int rowHeight = 49;
        ViewGroup.LayoutParams layoutParams = gdvAssuntosEstudados.getLayoutParams();
        layoutParams.height = rowHeight * estudos.size();
        gdvAssuntosEstudados.setLayoutParams(layoutParams);
    }

    static class RecordHolder {
		TextView txvNumSeq;
		TextView txvNomeAssunto;
		TextView txvTempoEstudo;
		ImageButton btnRemover;
	}

}
