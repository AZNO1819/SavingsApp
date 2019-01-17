package se.kth.ht100x.sparapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daasuu.cat.CountAnimationTextView;

import java.util.ArrayList;

import info.abdolahi.CircularMusicProgressBar;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Size;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * The RecyclerViewAdapter is a class that manages the application's swipe functionality
 * in the detailed view of a goal.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Vibrator myVib;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Drawable> mImage = new ArrayList<>();
    private Context mContext;
    private DBhelper dBhelper;
    private String saveGoal;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<Drawable> imageUrls, String savegoal) {
        mNames = names;
        mImage = imageUrls;
        mContext = context;
        this.saveGoal = savegoal;
        dBhelper = new DBhelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_images, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        myVib = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
        final int positionInList = position % mNames.size();
        holder.name.setText(mNames.get(positionInList));
        holder.image.setImageDrawable(mImage.get(positionInList));
        CircularMusicProgressBar img = (CircularMusicProgressBar) ((Activity) mContext).findViewById(R.id.imgGoal);

        holder.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                View inputView = LayoutInflater.from(mContext).inflate(R.layout.fragment_input, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                alertBuilder.setView(inputView);
                EditText userInput2 = (EditText) inputView.findViewById(R.id.editValue);

                userInput2.setInputType(InputType.TYPE_CLASS_NUMBER);
                alertBuilder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!(userInput2.getText().toString().equals("")))
                                    holder.button.setText(userInput2.getText() +" KR" + "\n" +
                                        (Integer.parseInt(userInput2.getText().toString()))/5 + " XP");
                            }
                        })
                        .setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertBuilder.show();
            }
        });

        img.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale);
                final Animation anim2 = AnimationUtils.loadAnimation(mContext, R.anim.unscale);

                anim.setFillEnabled(true);
                anim.setFillAfter(true);

                anim2.setFillEnabled(true);
                anim2.setFillAfter(true);

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        img.startAnimation(anim);
                        return true;

                    case DragEvent.ACTION_DROP:
                        img.startAnimation(anim2);
                        KonfettiView konfetti = (KonfettiView) ((Activity) mContext).findViewById(R.id.konfettiView);
                        CountAnimationTextView amountSaved = (CountAnimationTextView) ((Activity) mContext).findViewById(R.id.amountSaved);
                        TextView amount = (TextView) ((Activity) mContext).findViewById(R.id.amount);
                        int amountSavedInt = Integer.parseInt(amountSaved.getText().toString());
                        float amountSavedNumber = Float.parseFloat(amountSaved.getText().toString());
                        float amountNumber = Float.parseFloat(amount.getText().toString());
                        float progress = ((amountSavedNumber +100) / amountNumber) * 100;

                        dBhelper.updateDB(saveGoal,100, amountSavedInt);
                        amountSaved.setAnimationDuration(800).countAnimation(amountSavedInt, (amountSavedInt + 100));
                        img.setValue(progress);

                        int color1 = Color.parseColor("#F7C075");
                        int color2 = Color.parseColor("#FAD6A5");
                        int color3 = Color.parseColor("#FAE4A5");

                        konfetti.build()
                                .addColors(color1, color2, color3)
                                .setDirection(0.0, 359.0)
                                .setSpeed(7f, 11f)
                                .setFadeOutEnabled(true)
                                .setTimeToLive(1000L)
                                .addShapes(nl.dionsegijn.konfetti.models.Shape.RECT, nl.dionsegijn.konfetti.models.Shape.CIRCLE)
                                .addSizes(new Size(8, 5))
                                .setPosition(-50f, konfetti.getWidth() + 50f, -50f, -50f)
                                .streamFor(350, 1000L);

                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        img.startAnimation(anim2);

                        return false;
                }
                return false;
            }
        });

        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                myVib = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.VISIBLE);
                myVib.vibrate(50);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        Button button;

        public ViewHolder(final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            button = itemView.findViewById(R.id.button);
        }
    }









}