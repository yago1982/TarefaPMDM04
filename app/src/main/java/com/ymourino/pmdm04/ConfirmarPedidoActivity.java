package com.ymourino.pmdm04;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ymourino.pmdm04.db.SqliteDBHelper;
import com.ymourino.pmdm04.modelos.Pedido;
import com.ymourino.pmdm04.modelos.Usuario;

public class ConfirmarPedidoActivity extends AppCompatActivity {

    private TextView label_categoria;
    private TextView label_producto;
    private TextView label_cantidad;
    private TextView label_direccion;
    private TextView label_ciudad;
    private TextView label_cp;

    private Usuario usuario;
    private String categoria;
    private String producto;
    private Integer cantidad;
    private String direccion;
    private String ciudad;
    private Integer cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        label_categoria = findViewById(R.id.label_categoriaShow);
        label_producto = findViewById(R.id.label_productoShow);
        label_cantidad = findViewById(R.id.label_cantidadShow);
        label_direccion = findViewById(R.id.label_direccionShow);
        label_ciudad = findViewById(R.id.label_ciudadShow);
        label_cp = findViewById(R.id.label_cpShow);

        Intent intent = getIntent();

        usuario = (Usuario) intent.getSerializableExtra(LoginActivity.USUARIO);
        categoria = intent.getStringExtra(HacerPedidoActivity.CATEGORIA);
        producto = intent.getStringExtra(HacerPedidoActivity.PRODUCTO);
        cantidad = intent.getIntExtra(HacerPedidoActivity.CANTIDAD, 1);
        direccion = intent.getStringExtra(EnviarPedidoActivity.DIRECCION);
        ciudad = intent.getStringExtra(EnviarPedidoActivity.CIUDAD);
        cp = intent.getIntExtra(EnviarPedidoActivity.CP, 0);

        label_categoria.setText(categoria);
        label_producto.setText(producto);
        label_cantidad.setText(cantidad.toString());
        label_direccion.setText(direccion);
        label_ciudad.setText(ciudad);
        label_cp.setText(cp.toString());
    }

    public void guardar(View view) {
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(getApplicationContext());

        long temp_id = 1;
        Pedido pedido = new Pedido(temp_id, usuario.getId(), categoria, producto, cantidad, direccion, ciudad, cp, 0);

        sqliteDBHelper.createPedido(pedido);

        Toast.makeText(this, "Se ha guardado el pedido", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivityCliente.class);
        intent.putExtra(LoginActivity.USUARIO, usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancelar(View view) {
        Intent intent = new Intent(this, MainActivityCliente.class);
        intent.putExtra(LoginActivity.USUARIO, usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, EnviarPedidoActivity.class);
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
