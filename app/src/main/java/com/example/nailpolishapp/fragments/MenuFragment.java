package com.example.nailpolishapp.fragments;

import static com.example.nailpolishapp.fragments.PolishFragment.decodeFromFirebaseBase64;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nailpolishapp.R;
import com.example.nailpolishapp.databinding.FragmentMenuBinding;
import com.example.nailpolishapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MenuFragment extends Fragment {

    FragmentMenuBinding binding;
    Bitmap selectedImageBitmap;
    String imageEncoded;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference docRef = db.collection("users").document(user.getUid());



    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);





        docRef.get().addOnSuccessListener(documentSnapshot -> {
            User userDoc = documentSnapshot.toObject(User.class);
            Log.d("User Test", "onSuccess: " + userDoc.getName());
            binding.textViewUserName.setText(String.format("Hi %s!", userDoc.getName()));

            if (userDoc.getProfileImageURL() ==null){
                //Profile handling
                binding.imageViewProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        launchSomeActivity.launch(i);


                    }
                });

            }
            else{

                if (userDoc.getProfileImageURL() !=null && !userDoc.getProfileImageURL().contains("http")) {
                    try {
                        Bitmap imageBitmap = decodeFromFirebaseBase64(userDoc.getProfileImageURL());
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                imageBitmap, 200, 200, false);

                        binding.imageViewProfile.setImageBitmap(resizedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    Picasso.get()
                            .load(userDoc.getProfileImageURL())
//                    .resize(MAX_WIDTH, MAX_HEIGHT)
//                        .centerCrop()
                            .into(binding.imageViewProfile);
                }

            }








        });




        //go to PolishFragment
        binding.buttonList.setOnClickListener(v -> mListener.gotoList());


        //go to AddOnFragment
        binding.buttonAddOn.setOnClickListener(v -> mListener.gotoAddOn());


        //go to FavoriteFragment
        binding.buttonFavorite.setOnClickListener(v -> mListener.gotoFavorites());

        //log out
        binding.buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mListener.login();

        });




    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();

                // uploadImage();
                Log.d("Image", ": " + selectedImageUri);
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                binding.imageViewProfile.setImageBitmap(selectedImageBitmap);
                binding.imageViewProfile.getLayoutParams().height = 400;
                binding.imageViewProfile.getLayoutParams().width = 400;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                docRef.update("profileImageURL", imageEncoded).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast
                                .makeText(getActivity(),
                                        "Profile Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast
                                .makeText(getActivity(),
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });





            }
        }
    });





    MenuListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MenuListener) context;
    }

    public interface MenuListener{
        void gotoList();
        void gotoAddOn();
        void gotoFavorites();
        void login();
       // void goSearch();

    }

}