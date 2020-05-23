package com.khhs.adminappbatch4;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {

    public SeriesFragment() {
        // Required empty public constructor
    }


    RadioButton rbtSereisName,rbtCategoryName;
    RadioGroup searchPanel;
    EditText search;
    FloatingActionButton add ;
    ListView serieslist;
    CollectionReference seriesRef;
    ArrayList<String> seriesIds=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View myView =inflater.inflate(R.layout.fragment_series, container, false);
        add = myView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesPopUp popUp = new SeriesPopUp();
                popUp.show(getFragmentManager(),"Show Series");
            }
        });
        serieslist = myView.findViewById(R.id.serieslist);

        rbtSereisName = myView.findViewById(R.id.rbtseriesName);
        rbtCategoryName = myView.findViewById(R.id.rbtseriesCategory);
        searchPanel = myView.findViewById(R.id.searchPanel);
        search = myView.findViewById(R.id.search);

        rbtSereisName.setChecked(true);


        FirebaseFirestore db  = FirebaseFirestore.getInstance();
        final CollectionReference settingRef = db.collection(getString(R.string.setting_str));
        settingRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                SettingModel model = new SettingModel();
                try {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                        model = snapshot.toObject(SettingModel.class);

                    }
                    if(model.findByCategory.equals(getString(R.string.yes_str)))
                    {
                        searchPanel.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        searchPanel.setVisibility(View.GONE);
                    }
                }
                catch (Exception ex) {
                    Toasty.error(getContext(),"Incomplete Data",Toasty.LENGTH_LONG).show();
                }

            }
        });
         seriesRef = db.collection(getString(R.string.series_str));
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
                     if(rbtSereisName.isChecked())
                     {
                         seriesRef.orderBy("seriesTitle")
                                 .startAt(search.getText().toString().trim())
                                 .endAt(search.getText().toString().trim()+"\uf8ff")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                             @Override
                             public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                 seriesIds.clear();
                                 ArrayList<SeriesModel> seriesModels = new ArrayList<SeriesModel>();
                                 for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                 {

                                     seriesIds.add(snapshot.getId());
                                     seriesModels.add(snapshot.toObject(SeriesModel.class));
                                 }

                                 SeriesAdapter adapter = new SeriesAdapter(seriesModels);
                                 serieslist.setAdapter(adapter);

                             }
                         });
                     }
                     else
                     {
                         seriesRef.orderBy("seriesCategory")
                                 .startAt(search.getText().toString().trim())
                                 .endAt(search.getText().toString().trim()+"\uf8ff")
                                 .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                     @Override
                                     public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                         seriesIds.clear();
                                         ArrayList<SeriesModel> seriesModels = new ArrayList<SeriesModel>();
                                         for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                         {

                                             seriesIds.add(snapshot.getId());
                                             seriesModels.add(snapshot.toObject(SeriesModel.class));
                                         }

                                         SeriesAdapter adapter = new SeriesAdapter(seriesModels);
                                         serieslist.setAdapter(adapter);

                                     }
                                 });
                     }
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });


        return  myView;
    }



    public void loadData()
    {
        seriesRef.orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                seriesIds.clear();
                ArrayList<SeriesModel> seriesModels = new ArrayList<SeriesModel>();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {

                    seriesIds.add(snapshot.getId());
                    seriesModels.add(snapshot.toObject(SeriesModel.class));
                }

                SeriesAdapter adapter = new SeriesAdapter(seriesModels);
                serieslist.setAdapter(adapter);

            }
        });
    }
    private  class SeriesAdapter extends BaseAdapter{

        ArrayList<SeriesModel> seriesModels = new ArrayList<>();

        public SeriesAdapter(ArrayList<SeriesModel> seriesModels) {
            this.seriesModels = seriesModels;
        }

        @Override
        public int getCount() {
            return seriesModels.size();
        }

        @Override
        public Object getItem(int position) {

            return seriesModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            View myView = getLayoutInflater().inflate(R.layout.seriesitem,parent,false);
            TextView  txtsr = myView.findViewById(R.id.sr);
            final ImageView image  =  myView.findViewById(R.id.image);
            TextView txtname = myView.findViewById(R.id.name);
            final TextView txtcategory = myView.findViewById(R.id.category);
           txtsr.setText(position+1+"");
           txtname.setText(seriesModels.get(position).seriesTitle);

            Glide.with(getContext())
                    .load(seriesModels.get(position).seriesImage)
                    .into(image);
            txtcategory.setText(seriesModels.get(position).seriesCategory);
            RelativeLayout datapanel = myView.findViewById(R.id.datapanel);

            datapanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesDetFragment fragment = new SeriesDetFragment();
                    fragment.model = seriesModels.get(position);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,fragment);
                    ft.commit();
                }
            });
            datapanel.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),txtcategory);
                    popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId()==R.id.editmenu)
                            {
                                SeriesPopUp popUp  = new SeriesPopUp();
                                popUp.editModel = seriesModels.get(position);
                                popUp.editIds =seriesIds.get(position);
                                popUp.show(getFragmentManager(),"Edit Series");

                            }

                            else
                            {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle("Confirmation")
                                        .setMessage("Are You Sure To Exit?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                seriesRef.document(seriesIds.get(position)).delete();
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
                            return false;
                        }
                    });
                    return false;
                }
            });
            return myView;
        }
    }


}
