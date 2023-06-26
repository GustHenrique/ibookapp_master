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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ibookApp.APIs.AuthApiClient;
import com.example.ibookApp.APIs.EmailExistenteApiClient;
import com.example.ibookApp.APIs.InsertUsuarioApi;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

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
    Button fabCadastrarUsuario;
    public String finalImagePath;
    public Boolean temEmailValidate = false;
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

    public void inserirUsuario(String email, String senha, String nome, String imagem) {
        InsertUsuarioApi.InsertUsuarioAsyncTask task = new InsertUsuarioApi.InsertUsuarioAsyncTask(email, senha, nome, imagem, new InsertUsuarioApi.InsertUsuarioListener() {
            @Override
            public void onInsertBookReceived(boolean success) {
                // Resultado recebido do AsyncTask
                if (success) {
                    Toast.makeText(telacadastro.this,"Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent temConta = new Intent(getApplicationContext(), telalogin.class);
                    startActivity(temConta);
                } else {
                    Toast.makeText(telacadastro.this,"Ops! Algo deu errado!", Toast.LENGTH_LONG).show();
                }
            }
        });
        task.execute();
    }

    public void cadastrarUsuario(View view) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        nome =  tvUsuNome.getText().toString();
        email = tvUsuEmail.getText().toString();
        senha = tvUsuSenha.getText().toString();
        confirmaSenha = tvUsuConfirmarSenha.getText().toString();

        if (!email.isEmpty() && !nome.isEmpty() && !senha.isEmpty() && !confirmaSenha.isEmpty()) {
            if (senha.contentEquals(confirmaSenha)) {
                if (validarSenha(senha)) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        //verificarExistenciaEmail(email);
                        if (!temEmailValidate){
                            SecretKey secret = Utils.generateKey();
                            byte[] encryptSenha = Utils.encryptMsg(senha, secret);
                            senha = bytesToString(encryptSenha);
                            String imageFilePath = null;
                            if (imageUri != null){
                                imageFilePath = imageUri.getPath();
                            }
                            UploadImageTask uploadTask = new UploadImageTask(imageFilePath);
                            uploadTask.execute();
                            Toast.makeText(telacadastro.this, "Usuário Cadastrado com Sucesso!", Toast.LENGTH_LONG);
                        }
                        else{
                            Toast.makeText(telacadastro.this, "Email já cadastrado!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Formato do E-mail incorreto!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Senha fraca! Minímo 8 digitos entre letras e números!", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_LONG).show();
        }
    }

    /*private void verificarExistenciaEmail(String email) {
        EmailExistenteApiClient.EmailExistenteAsyncTask task = new EmailExistenteApiClient.EmailExistenteAsyncTask(email, new EmailExistenteApiClient.EmailExistenteListener() {
            @Override
            public void onEmailExistenteReceived(UsuarioDTO usuario) {
                // Faça algo com o resultado recebido do serviço da API
                if (usuario != null) {
                    temEmailValidate = true;
                } else {
                    // Email não existe
                }
            }
        });

        task.execute();
    }
*/
    private class UploadImageTask extends AsyncTask<Void, Void, String> {
        private String imageFilePath;

        public UploadImageTask(String imageFilePath) {
            this.imageFilePath = imageFilePath;
        }

        @Override
        protected String doInBackground(Void... params) {
            String accessKey = "AKIAXDEVVEMAF3Q4K5S3";
            String secretKey = "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";
            String bucketName = "ibookimageusuarios";
            String objectKey = UUID.randomUUID().toString() + ".jpg";
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            s3client.setRegion(Region.getRegion(Regions.SA_EAST_1)); // Substitua pela sua região
            if (imageFilePath != null){
                File imageFile = new File(imageFilePath); // Escolha um nome adequado para a imagem
                s3client.putObject(new PutObjectRequest(bucketName, objectKey, imageFile));
                Date expiration = new Date(System.currentTimeMillis() + 3600000);
                URL imageUrl = s3client.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration));

                String finalPath = imageUrl.toString();
                return finalPath;
            }
            else{
                return null;
            }
        }

        @Override
        protected void onPostExecute(String finalPath) {
                if (finalPath == null || finalPath == ""){
                    finalPath = null;
                }
                inserirUsuario(email, senha, nome, finalPath);
                Intent temConta = new Intent(getApplicationContext(), telalogin.class);
                startActivity(temConta);
        }
    }

    public boolean validarSenha(String senha) {
        if (senha.length() < 8) {
            return false; // A senha é muito curta
        }

        boolean temLetraMaiuscula = false;
        boolean temLetraMinuscula = false;
        boolean temNumero = false;

        for (int i = 0; i < senha.length(); i++) {
            char c = senha.charAt(i);

            if (Character.isUpperCase(c)) {
                temLetraMaiuscula = true;
            } else if (Character.isLowerCase(c)) {
                temLetraMinuscula = true;
            } else if (Character.isDigit(c)) {
                temNumero = true;
            }
        }

        return temLetraMaiuscula && temLetraMinuscula && temNumero;
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