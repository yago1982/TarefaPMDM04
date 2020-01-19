package com.ymourino.pmdm03.modelos;

import java.io.Serializable;

/**
 * Clase utilizada para almacenar los datos de un pedido, ya sea para almacenarlos
 * luego en la base de datos o cuando se recuperan de ella.
 */
public class Pedido implements Serializable {

    /*
     * Datos utilizados para la creación de la tabla de los pedidos: nombre de la tabla,
     * nombre del campo clave, etc...
     */
    public static final String TABLE_NAME = "pedidos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CATEGORIA = "categoria";
    public static final String COLUMN_PRODUCTO = "producto";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_DIRECCION = "direccion";
    public static final String COLUMN_LOCALIDAD = "localidad";
    public static final String COLUMN_CP = "cp";
    public static final String COLUMN_ESTADO = "estado";

    /*
     * Se construye la sentencia SQL que creará la tabla usando los datos antes definidos.
     * No es necesario añadir "IF NOT EXISTS" porque la creación de la tabla solo
     * se intentará si no existe, ya que así es como está diseñado Android.
     */
    public static final String CREATE_PEDIDOS_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID + " INTEGER NOT NULL,"
                    + COLUMN_CATEGORIA + " STRING NOT NULL,"
                    + COLUMN_PRODUCTO + " STRING NOT NULL,"
                    + COLUMN_CANTIDAD + " INTEGER NOT NULL,"
                    + COLUMN_DIRECCION + " STRING NOT NULL,"
                    + COLUMN_LOCALIDAD + " STRING NOT NULL,"
                    + COLUMN_CP + " INTEGER NOT NULL,"
                    + COLUMN_ESTADO + " NUMERIC NOT NULL DEFAULT 0,"
                    + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + Usuario.TABLE_NAME + "(" + Usuario.COLUMN_ID + ")"
                    + ")";

    /*
     * Datos del pedido.
     */
    private long id;
    private long user_id;
    private String categoria;
    private String producto;
    private int cantidad;
    private String direccion;
    private String localidad;
    private int cp;
    private int estado;


    /**
     * Constructor sin parámetros.
     */
    public Pedido() {}

    /**
     * Constructor con parámetros.
     *
     * @param id Identificador del pedido.
     * @param user_id Identificador del usuario que ha hecho el pedido.
     * @param categoria Categoría del producto.
     * @param producto Producto.
     * @param cantidad Cantidad solicitada del producto.
     * @param direccion Dirección de envío del pedido.
     * @param localidad Localidad de envío del pedido.
     * @param cp Código posta de envío del pedido.
     * @param estado Estado del pedido (0: en trámite, 1: aceptado, 2: rechazado)
     */
    public Pedido(long id, long user_id, String categoria, String producto, int cantidad, String direccion, String localidad, int cp, int estado) {
        this.id = id;
        this.user_id = user_id;
        this.categoria = categoria;
        this.producto = producto;
        this.cantidad = cantidad;
        this.direccion = direccion;
        this.localidad = localidad;
        this.cp = cp;
        this.estado = estado;
    }


    /*
     * A partir de aquí se encuentran los getters y los setters de las propiedades
     * del pedido.
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
