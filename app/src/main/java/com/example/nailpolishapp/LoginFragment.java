package com.example.nailpolishapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.nailpolishapp.databinding.FragmentLoginBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private final String TAG="final";
    public FirebaseAuth mAuth;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.buttonLogin.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            if(email.isEmpty()){
                alertBuilder.setTitle(R.string.error)
                        .setMessage("Email is required")
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                alertBuilder.create().show();
            } else if (password.isEmpty()){
                alertBuilder.setTitle(R.string.error)
                        .setMessage("Password is required")
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                alertBuilder.create().show();
            } else {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), task -> {
                            if(task.isSuccessful()) {
                                mListener.gotoMenu();
                            } else {
                                alertBuilder.setTitle(R.string.error)
                                        .setMessage("Can't login! Wrong email or password!")
                                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> Log.d(TAG, "onClick: "));
                                alertBuilder.create().show();
                            }
                        });
            }
        });

        binding.buttonCreateNewAccount.setOnClickListener(v -> mListener.createNewAccount());

        //Bottom navbar not in login
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.INVISIBLE);

        getActivity().setTitle(R.string.login_label);

    }



    public LoginListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (LoginListener) context;
    }




    public interface LoginListener {
        void createNewAccount();
        void gotoMenu();
    }
}