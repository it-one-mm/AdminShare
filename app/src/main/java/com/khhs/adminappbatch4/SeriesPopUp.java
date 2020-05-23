package com.khhs.adminappbatch4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SeriesPopUp extends BottomSheetDialogFragment {

    SeriesModel editModel;
    String editIds;
    Spinner spCategory;
    EditText edtSeriesName,edtSeriesImage;
    Button btnsave,btncancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.seriespopup,container,false);
        ImageView close = myView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        spCategory = myView.findViewById(R.id.spcategory);
        edtSeriesName = myView.findViewById(R.id.sereisName);
        edtSeriesImage = myView.findViewById(R.id.seriesImage);
        btnsave = myView.findViewById(R.id.btnsave);
        btncancel = myView.findViewById(R.id.btncancel);

        if(editModel!=null)
        {
            edtSeriesName.setText(editModel.seriesTitle);
            edtSeriesImage.setText(editModel.seriesImage);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference categoryRef = db.collection(getString(R.string.category_str));
        final CollectionReference seriesRef= db.collection(getString(R.string.series_str));
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
                            if(categoryNames.get(i).equals(editModel.seriesCategory))
                            {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }
        });



        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSeriesName.getText().toString().trim().equals(""))
                {
                    Toasty.error(getContext(),"Fail",Toasty.LENGTH_LONG).show();
                }
                else
                {
                    SimpleDateFormat sd = new SimpleDateFormat(getString(R.string.dateformat));
                    SeriesModel model = new SeriesModel();
                    model.seriesTitle=edtSeriesName.getText().toString().trim();
                    model.seriesImage = edtSeriesImage.getText().toString().trim();
                    model.seriesCategory = spCategory.getSelectedItem().toString();


                   if(editModel!=null)
                   {
                       model.createdAt=editModel.createdAt;
                       model.seriesCount = editModel.seriesCount;
                       seriesRef.document(editIds).set(model);

                       editModel=null;
                       editIds="";
                       Toasty.info(getContext(),"Update Success",Toasty.LENGTH_LONG).show();
                   }
                   else
                   {
                       model.createdAt= sd.format(new Date());
                       model.seriesCount=0;
                       seriesRef.add(model);
                       Toasty.success(getContext(),"Save Success",Toasty.LENGTH_LONG).show();;

                   }

                    edtSeriesName.setText("");
                    edtSeriesImage.setText("");

                }
            }
        });

        return  myView;
    }
}
