package com.example.nailpolishapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.nailpolishapp.R;
import com.example.nailpolishapp.databinding.FragmentCreateAccountBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class CreateAccountFragment extends Fragment {

    private final String TAG="final";
    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentCreateAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.buttonCancel.setOnClickListener(v -> mListener.login());


        //Registration using email password
        binding.buttonRegister.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String name = binding.editTextName.getText().toString();

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

            if (email.isEmpty()) {
                alertBuilder.setTitle(R.string.error)
                        .setMessage("Email is required")
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                alertBuilder.create().show();
            } else if (password.isEmpty()) {
                alertBuilder.setTitle(R.string.error)
                        .setMessage("Password is at least 8 character")
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                alertBuilder.create().show();
            } else if (name.isEmpty()) {
                alertBuilder.setTitle(R.string.error)
                        .setMessage("Name is required")
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                alertBuilder.create().show();
            } else {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("name", name);
                                data.put("uid", mAuth.getCurrentUser().getUid());
                                //data.put("isOnline", true);

                                db.collection("users").document(mAuth.getCurrentUser().getUid()).set(data)
                                        .addOnCompleteListener(task1 -> {
                                            Toast toast = Toast.makeText(getContext(), "Register Successful!", Toast.LENGTH_LONG);
                                            toast.show();
                                            mListener.gotoMenu();
                                        });
                            } else {
                                alertBuilder.setTitle(R.string.error)
                                        .setMessage("Error in creating user! Try again!")
                                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                            }
                        });
                    //Handle user invalid inputs
                    } else {
                        alertBuilder.setTitle(R.string.error)
                                .setMessage(task.getException().toString())
                                .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                        alertBuilder.create().show();
                    }
                });

            }
        });

        //Bottom navbar not in create account
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.INVISIBLE);

        getActivity().setTitle("Sign Up");
    }

    CreateAccountListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateAccountListener) context;
    }

    public interface CreateAccountListener {
        void login();
        void gotoMenu();
    }


}