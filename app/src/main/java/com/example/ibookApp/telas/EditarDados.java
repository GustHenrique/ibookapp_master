package com.example.ibookApp.telas;

import static com.example.ibookApp.functions.Utils.bytesToString;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.ibookApp.APIs.AtualizarUsuarioApi;
import com.example.ibookApp.DTOs.UsuarioDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.fragments.FragmentProfile;
import com.example.ibookApp.functions.UserSingleton;
import com.example.ibookApp.functions.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
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

public class EditarDados extends AppCompatActivity {

    private String[] cameraPermission;
    private String[] storagePermission;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;
    private Button btnLogout, btnHome, btnAttPerfil;
    private Uri imageUri;
    private EditText edtNome, edtSenha, edtConfirmarSenha, edtSenhaAtual;
    ImageView imgProfile;
    TextView txtNome;
    UsuarioDTO userLogado = UserSingleton.getInstance().getUser();
    UsuarioDTO attUsuario = new UsuarioDTO(null,null,null,null,null,null);
    private String nome, senha, confirmarSenha, imagem,senhaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dados);
        btnLogout = findViewById(R.id.btnLogoutDetalhesObra);
        btnHome = findViewById(R.id.btnBackHome);
        txtNome = (TextView) findViewById(R.id.txtNomeUsuarioProfile);
        edtNome = findViewById(R.id.txtNomeCadastro);
        edtSenha = findViewById(R.id.txtSenhaAtt);
        edtConfirmarSenha = findViewById(R.id.txtConfirmarSenhaAtt);
        edtSenhaAtual = findViewById(R.id.txtSenhaAtual);
        btnAttPerfil = findViewById(R.id.btnAttPerfil);
        imgProfile = findViewById(R.id.imgProfile);
        txtNome.setText("Olá " + userLogado.getNome() + "!");
        edtNome.setText(userLogado.getNome());
        String imagemPath = userLogado.getImagem();
        if (imagemPath != null && !imagemPath.equals("null") && !imagemPath.isEmpty()) {
            imageUri = Uri.parse(imagemPath);
            Glide.with(this)
                    .load(imageUri)
                    .into(imgProfile);
        }
        else {
            imgProfile.setImageDrawable(null);
            imgProfile.setImageResource(R.drawable.ic_baseline_person_24);
        }

        btnAttPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    atualizarPerfil();
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Acessar);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

    }

    public void atualizarPerfil() throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        boolean podealterar = false;
        nome = edtNome.getText().toString();
        senha = edtSenha.getText().toString();
        confirmarSenha = edtConfirmarSenha.getText().toString();
        senhaAtual = edtSenhaAtual.getText().toString();
        imagem = "" + imageUri;
        attUsuario = new UsuarioDTO(userLogado.getEmail(), userLogado.getSenha(), nome, userLogado.getId(), imagem, userLogado.getAdministrador());
        if (nome != null && !nome.isEmpty()) {
            if (!senha.isEmpty() || !confirmarSenha.isEmpty() || !senhaAtual.isEmpty()) {
                if (validarSenha(senha)) {
                    if (senha.contentEquals(confirmarSenha)) {
                        senhaAtual = encryptPass(senhaAtual);
                        if (senhaAtual.equals(userLogado.getSenha())){
                            senha = encryptPass(senha);
                            attUsuario.setSenha(senha);
                            podealterar = true;
                        }
                        else{
                            Toast.makeText(EditarDados.this, "Senha atual inválida!", Toast.LENGTH_LONG);
                        }
                    }
                    else{
                        Toast.makeText(EditarDados.this, "Nova senha e Confirmar Nova Senha não são identicas!", Toast.LENGTH_LONG);
                    }
                }
                else{
                    Toast.makeText(EditarDados.this, "Senha inválida!", Toast.LENGTH_LONG);
                }
            } else {
                if (!userLogado.equals(attUsuario)) {
                    if (nome.length() < 2){
                        Toast.makeText(EditarDados.this, "Campo nome deve ter mais de uma letra!", Toast.LENGTH_LONG);
                    }
                    else{
                        podealterar = true;
                    }
                }
            }
        }
        else{
            Toast.makeText(EditarDados.this, "Campo nome vazio!", Toast.LENGTH_LONG);
        }


        if(podealterar){
            if (userLogado.getImagem().equals(attUsuario.getImagem())){
                if (!userLogado.equals(attUsuario)){
                    AtualizarUsuarioApi.AtualizarUsuarioObrasAsyncTask task = new AtualizarUsuarioApi.AtualizarUsuarioObrasAsyncTask(attUsuario, new AtualizarUsuarioApi.AtualizarUsuarioObrasAListener() {
                        @Override
                        public void onInsertObrasReceived() {
                            UserSingleton.getInstance().setUser(attUsuario);
                            Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(Acessar);
                        }
                    });
                    task.execute();
                }
            }else{
                String imageFilePath = null;
                if (imageUri != null){
                    imageFilePath = imageUri.getPath();
                }
                UploadImageTask uploadTask = new UploadImageTask(imageFilePath);
                uploadTask.execute();
            }
        }
        else{
            Toast.makeText(EditarDados.this, "Ops! Algo deu errado.", Toast.LENGTH_LONG);
        }
    }

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
            String objectKey = userLogado.getImagem();
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            s3client.setRegion(Region.getRegion(Regions.SA_EAST_1));

            if (imageFilePath != null) {
                File imageFile = new File(imageFilePath);

                // Use o Glide para carregar a imagem e aplicar as opções de compressão
                RequestOptions requestOptions = new RequestOptions()
                        .override(Target.SIZE_ORIGINAL)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);

                Bitmap compressedBitmap = null;
                try {
                    compressedBitmap = Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(imageFile)
                            .apply(requestOptions)
                            .submit()
                            .get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (compressedBitmap != null) {
                    // Salve a imagem comprimida em um novo arquivo temporário
                    File compressedImageFile = new File(getCacheDir(), "compressed_image.jpg");
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(compressedImageFile);
                        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                        outputStream.close();

                        // Faça o upload do novo arquivo comprimido
                        s3client.putObject(new PutObjectRequest(bucketName, objectKey, compressedImageFile));

                        Date expiration = new Date(System.currentTimeMillis() + 3600000);
                        URL imageUrl = s3client.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, objectKey)
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration));

                        String finalPath = imageUrl.toString();
                        return finalPath;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(String finalPath) {
            if (finalPath == null || finalPath == ""){
                finalPath = null;
            }
            attUsuario.setImagem(finalPath);
            AtualizarUsuarioApi.AtualizarUsuarioObrasAsyncTask task = new AtualizarUsuarioApi.AtualizarUsuarioObrasAsyncTask(attUsuario, new AtualizarUsuarioApi.AtualizarUsuarioObrasAListener() {
                @Override
                public void onInsertObrasReceived() {
                    UserSingleton.getInstance().setUser(attUsuario);
                }
            });
            task.execute();
            Intent Acessar = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(Acessar);
        }
    }

    public String encryptPass(String pass) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKey secret = Utils.generateKey();
        byte[] encryptSenha = Utils.encryptMsg(pass, secret);
        return bytesToString(encryptSenha);
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
        String options[] = {"Camera", "Galeria"};

        // Alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setTitle
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item click
                if (which == 0) { //start from 0 index
                    //camera selected
                    if (!checkCameraPermission()) {
                        //request camera permission
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    //Gallery selected
                    if (!checkStoragePermission()) {
                        //request storage permission
                        requestStoragePermission();
                    } else {
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
        startActivityForResult(galleryIntent, IMAGE_FROM_GALLERY_CODE);
    }

    private void pickFromCamera() {

//       ContentValues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "IMAGE_DETAIL");

        //save imageUri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_CODE); // handle request permission on override method
    }

    //check storage permission
    private boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result1;
    }

    //request for camera permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_FROM_GALLERY_CODE) {
                // picked image from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(EditarDados.this);

            } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                //picked image from camera
                //crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(EditarDados.this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                //cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                imgProfile.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //for error handling
                Toast.makeText(getApplicationContext(), "Algo deu errado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void logout() {
        Utils.logout();
        Intent acessar = new Intent(getApplicationContext(), telalogin.class);
        startActivity(acessar);
    }
}