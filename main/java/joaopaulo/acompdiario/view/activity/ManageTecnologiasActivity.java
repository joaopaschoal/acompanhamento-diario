package joaopaulo.acompdiario.view.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import joaopaulo.acompdiario.R;
import joaopaulo.acompdiario.business.bo.TecnologiaBO;
import joaopaulo.acompdiario.business.bo.TecnologiaBOImpl;
import joaopaulo.acompdiario.persistence.model.Tecnologia;

public class ManageTecnologiasActivity extends BasicCrudActivity<Tecnologia, TecnologiaBO> {

	@Override
	protected int getActivitylayout() {
		return R.layout.activity_manage_tecnologias;
	}

	@Override
	protected TecnologiaBO getConcreteBO() {
		return TecnologiaBOImpl.getInstance(this);
	}

	@Override
	protected String getModelActualName() {
		return Tecnologia.ACTUAL_NAME;
	}

    @Override
    protected Button getBtnSave() {
        return (Button)findViewById(R.id.manage_tecnologias_btn_save_tecnologia);
    }

    @Override
	protected TextView getTextViewEmpty() {
		return (TextView)findViewById(R.id.manage_tecnologias_txv_empty_tecnologia);
	}

	@Override
	protected ListView getListViewItens() {
		return (ListView)findViewById(R.id.manage_tecnologias_ltv_itens_tecnologia);
	}

	@Override
	protected EditText getEditTextName() {
		return (EditText)findViewById(R.id.manage_tecnologias_edt_name_tecnologia);
	}

	@Override
	protected Tecnologia createConcreteModel() {
        return new Tecnologia();
	}

	@Override
	protected String getModelDefaultText(Tecnologia model) {
		return model.getNome();
	}

	@Override
	protected void setModelDefaultText(Tecnologia model, String text) {
        model.setNome(text);
	}
	
}
