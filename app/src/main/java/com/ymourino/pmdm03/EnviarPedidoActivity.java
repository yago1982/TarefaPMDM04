package com.ymourino.pmdm03;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ymourino.pmdm03.modelos.Usuario;

public class EnviarPedidoActivity extends AppCompatActivity {

    public static final String DIRECCION = "com.ymourino.pmdm03.DIRECCION";
    public static final String CIUDAD = "com.ymourino.pmdm03.CIUDAD";
    public static final String CP = "com.ymourino.pmdm03.CP";

    private Usuario usuario;
    private String categoria;
    private String producto;
    private Integer cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_pedido);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*
         * Obtenemos los detalles del envío que ha recibido esta actividad.
         */
        Intent intent = getIntent();

        usuario = (Usuario) intent.getSerializableExtra(LoginActivity.USUARIO);

        categoria = intent.getStringExtra(HacerPedidoActivity.CATEGORIA);
        producto = intent.getStringExtra(HacerPedidoActivity.PRODUCTO);
        cantidad = intent.getIntExtra(HacerPedidoActivity.CANTIDAD, 1);
    }


    /**
     * Al finalizar el pedido se mostrará un toast con la información del mismo y se termina la
     * actividad.
     * @param view
     */
    public void confirmarPedido(View view) {
        String direccion = ((EditText) findViewById(R.id.edit_direccion)).getText().toString();
        String ciudad = ((EditText) findViewById(R.id.edit_ciudad)).getText().toString();
        String cp = ((EditText) findViewById(R.id.edit_cp)).getText().toString();

        Intent intent = new Intent(this, ConfirmarPedidoActivity.class);

        intent.putExtra(LoginActivity.USUARIO, usuario);
        intent.putExtra(HacerPedidoActivity.CATEGORIA, categoria);
        intent.putExtra(HacerPedidoActivity.PRODUCTO, producto);
        intent.putExtra(HacerPedidoActivity.CANTIDAD, cantidad);
        intent.putExtra(DIRECCION, direccion);
        intent.putExtra(CIUDAD, ciudad);

        if (cp.isEmpty()) {
            intent.putExtra(CP, 0);
        } else {
            intent.putExtra(CP, Integer.valueOf(cp));
        }

        startActivity(intent);
    }


    @Override
    public boolean onSupportNavigateUp() {
        String direccion = ((EditText) findViewById(R.id.edit_direccion)).getText().toString();
        String ciudad = ((EditText) findViewById(R.id.edit_ciudad)).getText().toString();
        String cp = ((EditText) findViewById(R.id.edit_cp)).getText().toString();

        Intent intent = new Intent(this, HacerPedidoActivity.class);
        intent.putExtra(LoginActivity.USUARIO, usuario);
        intent.putExtra(HacerPedidoActivity.CATEGORIA, categoria);
        intent.putExtra(HacerPedidoActivity.PRODUCTO, producto);
        intent.putExtra(HacerPedidoActivity.CANTIDAD, cantidad);
        intent.putExtra(EnviarPedidoActivity.DIRECCION, direccion);
        intent.putExtra(EnviarPedidoActivity.CIUDAD, ciudad);
        intent.putExtra(EnviarPedidoActivity.CP, cp);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
}
