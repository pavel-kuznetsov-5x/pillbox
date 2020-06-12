package com.cucumber007.pillbox.activities.fragments.water.gl;

import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by anton on 06.08.2015.
 */
public class Shader {
    private int programId;
    private HashMap<String, Integer> slots = new HashMap<>();

    public Shader(String fragment, String vertex, String attrs, String unifs){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertex);
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragment);
        programId = GLES20.glCreateProgram();
        int glErr = GLES20.glGetError();
        GLES20.glAttachShader(programId, vertexShader);
        glErr = GLES20.glGetError();
        GLES20.glAttachShader(programId, pixelShader);
        glErr = GLES20.glGetError();
        LinkProgram();
        glErr = GLES20.glGetError();
        String[] uniforms = splitNames(unifs);
        for (String unif : uniforms)
            slots.put(unif, GLES20.glGetUniformLocation(programId, unif));

        String[] attributes = splitNames(attrs);
        for (String attr : attributes)
            slots.put(attr, GLES20.glGetAttribLocation(programId, attr));
        for (String attr : attributes)
            GLES20.glEnableVertexAttribArray(slots.get(attr).intValue());

        GLES20.glReleaseShaderCompiler();
    }

    public int getSlot(String name){
        return slots.get(name).intValue();
    }

    private String[] splitNames(String names){
        return names.split(" ");
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        int glErr = GLES20.glGetError();
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            glErr = GLES20.glGetError();
            GLES20.glCompileShader(shader);
            glErr = GLES20.glGetError();
            int[] compiled = new int[1];
            glErr = GLES20.glGetError();
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            glErr = GLES20.glGetError();
            if (compiled[0] == 0) {
                Log.e("Error", "Could not compile shader name:" + source + " =>" + shaderType + ":");
                Log.e("Error", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
            int a = 0;
        }
        return shader;
    }

    public void LinkProgram(){
        GLES20.glLinkProgram(programId);
    }

    public int getProgramId(){
        return programId;
    }

}