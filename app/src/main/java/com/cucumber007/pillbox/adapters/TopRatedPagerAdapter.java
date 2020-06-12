package com.cucumber007.pillbox.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.TrainingActivity;
import com.cucumber007.pillbox.objects.gym.Training;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class TopRatedPagerAdapter extends PagerAdapter {
    //Adater for TopRated view in FragmentGym (temporarily disabled)

    private Context context;
    private List<Training> trainingList;

    public TopRatedPagerAdapter(Context context, List<Training> trainingList) {
        super();
        this.context = context;
        this.trainingList = trainingList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pager_training, null, false);
        final Training tran = trainingList.get(position);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(tran.getPreviewImageUrl(), (ImageView) view.findViewById(R.id.training_preview));

        ((TextView) view.findViewById(R.id.producer_name)).setText(tran.getProducerName());
        ((TextView) view.findViewById(R.id.training_title)).setText(tran.getTitle());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrainingActivity.class);
                intent.putExtra("training_id", tran.getId());
                context.startActivity(intent);
            }
        });



        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return trainingList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


}
