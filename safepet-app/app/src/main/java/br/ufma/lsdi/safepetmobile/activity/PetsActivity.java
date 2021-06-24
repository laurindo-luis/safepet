package br.ufma.lsdi.safepetmobile.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import br.ufma.lsdi.safepetmobile.R;
import br.ufma.lsdi.safepetmobile.ServicesSQLite.PetServiceSQLite;
import br.ufma.lsdi.safepetmobile.ServicesSQLite.TutorServiceSQLite;
import br.ufma.lsdi.safepetmobile.adapter.PetAdapter;
import br.ufma.lsdi.safepetmobile.databaseSQLite.DatabaseHelper;
import br.ufma.lsdi.safepetmobile.domain.Pet;
import br.ufma.lsdi.safepetmobile.domain.Tutor;
import br.ufma.lsdi.safepetmobile.mqtt.BrokerMqtt;

public class PetsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DatabaseHelper databaseHelper;
    private Spinner spinnerType;
    private AlertDialog alertDialog;

    private EditText editTextName;
    private EditText editTextIdColeira;
    private EditText editTextRadiusInMeters;

    private List<Pet> pets;

    private Tutor tutor;
    private Integer type = 0;

    private Pet petSelected = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pets");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(this.toolbar);
        toolbar.setNavigationOnClickListener(onClick -> finish());

        this.editTextName = findViewById(R.id.editTextName);
        this.editTextIdColeira = findViewById(R.id.editTextIdColeira);
        this.editTextRadiusInMeters = findViewById(R.id.editTextRadiusInMeters);

        this.alertDialog = createAlertDialog();

        //Spinner
        spinnerType = findViewById(R.id.spinnerTypePet);
        spinnerType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[] {"gato", "cachorro"}));
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (int)id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewPets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tutor = TutorServiceSQLite.getTutor(databaseHelper);

        loadPets();

    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções");
        builder.setItems(new CharSequence[]{"Editar", "Excluir", "Parar monitoramento", "Iniciar monitoramento"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        break;
                    case 1:
                        break;

                    case 2:
                        petSelected.setMonitored(false);
                        BrokerMqtt.unsubscribe(BrokerMqtt.getClient(), "event/status/"+petSelected.getIdColeira());
                        PetServiceSQLite.update(databaseHelper, petSelected);
                        loadPets();
                        break;

                    case 3:
                        petSelected.setMonitored(true);
                        BrokerMqtt.subscribe(BrokerMqtt.getClient(), "event/status/"+petSelected.getIdColeira());
                        PetServiceSQLite.update(databaseHelper, petSelected);
                        loadPets();
                        break;
                }

            }
        });
        return builder.create();
    }

    private void cleanFields() {
        editTextName.setText("");
        editTextIdColeira.setText("");
        editTextRadiusInMeters.setText("");
    }

    private void loadPets() {
        pets = PetServiceSQLite.getPets(databaseHelper);
        PetAdapter petAdapter = new PetAdapter(pets, onClick());
        recyclerView.setAdapter(petAdapter);
    }

    public void save(View view) {
        String name = editTextName.getText().toString();
        String idColeira = editTextIdColeira.getText().toString();
        String radiusInMeters = editTextRadiusInMeters.getText().toString();

        if(name.trim().equals("")) {
            Toast.makeText(this, "Campo nome não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(idColeira.trim().equals("")) {
            Toast.makeText(this, "Campo ID da coleira não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(radiusInMeters.trim().equals("")) {
            Toast.makeText(this, "Campo raio seguro não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }

        Pet pet = new Pet(null, name, Double.valueOf(radiusInMeters), type, Integer.valueOf(idColeira),
                tutor.getId(), true);
        long id = PetServiceSQLite.save(databaseHelper, pet);
        if(id != -1) {

            //Criando as EPLs do Pet
            try {
                String payload = String.format("%s;%s;%s", pet.getIdColeira(), pet.getRadiusInMeters(), tutor.getSafePlace());
                BrokerMqtt.getClient().publish("create/epl", new MqttMessage(payload.getBytes()));
            } catch (MqttException e) {
                e.printStackTrace();
            }

            //Fazendo o inscrição para receber os eventos complexos do pet
            BrokerMqtt.subscribe(BrokerMqtt.getClient(), "event/status/"+pet.getIdColeira());

            Toast.makeText(this, "Cadastro do pet realizado com sucesso!", Toast.LENGTH_SHORT).show();

            cleanFields();
        } else {
            Toast.makeText(this, "Não foi possível realizar o cadastro do pet.", Toast.LENGTH_SHORT).show();
        }

        loadPets();

    }

    public PetAdapter.PetAdapterClickListener onClick() {
        return new PetAdapter.PetAdapterClickListener() {
            @Override
            public void onClickMenu(View view, int index) {
                petSelected = pets.get(index);
                alertDialog.show();
            }
        };
    }
}
