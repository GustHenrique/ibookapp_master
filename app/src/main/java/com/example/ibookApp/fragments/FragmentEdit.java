package com.example.ibookApp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.example.ibookApp.APIs.InsertObrasApi;
import com.example.ibookApp.APIs.ibookApi;
import com.example.ibookApp.DTOs.obrasDTO;
import com.example.ibookApp.R;
import com.example.ibookApp.functions.ObrasListSingleton;
import com.example.ibookApp.functions.Utils;
import com.example.ibookApp.telas.telalogin;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEdit extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEdit.
     */
    public static FragmentEdit newInstance(String param1, String param2) {
        FragmentEdit fragment = new FragmentEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    TextView tvCadastroObraCategorias;
    boolean[] selectedCategorias;
    private int selectedTipo, selectedStatus;
    ArrayList<Integer> categoriasList = new ArrayList<>();
    ArrayList<Integer> tipoList = new ArrayList<>();
    ArrayList<Integer> statusList = new ArrayList<>();
    String[] categoriaArray = {
            "Ficção", "Biografia", "Autoajuda", "Negócios", "Romance", "Suspense", "Mistério", "Fantasia", "Histórico",
            "Clássico", "Ciência", "Tecnologia", "Medicina", "Culinária", "Esportes", "Infantil", "Adulto"
    };

    String[] statusArray = {
            "Atualizando", "Finalizado", "Sem informação"
    };

    String[] tipoArray = {
            "Manga", "Livro"
    };
    private Uri imageUri;
    private String[] cameraPermission;
    private String[] storagePermission;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;
    private ImageView civImageCad;

    private EditText etdPublicacao, etdFinalizacao;

    TextView tvIsbn, tvTitulo, tvAutor, tvCategorias, tvTipo, tvStatus,tvSinopse, tvEditora
            ,tvSubitulo,tvPaginas, tvTraducao;
    String  titulo, subtitulo, sinopse, autor, editora, dataPublicacao, dataFinalizacao, isbn
            ,paginas, traducao, tipo, status, categorias;
    Float raiting;
    private RatingBar rbAvaliacao;
    private Button btnCadastrarObra, btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        civImageCad = (ImageView)rootView.findViewById(R.id.imgCadastroObra);
        tvTitulo = (TextView)rootView.findViewById(R.id.txtCadastroObraTitulo);
        tvSubitulo = (TextView)rootView.findViewById(R.id.txtCadastroObraSubTitulo);
        tvSinopse = (TextView)rootView.findViewById(R.id.txtCadastroObraSinopse);
        tvAutor = (TextView)rootView.findViewById(R.id.txtCadastroObraAutor);
        tvEditora = (TextView)rootView.findViewById(R.id.txtCadastroObraEditora);
        etdPublicacao = (EditText)rootView.findViewById(R.id.txtCadastroObraDataPublic);
        etdFinalizacao = (EditText)rootView.findViewById(R.id.txtCadastroObraDataFinal);
        tvIsbn = (TextView)rootView.findViewById(R.id.txtCadastroObraISBN);
        tvPaginas = (TextView)rootView.findViewById(R.id.txtCadastroObraPaginas);
        tvTraducao = (TextView)rootView.findViewById(R.id.txtCadastroObraTraducao);
        tvTipo = (TextView)rootView.findViewById(R.id.txtCadastroObraTipo);
        tvStatus = (TextView)rootView.findViewById(R.id.txtCadastroObraStatus);
        tvCadastroObraCategorias = (TextView)rootView.findViewById(R.id.txtCadastroObraCategorias);
        rbAvaliacao = (RatingBar)rootView.findViewById(R.id.rbCadastroObraAvaliacao);
        btnCadastrarObra = (Button)rootView.findViewById(R.id.btnCadastroObraCadastrar);
        btnLogout = (Button)rootView.findViewById(R.id.btnLogoutDetalhesObra);
        tvCadastroObraCategorias.setKeyListener(null);
        selectedCategorias = new boolean[categoriaArray.length];
        tvTipo.setKeyListener(null);
        tvStatus.setKeyListener(null);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        etdFinalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("final");
            }
        });
        etdPublicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("public");
            }
        });
        tvTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Categoria Selecionada");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(tipoArray, selectedTipo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Atualizar a seleção do item
                        selectedTipo = i;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Atualizar o TextView com o item selecionado
                        tvTipo.setText(tipoArray[selectedTipo]);
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Status Selecionado");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(statusArray, selectedStatus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Atualizar a seleção do item
                        selectedStatus = i;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Atualizar o TextView com o item selecionado
                        tvStatus.setText(statusArray[selectedStatus]);
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        tvCadastroObraCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Categoria Selecionada");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(categoriaArray, selectedCategorias, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean b) {
                        if (b){
                            categoriasList.add(i);
                            Collections.sort(categoriasList);
                        }
                        else{
                            categoriasList.remove((Integer) i);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j=0; j<categoriasList.size(); j++){
                            stringBuilder.append(categoriaArray[categoriasList.get(j)]);

                            if (j != categoriasList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        tvCadastroObraCategorias.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Limpar Todos", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        for (int j=0; j< selectedCategorias.length; j++){
                            selectedCategorias[j] = false;
                            categoriasList.clear();
                            tvCadastroObraCategorias.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
        civImageCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        btnCadastrarObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarObra();
            }
        });

        return rootView;
    }

    private void showDatePicker(String tipoData) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        if (tipoData == "public"){
                            etdPublicacao.setText(selectedDate);
                        }else{
                            etdFinalizacao.setText(selectedDate);
                        }
                    }
                }, year, month, day);

        datePickerDialog.show();
    }


    public void cadastrarObra(){
        titulo = tvTitulo.getText().toString();
        subtitulo = tvSubitulo.getText().toString();
        sinopse = tvSinopse.getText().toString();
        autor = tvAutor.getText().toString();
        editora = tvEditora.getText().toString();
        dataPublicacao = etdPublicacao.getText().toString();
        dataFinalizacao = etdFinalizacao.getText().toString();
        isbn = tvIsbn.getText().toString();
        paginas = tvPaginas.getText().toString();
        traducao = tvTraducao.getText().toString();
        tipo = tvTipo.getText().toString();
        status = tvStatus.getText().toString();
        categorias = tvCadastroObraCategorias.getText().toString();

        raiting = rbAvaliacao.getRating();

        if(titulo.isEmpty()){
            Toast.makeText(getContext(),"Título é um campo obrigatório!", Toast.LENGTH_LONG).show();
        }else if (autor.isEmpty()){
            Toast.makeText(getContext(),"Autor é um campo obrigatório!", Toast.LENGTH_LONG).show();
        }else if (sinopse.isEmpty()){
            Toast.makeText(getContext(),"Sinopse é um campo obrigatório!", Toast.LENGTH_LONG).show();
        }else if (tipo.isEmpty()){
            Toast.makeText(getContext(),"Tipo é um campo obrigatório!", Toast.LENGTH_LONG).show();
        }else if (status.isEmpty()){
            Toast.makeText(getContext(),"Status é um campo obrigatório!", Toast.LENGTH_LONG).show();
        }else if (categorias.isEmpty()){
            Toast.makeText(getContext(),"Categorias é um campo obrigatório!", Toast.LENGTH_LONG).show();
        }else{
            String imageFilePath = null;
            if (imageUri != null){
                imageFilePath = imageUri.getPath();
            }
            UploadImageTask uploadImageTask = new UploadImageTask(getContext(), imageFilePath);
            uploadImageTask.execute();
        }
    }
    public void inserirObra(String title, String subtitle, String synopsis, String author, String editora, String dataPublicacao, String dataFinalizacao, String isbn, String paginas, String image, String traducao, String tipo, String avarageRating, String statusObra, String categorias) {
        InsertObrasApi.InsertUsuarioAsyncTask task = new InsertObrasApi.InsertUsuarioAsyncTask(
                title, subtitle, synopsis, author, editora, dataPublicacao, dataFinalizacao, isbn,
                paginas, image, traducao, tipo, avarageRating, statusObra, categorias,
                new InsertObrasApi.InsertUsuarioListener() {
                    @Override
                    public void onInsertBookReceived(boolean success) {
                        if (success) {
                        } else {
                            Toast.makeText(getContext(),"Ops! Algo deu errado!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        task.execute();

        ObrasListSingleton obrasSingleton = ObrasListSingleton.getInstance();
        ObrasListSingleton.getInstance().resetInstance();
        tvTitulo.setText("");
        tvSubitulo.setText("");
        tvSinopse.setText("");
        tvAutor.setText("");
        tvEditora.setText("");
        etdPublicacao.setText("");
        etdFinalizacao.setText("");
        tvIsbn.setText("");
        tvPaginas.setText("");
        tvTraducao.setText("");
        tvTipo.setText("");
        tvStatus.setText("");
        tvCadastroObraCategorias.setText("");
        rbAvaliacao.setRating(2.5f);
        ibookApi.getBookList(new ibookApi.BookListListener() {
            @Override
            public void onBookListReceived(List<obrasDTO> bookList) {
                ObrasListSingleton obrasSingleton = ObrasListSingleton.getInstance();
                for (obrasDTO obra : bookList) {
                    obrasSingleton.adicionarObra(obra);
                }
            }
        });

        Toast.makeText(getContext(),"Obra cadastrada com sucesso!", Toast.LENGTH_LONG).show();
    }


    private class UploadImageTask extends AsyncTask<Void, Void, String> {
        private String imageFilePath;
        private Context context;

        public UploadImageTask(Context context, String imageFilePath) {
            this.context = context;
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
                    compressedBitmap = Glide.with(context)
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
                    File compressedImageFile = new File(context.getCacheDir(), "compressed_image.jpg");
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
            if (finalPath == null || finalPath.equals("")) {
                finalPath = null;
            }
            inserirObra(titulo, subtitulo, sinopse, autor, editora, dataPublicacao, dataFinalizacao,
                    isbn, paginas, finalPath, traducao, tipo, raiting.toString(), status, categorias);
        }
    }


    public void logout(){
        Utils.logout();
        Intent acessar = new Intent(getActivity(), telalogin.class);
        startActivity(acessar);
    }
    public void showImagePickerDialog() {
        //option for dialog
        String options[] = {"Camera","Galeria"};
        // Alert dialog builder
        AlertDialog.Builder builder  = new AlertDialog.Builder(requireActivity());
        //setTitle
        builder.setTitle("Escolha uma opção");
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
        //ContentValues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");
        //save imageUri
        imageUri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_FROM_CAMERA_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result & result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(requireActivity(),cameraPermission,CAMERA_PERMISSION_CODE); // handle request permission on override method
    }

    //check storage permission
    private boolean checkStoragePermission(){
        boolean result1 = ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(requireActivity(),storagePermission,STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_FROM_GALLERY_CODE) {
                // picked image from gallery
                // crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(requireContext(), this);
            } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                // picked image from camera
                // crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(requireContext(), this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                // cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                civImageCad.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // for error handling
                Toast.makeText(requireContext(), "Algo deu errado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}