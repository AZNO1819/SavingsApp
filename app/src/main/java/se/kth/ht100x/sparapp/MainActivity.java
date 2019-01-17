package se.kth.ht100x.sparapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * The MainActivity class is invoked when the application is launched.
 * It sets the applications homepage as the first view and shifts between fragments by listening
 * to the user's selection on the navigation bar.
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment(), false);
    }

    /**
     * Replaces the current fragment with the specific fragment that the user has clicked in the navigation bar.
     * @param fragment The fragment to be loaded.
     * @param addToBackStack Conditional variable to check whether or not the fragment is placed on the backstack.
     * @return
     */
    private boolean loadFragment(Fragment fragment, boolean addToBackStack){
        if(fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.main_frame, fragment);

            if(addToBackStack){
                transaction.addToBackStack(null);
            }

            transaction.commit();

            return true;
        }

        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        String titel ="";

        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                titel = "Sparapp";
                break;

            case R.id.navigation_tip:
                fragment = new TipFragment();
                titel = "Tips";
                break;

            case R.id.navigation_newGoal:
                fragment = new NewGoalFragment();
                titel = "Nytt mål";
                break;

            case R.id.navigation_statistics:
                fragment = new StatisticsFragment();
                titel = "Statistik";
                break;

            case R.id.navigation_settings:
                fragment = new SettingsFragment();
                titel = "Inställningar";
                break;
        }

        getSupportActionBar().setTitle(titel);

        return loadFragment(fragment, true);
    }
}
