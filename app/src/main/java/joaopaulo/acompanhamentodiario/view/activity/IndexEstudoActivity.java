package joaopaulo.acompanhamentodiario.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import joaopaulo.acompanhamentodiario.R;

public class IndexEstudoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_estudo);

        this.registerInternalBindings();
    }

    protected void registerInternalBindings() {
        Button btnCategoriasCientificas = findViewById(R.id.index_estudo_btn_categorias_cientificas);
        btnCategoriasCientificas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexEstudoActivity.this, ManageCategoriasCientificasActivity.class);
                startActivity(it);
            }
        });

        Button btnDisciplinas = findViewById(R.id.index_estudo_btn_disciplinas);
        btnDisciplinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexEstudoActivity.this, ManageDisciplinasActivity.class);
                startActivity(it);
            }
        });

        Button btnAssuntos = findViewById(R.id.index_estudo_btn_assuntos);
        btnAssuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexEstudoActivity.this, ManageAssuntosActivity.class);
                startActivity(it);
            }
        });
    }

}
