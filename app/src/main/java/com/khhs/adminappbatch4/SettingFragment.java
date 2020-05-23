package com.khhs.adminappbatch4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }


    public static SettingModel model = new SettingModel();

    public  SettingModel prevmodel = new SettingModel();
    String id;


    Button btnsave,btncancel;
    FirebaseFirestore db;
    CollectionReference settingRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_setting, container, false);
        final CheckBox chbUseSlide = myView.findViewById(R.id.chbuseslide);

        final CheckBox chbFindByCategory = myView.findViewById(R.id.chbfindbycategoryName);
        final CheckBox chbUseCategoryReplace = myView.findViewById(R.id.chbusecategoryreplace);

        db=FirebaseFirestore.getInstance();
        settingRef = db.collection(getString(R.string.setting_str));
        chbUseSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chbUseSlide.isChecked())
                {
                    SlidePopUp popUp =new SlidePopUp();
                    popUp.show(getFragmentManager(),"Slide PooUP");
                }
            }
        });
        btnsave = myView.findViewById(R.id.btnsave);
        btncancel = myView.findViewById(R.id.btncancel);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chbFindByCategory.isChecked())
                {
                    model.findByCategory=getString(R.string.yes_str);
                }
                else
                {
                    model.findByCategory=getString(R.string.no_str);
                }

                if(chbUseCategoryReplace.isChecked())
                {
                    model.useCategoryReplace = getString(R.string.yes_str);
                }
                else
                {
                    model.useCategoryReplace=getString(R.string.no_str);
                }
                if(chbUseSlide.isChecked())
                {
                    model.useSlideShow=getString(R.string.yes_str);


                }
                else
                {
                    model.useSlideShow=getString(R.string.no_str);
                }
                if(prevmodel!=null)
                {

                    if(model.slide1==null && model.slide2==null && model.slide3==null && model.slide4==null &&
                    model.slide5==null)
                    {
                        model.slide1=prevmodel.slide1;
                        model.slide2=prevmodel.slide2;
                        model.slide3=prevmodel.slide3;
                        model.slide4=prevmodel.slide4;
                        model.slide5=prevmodel.slide5;
                    }
                    try {
                        settingRef.document(id).set(model);
                    }
                    catch (Exception ex)
                    {
                        settingRef.add(model);
                    }
                }
                else {
                    settingRef.add(model);
                }
                Toasty.success(getContext(),"Setting Save Success!",Toasty.LENGTH_LONG).show();
            }
        });


        settingRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                try{

                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                    {
                        prevmodel=snapshot.toObject(SettingModel.class);
                        id=snapshot.getId();
                    }
                    if(prevmodel!=null)
                    {
                        if(prevmodel.useSlideShow.equals(getString(R.string.yes_str)))
                        {
                            chbUseSlide.setChecked(true);
                            /*SlidePopUp.s1.setText(prevmodel.slide1);
                            SlidePopUp.s2.setText(prevmodel.slide2);
                            SlidePopUp.s3.setText(prevmodel.slide3);
                            SlidePopUp.s4.setText(prevmodel.slide4);
                            SlidePopUp.s5.setText(prevmodel.slide5);*/
                        }
                        else
                        {
                            SlidePopUp.s1.setText(prevmodel.slide1);
                            SlidePopUp.s2.setText(prevmodel.slide2);
                            SlidePopUp.s3.setText(prevmodel.slide3);
                            SlidePopUp.s4.setText(prevmodel.slide4);
                            SlidePopUp.s5.setText(prevmodel.slide5);
                        }

                        if(prevmodel.useCategoryReplace.equals(getString(R.string.yes_str)))
                        {
                            chbUseCategoryReplace.setChecked(true);
                        }
                        if(prevmodel.findByCategory.equals(getString(R.string.yes_str)))
                        {
                            chbFindByCategory.setChecked(true);
                        }
                    }
                }
                catch (Exception ex)
                {
                    Toasty.error(getContext(),"Setting Incomplete!",Toasty.LENGTH_LONG).show();
                }
            }
        });

        return  myView;
    }
}
