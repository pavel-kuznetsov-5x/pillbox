package com.cucumber007.pillbox.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.MainActivity;
import com.cucumber007.pillbox.activities.MedActivity;
import com.cucumber007.pillbox.adapters.CalendarViewPagerAdapter;
import com.cucumber007.pillbox.adapters.PillboxEventListAdapter;
import com.cucumber007.pillbox.listeners.OnPageChangeListenerWithGeneration;
import com.cucumber007.pillbox.objects.pills.PillboxEventHolder;
import com.cucumber007.pillbox.views.CalendarViewPager;
import com.cucumber007.pillbox.views.MyAnimatedExpandableListView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentReminder extends Fragment {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.month_year_name) TextView monthYearName;
    @BindView(R.id.week_view_pager) CalendarViewPager weekViewPager;
    @BindView(R.id.reminder_list_view) MyAnimatedExpandableListView reminderListView;
    @BindView(R.id.slide_menu_button) ImageButton slideMenuButton;
    @BindView(R.id.add_button) ImageButton addButton;
    @BindView(R.id.buy_button) Button buyButton;
    @BindView(R.id.toolbar) RelativeLayout toolbar;
    @BindView(R.id.events_placeholder) TextView eventsPlaceholder;

    private PillboxEventListAdapter listViewAdapter;
    private CalendarViewPagerAdapter calendarAdapter;
    private int lastExpandedPosition = -1;

    private Context context;

    public FragmentReminder() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder, null);
        ButterKnife.bind(this, v);

        List<PillboxEventHolder> pillboxEvents = new ArrayList<>();
        listViewAdapter = new PillboxEventListAdapter(context, new PillboxEventListAdapter.OnEditClickListener() {
            @Override
            public void onClick(View view, int medId) {
                final Intent intent = new Intent(context, MedActivity.class);
                intent.putExtra("med_id", medId);
                startActivityForResult(intent, 1);
            }
        });
        listViewAdapter.setData(pillboxEvents);

        calendarAdapter = new CalendarViewPagerAdapter(context);
        calendarAdapter.setOnDayChangeListener(new CalendarViewPagerAdapter.OnWeekDayChangeListener() {
            @Override
            public void onChanged(LocalDate day) {
                listViewAdapter.displayDay(day);
            }
        });

        //////////////////////////////
        addButton.setVisibility(View.VISIBLE);

        calendarAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                weekViewPager.refreshFrontView();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                weekViewPager.refreshFrontView();
            }
        });

        weekViewPager.setAdapter(calendarAdapter);
        weekViewPager.setCurrentItem(calendarAdapter.getCount() / 2);
        weekViewPager.addOnPageChangeListener(new OnPageChangeListenerWithGeneration(calendarAdapter, monthYearName));

        //////////////////////

        reminderListView.setGroupIndicator(null);
        reminderListView.setChildIndicator(null);
        reminderListView.setDivider(getResources().getDrawable(R.color.title_shadow));
        reminderListView.setDividerHeight(1);
        reminderListView.setAdapter(listViewAdapter);

        // In order to show animations, we need to use a custom click handler
        //todo to custom view
        reminderListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (reminderListView.isGroupExpanded(groupPosition)) {
                    reminderListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    reminderListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        reminderListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    reminderListView.collapseGroupWithAnimation(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        listViewAdapter.setOnDayChangeListener(new PillboxEventListAdapter.OnDayChangeListener() {

            @Override
            public void onChanged(LocalDate day, int count) {
                reminderListView.collapseGroupWithAnimation(lastExpandedPosition);
                lastExpandedPosition = -1;
                eventsPlaceholder.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
            }

        });

        return v;
    }

    @OnClick(R.id.slide_menu_button)
    public void openSlideMenu() {
        ((MainActivity) getActivity()).openSlideMenu();
    }

    @OnClick(R.id.add_button)
    public void addPill() {
        startActivityForResult(new Intent(context, MedActivity.class), 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        weekViewPager.setCurrentItem(calendarAdapter.getCount() / 2);
        calendarAdapter.notifyDataSetChanged();
        if (calendarAdapter.getSelectedDate() != null) {
            listViewAdapter.displayDay(calendarAdapter.getSelectedDate());
        } else listViewAdapter.displayDay(calendarAdapter.TODAY_DATE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            listViewAdapter.refreshDay();
            calendarAdapter.notifyDataSetChanged();
            weekViewPager.refreshFrontView();
        }
    }


}
