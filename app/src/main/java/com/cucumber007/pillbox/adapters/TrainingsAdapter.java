package com.cucumber007.pillbox.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.TrainingActivity;
import com.cucumber007.pillbox.objects.gym.Training;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrainingsAdapter extends ArrayAdapter<Training> {
    //Adapter for training lists in FragmentGym

    private static final int resourceId = R.layout.training_row;
    private List<Training> dataSet = new ArrayList<>();
    private int order;
    private Context context;
    private boolean purchasedMode = false;

    public TrainingsAdapter(Context context) {
        super(context, resourceId);
        this.context = context;
    }

    public TrainingsAdapter(Context context, boolean purchasedMode) {
        super(context, resourceId);
        this.context = context;
        this.purchasedMode = purchasedMode;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //todo enable viewholder or use recyclerview

        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resourceId, null);
        ViewHolder holder = new ViewHolder(view);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getItem(position).getPreviewImageUrl(), holder.trainingPreview);

        holder.price.setText(getItem(position).getPriceString());
        if(getItem(position).isForShare())
            holder.price.setTextColor(context.getResources().getColor(R.color.share_blue));

        if (getItem(position).isDownloaded()) {
            holder.price.setText("Downloaded");
        } else if (purchasedMode) {
            holder.price.setVisibility(View.GONE);
        }

        holder.trainingTitle.setText(getItem(position).getTitle());
        holder.producerName.setText(getItem(position).getProducerName());

        String goals = "";
        for (int i = 0; i < getItem(position).getGoalIds().size(); i++) {
            goals += getItem(position).getGoalFromId(i) + "\n";
        }
        holder.goal.setText(goals);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TrainingActivity.class);
                intent.putExtra("training_id", getItem(position).getId());
                intent.putExtra("is_purchased", getItem(position).isPurchased());
                getContext().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public Training getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }


    public void setTrainings(List<Training> trainings) {
        dataSet = trainings;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.producer_name) TextView producerName;
        @BindView(R.id.training_title) TextView trainingTitle;
        @BindView(R.id.goal) TextView goal;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.downloaded) ImageView downloaded;
        @BindView(R.id.imageView14) ImageView imageView14;
        @BindView(R.id.training_preview) ImageView trainingPreview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
