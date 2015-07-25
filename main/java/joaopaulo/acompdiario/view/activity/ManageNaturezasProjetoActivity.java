package joaopaulo.acompdiario.view.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.NaturezaProjetoBO;
import joaopaulo.acompdiario.business.bo.NaturezaProjetoBOImpl;
import joaopaulo.acompdiario.persistence.model.NaturezaProjeto;

public class ManageNaturezasProjetoActivity extends BasicCrudActivity<NaturezaProjeto, NaturezaProjetoBO> {

	@Override
	protected int getActivitylayout() {
		return R.layout.activity_manage_naturezas_projeto;
	}

	@Override
	protected NaturezaProjetoBO getConcreteBO() {
		return NaturezaProjetoBOImpl.getInstance(this);
	}

	@Override
	protected String getModelActualName() {
		return NaturezaProjeto.ACTUAL_NAME;
	}

    @Override
    protected Button getBtnSave() {
        return (Button)findViewById(R.id.manage_naturezas_projeto_btn_save);
    }

    @Override
	protected TextView getTextViewEmpty() {
		return (TextView)findViewById(R.id.manage_naturezas_projeto_txv_empty);
	}

	@Override
	protected ListView getListViewItens() {
		return (ListView)findViewById(R.id.manage_naturezas_projeto_ltv_itens);
	}

	@Override
	protected EditText getEditTextName() {
		return (EditText)findViewById(R.id.manage_naturezas_projeto_edt_description);
	}

	@Override
	protected NaturezaProjeto createConcreteModel() {
        return new NaturezaProjeto();
	}

	@Override
	protected String getModelDefaultText(NaturezaProjeto model) {
		return model.getDescricao();
	}

	@Override
	protected void setModelDefaultText(NaturezaProjeto model, String text) {
        model.setDescricao(text);
	}
	
}
