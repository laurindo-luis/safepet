package br.ufma.lsdi.safepetmobile.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.ufma.lsdi.safepetmobile.R;

public class MapSelectedLocationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Selecionar local seguro");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(this.toolbar);
        toolbar.setNavigationOnClickListener(onClick -> finish());


    }
}
