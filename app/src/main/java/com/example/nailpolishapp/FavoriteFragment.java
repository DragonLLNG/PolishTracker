package com.example.nailpolishapp;

import static com.example.nailpolishapp.PolishFragment.decodeFromFirebaseBase64;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailpolishapp.databinding.FragmentFavoriteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoritePolishListAdapter(polishArrayList);
        binding.recyclerView.setAdapter(adapter);




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
            holder.polishType.setText(polish.getType().toString());
            holder.date.setText(polish.getCreatedAt().toString());



            if (polish.imageURL!=null && !polish.imageURL.contains("http")) {
                try {
                    Bitmap imageBitmap = decodeFromFirebaseBase64(polish.imageURL);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                            imageBitmap, 200, 200, false);

                    holder.polishImage.setImageBitmap(resizedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                Picasso.get()
                        .load(polish.imageURL)
                        .into(holder.polishImage);
            }



        }

        @Override
        public int getItemCount() {
            return polishArrayList.size();
        }

        public class FavoritePolishListViewHolder extends RecyclerView.ViewHolder{
            TextView polishName, polishType, date;
            ImageView polishImage;
            Button remove;
            Polish polish;


            public FavoritePolishListViewHolder(@NonNull View itemView) {
                super(itemView);
                polishName = itemView.findViewById(R.id.textViewPolishName);
                polishType = itemView.findViewById(R.id.textViewPolishType);
                date = itemView.findViewById(R.id.textViewDate);
                polishImage = itemView.findViewById(R.id.imageViewPolish);
                remove = itemView.findViewById(R.id.button_remove);


                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore.getInstance()
                                .collection("Polish").document(user.getUid())
                                .collection("PolishDetail").document(polish.id)
                                .update("liked", false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        adapter.notifyDataSetChanged();

                                    }
                                });

                    }
                });

            }
        }
    }

    FavoriteListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (FavoriteListListener) context;
    }

    interface FavoriteListListener {
        void gotoMenu();

    }
}