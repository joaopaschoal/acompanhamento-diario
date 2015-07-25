package joaopaulo.acompdiario.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import joaopaulo.acompdiario.R;

public class IndexFerramentasActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_ferramentas);

        this.registerInternalBindings();
    }

    protected void registerInternalBindings() {
        Button btnExportarImportarBD = (Button)findViewById(R.id.index_ferramentas_btn_categorias_cientificas);
        btnExportarImportarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexFerramentasActivity.this, ExportarBDActivity.class);
                startActivity(it);
            }
        });
    }

}
