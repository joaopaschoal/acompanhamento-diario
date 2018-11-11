package joaopaulo.acompanhamentodiario.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import joaopaulo.acompanhamentodiario.R;

public class IndexTrabalhoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_trabalho);

        this.registerInternalBindings();
    }

    protected void registerInternalBindings() {
        Button btnProjeto = findViewById(R.id.index_trabalho_btn_projeto);
        btnProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ListProjetosActivity.class);
                startActivity(it);
            }
        });

        Button btnNaturezaProjeto = findViewById(R.id.index_trabalho_btn_natureza_projeto);
        btnNaturezaProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ManageNaturezasProjetoActivity.class);
                startActivity(it);
            }
        });

        Button btnTecnologia = findViewById(R.id.index_trabalho_btn_tecnologia);
        btnTecnologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ManageTecnologiasActivity.class);
                startActivity(it);
            }
        });

        Button btnDonoDoProduto = findViewById(R.id.index_trabalho_btn_dono_produto);
        btnDonoDoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ManageDonosProdutoActivity.class);
                startActivity(it);
            }
        });
    }

}
