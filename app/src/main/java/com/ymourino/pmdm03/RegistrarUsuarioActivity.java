package com.ymourino.pmdm03;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ymourino.pmdm03.db.SqliteDBHelper;
import com.ymourino.pmdm03.modelos.Usuario;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private EditText edit_username;
    private EditText edit_password;
    private EditText edit_nombre;
    private EditText edit_apellidos;
    private CheckBox check_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        edit_nombre = findViewById(R.id.edit_nombre);
        edit_apellidos = findViewById(R.id.edit_apellidos);
        check_admin = findViewById(R.id.check_admin);
    }

    public void register(View view) {
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(getApplicationContext());
        String username = edit_username.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
        String nombre = edit_nombre.getText().toString().trim();
        String apellidos = edit_apellidos.getText().toString().trim();
        boolean admin = check_admin.isChecked();

        if (sqliteDBHelper.getUserByName(username) == null) {
            long temp_id = 1;
            Usuario usuario = new Usuario(temp_id, username, password, nombre, apellidos, admin);
            sqliteDBHelper.createUser(usuario);

            Toast.makeText(this, "El usuario " + username + " ha sido registrado", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "El usuario " + username + " ya existe en la base de datos", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
}
