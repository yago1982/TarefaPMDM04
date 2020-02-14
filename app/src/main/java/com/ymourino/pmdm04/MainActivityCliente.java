package com.ymourino.pmdm04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymourino.pmdm04.modelos.Usuario;

public class MainActivityCliente extends AppCompatActivity {

    private Usuario usuario;
    public static final String TIPO_ACTIVIDAD = "com.ymourino.pmdm04.TIPO_ACTIVIDAD";
    public static final String ACTIVIDAD_CLIENTE = "com.ymourino.pmdm04.ACTIVIDAD_CLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * Se obtiene el intent actual para tener acceso que se hayan pasado a esta actividad.
         */
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra(LoginActivity.USUARIO);

        ((TextView) findViewById(R.id.label_usuario)).setText(usuario.getNombre() + " " + usuario.getApellidos());

        ImageView avatarUsuario = (ImageView) findViewById(R.id.image_avatarUsuario);
        Context context = getApplicationContext();
        Bitmap avatar = BitmapFactory.decodeFile(context.getFileStreamPath(usuario.getUsername() + ".jpg").getAbsolutePath());
        avatarUsuario.setImageBitmap(avatar);
    }


    /**
     * Se inicia la actividad para realizar un nuevo pedido.
     * @param view
     */
    public void hacerPedido(View view) {
        Intent intent = new Intent(this, HacerPedidoActivity.class);
        intent.putExtra(LoginActivity.USUARIO, usuario);
        startActivity(intent);
    }

    /**
     * Se inicia la actividad para ver los pedidos en trámite.
     * @param view
     */
    public void pedidosEnTramite(View view) {
        Intent intent = new Intent(this, PedidosTramiteActivity.class);
        intent.putExtra(LoginActivity.USUARIO, usuario);
        startActivity(intent);
    }

    /**
     * Se inicia la actividad para ver las compras realizadas.
     * @param view
     */
    public void comprasRealizadas(View view) {
        Intent intent = new Intent(this, ComprasRealizadasActivity.class);
        intent.putExtra(LoginActivity.USUARIO, usuario);
        startActivity(intent);
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
        inflater.inflate(R.menu.main_menu_cliente, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_nuevoPedido:
                Intent intent_nuevoPedido = new Intent(this, HacerPedidoActivity.class);
                intent_nuevoPedido.putExtra(LoginActivity.USUARIO, usuario);
                startActivity(intent_nuevoPedido);
                return true;

            case R.id.item_pedidosTramite:
                Intent intent_pedidosTramite = new Intent(this, PedidosTramiteActivity.class);
                intent_pedidosTramite.putExtra(LoginActivity.USUARIO, usuario);
                startActivity(intent_pedidosTramite);
                return true;

            case R.id.item_comprasRealizadas:
                Intent intent_comprasRealizadas = new Intent(this, ComprasRealizadasActivity.class);
                intent_comprasRealizadas.putExtra(LoginActivity.USUARIO, usuario);
                startActivity(intent_comprasRealizadas);
                return true;

            case R.id.item_modificarDatos:
                Intent intent_modificarDatos = new Intent(this, ModificarDatosActivity.class);
                intent_modificarDatos.putExtra(LoginActivity.USUARIO, usuario);
                intent_modificarDatos.putExtra(TIPO_ACTIVIDAD, ACTIVIDAD_CLIENTE);
                startActivity(intent_modificarDatos);
                return true;

            case R.id.item_salir:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
