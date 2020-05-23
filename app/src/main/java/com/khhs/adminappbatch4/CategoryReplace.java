package com.khhs.adminappbatch4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import es.dmoral.toasty.Toasty;

public class CategoryReplace extends BottomSheetDialogFragment {
    Spinner findCategory,replaceCategory;
    RadioButton rbtmovieonly,rbtseriesonly,rbtboth;
    Button btnreplace;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.category_replace,container,false);
        findCategory = myView.findViewById(R.id.spFind);
        replaceCategory = myView.findViewById(R.id.spReplace);
        rbtmovieonly =  myView.findViewById(R.id.rbtonlymovie);
        rbtseriesonly = myView.findViewById(R.id.rbtonlyseies);
        rbtmovieonly.setChecked(true);
        rbtboth = myView.findViewById(R.id.rbtboth);
        btnreplace = myView.findViewById(R.id.replaceAll);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference movieRef = db.collection(getString(R.string.movie_str));
        final CollectionReference seriesRef = db.collection(getString(R.string.series_str));
        CollectionReference categoryRef = db.collection(getString(R.string.category_str));
        categoryRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> categoryNames = new ArrayList<>();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                    categoryNames.add(snapshot.toObject(CategoryModel.class).categoryTitle);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        categoryNames
                );
                findCategory.setAdapter(adapter);
                replaceCategory.setAdapter(adapter);

            }
        });
        btnreplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rbtmovieonly.isChecked())
                {
                    movieRef.whereEqualTo("movieCategory",findCategory.getSelectedItem().toString())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        MovieModel movieModel = snapshot.toObject(MovieModel.class);
                                        movieModel.movieCategory =replaceCategory.getSelectedItem().toString();
                                        movieRef.document(snapshot.getId()).set(movieModel);
                                    }
                                }
                            });
                }
                else if(rbtseriesonly.isChecked())
                {
                    seriesRef.whereEqualTo("seriesCategory",findCategory.getSelectedItem().toString())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        SeriesModel seriesModel =snapshot.toObject(SeriesModel.class);
                                        seriesModel.seriesCategory = replaceCategory.getSelectedItem().toString();
                                        seriesRef.document(snapshot.getId()).set(seriesModel);
                                    }
                                }
                            });
                }
                else
                {
                    movieRef.whereEqualTo("movieCategory",findCategory.getSelectedItem().toString())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        MovieModel movieModel = snapshot.toObject(MovieModel.class);
                                        movieModel.movieCategory =replaceCategory.getSelectedItem().toString();
                                        movieRef.document(snapshot.getId()).set(movieModel);
                                    }
                                }
                            });

                    seriesRef.whereEqualTo("seriesCategory",findCategory.getSelectedItem().toString())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        SeriesModel seriesModel = snapshot.toObject(SeriesModel.class);
                                        seriesModel.seriesCategory = replaceCategory.getSelectedItem().toString();
                                        seriesRef.document(snapshot.getId()).set(seriesModel);
                                    }
                                }
                            });
                }
                Toasty.success(getContext(),"Update Success",Toasty.LENGTH_LONG).show();
            }
        });
        return  myView;
    }
}
