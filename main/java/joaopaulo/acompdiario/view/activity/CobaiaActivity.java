package joaopaulo.acompdiario.view.activity;

import java.util.List;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.AcompanhamentoBO;
import joaopaulo.acompdiario.business.bo.AcompanhamentoBOImpl;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.persistence.model.Acompanhamento;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CobaiaActivity extends BaseActivity {

	private AcompanhamentoBO bo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cobaia);
		bo =  AcompanhamentoBOImpl.getInstance(CobaiaActivity.this);
		
		Button btn = (Button)findViewById(R.id.cobaia_btn_teste);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Acompanhamento> list = null;
//				try {
//					list = bo.selectAll();
//				} catch (QueryModelException e) {
//					e.printStackTrace();
//				}
//				for (Acompanhamento acompanhamento : list) {
//					acompanhamento.getPeriodoEstudo();  
//				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		bo.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		bo.close();
		super.onPause();
	}

}
