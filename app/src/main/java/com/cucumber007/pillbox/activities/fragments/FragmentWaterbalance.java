package com.cucumber007.pillbox.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.MainActivity;
import com.cucumber007.pillbox.activities.WaterAddActivity;
import com.cucumber007.pillbox.activities.fragments.water.WaveRender;
import com.cucumber007.pillbox.activities.fragments.water.gl.Config2D888MSAA;
import com.cucumber007.pillbox.activities.settings.AbstractSettingActivity;
import com.cucumber007.pillbox.activities.settings.dialogs.SystemDialogActivity;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.models.WaterModel;

public class FragmentWaterbalance extends Fragment implements SensorEventListener {


    private GLSurfaceView glSurfaceView;
    private WaveRender render;
    private Config2D888MSAA ConfigChooser;
    private Handler drawHandler = new Handler();
    private Boolean pause = false;
    private int FPS = 60;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Context context;

    private WaterModel waterModel;
    private boolean ml;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_waterbalance, null);
        context = getActivity();

        waterModel = ModelManager.getInstance(context).getWaterModel();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        glSurfaceView = (GLSurfaceView)v.findViewById(R.id.surfaceviewclass);
        if(Build.VERSION.SDK_INT > 10) {
            glSurfaceView.setPreserveEGLContextOnPause(true);
        }
        glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(ConfigChooser = new Config2D888MSAA());

        render = new WaveRender(getActivity());

        render.onInit = new Runnable() {
            @Override
            public void run() {
                setWaterLevel();
                setWaterText();
            }
        };

        glSurfaceView.setRenderer(render);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
//        v.addView(glSurfaceView);
        v.findViewById(R.id.slide_menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openSlideMenu();
            }
        });

        /*v.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEvent(event);
            }
        });*/

        v.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context, WaterAddActivity.class), 1);
            }
        });

        v.findViewById(R.id.add_button).setVisibility(View.VISIBLE);
        return v;
    }

    void requestRender(){
        drawHandler.removeCallbacks(drawer);
        if(!pause){
            drawHandler.postDelayed(drawer, 1000 / FPS); // отложенный вызов mDrawRa
            glSurfaceView.requestRender();
        }
    }

    private void setWaterLevel() {
        glSurfaceView.queueEvent(new Runnable() {
            public void run() {
                float percent = waterModel.getWaterPercent();
//                if (percent > 1f) percent = 1f;
                render.setWaterPercent(percent);
                //"взболтать ее", первое значение - количество источников, второе - сила с направлением
                render.shakeWater(3, 0.2f * (true ? 1 : -1));
            }
        });
    }

    private final Runnable drawer = new Runnable() {
        public void run() {
            requestRender();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        pause = true;
        glSurfaceView.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        ml = context.getSharedPreferences(AbstractSettingActivity.SETTINGS_SHARED_PREFERENCES, Activity.MODE_PRIVATE)
                .getBoolean(SystemDialogActivity.SYSTEM_OPTION, true);
        pause = false;
        glSurfaceView.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        requestRender();
    }

    //@Override
    public void onRestart(){
        //super.onRestart();
        pause = false;
        glSurfaceView.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop(){
        super.onStop();
        pause = true;
        //this.finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float x = event.values[0];
            final float y = event.values[1];
            final float z = event.values[2];
            glSurfaceView.queueEvent(new Runnable() {
                public void run() {
                    render.updateAccelerometerData(x, y, z);
                }
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private boolean increase = false;

    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            glSurfaceView.queueEvent(new Runnable() {
                public void run() {
                    boolean prevIncrese = increase;
                    float percent = render.getWaterPercent();
                    percent += 0.1f * (increase ? 1 : -1);
                    if (percent<=0.0f) {
                        percent = 0.0f;
                        increase = true;
                    } else if (percent>=1.0f) {
                        percent = 1.0f;
                        increase = false;
                    }
                    //это по сути и есть все апи
                    //задать уровень воды
                    render.setWaterPercent(percent);
                    //"взболтать ее", первое значение - количество источников, второе - сила с направлением
                    render.shakeWater(3, 0.2f * (prevIncrese ? 1 : -1));
                }
            });
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            final int plus = data.getIntExtra("value", 0);
            waterModel.addWater(plus);
            setWaterText();

            glSurfaceView.queueEvent(new Runnable() {
                public void run() {
                    setWaterLevel();
                }
            });
        }

    }

    private void setWaterText() {
        final String text;
        if(ml) text = waterModel.getWaterLevelMl() + " / " + waterModel.getWaterLimitMl() + " ml";
        else text = waterModel.getWaterLevelOz() + " / " + waterModel.getWaterLimitOz() + " oz";
        if (glSurfaceView != null) {
            glSurfaceView.queueEvent(new Runnable() {
                public void run() {
                    render.setTooltipText(text);
                }
            });
        }
    }


    //пример
//    glSurfaceView.queueEvent(new Runnable() { //желательно делать вызов иммено таким образом, так якобы
//                                  //потоко-безопнее, но поскольку меняется всего пару значений, то на практике пох
//        public void run() {
//            //задать уровень воды 30%
//            render.setWaterPercent(0.3f);
//            //"взболтать ее", 3 источника, 0.3 сила (изменение скорости колонки воды) вверх
//            render.shakeWater(3, 0.3f);
//        }
//    });
}
