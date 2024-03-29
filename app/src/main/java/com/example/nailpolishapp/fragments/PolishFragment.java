package com.example.nailpolishapp.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailpolishapp.R;
import com.example.nailpolishapp.databinding.FragmentPolishBinding;
import com.example.nailpolishapp.models.Polish;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class PolishFragment extends Fragment {

    FragmentPolishBinding binding;
    ArrayList<Polish> polishArrayList = new ArrayList<>();
    Bitmap selectedImageBitmap;
    PolishListAdapter adapter;




    public PolishFragment() {
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
        binding = FragmentPolishBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Search function
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
                adapter.setFilter(newList);
                return true;
            } });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My List");
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PolishListAdapter(polishArrayList);
        binding.recyclerView.setAdapter(adapter);
        getPolishList();



    }


    //Get Polish list from Firebase store
    public void getPolishList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("Polish").document(user.getUid()).collection("PolishDetail")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    polishArrayList.clear();
                    for (QueryDocumentSnapshot polishDoc : value) {
                        Polish polish = polishDoc.toObject(Polish.class);
                        polishArrayList.add(polish);
                        adapter.notifyDataSetChanged();
                        Log.d("test", "onEvent: "+polishArrayList);
                    }
                });
    }


    //Custom Adapter
    class PolishListAdapter extends RecyclerView.Adapter<PolishListAdapter.PolishListViewHolder>{
        ArrayList<Polish> polishArrayList = new ArrayList<>();

        public PolishListAdapter(ArrayList<Polish> data) {
            this.polishArrayList = data;
        }

        @NonNull
        @Override
        public PolishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.polish_item, parent, false);
            PolishListAdapter.PolishListViewHolder polishListViewHolder = new PolishListViewHolder(view);

            return polishListViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PolishListViewHolder holder, int position) {
            Polish polish = polishArrayList.get(position);

            holder.polish = polish;

            holder.polishName.setText(polish.getName());
            holder.polishType.setText(polish.getType().toString());
            holder.date.setText(DateFormat.getDateInstance().format(polish.getCreatedAt()));
            holder.polishImage.setImageBitmap(selectedImageBitmap);
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

        public class PolishListViewHolder extends RecyclerView.ViewHolder{
            TextView polishName, polishType, date;
            ImageView polishImage;
            Polish polish;


            public PolishListViewHolder(@NonNull View itemView) {
                super(itemView);
                polishName = itemView.findViewById(R.id.textViewPolishName);
                polishType = itemView.findViewById(R.id.textViewPolishType);
                date = itemView.findViewById(R.id.textViewDate);
                polishImage = itemView.findViewById(R.id.imageViewPolish);

                //Display polish data in different fragment
                itemView.setOnClickListener(v -> mListener.gotoPolishDetail(polish));
            }
        }


        //filter for search function
        public void setFilter(List<Polish> newList){
            polishArrayList = new ArrayList<>();
            polishArrayList.addAll(newList);
            notifyDataSetChanged();
        }



    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }


    //interface
    PolishFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (PolishFragmentListener) context;
    }

    public interface PolishFragmentListener {
        void gotoPolishDetail(Polish polish);
    }
}