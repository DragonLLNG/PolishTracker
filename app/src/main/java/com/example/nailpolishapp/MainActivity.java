package com.example.nailpolishapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, CreateAccountFragment.CreateAccountListener, MenuFragment.MenuListener,
        AddOnFragment.AddOnListener, PolishFragment.PolishFragmentListener, PolishDetailFragment.DetailListener, FavoriteFragment.FavoriteListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        if (mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new MenuFragment())
                    .commit();
        }
    }


    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new CreateAccountFragment())
                .commit();

    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoMenu() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new MenuFragment()).addToBackStack(null)
                .commit();


    }

    @Override
    public void gotoList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new PolishFragment()).addToBackStack(null)
                .commit();

    }

    @Override
    public void gotoAddOn() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new AddOnFragment()).addToBackStack(null)
                .commit();

    }

    @Override
    public void gotoFavorites() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new FavoriteFragment()).addToBackStack(null)
                .commit();

    }


    @Override
    public void gotoPolishDetail(Polish polish) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new PolishDetailFragment().newInstance(polish)).addToBackStack(null)
                .commit();

    }
}