package ec.com.lemas.firebaselemas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CrearMascotaActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView fotoMascota;
    Button btnSubirFoto, btnEliminarFoto;
    LinearLayout lyt_images;
    EditText etNombreMascota, etEdadMascota, etColorMascota, etRazaMascota;
    Button btnGuardarMascota;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    boolean isNecesarioActualizar = false;
    String idMascota;

    int COD_SEL_IMAGE = 14;
    Uri imageUrl;
    ProgressDialog progressDialog;
    String rutaAlmacenamiento = "imagenes/*";
    String photo = "photo";
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_mascota);
        this.setTitle("Crear Mascota");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fotoMascota = findViewById(R.id.foto_mascota);
        btnSubirFoto = findViewById(R.id.btn_subir_foto);
        btnEliminarFoto = findViewById(R.id.btn_eliminar_foto);
        lyt_images = findViewById(R.id.lyt_images);
        etNombreMascota = findViewById(R.id.et_nombre_mascota);
        etEdadMascota = findViewById(R.id.et_edad_mascota);
        etColorMascota = findViewById(R.id.et_color_mascota);
        etRazaMascota = findViewById(R.id.et_raza);
        btnGuardarMascota = findViewById(R.id.btn_guardar_mascota);
        btnSubirFoto.setOnClickListener(this);
        btnEliminarFoto.setOnClickListener(this);
        btnGuardarMascota.setOnClickListener(this);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_subir_foto:
                subirFoto();
                break;
            case R.id.btn_eliminar_foto:
                eliminarFoto();
                break;
            case R.id.btn_guardar_mascota:
                if (isNecesarioActualizar) {
                    actualizarMascota(idMascota);
                } else {
                    guardarMascota();
                }
                break;
        }
    }

    private void guardarMascota() {
        String nombreMascota = etNombreMascota.getText().toString().trim();
        String edadMascota = etEdadMascota.getText().toString().trim();
        String colorMascota = etColorMascota.getText().toString().trim();
        String razaMascota = etRazaMascota.getText().toString().trim();

        if(nombreMascota.isEmpty()){
            Toast.makeText(this, "Se requiere el nombre de la mascota", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edadMascota.isEmpty()){
            Toast.makeText(this, "Se requiere la edad de la mascota", Toast.LENGTH_SHORT).show();
            return;
        }
        if(colorMascota.isEmpty()){
            Toast.makeText(this, "Se requiere el color de la mascota", Toast.LENGTH_SHORT).show();
            return;
        }
        if(razaMascota.isEmpty()){
            Toast.makeText(this, "Se requiere la raza de la mascota", Toast.LENGTH_SHORT).show();
            return;
        }

        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference _id = mFirestore.collection("mascotas").document();
        Map<String, Object> map = new HashMap<>();
        map.put("idUser", idUser);
        map.put("id", _id);
        map.put("nombre", nombreMascota);
        map.put("edad", edadMascota);
        map.put("color", colorMascota);
        map.put("razaMascota", razaMascota);

        mFirestore.collection("mascotas")
                .document(idMascota)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}