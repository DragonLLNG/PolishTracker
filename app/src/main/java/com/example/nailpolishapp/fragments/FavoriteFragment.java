package com.example.nailpolishapp.fragments;

import static com.example.nailpolishapp.fragments.PolishFragment.decodeFromFirebaseBase64;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailpolishapp.R;
import com.example.nailpolishapp.databinding.FragmentFavoriteBinding;
import com.example.nailpolishapp.models.Polish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FavoriteFragment extends Fragment {

    FragmentFavoriteBinding binding;
    ArrayList<Polish> polishArrayList = new ArrayList<>();
    FavoritePolishListAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public FavoriteFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }



    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Favorite", "onQueryTextSubmit: "+query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Favorite", "onQueryTextSubmit: "+newText);
                newText = newText.toLowerCase();
                ArrayList<Polish> newList = new ArrayList<>();
                for (Polish polish: polishArrayList){
                    String polishName = polish.getName().toLowerCase();
                    if (polishName.contains(newText)){
                        newList.add(polish);
                    }
                }
                adapter.setFilterFavorite(newList);
                return true;
            } });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getActivity().setTitle("My Favorite List");

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);


        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoritePolishListAdapter(polishArrayList);
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));


        db.collection("Polish").document(user.getUid()).collection("PolishDetail")
                .whereEqualTo("liked", true)
                //.orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        polishArrayList.clear();
                        for (QueryDocumentSnapshot polishDoc : value) {
                            Polish polish = polishDoc.toObject(Polish.class);
                            polishArrayList.add(polish);
                            adapter.notifyDataSetChanged();
                            Log.d("test", "onEvent: "+polishArrayList);
                            bottomNavigationView.getOrCreateBadge(R.id.favorite).setNumber(polishArrayList.size());
                        }
                    }
                });

    }


    //Custom Adapter
    class FavoritePolishListAdapter extends RecyclerView.Adapter<FavoritePolishListAdapter.FavoritePolishListViewHolder>{
        ArrayList<Polish> polishArrayList = new ArrayList<>();
        public FavoritePolishListAdapter(ArrayList<Polish> data) {
            this.polishArrayList = data;
        }


        @NonNull
        @Override
        public FavoritePolishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
            FavoritePolishListViewHolder favoritePolishListViewHolder = new FavoritePolishListViewHolder(view);
            return favoritePolishListViewHolder;
        }



        @Override
        public void onBindViewHolder(@NonNull FavoritePolishListViewHolder holder, int position) {
            Polish polish = polishArrayList.get(position);
            holder.polish = polish;
            holder.polishName.setText(polish.getName());

            if (polish.getImageURL() !=null && !polish.getImageURL().contains("http")) {
                try {
                    Bitmap imageBitmap = decodeFromFirebaseBase64(polish.getImageURL());
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                            imageBitmap, 200, 200, false);

                    holder.polishImage.setImageBitmap(resizedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                Picasso.get()
                        .load(polish.getImageURL())
                        .into(holder.polishImage);
            }
        }

        @Override
        public int getItemCount() {
            return polishArrayList.size();
        }

        public class FavoritePolishListViewHolder extends RecyclerView.ViewHolder{
            TextView polishName;
            ImageView polishImage, remove, share;
            Polish polish;


            public FavoritePolishListViewHolder(@NonNull View itemView) {
                super(itemView);
                polishName = itemView.findViewById(R.id.textViewPolishName);
                polishImage = itemView.findViewById(R.id.imageViewPolish);
                remove = itemView.findViewById(R.id.imageViewDelete);
                share = itemView.findViewById(R.id.imageViewShare);

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore.getInstance()
                                .collection("Polish").document(user.getUid())
                                .collection("PolishDetail").document(polish.getId())
                                .update("liked", false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        adapter.notifyDataSetChanged();


                                    }
                                });

                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Handle share action
                        Random rand = new Random();
                        int randNo = rand.nextInt(100000);
                        String imgBitmapPath = null;
                        try {
                            imgBitmapPath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                                    decodeFromFirebaseBase64(polish.getImageURL()), "IMG:" + randNo, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri imgBitmapUri = Uri.parse(imgBitmapPath);

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                        shareIntent.setType("image/png");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, polish.getName());
                        startActivity(Intent.createChooser(shareIntent, "Share with"));
                    }
                });
            }
        }
        //filter for search function
        public void setFilterFavorite(List<Polish> newList){
            polishArrayList = new ArrayList<>();
            polishArrayList.addAll(newList);
            notifyDataSetChanged();
        }
    }

    FavoriteListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (FavoriteListListener) context;
    }

    public interface FavoriteListListener {
        void gotoMenu();
    }
}