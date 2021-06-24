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

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTextUserName;
    private EditText editTextPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(this.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(onClick -> finish());

        databaseHelper = new DatabaseHelper(this);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

    }

    public void login(View view) {
        String userName = editTextUserName.getText().toString();
        String password = editTextPassword.getText().toString();

        if(userName.trim().equals("")) {
            Toast.makeText(this, "Campo usuário não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.trim().equals("")) {
            Toast.makeText(this, "Campo senha não pode ser nulo", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isLogin = TutorServiceSQLite.login(databaseHelper, userName, password);
        if(isLogin)
            startActivity(new Intent(this, HomeActivity.class));
        else
            Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();

    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterUserActivity.class));
    }
}
