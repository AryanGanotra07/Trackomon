package com.codingee.trackamon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }


    public int[] slideimages={

            R.drawable.logo,R.drawable.fragmenttwo,R.drawable.stats

    };

    public String[] slideheadings={
            "Welcome User","Record Everything","View Stats"
    };

    public String[] slidedescription={

            "Keep a track of your expenses.",
            "Travelling, Fooding, Lendering, Shopping, Others.",
            "Weekly, monthly, yearly statistics of your expenses."

    };
    @Override
    public int getCount() {
        return slideheadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==(RelativeLayout) o;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slideview,container,false);

        ImageView slidemage=(ImageView)view.findViewById(R.id.round);
        TextView textView1=(TextView)view.findViewById(R.id.textView);
        TextView textView2=(TextView)view.findViewById(R.id.textView2);

        slidemage.setImageResource(slideimages[position]);
        textView1.setText(slideheadings[position]);
        textView2.setText(slidedescription[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
