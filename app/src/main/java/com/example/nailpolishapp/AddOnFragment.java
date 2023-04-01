package com.example.nailpolishapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nailpolishapp.databinding.FragmentAddOnBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddOnFragment extends Fragment {
    private final String TAG="Addon";

    FragmentAddOnBinding binding;
    Bitmap selectedImageBitmap;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DocumentReference polishDocRef = FirebaseFirestore.getInstance()
            .collection("Polish").document(mAuth.getCurrentUser().getUid());
    DocumentReference polishDetailDocRef = FirebaseFirestore.getInstance()
            .collection("Polish").document(mAuth.getCurrentUser().getUid())
            .collection("PolishDetail").document();
    Polish polish = new Polish();
    private Uri selectedImageUri;


    FirebaseStorage storage;
    StorageReference storageReference;

    public AddOnFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddOnBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                mListener.gotoMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Add Polish");

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

        binding.buttonAddOnPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String polishName = binding.editTextPolishName.getText().toString();


                ArrayList<Polish> polishArrayList = new ArrayList<>();


                polish.setId(polishDetailDocRef.getId());


                if (polishName.isEmpty()) {
                    alertBuilder.setTitle(R.string.error)
                            .setMessage("Please enter a polish name")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: ");
                                }
                            });
                    alertBuilder.create().show();
                } else
                {
                    polish.setName(polishName);
                    polish.setCreatedAt(new Date());


                    ArrayList<String> polishType = new ArrayList<>();

                    if (!binding.checkboxButtonRegular.isChecked() &&
                            !binding.checkboxButtonGel.isChecked() &&
                            !binding.checkboxButtonDipping.isChecked()) {

                        alertBuilder.setTitle(R.string.error)
                                .setMessage("What type of polish is it?")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.d(TAG, "onClick: ");
                                    }
                                });
                        alertBuilder.create().show();
                    } else {
                        if (binding.checkboxButtonRegular.isChecked()) {
                            polishType.add("Regular");
                        }
                        if (binding.checkboxButtonGel.isChecked()) {
                            polishType.add("Gel");
                        }
                        if (binding.checkboxButtonDipping.isChecked()) {
                            polishType.add("Dipping Powder");
                        }

                        polish.setType(polishType);
                        polish.setUserID(mAuth.getCurrentUser().getUid());

                        polishArrayList.add(polish);


                        HashMap<String, Object> polishData = new HashMap<>();

                        polishData.put("userId", mAuth.getCurrentUser().getUid());
                        polishData.put("polishList", polishArrayList);


                        polishDocRef.set(polishData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    polishDetailDocRef.set(polish).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                alertBuilder.setTitle("Success")
                                                        .setMessage("Add on successful")
                                                        .setPositiveButton("Add more", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                mListener.gotoAddOn();

                                                            }
                                                        })
                                                        .setNegativeButton("List", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                mListener.gotoList();
                                                            }
                                                        });
                                                alertBuilder.create().show();


                                            } else {
                                                Toast.makeText(getActivity(), "Error creating a Polish instance", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    Log.d("Polish", "onComplete: " + polish.name);
                                } else {
                                    Toast.makeText(getActivity(), "Error creating a Polish instance", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }




                }



            }

        });


        binding.imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                launchSomeActivity.launch(i);

            }
        });


        //Cancel button
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoMenu();
            }
        });


    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();

                // uploadImage();
                Log.d("Image", ": " + selectedImageUri);
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*
                binding.imageViewUpload.setImageBitmap(selectedImageBitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                polish.imageURL = imageEncoded;
                
                 */

            }
        }
    });

    AddOnListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (AddOnListener) context;
    }

    interface AddOnListener {
        void gotoList();

        void gotoMenu();

        void gotoAddOn();
    }


}