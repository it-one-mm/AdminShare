package com.khhs.adminappbatch4;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    RadioGroup findByCategory;
    ArrayList<String> movieIds = new ArrayList<String>();
    CollectionReference moiveRef;
    public MovieFragment() {
        // Required empty public constructor
    }


    FloatingActionButton add;
    RadioButton rbtfindByMovieName,rbtfindByCategoryName;
    ListView list;
    EditText search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_movie, container, false);
        add = myView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoviePopUp popUp = new MoviePopUp();
                popUp.show(getFragmentManager(),"Add Category");
            }
        });
        search = myView.findViewById(R.id.search);
        rbtfindByMovieName = myView.findViewById(R.id.bymoviename);
        rbtfindByCategoryName = myView.findViewById(R.id.bycategoryname);
        rbtfindByMovieName.setChecked(true);
        list = myView.findViewById(R.id.movielist);
        FirebaseFirestore db  = FirebaseFirestore.getInstance();
         moiveRef = db.collection(getString(R.string.movie_str));

         loadData();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search.getText().toString().trim().equals(""))
                {
                    loadData();
                }
                else
                {
                   if(rbtfindByMovieName.isChecked())
                   {
                       moiveRef.orderBy("movieTitle")
                               .startAt(search.getText().toString().trim())
                               .endAt(search.getText().toString().trim()+"\uf8ff")
                               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                   @Override
                                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                       movieIds.clear();;

                                       ArrayList<MovieModel> movieModels = new ArrayList<MovieModel>();
                                       for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                       {
                                           movieIds.add(snapshot.getId());
                                           movieModels.add(snapshot.toObject(MovieModel.class));
                                       }
                                       MovieAdapter adapter = new MovieAdapter(movieModels);
                                       list.setAdapter(adapter);
                                   }
                               });
                   }
                   else
                   {
                       moiveRef.orderBy("movieCategory")
                               .startAt(search.getText().toString().trim())
                               .endAt(search.getText().toString().trim()+"\uf8ff")
                               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                   @Override
                                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                       movieIds.clear();;

                                       ArrayList<MovieModel> movieModels = new ArrayList<MovieModel>();
                                       for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                       {
                                           movieIds.add(snapshot.getId());
                                           movieModels.add(snapshot.toObject(MovieModel.class));
                                       }
                                       MovieAdapter adapter = new MovieAdapter(movieModels);
                                       list.setAdapter(adapter);
                                   }
                               });
                   }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findByCategory = myView.findViewById(R.id.findByCategory);
        CollectionReference settingRef = db.collection(getString(R.string.setting_str));
        settingRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                try
                {
                    SettingModel settingModel = new SettingModel();
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                    {
                        settingModel = snapshot.toObject(SettingModel.class);
                    }
                    if(settingModel.findByCategory.equals(getString(R.string.yes_str)))
                    {
                        findByCategory.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        findByCategory.setVisibility(View.GONE);
                    }
                }
                catch (Exception ex)
                {
                    Log.e("Error","Incomplete");
                }
            }
        });
        return  myView;
    }

    public void loadData()
    {
        moiveRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        movieIds.clear();;

                        ArrayList<MovieModel> movieModels = new ArrayList<MovieModel>();
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                        {
                            movieIds.add(snapshot.getId());
                            movieModels.add(snapshot.toObject(MovieModel.class));
                        }
                        MovieAdapter adapter = new MovieAdapter(movieModels);
                        list.setAdapter(adapter);
                    }
                });
    }

    private class MovieAdapter extends BaseAdapter{

        ArrayList<MovieModel> movieModels = new ArrayList<>();

        public MovieAdapter(ArrayList<MovieModel> movieModels) {
            this.movieModels = movieModels;
        }

        @Override
        public int getCount() {
            return movieModels.size();
        }

        @Override
        public Object getItem(int position) {
            return movieModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View myView = getLayoutInflater().inflate(R.layout.movieitem,parent,false);
            TextView sr = myView.findViewById(R.id.sr);
            TextView name = myView.findViewById(R.id.name);
            final TextView category =  myView.findViewById(R.id.category);
            final ImageView image =  myView.findViewById(R.id.image);
            Glide.with(getContext())
                    .load(movieModels.get(position).movieImage)
                    .into(image);
            sr.setText(position+1+"");
            name.setText(movieModels.get(position).movieTitle);
            category.setText(movieModels.get(position).movieCategory);
            RelativeLayout datapanel = myView.findViewById(R.id.datapanel);
            datapanel.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),category);
                    popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId()==R.id.editmenu)
                            {
                                MoviePopUp popUp = new MoviePopUp();
                                popUp.editModel = movieModels.get(position);
                                popUp.editMovieId=movieIds.get(position);
                                popUp.show(getFragmentManager(),"Show Movie");

                            }
                            else
                            {


                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle("Confirmation")
                                        .setMessage("Are you sure To Delete")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                moiveRef.document(movieIds.get(position)).delete();
                                                Toasty.error(getContext(),"Delete Success",Toasty.LENGTH_LONG).show();;
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                alert.show();

                            }
                            return true;
                        }
                    });
                    return false;
                }
            });
            return myView;
        }
    }
}
