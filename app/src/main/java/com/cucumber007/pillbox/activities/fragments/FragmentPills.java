package com.cucumber007.pillbox.activities.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.MainActivity;
import com.cucumber007.pillbox.activities.MedActivity;
import com.cucumber007.pillbox.adapters.PillsListCursorAdapter;
import com.cucumber007.pillbox.models.ModelManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentPills extends Fragment {

    @BindView(R.id.meds_list) ListView medsList;
    @BindView(R.id.add_button) ImageButton addButton;
    @BindView(R.id.pills_placeholder) TextView placeholder;

    private Context context;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pills, null);
        ButterKnife.bind(this, v);
        context = container.getContext();

        final PillsListCursorAdapter cursorAdapter = new PillsListCursorAdapter(context, ModelManager.getInstance(context).getPillsModel().getMedsCursor(), true);
        placeholder.setVisibility(cursorAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
        cursorAdapter.setObserver(new PillsListCursorAdapter.MedListObserver() {
            @Override
            public void update(int medQuantity) {
                if (placeholder != null) {
                    placeholder.setVisibility(medQuantity == 0 ? View.VISIBLE : View.GONE);
                }
            }
        });

        medsList.setAdapter(cursorAdapter);

        addButton.setVisibility(View.VISIBLE);
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


}
