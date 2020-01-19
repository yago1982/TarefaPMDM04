package com.ymourino.pmdm03;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.ymourino.pmdm03.adaptadores.PedidosRecViewAdapter;
import com.ymourino.pmdm03.db.SqliteDBHelper;
import com.ymourino.pmdm03.modelos.Pedido;
import com.ymourino.pmdm03.modelos.Usuario;

import java.util.ArrayList;

public class PedidosRechazadosActivity extends AppCompatActivity {

    private Usuario usuario;
    private ArrayList<Pedido> pedidos;
    private SqliteDBHelper sqliteDBHelper;
    private RecyclerView recyclerView;
    private PedidosRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras_realizadas_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*
         * Se obtiene el intent actual para tener acceso que se hayan pasado a esta actividad.
         */
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra(LoginActivity.USUARIO);

        sqliteDBHelper = new SqliteDBHelper(this);
        recyclerView = findViewById(R.id.pedidosTramiteRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadPedidos();
    }

    private void loadPedidos() {
        pedidos = sqliteDBHelper.getPedidosRechazados();

        adapter = new PedidosRecViewAdapter(getApplicationContext(), pedidos, false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivityAdmin.class);
        intent.putExtra(LoginActivity.USUARIO, usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
}
