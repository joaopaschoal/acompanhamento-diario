package joaopaulo.acompanhamentodiario.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import joaopaulo.acompanhamentodiario.R;
import joaopaulo.acompanhamentodiario.business.bo.BO;
import joaopaulo.acompanhamentodiario.business.exception.QueryModelException;
import joaopaulo.acompanhamentodiario.business.exception.SaveModelException;
import joaopaulo.acompanhamentodiario.persistence.model.Model;
import joaopaulo.acompanhamentodiario.view.util.ParametersView;

/**
 * Created by JoaoPaulo on 13/07/2015.
 */
public abstract class BasicCrudActivity<TModel extends Model, TBO extends BO> extends BaseActivity {
    // ----- Attributes ----- //
    private ArrayAdapter<TModel> adapter;
    private TModel model;
    private TBO bo;


    // ----- Events ----- //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getActivitylayout());

        bo = this.getConcreteBO();

        this.registerBtnSaveClick();
    }

    @Override
    protected void onResume() {
        bo.open();
        super.onResume();

        loadListView();
    }

    @Override
    protected void onPause() {
        bo.close();
        super.onPause();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        TextView txtEmpty = this.getTextViewEmpty();
        ListView ltvItens = this.getListViewItens();
        ltvItens.setEmptyView(txtEmpty);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == this.getListViewItens().getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            TModel model = adapter.getItem(info.position);
            menu.setHeaderTitle(model.toString());
            menu.add(Menu.NONE, ParametersView.MENU_EDIT, 1, R.string.edit);
            menu.add(Menu.NONE, ParametersView.MENU_DELETE, 2, R.string.delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int idItem = item.getItemId();

        switch (idItem) {
            case ParametersView.MENU_EDIT:
                this.model = adapter.getItem(info.position);
                fillScreenFromObject(model);
                break;

            case ParametersView.MENU_DELETE:
                this.model = adapter.getItem(info.position);
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_confirm_delete)
                        .setMessage(R.string.msg_confirm_delete)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    bo.delete(BasicCrudActivity.this.model);
                                    intializeScreen();
                                    Toast.makeText(BasicCrudActivity.this, getString(R.string.model_successful_deleted, BasicCrudActivity.this.getModelActualName()), Toast.LENGTH_LONG).show();
                                } catch (SQLException ex) {
                                    Toast.makeText(BasicCrudActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                } catch (Exception ex) {
                                    Toast.makeText(BasicCrudActivity.this, getString(R.string.failed_deleting_model, BasicCrudActivity.this.getModelActualName()), Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                break;
        }

        return true;
    }

    // ----- Abstract Protected Methods ----- //

    protected abstract int getActivitylayout();

    protected abstract TBO getConcreteBO();

    protected abstract String getModelActualName();

    protected abstract Button getBtnSave();

    protected abstract TextView getTextViewEmpty();

    protected abstract ListView getListViewItens();

    protected abstract EditText getEditTextName();

    protected abstract TModel createConcreteModel();

    protected abstract String getModelDefaultText(TModel model);

    protected abstract void setModelDefaultText(TModel model, String text);

    protected void registerBtnSaveClick() {
        Button btnSalvar = this.getBtnSave();
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model = fillObjectFromScreen(model);

                try {
                    bo.save(model);
                    Toast.makeText(BasicCrudActivity.this, getString(R.string.model_successful_saved, BasicCrudActivity.this.getModelActualName()), Toast.LENGTH_LONG).show();
                } catch (SaveModelException ex) {
                    new AlertDialog.Builder(BasicCrudActivity.this)
                            .setTitle("Falha ao Excluir Registro")
                            .setMessage(ex.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                intializeScreen();
            }
        });
    }

    // ----- Public Methods ----- //

    public void intializeScreen() {
        clearScreen();
        loadListView();
    }

    public void clearScreen() {
        this.model = null;
        EditText edtName = this.getEditTextName();
        edtName.getText().clear();
    }




    // ----- Private Methods ----- //

    protected TModel fillObjectFromScreen(TModel model) {
        if (model == null) {
            model = createConcreteModel();
        }
        this.setModelDefaultText(model, this.getEditTextName().getText().toString().toUpperCase(Locale.getDefault()));
        return model;
    }

    protected void fillScreenFromObject(TModel model) {
        this.getEditTextName().setText(this.getModelDefaultText(model));
    }

    protected void loadListView() {
        List<TModel> list;
        try {
            list = bo.selectAll();
            adapter = new ArrayAdapter<TModel>(this,android.R.layout.simple_list_item_1, list);
            ListView ltvItens = this.getListViewItens();
            ltvItens.setAdapter(adapter);
            registerForContextMenu(ltvItens);
        } catch (QueryModelException ex) {
            ex.printStackTrace();
            Toast.makeText(this, getString(R.string.failed_loading_model_list, this.getModelActualName()), Toast.LENGTH_LONG).show();
        }
    }

    
}
