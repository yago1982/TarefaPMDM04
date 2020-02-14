package com.ymourino.pmdm04;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymourino.pmdm04.db.SqliteDBHelper;
import com.ymourino.pmdm04.modelos.Usuario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModificarDatosActivity extends AppCompatActivity {

    private Usuario usuario;
    private String actividadPadre;

    private static final int REALIZAR_FOTO = 0x0001;
    private static final int SELECCIONAR_AVATAR = 0x0002;
    private static final int PEDIR_PERMISO_FOTO = 0x0003;

    private EditText edit_password;
    private EditText edit_confirmPassword;
    private EditText edit_nombre;
    private EditText edit_apellidos;
    private CheckBox check_admin;
    private ImageView image_avatarUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        edit_password = findViewById(R.id.edit_password);
        edit_confirmPassword = findViewById(R.id.edit_confirmPassword);
        edit_nombre = findViewById(R.id.edit_nombre);
        edit_apellidos = findViewById(R.id.edit_apellidos);
        check_admin = findViewById(R.id.check_admin);
        image_avatarUsuario = findViewById(R.id.image_avatarUsuario);

        /*
         * Se obtiene el intent actual para tener acceso que se hayan pasado a esta actividad.
         */
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra(LoginActivity.USUARIO);
        actividadPadre = intent.getStringExtra(MainActivityCliente.TIPO_ACTIVIDAD);

        ((TextView) findViewById(R.id.label_username)).setText(usuario.getUsername());

        Context context = getApplicationContext();
        Bitmap avatar = BitmapFactory.decodeFile(context.getFileStreamPath(usuario.getUsername() + ".jpg").getAbsolutePath());
        image_avatarUsuario.setImageBitmap(avatar);

        edit_nombre.setText(usuario.getNombre());
        edit_apellidos.setText(usuario.getApellidos());
        check_admin.setChecked(usuario.isAdmin());
    }

    public void guardarDatos(View view) {
        SqliteDBHelper sqliteDBHelper = new SqliteDBHelper(getApplicationContext());
        String password = edit_password.getText().toString().trim();
        String passwordConfirmation = edit_confirmPassword.getText().toString().trim();
        String nombre = edit_nombre.getText().toString().trim();
        String apellidos = edit_apellidos.getText().toString().trim();
        boolean admin = check_admin.isChecked();

        if (password.equals(passwordConfirmation)) {
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setAdmin(admin);

            if (!password.isEmpty()) {
                usuario.setPassword(password);
            }

            // Cogemos la imagen del image_avatarUsuario y la guardamos en disco con el nombre del usuario.
            BitmapDrawable drawable = (BitmapDrawable) image_avatarUsuario.getDrawable();
            Bitmap image = drawable.getBitmap();

            Context context = getApplicationContext();
            File avatar = new File(context.getFilesDir(), usuario.getUsername() + ".jpg");

            try (FileOutputStream fos = context.openFileOutput(usuario.getUsername() + ".jpg", Context.MODE_PRIVATE)) {
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (IOException e) {}

            if (sqliteDBHelper.updateUser(usuario)) {
                Toast.makeText(this, "Usuario modificado correctamente", Toast.LENGTH_LONG).show();
                onSupportNavigateUp();
            } else {
                Toast.makeText(this, "No se ha podido modificar el usuario", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "La contraseña y la confirmación deben coincidir", Toast.LENGTH_LONG).show();
        }
    }


    public void hacerFoto(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, PEDIR_PERMISO_FOTO);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REALIZAR_FOTO);
        }
    }

    public void seleccionaAvatar(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECCIONAR_AVATAR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PEDIR_PERMISO_FOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REALIZAR_FOTO);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REALIZAR_FOTO:
                if (resultCode == RESULT_OK && data != null) {
                    image_avatarUsuario.setImageBitmap((Bitmap) data.getExtras().get("data"));
                }

                break;

            case SELECCIONAR_AVATAR:
                if (resultCode == RESULT_OK && data != null) {
                    Uri imageData = data.getData();
                    image_avatarUsuario.setImageURI(imageData);
                }

                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent;

        /*if (usuario.isAdmin()) {
            intent = new Intent(this, MainActivityAdmin.class);
        } else {
            intent = new Intent(this, MainActivityCliente.class);
        }*/

        if (actividadPadre.equals(MainActivityCliente.ACTIVIDAD_CLIENTE)) {
            intent = new Intent(this, MainActivityCliente.class);
        } else {
            intent = new Intent(this, MainActivityAdmin.class);
        }

        intent.putExtra(LoginActivity.USUARIO, usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
}
