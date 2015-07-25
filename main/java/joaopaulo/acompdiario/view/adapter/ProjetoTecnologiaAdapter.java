package joaopaulo.acompdiario.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.TecnologiaBO;
import joaopaulo.acompdiario.business.bo.TecnologiaBOImpl;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.ProjetoTecnologia;
import joaopaulo.acompdiario.view.activity.NewProjetoActivity;

public class ProjetoTecnologiaAdapter extends GridViewAdapter<ProjetoTecnologia> {


	public ProjetoTecnologiaAdapter(List<ProjetoTecnologia> tecnologiasProjeto, Context context) {
		super(tecnologiasProjeto, context);
	}

    @Override
    public void add(ProjetoTecnologia item) {
        if (item.isFlgTecnologiaPrincipal()) {
            for (ProjetoTecnologia existingItem : this.items) {
                existingItem.setFlgTecnologiaPrincipal(false);
            }
        }
        super.add(item);
    }

    public void remove(ProjetoTecnologia item) {
        this.items.remove(item);
        if (this.getCount() == 1) {
            this.items.get(0).setFlgTecnologiaPrincipal(true);
        }
    }

    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gdv_tecnologias_projeto, null);
		}
		RecordHolder holder = new RecordHolder();

		holder.txvNumSeq = (TextView)convertView.findViewById(R.id.gdv_tecnologias_projeto_txv_num_seq);
        holder.imgIsTecnologiaPrincipal = (ImageView)convertView.findViewById(R.id.gdv_tecnologias_projeto_img_is_tecnologia_principal);
        holder.txvNomeTecnologia = (TextView)convertView.findViewById(R.id.gdv_tecnologias_projeto_txv_nome_tecnologia);
        holder.btnRemover = (ImageButton)convertView.findViewById(R.id.gdv_tecnologias_projeto_btn_remover);

		final ProjetoTecnologia tecnologiaProjeto = items.get(position);

        //load tecnologia if necessary
		if (tecnologiaProjeto.getTecnologia() == null || tecnologiaProjeto.getTecnologia().getNome() == null || tecnologiaProjeto.getTecnologia().getNome().isEmpty()) {
			TecnologiaBO bo = TecnologiaBOImpl.getInstance(this.context);
            try {
                tecnologiaProjeto.setTecnologia(bo.selectOneById(tecnologiaProjeto.getTecnologia().getId()));
            } catch (QueryModelException e) {
                e.printStackTrace();
            }
        }

		holder.txvNumSeq.setText(String.valueOf(position + 1));
		Drawable drwAlert = tecnologiaProjeto.isFlgTecnologiaPrincipal() ? context.getResources().getDrawable(R.drawable.alert) : null;
		holder.imgIsTecnologiaPrincipal.setImageDrawable(drwAlert);
		// holder.imgIsTecnologiaPrincipal.setVisibility(tecnologiaProjeto.isFlgTecnologiaPrincipal() ? View.VISIBLE : View.INVISIBLE);
		holder.txvNomeTecnologia.setText(String.valueOf(tecnologiaProjeto.getTecnologia().getNome()));
        final GridView gridView = (GridView)parent;
		holder.btnRemover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjetoTecnologia tecnologiaProjeto = items.get(position);
                ProjetoTecnologiaAdapter.this.remove(tecnologiaProjeto);
                calcGridViewHeight(gridView);
                notifyDataSetChanged();
            }
        });

		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected GridView getGridView() {
		return ((NewProjetoActivity)context).getGridViewTecnologiasProjeto();
	}


	static class RecordHolder {
		TextView txvNumSeq;
		ImageView imgIsTecnologiaPrincipal;
		TextView txvNomeTecnologia;
		ImageButton btnRemover;
	}

}
