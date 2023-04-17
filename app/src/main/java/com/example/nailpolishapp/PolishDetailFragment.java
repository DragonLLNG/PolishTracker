package com.example.nailpolishapp;

import static com.example.nailpolishapp.PolishFragment.decodeFromFirebaseBase64;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nailpolishapp.databinding.FragmentPolishDetailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

public class PolishDetailFragment extends Fragment {

    private static final String ARG_PARAM_POLISH = "param_polish";





    private Polish mParamPolish;

    FragmentPolishDetailBinding binding;
    public PolishDetailFragment() {
        // Required empty public constructor
    }




    public static PolishDetailFragment newInstance(Polish mParamPolish) {
        PolishDetailFragment fragment = new PolishDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_POLISH, mParamPolish);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamPolish = (Polish) getArguments().getSerializable(ARG_PARAM_POLISH);
        }
        setHasOptionsMenu(true);

    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                mListener.goSearch();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPolishDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        binding.textViewPolishName.setText(mParamPolish.getName());
        binding.textViewPolishType.setText(mParamPolish.getType().toString());
        binding.textViewDate.setText(mParamPolish.getCreatedAt().toString());
        if(mParamPolish.comment!=null) {
            binding.textViewComment.setText(mParamPolish.getComment());
        }
        else{
            binding.textViewComment.setText("");
        }


        if (mParamPolish.imageURL!=null && !mParamPolish.imageURL.contains("http")) {
            try {
                Bitmap imageBitmap = decodeFromFirebaseBase64(mParamPolish.imageURL);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                        imageBitmap, 200, 200, false);

                binding.imageViewPolish.setImageBitmap(resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Picasso.get()
                    .load(mParamPolish.imageURL)
//                    .resize(MAX_WIDTH, MAX_HEIGHT)
//                        .centerCrop()
                    .into(binding.imageViewPolish);
        }

        if (mParamPolish.liked==true){
            binding.imageViewFavorite.setImageResource(R.drawable.ic_heart_favorite);
        }
        else {
            binding.imageViewFavorite.setImageResource(R.drawable.ic_heart_not_favorite);
        }



        binding.imageViewPolish.setOnClickListener(new DoubleClickListener() {
            Boolean clicked = true;
            @Override
            public void onSingleClick(View v) {
                clicked = true;
            }

            @Override
            public void onDoubleClick(View v) {
                if (clicked) {
                    clicked = false;
                    binding.imageViewFavorite.setImageResource(R.drawable.ic_heart_favorite);
                    FirebaseFirestore.getInstance()
                            .collection("Polish").document(mAuth.getCurrentUser().getUid())
                            .collection("PolishDetail").document(mParamPolish.id)
                            .update("liked", true).addOnCompleteListener(task -> {

                            });
                } else {
                    clicked = true;
                    binding.imageViewFavorite.setImageResource(R.drawable.ic_heart_not_favorite);
                }
            }
        });



        binding.imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            Boolean clicked = true;
            @Override
            public void onClick(View v) {

                if (clicked) {
                    clicked = false;
                    binding.imageViewFavorite.setImageResource(R.drawable.ic_heart_favorite);
                    FirebaseFirestore.getInstance()
                            .collection("Polish").document(mAuth.getCurrentUser().getUid())
                            .collection("PolishDetail").document(mParamPolish.id)
                            .update("liked", true).addOnCompleteListener(task -> {

                            });
                } else {
                    clicked = true;
                    binding.imageViewFavorite.setImageResource(R.drawable.ic_heart_not_favorite);
                }

            }
        });

        binding.imageViewDelete.setOnClickListener(v -> {
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            FirebaseFirestore.getInstance()
                    .collection("Polish").document(mAuth.getCurrentUser().getUid())
                    .collection("PolishDetail").document(mParamPolish.id)
                    .delete().addOnCompleteListener(task -> {

                        progressDialog.dismiss();
                        Toast
                                .makeText(getActivity(),
                                        "Delete Successful",
                                        Toast.LENGTH_SHORT)
                                .show();
                        mListener.gotoList();


                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast
                                .makeText(getActivity(),
                                        "Fail!!",
                                        Toast.LENGTH_SHORT)
                                .show();

                    });
        });



        if (mParamPolish.comment==null){
            binding.textViewComment.setVisibility(View.INVISIBLE);
        }
        else {
            binding.textViewComment.setVisibility(View.VISIBLE);
            binding.buttonAddComment.setText("Edit Comment");
            binding.editTextNote.setHint("Comment");
        }


        binding.buttonAddComment.setOnClickListener(v -> {
            binding.textViewComment.setVisibility(View.VISIBLE);
            String note = binding.editTextNote.getText().toString();
            ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            FirebaseFirestore.getInstance()
                    .collection("Polish").document(mAuth.getCurrentUser().getUid())
                    .collection("PolishDetail").document(mParamPolish.id)
                    .update("comment",note).addOnCompleteListener(task -> {

                        binding.textViewComment.setText(note);
                        progressDialog.dismiss();
                        Toast
                                .makeText(getActivity(),
                                        "Comment Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                        binding.editTextNote.setText("");

                    })
                    .addOnFailureListener(e -> {
                        // Error, Comment not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(getActivity(),
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    });


        });

        binding.imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Handle share action
                Random rand = new Random();
                int randNo = rand.nextInt(100000);
                String imgBitmapPath = null;
                try {
                    imgBitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                            decodeFromFirebaseBase64(mParamPolish.imageURL), "IMG:" + randNo, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri imgBitmapUri = Uri.parse(imgBitmapPath);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                shareIntent.setType("image/png");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mParamPolish.name);
                startActivity(Intent.createChooser(shareIntent, "Share with"));


            }
        });


    }



    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }





    DetailListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (DetailListener) context;
    }

    interface DetailListener {
        void gotoList();
        void goSearch();

    }
}