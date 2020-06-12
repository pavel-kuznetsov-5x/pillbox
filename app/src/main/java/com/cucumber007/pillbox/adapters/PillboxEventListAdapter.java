package com.cucumber007.pillbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.IconChoiseActivity;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.objects.pills.PillboxEvent;
import com.cucumber007.pillbox.objects.pills.PillboxEventHolder;
import com.cucumber007.pillbox.views.MyAnimatedExpandableListView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PillboxEventListAdapter extends MyAnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private static EventViewHolder eventHolder;
    private List<PillboxEventHolder> items;

    private LocalDate day;

    private OnEditClickListener onEditClickListener;
    private OnDayChangeListener onDayChangeListener;


    public PillboxEventListAdapter(Context context, OnEditClickListener onEditClickListener) {
        this.context = context;
        this.onEditClickListener = onEditClickListener;
        inflater = LayoutInflater.from(context);
        day = LocalDate.now();
    }

    public void setData(List<PillboxEventHolder> items) {
        this.items = items;
        Collections.sort(items);
        notifyDataSetChanged();
    }

    @Override
    public ButtonsViewHolder getChild(int groupPosition, int childPosition) {
        return new ButtonsViewHolder();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pillbox_event_buttons, parent, false);
        }

        convertView.findViewById(R.id.pillbox_button_take).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelManager.getInstance(context).getReminderModel().setPillboxEventStatus(items.get(groupPosition).getId(), PillboxEvent.STATUS_TAKEN);
                notifyDataSetChanged();
                refreshDay();
            }
        });

        convertView.findViewById(R.id.pillbox_button_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelManager.getInstance(context).getReminderModel().setPillboxEventStatus(items.get(groupPosition).getId(), PillboxEvent.STATUS_SKIPPED);
                notifyDataSetChanged();
                refreshDay();
            }
        });

        convertView.findViewById(R.id.pillbox_button_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClickListener.onClick(v, (int) items.get(groupPosition).getMedId());
            }
        });

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public PillboxEventHolder getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        EventViewHolder holder;
        final PillboxEventHolder eventData = items.get(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pillbox_event_item, parent, false);
            holder = new EventViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (EventViewHolder) convertView.getTag();
        }
        PillboxEventHolder event = items.get(groupPosition);
        holder.time.setText(event.getDate());
        holder.icon.setImageResource(event.getIconResourceId());
        holder.icon.setImageDrawable(IconChoiseActivity.createTintedDrawable(
                        context.getResources().getDrawable(event.getIconResourceId()),
                        eventData.getIconColor())
        );
        holder.title.setText(event.getName());
        holder.dosage.setText(event.getDosage().getTag());

        switch (eventData.getStatus()) {
            case PillboxEvent.STATUS_TAKEN:
                convertView.findViewById(R.id.pillbox_taken).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.pillbox_event_fade).setVisibility(View.GONE);
                break;
            case PillboxEvent.STATUS_SKIPPED:
                convertView.findViewById(R.id.pillbox_event_fade).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.pillbox_taken).setVisibility(View.GONE);
                break;
            case PillboxEvent.STATUS_RESCHEDULED:
                convertView.setBackgroundResource(R.color.blue);
                break;
            case PillboxEvent.STATUS_NONE:
                convertView.findViewById(R.id.pillbox_taken).setVisibility(View.GONE);
                convertView.findViewById(R.id.pillbox_event_fade).setVisibility(View.GONE);
                break;
            default:
                break;
        }

        return convertView;
    }

    public void displayDay(LocalDate day) {
        //todo on activity result run multiple times
        this.day = day;

        List<PillboxEvent> pillboxEvents = ModelManager.getInstance(context).getReminderModel().getEventsByDay(day);
        List<PillboxEventHolder> pillboxEventHolders = new ArrayList<>();

        for (int i = 0; i < pillboxEvents.size(); i++) {
            PillboxEvent event = pillboxEvents.get(i);
            pillboxEventHolders.add(new PillboxEventHolder(event, IconChoiseActivity.MED_ICONS.get(event.getIconName()).getIconResource(), event.getIconColor()));
        }
        
        setData(pillboxEventHolders);

        if (onDayChangeListener != null) {
            onDayChangeListener.onChanged(day, pillboxEvents.size());
        }
    }

    public void refreshDay() {
        displayDay(day);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    public void setOnDayChangeListener(OnDayChangeListener onDayChangeListener) {
        this.onDayChangeListener = onDayChangeListener;
    }

    public interface OnEditClickListener {
        void onClick(View view, int medId);
    }

    public static class EventViewHolder {

        public TextView time, title;
        public TextView dosage;
        public ImageView icon;

        public EventViewHolder(View v) {
            time = (TextView) v.findViewById(R.id.time);
            icon = (ImageView) v.findViewById(R.id.icon);
            title = (TextView) v.findViewById(R.id.title);
            dosage = (TextView) v.findViewById(R.id.dosage);
        }
    }

    public static class ButtonsViewHolder {

        public Button button1;

        public ButtonsViewHolder(View v) {
            button1 = (Button) v.findViewById(R.id.pillbox_button_take);
        }

        public ButtonsViewHolder() {
        }
    }

    public interface OnDayChangeListener {
        void onChanged(LocalDate day, int eventQuantity);
    }

}
