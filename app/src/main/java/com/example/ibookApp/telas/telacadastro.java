package com.example.ibookApp.telas;

import static com.example.ibookApp.functions.Utils.bytesToString;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibookApp.DAOs.UsuarioDAO;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class telacadastro extends AppCompatActivity {
    private String[] cameraPermission;
    private String[] storagePermission;
    private Uri imageUri;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;
    private ImageView civImageCad;
    TextView tvTemConta, tvUsuEmail, tvUsuSenha, tvUsuConfirmarSenha, tvUsuNome;
    String nome, email, senha, confirmaSenha, image;
    FloatingActionButton fabCadastrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telacadastro);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        tvTemConta = findViewById(R.id.txtPossuoConta);
        tvUsuEmail = findViewById(R.id.txtEmailCadastro);
        tvUsuSenha = findViewById(R.id.txtSenhaCadastro);
        tvUsuConfirmarSenha = findViewById(R.id.txtConfirmarSenhaCadastro);
        tvUsuNome = findViewById(R.id.txtNomeCadastro);
        civImageCad = findViewById(R.id.imgCadastro);
        fabCadastrarUsuario = findViewById(R.id.btnCadastrarUsuario);
        tvTemConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temConta = new Intent(getApplicationContext(), telalogin.class);
                startActivity(temConta);
            }
        });

        fabCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cadastrarUsuario(view);
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        civImageCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });
    }
    public void cadastrarUsuario(View view) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        nome =  tvUsuNome.getText().toString();
        email = tvUsuEmail.getText().toString();
        senha = tvUsuSenha.getText().toString();
        confirmaSenha = tvUsuConfirmarSenha.getText().toString();
        if (!email.isEmpty() && !nome.isEmpty() && !senha.isEmpty() && !confirmaSenha.isEmpty()){
            if (senha.contentEquals(confirmaSenha)){
                SecretKey secret = Utils.generateKey();
                byte[] encryptSenha = Utils.encryptMsg(senha, secret);
                senha = bytesToString(encryptSenha);
                UsuarioDTO usuario = new UsuarioDTO(email, senha, nome, null, ""+imageUri);
                UsuarioDAO UsuarioDAO = new UsuarioDAO(this);
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if (UsuarioDAO.existeEmailCadastrado(email)){
                        UsuarioDAO.inserirUsuario(usuario);
                        Toast.makeText(this,"Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent temConta = new Intent(getApplicationContext(), telalogin.class);
                        startActivity(temConta);
                    }
                    else{
                        Toast.makeText(this,"E-mail já cadastrado!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(this,"Formato do E-mail incorreto!", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this,"As senhas não coincidem!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Todos os campos são obrigatórios!", Toast.LENGTH_LONG).show();
        }
    }

    private void showImagePickerDialog() {

        //option for dialog
        String options[] = {"Camera","Galeria"};

        // Alert dialog builder
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);

        //setTitle
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item click
                if (which == 0){ //start from 0 index
                    //camera selected
                    if (!checkCameraPermission()){
                        //request camera permission
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }

                }else if (which == 1){
                    //Gallery selected
                    if (!checkStoragePermission()){
                        //request storage permission
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }

                }
            }
        }).create().show();
    }

    private void pickFromGallery() {
        //intent for taking image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // only Image

        startActivityForResult(galleryIntent,IMAGE_FROM_GALLERY_CODE);
    }
    private void pickFromCamera() {

//       ContentValues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");

        //save imageUri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        startActivityForResult(cameraIntent,IMAGE_FROM_CAMERA_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE); // handle request permission on override method
    }

    //check storage permission
    private boolean checkStoragePermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result1;
    }

    //request for camera permission
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_FROM_GALLERY_CODE){
                // picked image from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(telacadastro.this);

            }else if (requestCode == IMAGE_FROM_CAMERA_CODE){
                //picked image from camera
                //crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(telacadastro.this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                //cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                civImageCad.setImageURI(imageUri);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //for error handling
                Toast.makeText(getApplicationContext(), "Algo deu errado!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}