package avivi.com.pollardi;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Void on 08.04.2015.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    int [] imageId;
    String[] title;

    private static LayoutInflater inflater=null;
    public CustomAdapter(TestActivity testActivity, int[] prgmImages, String[] titles) {
        // TODO Auto-generated constructor stub
        title = titles;
        context=testActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageId.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        ImageView img;
        TextView tv;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.item_list_main, null);

        holder.img=(ImageView) rowView.findViewById(R.id.imageViewList);
        holder.img.setImageResource(imageId[position]);

        holder.tv = (TextView) rowView.findViewById(R.id.myImageViewText);
        holder.tv.setText(title[position]);
        Typeface typeFace=Typeface.createFromAsset(inflater.getContext().getAssets(),"fonts/title_pol.ttf");
        holder.tv.setTypeface(typeFace);
//        holder.layot = (LinearLayout) rowView.findViewById(R.id.linearList);
//        holder.layot.setBackgroundResource(imageId[position]);

//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation anim = AnimationUtils.loadAnimation(rowView.getContext(), R.anim.my_alpha);
//                holder.img.startAnimation(anim);
//            }
//        });

        // shadow onTouch
        holder.img.setOnTouchListener(new ImageView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                    holder.img.setColorFilter(Color.argb(50, 0, 0, 0));
//
//                } else if (MotionEvent.ACTION_UP == event.getAction()) {
//                    holder.img.clearColorFilter();
//                    v.performClick();
//                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Animation anim = AnimationUtils.loadAnimation(rowView.getContext(), R.anim.my_alpha);
                        holder.img.startAnimation(anim);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        holder.img.clearColorFilter();
                        break;

                    case MotionEvent.ACTION_UP:
                        holder.img.clearColorFilter();
                        break;

                }
                return false;

            }
        });

        return rowView;
    }
}
