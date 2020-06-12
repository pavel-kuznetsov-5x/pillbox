package com.cucumber007.pillbox.activities.fragments.water.gl;

/**
 * Created by anton on 05.08.2015.
 */
public class Vertex {
    public final static int BYTES_PER_INT = 4;
    public final static int BYTES_PER_FLOAT = 4;
    public final static int POSITION_SIZE = 3;
    public final static int COLOR_SIZE = 4;
    public final static int TEXTURE_SIZE = 2;
    public final static int BLOCK_SIZE = POSITION_SIZE + COLOR_SIZE + TEXTURE_SIZE;
    public final static int BLOCK_SIZE_BYTES = BLOCK_SIZE*BYTES_PER_FLOAT;
    public final static int POSITION_OFFSET = 0;
    public final static int POSITION_OFFSET_BYTES = BYTES_PER_FLOAT*POSITION_OFFSET;
    public final static int COLOR_OFFSET = POSITION_SIZE;
    public final static int COLOR_OFFSET_BYTES = BYTES_PER_FLOAT*COLOR_OFFSET;
    public final static int TEXTURE_OFFSET = COLOR_OFFSET+COLOR_SIZE;
    public final static int TEXTURE_OFFSET_BYTES = BYTES_PER_FLOAT*TEXTURE_OFFSET;

    public float[] position = {.0f, .0f, .0f};
    public float[] color = {.0f, .0f, .0f, .0f};
    public float[] texCoord = {.0f, .0f};

    public Vertex(float p1, float p2, float p3,
                  float c1, float c2, float c3, float c4,
                  float t1, float t2){
        position[0] = p1;
        position[1] = p2;
        position[2] = p3;
        color[0] = c1;
        color[1] = c2;
        color[2] = c3;
        color[3] = c4;
        texCoord[0] = t1;
        texCoord[1] = t2;
    }

    public static void writeToArray(int index, float[] target, float p1, float p2, float p3,
            float c1, float c2, float c3, float c4,
            float t1, float t2){
        index = index*BLOCK_SIZE;
        target[index] = p1;
        target[index+1] = p2;
        target[index+2] = p3;
        target[index+3] = c1;
        target[index+4] = c2;
        target[index+5] = c3;
        target[index+6] = c4;
        target[index+7] = t1;
        target[index+8] = t2;
    }

    public static void writeToArray(int index, float[] target, float p1, float p2, float p3, float[] color){
        index = index*BLOCK_SIZE;
        target[index] = p1;
        target[index+1] = p2;
        target[index+2] = p3;
        target[index+3] = color[0];
        target[index+4] = color[1];
        target[index+5] = color[2];
        target[index+6] = color[3];
        target[index+7] = 0.0f;
        target[index+8] = 0.0f;
    }

}

