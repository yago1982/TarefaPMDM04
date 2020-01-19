package com.ymourino.pmdm03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymourino.pmdm03.modelos.Usuario;

public class MainActivityAdmin extends AppCompatActivity {

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra(LoginActivity.USUARIO);

        ((TextView) findViewById(R.id.label_usuario)).setText(usuario.getNombre() + " " + usuario.getApellidos());

        ((ImageView) findViewById(R.id.image_avatarUsuario))
                .setImageDrawable(getResources()
                        .getDrawableForDensity(R.drawable.admin, getResources().getDisplayMetrics().densityDpi));
    }


    /**
     * Se sale a la pantalla de login.
     * @param view
     */
    public void salir(View view) {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_admin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_pedidosTramite:
                Intent intent_pedidosTramite = new Intent(this, PedidosTramiteAdminActivity.class);
                intent_pedidosTramite.putExtra(LoginActivity.USUARIO, usuario);
                startActivity(intent_pedidosTramite);
                return true;
            case R.id.item_comprasRealizadas:
                Intent intent_pedidosAceptados = new Intent(this, ComprasRealizadasAdminActivity.class);
                intent_pedidosAceptados.putExtra(LoginActivity.USUARIO, usuario);
                startActivity(intent_pedidosAceptados);
                return true;
            case R.id.item_pedidosRechazados:
                Intent intent_pedidosRechazados = new Intent(this, PedidosRechazadosActivity.class);
                intent_pedidosRechazados.putExtra(LoginActivity.USUARIO, usuario);
                startActivity(intent_pedidosRechazados);
                return true;
            case R.id.item_salir:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
