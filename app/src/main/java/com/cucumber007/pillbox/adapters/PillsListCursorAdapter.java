package com.cucumber007.pillbox.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.IconChoiseActivity;
import com.cucumber007.pillbox.activities.MedActivity;
import com.cucumber007.pillbox.database.tables.MedsTable;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.utils.PillboxNotificationManager;

public class PillsListCursorAdapter extends CursorAdapter {
    //Adapter for med list in FragmentPills

    private Context context;
    private MedListObserver myObserver;

    public PillsListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.med_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.med_name))
                .setText(cursor.getString(cursor.getColumnIndexOrThrow(MedsTable.COLUMN_NAME)));

        String iconName = cursor.getString(cursor.getColumnIndexOrThrow(MedsTable.COLUMN_ICON));
        if (iconName == null) {
            //todo android string
            iconName = "med_icon_tablet";
        }
        int iconResource;
        try {
            iconResource = (IconChoiseActivity.MED_ICONS.get(iconName).getIconResource());
        } catch (NullPointerException e) {
            e.printStackTrace();
            iconResource = (IconChoiseActivity.MED_ICONS.get("med_icon_tablet").getIconResource());
        }

        int color = cursor.getInt(cursor.getColumnIndexOrThrow(MedsTable.COLUMN_ICON_COLOR));
        ImageView iconView = ((ImageView) view.findViewById(R.id.med_icon));
        Drawable tinted = IconChoiseActivity.createTintedDrawable(
                context.getResources().getDrawable(iconResource),
                color
        );
        iconView.setImageDrawable(tinted);

        final int medId = cursor.getInt(cursor.getColumnIndex(MedsTable.COLUMN_ID));
        view.findViewById(R.id.med_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo out from adapter
                AskOption(medId).show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditActivity(medId);
            }
        });
    }

    private void startEditActivity(int medId) {
        final Intent intent = new Intent(context, MedActivity.class);
        intent.putExtra("med_id", medId);
        context.startActivity(intent);
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
        if (myObserver != null) {
            myObserver.update(getCount());
        }
    }

    public void setObserver(MedListObserver myObserver) {
        this.myObserver = myObserver;
    }

    //todo wtf is this?
    private AlertDialog AskOption(final int id)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to delete course?")
                //.setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                PillboxNotificationManager.getInstance(context).deleteAlarmsForEvents(ModelManager.getInstance(context).getReminderModel().getEventsByMed(id));
                                ModelManager.getInstance(context).getPillsModel().deleteMed(id);
                                return null;
                            }
                        }.execute();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    private class DeleteMedTask extends AsyncTask {

        @Override
        protected Object doInBackground(final Object[] params) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }
    }

    public interface MedListObserver {
        void update(int medQuantity);
    }

}
