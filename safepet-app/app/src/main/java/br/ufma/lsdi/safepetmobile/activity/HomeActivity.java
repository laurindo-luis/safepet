package br.ufma.lsdi.safepetmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufma.lsdi.safepetmobile.CONSTANTES;
import br.ufma.lsdi.safepetmobile.R;
import br.ufma.lsdi.safepetmobile.ServicesSQLite.PetServiceSQLite;
import br.ufma.lsdi.safepetmobile.adapter.MenuAdapter;
import br.ufma.lsdi.safepetmobile.databaseSQLite.DatabaseHelper;
import br.ufma.lsdi.safepetmobile.domain.Pet;
import br.ufma.lsdi.safepetmobile.mqtt.BrokerMqtt;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(this.toolbar);
        toolbar.setNavigationOnClickListener(onClick -> finish());

        recyclerView = findViewById(R.id.recyclerViewMenu);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        MenuAdapter menuAdapter = new MenuAdapter(new Integer[] {CONSTANTES.USER, CONSTANTES.MAP, CONSTANTES.PETS}, onClick());
        recyclerView.setAdapter(menuAdapter);

        databaseHelper = new DatabaseHelper(this);

        List<Pet> pets = PetServiceSQLite.getPets(databaseHelper);
        pets.forEach(pet -> {
            if(pet.isMonitored()) {
                BrokerMqtt.subscribe(BrokerMqtt.getClient(), "event/status/"+pet.getIdColeira());
            }
        });

    }

    public MenuAdapter.MenuAdapterClickListener onClick() {
        return new MenuAdapter.MenuAdapterClickListener() {
            @Override
            public void onClickMenu(View view, int index) {

                if(index == CONSTANTES.MAP) {
                    startActivity(new Intent(HomeActivity.this, MapsActivity.class));
                } else if (index == CONSTANTES.PETS) {
                    startActivity(new Intent(HomeActivity.this, PetsActivity.class));
                }
            }
        };
    }
}
