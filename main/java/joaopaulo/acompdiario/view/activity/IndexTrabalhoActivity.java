package joaopaulo.acompdiario.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import joaopaulo.acompdiario.R;

public class IndexTrabalhoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_trabalho);

        this.registerInternalBindings();
    }

    protected void registerInternalBindings() {
        Button btnProjeto = (Button)findViewById(R.id.index_trabalho_btn_projeto);
        btnProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ListProjetosActivity.class);
                startActivity(it);
            }
        });

        Button btnNaturezaProjeto = (Button)findViewById(R.id.index_trabalho_btn_natureza_projeto);
        btnNaturezaProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ManageNaturezasProjetoActivity.class);
                startActivity(it);
            }
        });

        Button btnTecnologia = (Button)findViewById(R.id.index_trabalho_btn_tecnologia);
        btnTecnologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ManageTecnologiasActivity.class);
                startActivity(it);
            }
        });

        Button btnDonoDoProduto = (Button)findViewById(R.id.index_trabalho_btn_dono_produto);
        btnDonoDoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(IndexTrabalhoActivity.this, ManageDonosProdutoActivity.class);
                startActivity(it);
            }
        });
    }

}
