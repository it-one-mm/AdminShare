package com.khhs.adminappbatch4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainframe,new MovieFragment());
        ft.commit();
        BottomNavigationView navView = findViewById(R.id.bottomnav);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.movie_menu)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new MovieFragment());
                    ft.commit();
                }
                else if(item.getItemId()==R.id.series_menu)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new SeriesFragment());
                    ft.commit();
                }
                else if(item.getItemId()==R.id.category_menu)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new CategoryFragment());
                    ft.commit();
                }
                else if(item.getItemId()==R.id.request_menu)
                {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new RequestFragment());
                    ft.commit();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference settingRef = db.collection(getString(R.string.setting_str));
        settingRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                SettingModel model = new SettingModel();
                try {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                        model = snapshot.toObject(SettingModel.class);
                    }
                    if(model.useCategoryReplace.equals(getString(R.string.yes_str)))
                    {
                        menu.getItem(2).setVisible(true);
                    }
                    else
                    {
                        menu.getItem(2).setVisible(false);
                    }
                }
                catch (Exception ex)
                {

                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.replace_menu)
        {
            CategoryReplace popUp = new CategoryReplace();
            popUp.show(getSupportFragmentManager(),"Show Catgory Replace");
        }


        if(item.getItemId()==R.id.setting_menu)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainframe,new SettingFragment());
            ft.commit();

        }
        if(item.getItemId()==R.id.about_menu)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainframe,new AboutFragment());
            ft.commit();
        }
        return true;
    }
}
