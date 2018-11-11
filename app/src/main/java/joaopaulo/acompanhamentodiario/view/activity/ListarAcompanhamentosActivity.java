package joaopaulo.acompanhamentodiario.view.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.AcompanhamentoBO;
import joaopaulo.acompanhamentodiario.business.bo.AcompanhamentoBOImpl;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.persistence.model.Acompanhamento;
import joaopaulo.acompanhamentodiario.view.adapter.AcompanhamentoAdapter;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

public class ListarAcompanhamentosActivity extends BaseActivity {

	// ----- Attributes ----- //
	private AcompanhamentoBO bo;
	private AcompanhamentoAdapter adapter;
	
	// ----- Events ----- //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_acompanhamentos);
		
		bo = AcompanhamentoBOImpl.getInstance(this);
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
	
	// --- Private Methods --- //
	protected void intializeScreen() {
		loadGridView();
	}
	
	protected void loadGridView() {
		List<Acompanhamento> list;
		try {
			list = bo.selectAll();
			Acompanhamento a = list.get(0);
			a.getId();
			Collections.sort(list, new Comparator<Acompanhamento>() {
				@Override
				public int compare(Acompanhamento a1, Acompanhamento a2) {
					return (a2.getDataAcompanhamento().compareTo(a1.getDataAcompanhamento()));
				}
			});
			adapter = new AcompanhamentoAdapter(this, list);
			GridView gdvItens = (GridView)findViewById(R.id.listar_acompanhamentos_gdv_itens_acompanhamento);
			gdvItens.setAdapter(adapter);
			registerForContextMenu(gdvItens);
		} catch (QueryModelException ex) {
			ex.printStackTrace();
			Toast.makeText(this, getString(R.string.failed_loading_model_list,Acompanhamento.ACTUAL_NAME), Toast.LENGTH_LONG).show();
		}
	}
	
}
