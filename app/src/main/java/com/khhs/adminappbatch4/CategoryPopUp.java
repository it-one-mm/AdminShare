package com.khhs.adminappbatch4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class CategoryPopUp extends BottomSheetDialogFragment {

    CategoryModel editModel;
    String editIds;
    Button btnsave,btncancel;
    EditText edtcategory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.categorypopup,container,false);
        ImageView close = myView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        edtcategory = myView.findViewById(R.id.categoryName);
        btnsave = myView.findViewById(R.id.btnsave);
        btncancel = myView.findViewById(R.id.btncancel);


        if(editModel!=null)
        {
            edtcategory.setText(editModel.getCategoryTitle());
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference ref = db.collection(getString(R.string.category_str));
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!edtcategory.getText().toString().trim().equals(""))
               {
                   SimpleDateFormat sd = new SimpleDateFormat(getString(R.string.dateformat));
                   CategoryModel model = new CategoryModel();
                   model.categoryTitle = edtcategory.getText().toString().trim();

                    if(editModel!=null)
                    {
                        model.createdAt = editModel.createdAt;
                        ref.document(editIds).set(model);
                        Toasty.info(getContext(),"Update Success",Toasty.LENGTH_LONG).show();
                        editModel=null;
                        editIds="";
                    }
                    else
                    {
                        model.createdAt = sd.format(new Date());
                        ref.add(model);
                        Toasty.success(getContext(),"Save Success ",Toasty.LENGTH_LONG).show();
                    }

                   edtcategory.setText("");

               }
               else
               {
                   Toasty .error(getContext(),"Please Fill Data",Toasty.LENGTH_LONG).show();
               }
            }
        });
        return  myView;
    }
}
