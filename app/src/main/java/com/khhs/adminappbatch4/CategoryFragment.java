package com.khhs.adminappbatch4;

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
public class CategoryFragment extends Fragment {

    public CategoryFragment() {
        // Required empty public constructor
    }

    FloatingActionButton add;

    ListView categorylist;
    FirebaseFirestore db;
    CollectionReference categoryRef;

    ArrayList<String>categoryIds = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View myView = inflater.inflate(R.layout.fragment_category, container, false);
       add = myView.findViewById(R.id.add);
       categorylist = myView.findViewById(R.id.categories);
       add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CategoryPopUp popUp = new CategoryPopUp();
               popUp.show(getFragmentManager(),"Add Category");
           }
       });
       db  = FirebaseFirestore.getInstance();
       categoryRef = db.collection(getString(R.string.category_str));

       categoryRef.orderBy("createdAt", Query.Direction.DESCENDING)
               .addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               ArrayList<CategoryModel> categoryModels = new ArrayList<>();

               categoryIds.clear();;
               for(DocumentSnapshot snapshot : queryDocumentSnapshots)
               {

                   categoryIds.add(snapshot.getId());
                   categoryModels.add(snapshot.toObject(CategoryModel.class));
               }
               CategoryAdapter adapter = new CategoryAdapter(categoryModels);
               categorylist.setAdapter(adapter);
           }
       });

       return  myView;
    }

   private class CategoryAdapter extends BaseAdapter
   {    ArrayList<CategoryModel> categoryModels;
       public CategoryAdapter(ArrayList<CategoryModel> categoryModels) {
           this.categoryModels=categoryModels;
       }

       @Override
       public int getCount() {
           return categoryModels.size();
       }

       @Override
       public Object getItem(int position) {
           return categoryModels.get(position);
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(final int position, View convertView, final ViewGroup parent) {
           View myView = getLayoutInflater().inflate(
                   R.layout.categoryitem,
                   parent,
                   false
           );

           TextView txtSr= myView.findViewById(R.id.sr);
           TextView txtName = myView.findViewById(R.id.name);
           txtSr.setText((position+1)+"");
           txtName.setText(categoryModels.get(position).categoryTitle);
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
                               CategoryPopUp popUp = new CategoryPopUp();
                               popUp.editModel = categoryModels.get(position);
                               popUp.editIds = categoryIds.get(position);
                               popUp.show(getFragmentManager(),"Edit Category");
                           }
                           else
                           {
                               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                               builder.setTitle("Confirmation!");
                               builder.setMessage("Are You Sure To Delet?")
                                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               categoryRef.document(categoryIds.get(position)).delete();
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
