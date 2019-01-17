package se.kth.ht100x.sparapp;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The NewGoalFragment class contains functionality for creating a new goal.
 *
 */
public class NewGoalFragment extends Fragment {
    private EditText displayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private CircleImageView upload;
    private static int galleryCode = 100;
    private DBhelper dbhelper;
    private Uri URI;
    private Bitmap bm;

    public NewGoalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_goal, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dbhelper = new DBhelper(getActivity());
        Button addBtn = (Button) getView().findViewById(R.id.addBtn);
        upload = (CircleImageView) getView().findViewById(R.id.listview_swipe_image);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromGallery();
            }
        });

        displayDate = (EditText) getView().findViewById(R.id.calenderInput);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) getView().findViewById(R.id.firstNumEditText);
                EditText sum = (EditText) getView().findViewById(R.id.secondNumEditText);
                EditText endDate = (EditText) getView().findViewById(R.id.calenderInput);

                EditText[] fields = {name,sum,endDate};
                boolean validated = helper(fields);
                boolean validated2 = true;

                if (upload.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.placeholder).getConstantState()) {
                    Toast.makeText(getContext(), "...hoppsan! Du försökte visst skapa ett mål utan att lägga till en bild ;)", Toast.LENGTH_LONG).show();
                    validated2 = false;
                }

                if(validated && validated2) {
                    dbhelper.addtoDB(name.getText().toString(), sum.getText().toString(), endDate.getText().toString(), bm);
                    Fragment fragment = new SaveFragment();
                    ((SaveFragment) fragment).setName(name.getText().toString());
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_frame,fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        R.style.DatePicker,
                        DateSetListener,
                        year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);
                dialog.show();
            }
        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = day + "/" + month + "/" + year;
                displayDate.setText(date, TextView.BufferType.EDITABLE);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryCode  && data != null){
            URI = data.getData();

            try{
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),URI);
            }

            catch(Exception e){
                e.printStackTrace();
            }

            upload.setImageURI(URI);
        }

    }

    /**
     * Starts an intent where the user can select an image from their photo gallery.
     */
    private void chooseImageFromGallery(){

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,galleryCode);
    }

    /**
     * Checks if the fields has content in them.
     * @param fields The fields in where the user has inputted data.
     * @return returns true if all fields contain data, else false.
     */
    private boolean helper(EditText[] fields){
        for(int i = 0; i < fields.length; i++)
            if(fields[i].getText().toString().length() <= 0)
                return false;
        return true;
    }

}
