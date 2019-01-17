package se.kth.ht100x.sparapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daasuu.cat.CountAnimationTextView;

import java.util.ArrayList;

import info.abdolahi.CircularMusicProgressBar;

/**
 * The SaveFragment class is used for the detailed view of a goal.
 *
 */
public class SaveFragment extends Fragment {

    private String saveGoal;
    private DBhelper dBhelper;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Drawable> mImage = new ArrayList<>();

    public SaveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_save, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(saveGoal);
        CircularMusicProgressBar img = (CircularMusicProgressBar)  view.findViewById(R.id.imgGoal);
        TextView amountTv = (TextView) getView().findViewById(R.id.amount);
        CountAnimationTextView amountCounter = (CountAnimationTextView) getView().findViewById(R.id.amountSaved);

        dBhelper = new DBhelper(getActivity());
        Cursor cursor = dBhelper.getGoal(saveGoal);
        cursor.moveToFirst();
        String amount = cursor.getString(2);
        byte[] image = cursor.getBlob(3);
        Bitmap bm = BitmapFactory.decodeByteArray(image, 0 ,image.length);
        String amountSaved = cursor.getString(5);
        amountTv.setText(amount);
        img.setImageBitmap(bm);
        amountCounter.setText(amountSaved);
        Float progress = (Float.parseFloat(amountSaved) / Float.parseFloat(amount)) * 100;
        img.setValueWithNoAnimation(progress);

        mImage.add(getResources().getDrawable(R.drawable.coffee));
        mImage.add(getResources().getDrawable(R.drawable.takeaway));
        mImage.add(getResources().getDrawable(R.drawable.coins));
        mImage.add(getResources().getDrawable(R.drawable.scissors));
        mImage.add(getResources().getDrawable(R.drawable.champagne));
        mImage.add(getResources().getDrawable(R.drawable.booth));

        mNames.add("Skippa köp-kaffe");
        mNames.add("Skippa hämtmat");
        mNames.add("Valfri summa");
        mNames.add("Hemmaklippning");
        mNames.add("Förkrök");
        mNames.add("Sålt på loppis");

        initRecyclerView();
    }


    /**
     * Sets the saveGoal variable to the current goal.
     * The variable is used for setting the title in the detailed savings view
     * and fetching data from the database.
     * @param name The name of the specific goal.
     */
    public void setName(String name){
        saveGoal = name;
    }

    /**
     * Initializes the recyclerView.
     * This recyclerView contains the saving alternatives.
     */
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mNames, mImage, saveGoal);
        recyclerView.setAdapter(adapter);
        recyclerView.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }
}
