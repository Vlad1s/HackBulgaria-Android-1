package com.android.vlad.puzzlegame;

import android.app.Activity;
import android.content.ClipData;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.puzzle);

        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                ClipData dragData = ClipData.newPlainText("piece", "" + view.getId());
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                view.startDrag(dragData, myShadow, null, 0);
                ((ImageView) view).setColorFilter(Color.WHITE);
                view.invalidate();

                return true;
            }
        };

        TypedArray ta = this.getResources().obtainTypedArray(R.array.pieces);
        ImageView[] pieces = new ImageView[ta.length()];

        ImageView imageView;
        int id;
        for (int i = 0; i < ta.length(); i++) {
            imageView = new ImageView(this);
            id = i + 1;
            imageView.setId(id);
            // Used to save the ID of the Drawable object
            imageView.setTag(id);
            imageView.setImageDrawable(ta.getDrawable(i));

            // Using fixed dimensions for now - should look fine on full HD displays and above
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(384, 216);

            if (i == 0) {
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT);
                layoutParams.addRule(RelativeLayout.ALIGN_TOP);
            } else if (i < 4) {
                layoutParams.addRule(RelativeLayout.RIGHT_OF, id - 1);
            } else if (i % 4 == 0) {
                layoutParams.addRule(RelativeLayout.BELOW, id - 4);
            } else {
                layoutParams.addRule(RelativeLayout.BELOW, id - 4);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, id - 1);
            }

            layoutParams.setMargins(1, 1, 1, 1);

            imageView.setLayoutParams(layoutParams);

            imageView.setOnLongClickListener(onLongClickListener);
            imageView.setOnDragListener(new myDragEventListener());

            mRelativeLayout.addView(imageView);
            pieces[i] = imageView;
        }

        ta.recycle();
        shufflePieces(pieces);
    }

    private void shufflePieces(ImageView[] pieces) {
        Random random = new Random();
        for (int i = pieces.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Drawable drawable = pieces[index].getDrawable();
            pieces[index].setImageDrawable(pieces[i].getDrawable());
            pieces[i].setImageDrawable(drawable);

            Object drawableId = pieces[index].getTag();
            pieces[index].setTag(pieces[i].getTag());
            pieces[i].setTag(drawableId);
        }
    }

    private boolean isPuzzleSolved() {
        for (int i = 0; i < mRelativeLayout.getChildCount(); i++) {
            if (mRelativeLayout.getChildAt(i).getId() != (Integer) mRelativeLayout.getChildAt(i).getTag()) {
                return false;
            }
        }

        return true;
    }

    private class myDragEventListener implements View.OnDragListener {
        public boolean onDrag(View view, DragEvent event) {

            final int action = event.getAction();

            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:

                    view.setAlpha(0.5f);

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    view.setAlpha(1.0f);

                    return true;

                case DragEvent.ACTION_DROP:

                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData = item.getText().toString();

                    ImageView droppedImage = (ImageView) findViewById(Integer.parseInt(dragData));
                    droppedImage.clearColorFilter();
                    droppedImage.invalidate();

                    Drawable drawable = ((ImageView) view).getDrawable();
                    ((ImageView) view).setImageDrawable(droppedImage.getDrawable());
                    droppedImage.setImageDrawable(drawable);

                    Object drawableId = view.getTag();
                    view.setTag(droppedImage.getTag());
                    droppedImage.setTag(drawableId);

                    view.setAlpha(1.0f);

                    if (isPuzzleSolved()) {
                        Toast.makeText(getApplicationContext(), "Puzzle solved!!!", Toast.LENGTH_LONG).show();
                    }

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    ((ImageView) view).clearColorFilter();
                    view.invalidate();

                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    }
}
