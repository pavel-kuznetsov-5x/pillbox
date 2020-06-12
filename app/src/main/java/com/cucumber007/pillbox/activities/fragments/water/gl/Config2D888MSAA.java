package com.cucumber007.pillbox.activities.fragments.water.gl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Created by anton on 04.08.2015.
 */
public class Config2D888MSAA implements GLSurfaceView.EGLConfigChooser {
    private int[] Value;
    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        Value = new int[1];
        int[] configSpec = {
                EGL10.EGL_RED_SIZE, 7,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 7,
                EGL10.EGL_ALPHA_SIZE, 2,
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL10.EGL_SAMPLE_BUFFERS, 1,
                EGL10.EGL_SAMPLES, 2,
                EGL10.EGL_NONE
        };
        if (!egl.eglChooseConfig(display, configSpec, null, 0,Value)) {
            throw new IllegalArgumentException("RGB888 eglChooseConfig failed");
        }
        int numConfigs = Value[0];
        if (numConfigs <= 0) { //else try RGB565
            configSpec = new int[]{
                    EGL10.EGL_RED_SIZE, 4,
                    EGL10.EGL_GREEN_SIZE, 5,
                    EGL10.EGL_BLUE_SIZE, 5,
                    EGL10.EGL_ALPHA_SIZE, 2,
                    EGL10.EGL_RENDERABLE_TYPE, 4,
                    EGL10.EGL_NONE
            };

            if (!egl.eglChooseConfig(display, configSpec, null, 0, Value)) {
                throw new IllegalArgumentException("RGB565 eglChooseConfig failed");
            }

            numConfigs = Value[0];

            if (numConfigs <= 0) {
                throw new IllegalArgumentException("No configs match configSpec RGB565");
            }
        }
        EGLConfig[] configs = new EGLConfig[numConfigs];
        int[] num_conf = new int[numConfigs];
        egl.eglChooseConfig(display, configSpec, configs, numConfigs, Value);
        return configs[0];
    }


}