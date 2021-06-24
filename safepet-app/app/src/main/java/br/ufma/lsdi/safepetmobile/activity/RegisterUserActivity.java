package br.ufma.lsdi.safepetmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.ufma.lsdi.safepetmobile.R;
import br.ufma.lsdi.safepetmobile.ServicesSQLite.TutorServiceSQLite;
import br.ufma.lsdi.safepetmobile.databaseSQLite.DatabaseHelper;
import br.ufma.lsdi.safepetmobile.domain.SafePlace;
import br.ufma.lsdi.safepetmobile.domain.Tutor;

public class RegisterUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTexUserName;
    private EditText editTextPassword;
    private EditText editTextSafeLocation;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastrar Tutor");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(this.toolbar);
        toolbar.setNavigationOnClickListener(onClick -> finish());

        databaseHelper = new DatabaseHelper(this);

        this.editTextName = findViewById(R.id.editTextName);
        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.editTexUserName = findViewById(R.id.editTextUserName);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.editTextSafeLocation = findViewById(R.id.editTextSafeLocation);
    }

    public void openMapSelectedSafeLocation (View view) {
        startActivity(new Intent(this, MapSelectedLocationActivity.class));
    }

    public void saveTutor(View view) {
        if(this.editTextName.getText().toString().equals("")) {
            Toast.makeText(this, "Campo nome não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(this.editTextEmail.getText().toString().equals("")) {
            Toast.makeText(this, "Campo email não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(this.editTextPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Campo senha não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(this.editTextSafeLocation.getText().toString().equals("")) {
            Toast.makeText(this, "Campo localização segura não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] location = editTextSafeLocation.getText().toString().split(";");
        SafePlace safePlace = new SafePlace(
                Double.valueOf(location[0].trim()),
                Double.valueOf(location[1].trim())
        );

        Tutor tutor = new Tutor(null, editTextName.getText().toString(), editTextEmail.getText().toString(),
                editTexUserName.getText().toString(), editTextPassword.getText().toString(), safePlace);

        long id = TutorServiceSQLite.save(databaseHelper, tutor);

        if(id != -1) {
            Toast.makeText(this, "Cadastro do tutor realizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Não foi possível realizar o cadastro do tutor.", Toast.LENGTH_SHORT).show();
        }
    }
}
