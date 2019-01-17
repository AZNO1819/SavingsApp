package se.kth.ht100x.sparapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import info.abdolahi.CircularMusicProgressBar;
import info.abdolahi.OnCircularSeekBarChangeListener;

/**
 * The SlideAdapter class is used for managing the goals listed on the homepage.
 */
public class SlideAdapter extends PagerAdapter {
    Cursor data;
    Context context;
    FragmentActivity fragmentActivity;
    LayoutInflater inflater;

    public SlideAdapter(Context context, Cursor data, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (ConstraintLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        data.moveToPosition(position);

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.swipe_item,container,false);
        ConstraintLayout layoutslide = (ConstraintLayout) view.findViewById(R.id.slidelinearlayout);
        CircularMusicProgressBar imgslide = (CircularMusicProgressBar)  view.findViewById(R.id.slideimg);
        TextView title = (TextView) view.findViewById(R.id.txttitle);
        TextView amountLeft = (TextView) view.findViewById(R.id.txtdescription);
        TextView daysLeft = (TextView) view.findViewById(R.id.txtdescription2);

        final String name = data.getString(1);
        String amount = data.getString(2);
        byte[] image = data.getBlob(3);
        Bitmap bm = BitmapFactory.decodeByteArray(image, 0 ,image.length);
        String date = data.getString(4);
        String amountSaved = data.getString(5);
        int remaining = Integer.parseInt(amount) - Integer.parseInt(amountSaved);
        Float progress =  (Float.parseFloat(amountSaved) / Float.parseFloat(amount)) * 100;

        imgslide.setImageBitmap(bm);
        imgslide.setValue(progress);

        title.setText(name);
        amountLeft.setText("[ " + numberOfDaysCalculator(date) + " dagar kvar ]");
        daysLeft.setText("[ "+ remaining + "kr kvar ] ");

        container.addView(view);

        imgslide.setOnCircularBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser) {

            }

            @Override
            public void onClick(CircularMusicProgressBar circularBar) {
                Fragment fragment = new SaveFragment();
                ((SaveFragment) fragment).setName(name);
                FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onLongPress(CircularMusicProgressBar circularBar) {

            }

        });

        imgslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout)object);
    }

    /**
     * Calculates the amount of days between two dates.
     * @param endDate The date on which the goal should be reached.
     * @return the amount of days left to save for the goal.
     */
    private long numberOfDaysCalculator(String endDate){

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String currentDate = day + " " + (month + 1) + " " + year;
        String parts[] =  endDate.split("/");
        String endDate2 = parts[0] + " " + parts[1] + " " + parts[2];
        long long2;

        try {
            Date a = myFormat.parse(endDate2);
            Date b = myFormat.parse(currentDate);

            long diff = a.getTime() - b.getTime();
            long2 = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }

        catch(ParseException e){
            long2 = 0;
            e.printStackTrace();
        }

        return long2;
    }
}
