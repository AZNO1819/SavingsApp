package se.kth.ht100x.sparapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * The TrophyFragment class is used for the displaying the application's trophies.
 */
public class TrophyFragment extends Fragment {


    public TrophyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Troféer");
        return inflater.inflate(R.layout.fragment_trophy, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = (ImageView) view.findViewById(R.id.trophy1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inputView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_coffee, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                alertBuilder.setView(inputView);

                alertBuilder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertBuilder.show();
            }
        });
    }

}
