package com.example.ibookApp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import com.example.ibookApp.R;
import com.example.ibookApp.functions.Utils;
import com.example.ibookApp.telas.telalogin;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

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
    boolean[] selectedCategorias,selectedTipo,selectedStatus ;
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

    TextView tvIsbn, tvTitulo, tvAutor, tvCategorias, tvTipo, tvStatus,tvSinopse, tvEditora;
    String isbn, titulo, autor, categorias, sinopse, editora;
    Float raiting;
    private RatingBar rbAvaliacao;
    private Button btnCadastrarObra;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        tvCadastroObraCategorias = (TextView)rootView.findViewById(R.id.txtCadastroObraCategorias);
        tvIsbn = (TextView)rootView.findViewById(R.id.txtCadastroObraISBN);
        tvEditora = (TextView)rootView.findViewById(R.id.txtCadastroObraEditora);
        tvTitulo = (TextView)rootView.findViewById(R.id.txtCadastroObraTitulo);
        //tvCategorias = (TextView)rootView.findViewById(R.id.txtCadastroObraCategorias);
        tvAutor = (TextView)rootView.findViewById(R.id.txtCadastroObraAutor);
        tvSinopse = (TextView)rootView.findViewById(R.id.txtCadastroObraSinopse);
        tvTipo = (TextView)rootView.findViewById(R.id.txtCadastroObraTipo);
        tvStatus = (TextView)rootView.findViewById(R.id.txtCadastroObraStatus);
        rbAvaliacao = (RatingBar)rootView.findViewById(R.id.rbCadastroObraAvaliacao);
        civImageCad = (ImageView)rootView.findViewById(R.id.imgCadastroObra);
        btnCadastrarObra = (Button)rootView.findViewById(R.id.btnCadastroObraCadastrar);
        etdPublicacao = (EditText)rootView.findViewById(R.id.txtCadastroObraDataPublic);
        etdFinalizacao = (EditText)rootView.findViewById(R.id.txtCadastroObraDataFinal);
        tvCadastroObraCategorias.setKeyListener(null);
        selectedCategorias = new boolean[categoriaArray.length];
        tvTipo.setKeyListener(null);
        selectedTipo = new boolean[tipoArray.length];
        tvStatus.setKeyListener(null);
        selectedStatus = new boolean[statusArray.length];

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
                builder.setMultiChoiceItems(tipoArray, selectedTipo, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean b) {
                        if (b){
                            tipoList.add(i);
                            Collections.sort(tipoList);
                        }
                        else{
                            tipoList.remove(i);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j=0; j<tipoList.size(); j++){
                            stringBuilder.append(tipoArray[tipoList.get(j)]);

                            if (j != tipoList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        tvTipo.setText(stringBuilder.toString());
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
                        for (int j=0; j< selectedTipo.length; j++){
                            selectedTipo[j] = false;
                            tipoList.clear();
                            tvTipo.setText("");
                        }
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
                builder.setMultiChoiceItems(statusArray, selectedStatus, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean b) {
                        if (b){
                            statusList.add(i);
                            Collections.sort(statusList);
                        }
                        else{
                            statusList.remove(i);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j=0; j<statusList.size(); j++){
                            stringBuilder.append(statusArray[statusList.get(j)]);

                            if (j != statusList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        tvStatus.setText(stringBuilder.toString());
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
                        for (int j=0; j< selectedStatus.length; j++){
                            selectedStatus[j] = false;
                            statusList.clear();
                            tvStatus.setText("");
                        }
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
                            categoriasList.remove(i);
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
        isbn = tvIsbn.getText().toString();
        titulo = tvTitulo.getText().toString();
        autor = tvAutor.getText().toString();
        categorias = tvCategorias.getText().toString();
        sinopse = tvSinopse.getText().toString();
        editora = tvEditora.getText().toString();
        raiting = rbAvaliacao.getRating();

        if(!titulo.isEmpty() &&!autor.isEmpty() &&!categorias.isEmpty() &&!sinopse.isEmpty()){

        }
        else{
            Toast.makeText(getContext(),"", Toast.LENGTH_LONG).show();
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