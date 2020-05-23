package com.khhs.adminappbatch4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class MoviePopUp extends BottomSheetDialogFragment {

    MovieModel editModel;
    String editMovieId;
    EditText edtName,edtImage,edtVideo;
    Spinner spCategory,spSeries;
    Button btnSave,btnCancel;

    CheckBox isEp;
    LinearLayout seriesPanel,categoryPanel,imagePanel;

    EpisodeModel editEpModel;
    String editEpId;

    FirebaseFirestore db;
    CollectionReference categoryRef;
    CollectionReference movieRef;
    CollectionReference seriesRef;
    CollectionReference epRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.moviepopup,container,false);
        ImageView close = myView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSave = myView.findViewById(R.id.btnsave);
        btnCancel = myView.findViewById(R.id.btncancel);
        edtName = myView.findViewById(R.id.movieName);
        edtImage = myView.findViewById(R.id.movieImage);
        edtVideo = myView.findViewById(R.id.movieVideo);
        spCategory = myView.findViewById(R.id.spcategory);
        seriesPanel = myView.findViewById(R.id.seriesPanel);
        categoryPanel = myView.findViewById(R.id.categoryPanel);
        imagePanel = myView.findViewById(R.id.imagePanel);
        isEp = myView.findViewById(R.id.isEp);
        spSeries  = myView.findViewById(R.id.spseries);

        db=FirebaseFirestore.getInstance();
        categoryRef = db.collection(getString(R.string.category_str));
        movieRef = db.collection(getString(R.string.movie_str));
        seriesRef = db.collection(getString(R.string.series_str));
        epRef = db.collection(getString(R.string.episode_str));

        seriesPanel.setVisibility(View.GONE);



        if(editModel!=null)
        {
            edtName.setText(editModel.movieTitle);
            edtImage.setText(editModel.movieImage);
            edtVideo.setText(editModel.movieVideo);
            isEp.setEnabled(false);
            seriesPanel.setVisibility(View.GONE);

        }


        if(editEpModel!=null)
        {
            isEp.setChecked(true);
            edtName.setText(editEpModel.epTitle);
            edtVideo.setText(editEpModel.getEpVideo());
            isEp.setEnabled(false);
            categoryPanel.setVisibility(View.GONE);
            imagePanel.setVisibility(View.GONE);
            seriesPanel.setVisibility(View.VISIBLE);
        }
        categoryRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> categoryNames = new ArrayList<>();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {

                    categoryNames.add(snapshot.toObject(CategoryModel.class).categoryTitle);
                }

                if(categoryNames.size()!=0)
                {
                    ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_dropdown_item_1line
                            ,categoryNames);
                    spCategory.setAdapter(namesAdapter);

                    if(editModel!=null)
                    {
                        for(int i=0;i<categoryNames.size();i++)
                        {
                            if(categoryNames.get(i).equals(editModel.movieCategory))
                            {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    }


                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtName.getText().toString().trim().equals(""))
                {
                    Toasty.error(getContext(),"Please Fill Data",Toasty.LENGTH_LONG).show();
                }
                else
                {
                    if(!isEp.isChecked()){
                        SimpleDateFormat sd = new SimpleDateFormat(getString(R.string.dateformat));

                        MovieModel movieModel = new MovieModel();
                        movieModel.movieTitle = edtName.getText().toString().trim();
                        movieModel.movieImage =  edtImage.getText().toString().trim();
                        movieModel.movieVideo = edtVideo.getText().toString().trim();
                        movieModel.movieCategory = spCategory.getSelectedItem().toString();


                        if(editModel!=null)
                        {


                            movieModel.movieCount = editModel.movieCount;
                            movieModel.createdAt = editModel.createdAt;
                            movieRef.document(editMovieId).set(movieModel);
                            editModel=null;
                            editMovieId="";
                            Toasty.info(getContext(),"Update Success",Toasty.LENGTH_LONG).show();
                        }
                        else{
                            movieModel.createdAt = sd.format(new Date());
                            movieModel.movieCount=0;
                            movieRef.add(movieModel);
                        }

                        Toasty.success(getContext(),"Save Success",Toasty.LENGTH_LONG).show();;

                    }
                    else
                    {
                        EpisodeModel episodeModel=new EpisodeModel();
                        episodeModel.epTitle=edtName.getText().toString().trim();
                        episodeModel.epSeires=spSeries.getSelectedItem().toString();
                        episodeModel.epVideo = edtVideo.getText().toString().trim();

                        SimpleDateFormat sd = new SimpleDateFormat(getString(R.string.dateformat));


                        if(editEpModel!=null)
                        {

                            episodeModel.createdAt = editModel.createdAt;
                            epRef.document(editEpId).set(episodeModel);
                            editEpModel=null;
                            editEpId="";
                            Toasty.info(getContext(),"Update Success",Toasty.LENGTH_LONG).show();
                        }
                        else {
                            new Date();
                            episodeModel.createdAt=sd.format(new Date());
                            epRef.add(episodeModel);
                            Toasty.success(getContext(), "Episode Save Success", Toasty.LENGTH_LONG).show();
                        }

                    }
                    edtName.setText("");
                    edtImage.setText("");
                    edtVideo.setText("");

                }
            }
        });

        seriesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<String>seriesNames = new ArrayList<>();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                    seriesNames.add(snapshot.toObject(SeriesModel.class).seriesTitle);
                }
                ArrayAdapter<String> seriesAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        seriesNames);
                spSeries.setAdapter(seriesAdapter);

                if(editEpModel!=null)
                {
                    for(int i=0;i<seriesNames.size();i++)
                    {
                        if(seriesNames.get(i).equals(editEpModel.epSeires))
                        {
                            spSeries.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });

        isEp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEp.isChecked())
                {
                    imagePanel.setVisibility(View.GONE);
                    categoryPanel.setVisibility(View.GONE);
                    seriesPanel.setVisibility(View.VISIBLE);
                }
                else{
                    imagePanel.setVisibility(View.VISIBLE);
                    categoryPanel.setVisibility(View.VISIBLE);
                    seriesPanel.setVisibility(View.GONE);
                }
            }
        });
        return  myView;
    }
}
