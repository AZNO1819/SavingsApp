package se.kth.ht100x.sparapp;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.relex.circleindicator.CircleIndicator;

/**
 * The HomeFragment class inflates the fragment_home.xml layout file.
 * The class functions as the homepage for the application.
 * It presents the user's profile section and the user's saving goals.
 */
public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    private SlideAdapter slideAdapter;
    private DBhelper dbhelper;
    private ImageView imageView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sparapp");
        viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        dbhelper = new DBhelper(getActivity());
        Cursor data = dbhelper.getData();
        slideAdapter = new SlideAdapter(getActivity(),data,getActivity());
        viewPager.setAdapter(slideAdapter);
        CircleIndicator indicator = (CircleIndicator) getView().findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        imageView = (ImageView) getView().findViewById(R.id.trophy);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TrophyFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}



