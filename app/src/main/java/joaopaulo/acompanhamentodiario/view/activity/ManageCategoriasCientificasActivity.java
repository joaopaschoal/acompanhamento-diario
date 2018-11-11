package joaopaulo.acompanhamentodiario.view.activity;


import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.CategoriaCientificaBO;
import joaopaulo.acompanhamentodiario.business.bo.CategoriaCientificaBOImpl;
import joaopaulo.acompanhamentodiario.persistence.model.CategoriaCientifica;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ManageCategoriasCientificasActivity extends BasicCrudActivity<CategoriaCientifica, CategoriaCientificaBO> {


	@Override
	protected int getActivitylayout() {
		return R.layout.activity_manage_categorias_cientificas;
	}

	@Override
	protected CategoriaCientificaBO getConcreteBO() {
		return CategoriaCientificaBOImpl.getInstance(this);
	}

	@Override
	protected String getModelActualName() {
		return CategoriaCientifica.ACTUAL_NAME;
	}

	@Override
	protected Button getBtnSave() {
		return (Button)findViewById(R.id.manage_categorias_cientificas_btn_save_categoria_cientifica);
	}

	@Override
	protected TextView getTextViewEmpty() {
		return (TextView)findViewById(R.id.manage_categorias_cientificas_txt_empty_categoria_cientifica);
	}

	@Override
	protected ListView getListViewItens() {
		return (ListView)findViewById(R.id.manage_categorias_cientificas_ltv_itens_categoria_cientifica);
	}

	@Override
	protected EditText getEditTextName() {
		return (EditText)findViewById(R.id.manage_categorias_cientificas_edt_name_categoria_cientifica);
	}

	@Override
	protected CategoriaCientifica createConcreteModel() {
		return new CategoriaCientifica();
	}

	@Override
	protected String getModelDefaultText(CategoriaCientifica model) {
		return model.getNome();
	}

	@Override
	protected void setModelDefaultText(CategoriaCientifica model, String text) {
		model.setNome(text);
	}
}
