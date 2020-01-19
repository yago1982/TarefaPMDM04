package com.ymourino.pmdm03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ymourino.pmdm03.db.SqliteDBHelper;
import com.ymourino.pmdm03.modelos.Usuario;

public class LoginActivity extends AppCompatActivity {

    public static final String USUARIO = "com.ymourino.pmdm03.USUARIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void login(View view) {
        /*
         * Se obtienen el nombre de usuario y la contraseña introducidos.
         */
        EditText edit_username = findViewById(R.id.edit_username);
        EditText edit_password = findViewById(R.id.edit_password);
        String username = edit_username.getText().toString();
        String password = edit_password.getText().toString();

        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(getApplicationContext());
        Usuario usuario = sqliteDBHelper.getUserByName(username);

        if (usuario != null && usuario.getPassword().equals(password)) {
            if (usuario.isAdmin()) {
                Intent intent = new Intent(this, MainActivityAdmin.class);
                intent.putExtra(USUARIO, usuario);
                startActivity(intent);

                edit_username.setText("");
                edit_password.setText("");
                edit_username.requestFocus();
            } else {
                Intent intent = new Intent(this, MainActivityCliente.class);
                intent.putExtra(USUARIO, usuario);
                startActivity(intent);

                edit_username.setText("");
                edit_password.setText("");
                edit_username.requestFocus();
            }
        } else {
            showToastMsg("Usuario o contraseña incorrectos");
        }
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegistrarUsuarioActivity.class);
        startActivity(intent);
    }

    private void showToastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}
