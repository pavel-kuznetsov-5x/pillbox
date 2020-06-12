package com.cucumber007.pillbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.objects.IconWithTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SlideMenuAdapter extends BaseAdapter {

    public static View selectedItemView;

    private Context mContext;
    private OnSlideItemClickListener listener;
    private List<IconWithTitle> slideMenuItems;
    private static int resource = R.layout.slide_menu_element;

    public SlideMenuAdapter(Context context, List<IconWithTitle> objects, OnSlideItemClickListener listener) {
        this.slideMenuItems = objects;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

            final View target = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position);
                }
            });

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IconWithTitle item = slideMenuItems.get(position);
        //viewHolder.selectedOverlay.setVisibility(selectedItemIndex == position ? View.VISIBLE : View.GONE);
        viewHolder.icon.setImageResource(item.getIconResource());
        viewHolder.title.setText(item.getTitle());

        if (selectedItemView == null) {
            selectedItemView = convertView;
        }
        return convertView;

    }

    @Override
    public int getCount() {
        return slideMenuItems.size();
    }

    @Override
    public Object getItem(int i) {
        return slideMenuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        @BindView(R.id.chosen) ImageView chosen;
        @BindView(R.id.selected_overlay) LinearLayout selectedOverlay;
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.title) TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnSlideItemClickListener {
        void onClick(View v, int id);
    }
}
