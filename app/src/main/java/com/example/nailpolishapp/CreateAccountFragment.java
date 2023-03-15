package com.example.nailpolishapp;

import android.content.Context;
import android.content.DialogInterface;
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


import com.example.nailpolishapp.databinding.FragmentCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
  /*      binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.login();
            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()||password.length()<8) {
                    Toast.makeText(getActivity(), "Enter valid password! Password length must be at least 8 characters", Toast.LENGTH_SHORT).show();
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        //getCurrentUser is from cache in app; not an API call
                                        //can also refresh cache

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            mListener.gotoMenu();
                                                            Log.d(TAG, "onComplete: User profile updated.");
                                                        } else {
                                                            Log.d(TAG, "onComplete: Error updating profile");
                                                        }
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(getActivity(), "Can't sign up!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        getActivity().setTitle("Create New Account");
*/


        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.login();
            }
        });


        //Registration using email password
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                String name = binding.editTextName.getText().toString();

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

                if (email.isEmpty()) {
                    alertBuilder.setTitle(R.string.error)
                            .setMessage("Email is required")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: ");
                                }
                            });
                    alertBuilder.create().show();
                } else if (password.isEmpty()) {
                    alertBuilder.setTitle(R.string.error)
                            .setMessage("Password is at least 8 character")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: ");
                                }
                            });
                    alertBuilder.create().show();
                } else if (name.isEmpty()) {
                    alertBuilder.setTitle(R.string.error)
                            .setMessage("Name is required")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: ");
                                }
                            });
                    alertBuilder.create().show();
                } else {

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                                            HashMap<String, Object> data = new HashMap<>();
                                            data.put("name", name);
                                            data.put("uid", mAuth.getCurrentUser().getUid());
                                            //data.put("isOnline", true);

                                            db.collection("users").document(mAuth.getCurrentUser().getUid()).set(data)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast toast = Toast.makeText(getContext(), "Register Successful!", Toast.LENGTH_LONG);
                                                            toast.show();
                                                            mListener.gotoMenu();
                                                        }
                                                    });
                                        } else {
                                            alertBuilder.setTitle(R.string.error)
                                                    .setMessage("Error in creating user! Try again!")
                                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Log.d(TAG, "onClick: ");
                                                        }
                                                    });
                                        }
                                    }
                                });
                            //Handle user invalid inputs
                            } else {
                                alertBuilder.setTitle(R.string.error)
                                        .setMessage(task.getException().toString())
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Log.d(TAG, "onClick: ");
                                            }
                                        });
                                alertBuilder.create().show();
                            }
                        }
                    });

                }
            }
        });

        getActivity().setTitle("Sign Up");
    }

    CreateAccountListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateAccountListener) context;
    }

    interface CreateAccountListener {
        void login();
        void gotoMenu();
    }


}