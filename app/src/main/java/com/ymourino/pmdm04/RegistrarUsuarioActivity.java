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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ymourino.pmdm04.db.SqliteDBHelper;
import com.ymourino.pmdm04.modelos.Usuario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private static final int REALIZAR_FOTO = 0x0001;
    private static final int SELECCIONAR_AVATAR = 0x0002;
    private static final int PEDIR_PERMISO_FOTO = 0x0003;

    private EditText edit_username;
    private EditText edit_password;
    private EditText edit_nombre;
    private EditText edit_apellidos;
    private CheckBox check_admin;
    private ImageView image_avatarUsuario;

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
        image_avatarUsuario = findViewById(R.id.image_avatarUsuario);

        image_avatarUsuario
                .setImageDrawable(getResources()
                        .getDrawableForDensity(R.drawable.cliente, getResources().getDisplayMetrics().densityDpi));
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

            // Cogemos la imagen del image_avatarUsuario y la guardamos en disco con el nombre del usuario.
            BitmapDrawable drawable = (BitmapDrawable) image_avatarUsuario.getDrawable();
            Bitmap image = drawable.getBitmap();

            Context context = getApplicationContext();
            File avatar = new File(context.getFilesDir(), username + ".jpg");

            try (FileOutputStream fos = context.openFileOutput(username + ".jpg", Context.MODE_PRIVATE)) {
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (IOException e) {}


            Toast.makeText(this, "El usuario " + username + " ha sido registrado", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "El usuario " + username + " ya existe en la base de datos", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
}
