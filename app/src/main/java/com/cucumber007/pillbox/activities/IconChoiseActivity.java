package com.cucumber007.pillbox.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.objects.pills.MedIcon;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IconChoiseActivity extends AbstractPortraitActivity {
    //Activity used to choose pills icon

    private static final int COLOR_BACKGROUND_ALPHA = 50;
    @BindView(R.id.colors_layout) View colorsLayout;
    @BindView(R.id.icons_table) TableLayout iconsTable;
    @BindView(R.id.buy_button) Button buyButton;
    @BindView(R.id.slide_menu_button) ImageButton slideMenuButton;

    @BindColor(R.color.med_default_tint) int defaultTintColor;
    @BindColor(R.color.pill_orange) int pillOrange;
    @BindColor(R.color.pill_yellow) int pillYellow;
    @BindColor(R.color.pill_green) int pillGreen;
    @BindColor(R.color.pill_cyan) int pillCyan;
    @BindColor(R.color.pill_blue) int pillBlue;
    @BindColor(R.color.pill_violet) int pillViolet;

    public static HashMap<String, MedIcon> MED_ICONS = new HashMap<>();
    static {
        MED_ICONS.put("med_icon_tablet", new MedIcon("Tablet", R.drawable.med_icon_tablet));
        MED_ICONS.put("med_icon_capsule", new MedIcon("Capsule", R.drawable.med_icon_capsule));
        MED_ICONS.put("med_icon_drops", new MedIcon("Drops", R.drawable.med_icon_drops));
        MED_ICONS.put("med_icon_injection", new MedIcon("Injection", R.drawable.med_icon_injection));
        MED_ICONS.put("med_icon_ointment", new MedIcon("Ointment", R.drawable.med_icon_ointment));
        MED_ICONS.put("med_icon_procedure", new MedIcon("Procedure", R.drawable.med_icon_procedure));
        MED_ICONS.put("med_icon_spray", new MedIcon("Spray", R.drawable.med_icon_spray));
        MED_ICONS.put("med_icon_syrup", new MedIcon("Syrup", R.drawable.med_icon_syrup));
    }

    public static ArrayList<Integer> COLORS;

    private String chosenIconKey = "med_icon_tablet";
    private int tintColor;
    private View selectedColorView;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_choise);
        ButterKnife.bind(this);
        buyButton.setVisibility(View.VISIBLE);
        buyButton.setText("Done");
        slideMenuButton.setVisibility(View.INVISIBLE);

        if (COLORS == null) {
            COLORS = new ArrayList<>();
            COLORS.add(pillOrange);
            COLORS.add(pillYellow);
            COLORS.add(pillGreen);
            COLORS.add(pillCyan);
            COLORS.add(pillBlue);
            COLORS.add(pillViolet);
        }

        tintColor = COLORS.get(0);

        ArrayList<String> keys = new ArrayList<>(MED_ICONS.keySet());
        int COLS = 2;
        int ROWS = 4;

        for (int i = 0; i < ROWS; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < COLS; j++) {
                ViewGroup iconView = (ViewGroup)getLayoutInflater().inflate(R.layout.med_icon, tableRow, false);
                tableRow.addView(iconView, j);
                final String key = keys.get(i * COLS + j);
                MED_ICONS.get(key).setView(iconView);

                Drawable drawable = DrawableCompat.wrap(getResources().getDrawable(MED_ICONS.get(key).getIconResource()));
                DrawableCompat.setTint(drawable, defaultTintColor);
                        ((ImageView) iconView.findViewById(R.id.med_icon)).setImageDrawable(drawable);
                ((TextView) iconView.findViewById(R.id.med_type_name)).setText(MED_ICONS.get(key).getName());

                if(key.equals(getIntent().getStringExtra("icon"))) {
                    chosenIconKey = key;
                    tintColor = COLORS.get(0);
                    paintIcon(tintColor);
                }

                iconView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if (!key.equals(chosenIconKey)) {
                        if(!chosenIconKey.equals("default")) {
                            DrawableCompat.setTint(
                                    ((ImageView) MED_ICONS.get(chosenIconKey).getView().findViewById(R.id.med_icon)).getDrawable(),
                                    defaultTintColor);
                            ((TextView) MED_ICONS.get(chosenIconKey).getView().findViewById(R.id.med_type_name))
                                    .setTextColor(defaultTintColor);
                        }
                        chosenIconKey = key;
                        paintIcon(tintColor);
                    }
                    }
                });
            }

            iconsTable.addView(tableRow);
        }

        for (int i = 0; i < COLORS.size(); i++) {
            ViewGroup colorView = (ViewGroup)getLayoutInflater().inflate(R.layout.color_view, (ViewGroup) colorsLayout, false);
            ((ViewGroup) colorsLayout).addView(colorView);

            ImageView colorImage = (ImageView)colorView.findViewById(R.id.color_image);

            if(i == 0) {
                selectedColorView = colorView;
                tintColor = COLORS.get(i);
                Drawable backDrawable = createTintedDrawable(getResources().getDrawable(R.drawable.color_circle), tintColor);
                backDrawable.setAlpha(COLOR_BACKGROUND_ALPHA);
                selectedColorView.findViewById(R.id.color_image).setBackground(backDrawable);
            }

            /*Drawable backDrawable = createTintedDrawable(root.getBackground(), COLORS.get(i));
            backDrawable.setAlpha(50);
            root.setBackground(backDrawable);*/

            colorImage.setImageDrawable(createTintedDrawable(colorImage.getDrawable(), COLORS.get(i)));
            final int index = i;

            colorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedColorView != null) {
                        selectedColorView.findViewById(R.id.color_image).setBackgroundColor(Color.TRANSPARENT);
                    }
                    selectedColorView = v;
                    tintColor = COLORS.get(index);
                    Drawable backDrawable = createTintedDrawable(getResources().getDrawable(R.drawable.color_circle), tintColor);
                    backDrawable.setAlpha(COLOR_BACKGROUND_ALPHA);
                    selectedColorView.findViewById(R.id.color_image).setBackground(backDrawable);
                    /*tintColor = COLORS.get(index);
                    paintIcon(tintColor);*/
                }
            });
        }

    }

    public static Drawable createTintedDrawable(Drawable drawable, int color) {
        drawable.mutate();
        Drawable clone = drawable.getConstantState().newDrawable();
        Drawable res = DrawableCompat.wrap(clone);
        DrawableCompat.setTint(res, color);
        return res;
    }

    private void paintIcon(int color) {
        if(!chosenIconKey.equals("default")) {
            color = COLORS.get(0);

            ImageView iconView = (ImageView)MED_ICONS.get(chosenIconKey).getView().findViewById(R.id.med_icon);
            DrawableCompat.setTint(iconView.getDrawable(), color);
            TextView textView = (TextView)MED_ICONS.get(chosenIconKey).getView().findViewById(R.id.med_type_name);
            textView.setTextColor(color);
        }

    }

    @OnClick(R.id.buy_button)
    public void confirm() {
        Intent intent = new Intent();
        intent.putExtra("icon", chosenIconKey);
        intent.putExtra("color", tintColor);
        setResult(RESULT_OK, intent);
        finish();
    }

}
