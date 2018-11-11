package joaopaulo.acompanhamentodiario.view.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import joaopaulo.acompanhamentodiario.R;

public class SobreActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        this.loadVersionName();
    }

    protected void loadVersionName() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            TextView txvVersionName = findViewById(R.id.txv_nome_versao);
            txvVersionName.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
