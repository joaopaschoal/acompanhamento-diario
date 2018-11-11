package joaopaulo.acompanhamentodiario.view.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.DonoProdutoBO;
import joaopaulo.acompanhamentodiario.business.bo.DonoProdutoBOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.DonoProduto;

public class ManageDonosProdutoActivity extends BasicCrudActivity<DonoProduto, DonoProdutoBO> {

	@Override
	protected int getActivitylayout() {
		return R.layout.activity_manage_donos_produto;
	}

	@Override
	protected DonoProdutoBO getConcreteBO() {
		return DonoProdutoBOImpl.getInstance(this);
	}

	@Override
	protected String getModelActualName() {
		return DonoProduto.ACTUAL_NAME;
	}

    @Override
    protected Button getBtnSave() {
        return (Button)findViewById(R.id.manage_donos_produto_btn_save);
    }

    @Override
	protected TextView getTextViewEmpty() {
		return (TextView)findViewById(R.id.manage_donos_produto_txv_empty);
	}

	@Override
	protected ListView getListViewItens() {
		return (ListView)findViewById(R.id.manage_donos_produto_ltv_itens);
	}

	@Override
	protected EditText getEditTextName() {
		return (EditText)findViewById(R.id.manage_donos_produto_edt_name);
	}

	@Override
	protected DonoProduto createConcreteModel() {
        return new DonoProduto();
	}

	@Override
	protected String getModelDefaultText(DonoProduto model) {
		return model.getNome();
	}

	@Override
	protected void setModelDefaultText(DonoProduto model, String text) {
        model.setNome(text);
	}
	
}
