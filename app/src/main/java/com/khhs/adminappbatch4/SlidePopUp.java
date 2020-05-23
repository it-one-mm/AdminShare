package com.khhs.adminappbatch4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class SlidePopUp extends BottomSheetDialogFragment {

   public  static EditText s1,s2,s3,s4,s5;
    ImageView close;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.slidepopup,container,false);
        Button btnsave = myView.findViewById(R.id.btnsave);
        s1 = myView.findViewById(R.id.slide1);
        s2= myView.findViewById(R.id.slide2);
        s3=myView.findViewById(R.id.slide3);
        s4=myView.findViewById(R.id.slide4);
        s5=myView.findViewById(R.id.slide5);
        close=myView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection(getString(R.string.setting_str));
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                try{
                    SettingModel model  =new SettingModel();

                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                    {
                        model = snapshot.toObject(SettingModel.class);
                    }

                    if(model.slide1!=null)
                    {
                        s1.setText(model.slide1);
                        s2.setText(model.slide2);
                        s3.setText(model.slide3);
                        s4.setText(model.slide4);
                        s5.setText(model.slide5);
                    }
                }
                catch (Exception ex)
                {
                    Log.e("Error", "Slide Not Incomplete");

                }
            }
        });

        Button btncancel = myView.findViewById(R.id.btncancel);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment.model.slide1=s1.getText().toString().trim();
                SettingFragment.model.slide2 = s2.getText().toString().trim();
                SettingFragment.model.slide3=s3.getText().toString().trim();
                SettingFragment.model.slide4=s4.getText().toString().trim();
                SettingFragment.model.slide5=s5.getText().toString().trim();


                s1.setText("");
                s2.setText("");
                s3.setText("");
                s4.setText("");
                s5.setText("");

            }
        });
        btncancel=myView.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.setText("");
                s2.setText("");
                s3.setText("");
                s4.setText("");
                s5.setText("");
            }
        });
        return myView;
    }
}
