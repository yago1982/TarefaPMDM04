package com.ymourino.pmdm03.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ymourino.pmdm03.R;
import com.ymourino.pmdm03.db.SqliteDBHelper;
import com.ymourino.pmdm03.modelos.Pedido;
import com.ymourino.pmdm03.modelos.Usuario;

import java.util.ArrayList;

/**
 * Se utiliza este adaptador para mostrar pedidos en las RecyclerView.
 */
public class PedidosRecViewAdapter extends RecyclerView.Adapter<PedidosRecViewAdapter.ViewHolder> {
    private boolean gestion;
    private Context context;
    private ArrayList<Pedido> data;
    private LayoutInflater inflater;

    /**
     * Constructor.
     *
     * @param context Contexto.
     * @param data Listado de los pedidos a mostrar.
     * @param gestion Indica si se deben mostrar los botones para aceptar y rechazar pedidos.
     */
    public PedidosRecViewAdapter(Context context, ArrayList<Pedido> data, boolean gestion) {
        this.gestion = gestion;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    /**
     * Genera las filas para la RecyclerView, seleccionando el layout normal o el de
     * gestión según sea necesario (propiedad gestión de esta clase).
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (gestion) {
            view = inflater.inflate(R.layout.pedido_gestion_row, parent, false);
        } else {
            view = inflater.inflate(R.layout.pedido_usuario_row, parent, false);
        }

        return new ViewHolder(view);
    }

    /**
     * Añade los datos de un pedido a una vista.
     *
     * @param holder Vista que mostrará los datos del pedido.
     * @param position Posición en la que se motrarán los datos.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(context);
        Usuario usuario = sqliteDBHelper.getUserByID(data.get(position).getUser_id());

        String detalles = usuario.getUsername()
                + " / " + data.get(position).getCategoria()
                + " / " + data.get(position).getProducto()
                + " / " + data.get(position).getCantidad();
        String envio = data.get(position).getDireccion()
                + " / " + data.get(position).getLocalidad()
                + " / " + data.get(position).getCp();

        holder.detallesPedido.setText(detalles);
        holder.envioPedido.setText(envio);
    }

    /**
     * Devuelve el número de pedidos.
     *
     * @return El número de pedidos.
     */
    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    /**
     * Se utiliza esta clase para poder darle funcionalidad a las vistas que forman
     * la RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button aceptarPedidoButton, rechazarPedidoButton;
        TextView detallesPedido, envioPedido;

        /**
         *
         * @param itemView
         */
        ViewHolder(View itemView) {
            super(itemView);
            detallesPedido = itemView.findViewById(R.id.text_detalles);
            envioPedido = itemView.findViewById(R.id.text_envio);

            /*
             * Si hay que gestionar los pedidos (lo que significa que se ha elegido
             * la vista con los botones), se les da funcionalidad a dichos botones.
             */
            if (gestion) {
                aceptarPedidoButton = (Button) itemView.findViewById(R.id.button_aceptarPedido);
                rechazarPedidoButton = (Button) itemView.findViewById(R.id.button_rechazarPedido);

                aceptarPedidoButton.setOnClickListener(this);
                rechazarPedidoButton.setOnClickListener(this);
            }
        }

        /**
         * Manejador para los clicks en las vistas.
         *
         * @param v Vista en la que se ha hecho click.
         */
        @Override
        public void onClick(View v) {
            SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(context);
            int position = getAdapterPosition();

            if (v.getId() == aceptarPedidoButton.getId()) {
                Log.i("ACEPTAR", "ACEPTAR PEDIDO " + data.get(position).getId());
                if (sqliteDBHelper.aceptarPedidoByID(data.get(getAdapterPosition()).getId())) {
                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    Toast.makeText(context, "Se ha aceptado el pedido", Toast.LENGTH_LONG).show();
                }
            } else if (v.getId() == rechazarPedidoButton.getId()){
                Log.i("RECHAZAR", "RECHAZAR PEDIDO " + data.get(position).getId());
                if (sqliteDBHelper.rechazarPedidoByID(data.get(getAdapterPosition()).getId())) {
                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    Toast.makeText(context, "Se ha rechazado el pedido", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
