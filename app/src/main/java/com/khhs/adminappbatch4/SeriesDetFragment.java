package com.khhs.adminappbatch4;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
public class SeriesDetFragment extends Fragment {

    SeriesModel model;
    int position;
    public SeriesDetFragment() {
        // Required empty public constructor
    }
    CollectionReference episodeRef;
    ArrayList<String> episodeIds = new ArrayList<>();
    ListView episodeList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView  =inflater.inflate(R.layout.fragment_series_det, container, false);
        TextView txtsr = myView.findViewById(R.id.sr);
        final ImageView image  =  myView.findViewById(R.id.image);
        TextView txtname = myView.findViewById(R.id.name);
        final TextView txtcategory = myView.findViewById(R.id.category);

        episodeList = myView.findViewById(R.id.episodeList);

        txtsr.setText(position+1+"");
        txtname.setText(model.seriesTitle);
        txtcategory.setText(model.seriesCategory);
        Glide.with(getContext())
                .load(model.seriesImage)
                .into(image);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
         episodeRef = db.collection(getString(R.string.episode_str));

        episodeRef.orderBy("createdAt", Query.Direction.DESCENDING).
                whereEqualTo("epSeires",model.seriesTitle)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        episodeIds.clear();;
                        ArrayList<EpisodeModel> episodeModels = new ArrayList<>();
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                        {
                            episodeIds.add(snapshot.getId());
                            episodeModels.add(snapshot.toObject(EpisodeModel.class));

                        }
                        EpisodeAdapter adapter = new EpisodeAdapter(episodeModels);
                        episodeList.setAdapter(adapter);
                    }
                });

        return myView;



         }

         private class EpisodeAdapter extends BaseAdapter{

        ArrayList<EpisodeModel> episodeModels = new ArrayList<>();

             public EpisodeAdapter(ArrayList<EpisodeModel> episodeModels) {
                 this.episodeModels = episodeModels;
             }

             @Override
             public int getCount() {
                 return episodeModels.size();
             }

             @Override
             public Object getItem(int position) {
                 return episodeModels.get(position);
             }

             @Override
             public long getItemId(int position) {
                 return position;
             }

             @Override
             public View getView(final int position, View convertView, final ViewGroup parent) {
                 View myView = getLayoutInflater().inflate(R.layout.categoryitem,parent,false);
                 TextView txtSr= myView.findViewById(R.id.sr);
                 TextView txtName = myView.findViewById(R.id.name);
                 txtSr.setText((position+1)+"");
                 txtName.setText(episodeModels.get(position).epTitle);
                 final ImageView options = myView.findViewById(R.id.options);
                 options.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         PopupMenu popupMenu = new PopupMenu(getContext(),options);
                         popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
                         popupMenu.show();;
                         popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                             @Override
                             public boolean onMenuItemClick(MenuItem item) {
                                 if(item.getItemId()==R.id.editmenu)
                                 {
                                    MoviePopUp popUp = new MoviePopUp();
                                    popUp.editEpId = episodeIds.get(position);
                                    popUp.editEpModel = episodeModels.get(position);
                                    popUp.show(getFragmentManager(),"Edit Episode");
                                 }
                                 else
                                 {
                                     AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                     builder.setTitle("Confirmation!");
                                     builder.setMessage("Are You Sure To Delete?")
                                             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                   episodeRef.document(episodeIds.get(position)).delete();
                                                     Toasty.error(getContext(),"Delete Success",Toasty.LENGTH_LONG).show();;
                                                 }
                                             }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     });
                                     builder.show();


                                 }
                                 return true;
                             }
                         });
                     }
                 });

                 return myView;
             }
         }
}
