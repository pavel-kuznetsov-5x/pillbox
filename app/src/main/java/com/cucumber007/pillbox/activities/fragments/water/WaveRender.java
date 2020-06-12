package com.cucumber007.pillbox.activities.fragments.water;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.cucumber007.pillbox.activities.fragments.water.gl.Shader;
import com.cucumber007.pillbox.activities.fragments.water.gl.Vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public class WaveRender implements GLSurfaceView.Renderer {
    private final static boolean TRACE_FPS = false;

    private final static String FONT_NAME = "ProximaNovaThin.otf";
    private final static String FONT_TOOLTIP_NAME = "ProximaNovaRegular.otf";
    private final static double LOW_PASS_FILTER_ALPHA = 0.1;
    private final static int SHAKE_NEIGHTBOURS = 7;
    private final static float[] COLOR_BLUE       = {45.0f/255.0f, 119.0f/255.0f, 212.0f/255.0f, 1.0f};//rgba
    private final static float[] COLOR_LIGHT_BLUE = {50.0f/255.0f, 205.0f/255.0f, 238.0f/255.0f, 1.0f};//rgba
    private final static float[] COLOR_TEXT = {50.0f/255.0f, 205.0f/255.0f, 238.0f/255.0f, 1.0f};//rgba

    private final static int COLUMNS_COUNT = 40;
    private final static float COLUMN_WIDTH = 1.0f/(COLUMNS_COUNT-1);

    private final static float TENSION = 0.015f;
    private final static float DAMPENING = 0.065f;
    private final static float SPREAD = 0.17f;
    private final static int WAVE_NEIGHTBOURS = 8;

    private final static float LYING_ANGLE = 9.0f;
    private final static float ROTATE_SPEED_MULTIPLIER = 0.005f;
    private final static float SHAKE_SPEED_MAX = 0.05f;
    private final static float FLIP_SPEED_MULTIPLIER = 0.01f;
    private final static float SHAKE_ON_FLIP_FACTOR = 0.98f;
    private final static long SHAKE_ON_IDLE_PERIOD = 500000000;//nanosec; 1 sec = 1000000000
    private final static float SHAKE_ON_IDLE_SPEED = 0.05f;
    private final static float MAGIC_TOP = 0.73f;
    private final static float MAGIC_BOTTOM = 0.22f;
    private final static boolean SHAKE_ON_ROTATION = true;


    private long shakeOnIdlePrevTime = 0;
    private float waterPercent = 0.0f;
    private float textPercent = 0.0f;

    private int fpsCounter = 0;
    private long fpsCounterPrevTime = 0;

    private Context cnx;
    private Shader waterShader;

    private float accelX = 0.0f;
    private float accelY = 0.0f;
    private float accelZ = 0.0f;

    private WaterColumn[] columns;

    private int screenWidth;
    private int screenHeight;
    private float scaleFactor = 1.2f;


    private Bitmap textBmp;
    private Canvas textCanvas;
    private Paint paintTootipText;
    private Paint textPaintPercent;
    private String tooltipText = "";
    private Typeface font;
    private Typeface fontTooltip;
    private int[] textureId;
    private String text;
    private Shader textShader;
    private FloatBuffer textBuffer;

    private boolean initComplete = false;

    public Runnable onInit = null;


    public WaveRender(Context context){
        cnx = context;
    }

    float[] vertsData;
    float[] matrix;

    FloatBuffer verticesBuffer;

    public float getWaterPercent(){
        return waterPercent;
    }

    public void setWaterPercent(float waterPercent){
        waterPercent =  (waterPercent < 0.0f) ? 0.0f : waterPercent;
        if (!initComplete) {
            return;
        }
        waterPercent = (waterPercent > 9.99) ? 9.99f : waterPercent;
        this.waterPercent = (waterPercent > 1.0) ? 1.0f : waterPercent;
        this.textPercent = waterPercent;
        for (WaterColumn column : columns) {
            column.targetHeight = this.waterPercent;
        }

        updateText();
    }

    public String getTooltipText(){
        return tooltipText;
    }

    public void setTooltipText(String value){
        tooltipText = value;
        updateText();
    }


    public void shakeWater(int shakersCount, float delta){
        for (int i=0; i<shakersCount; i++) {
            columns[(int) Math.round(random() * (columns.length - 1))].speed += delta;
        }
    }

    private double lowPass(double prev, double current){
        double res = 0;
        if (current - prev < -180){
            res = prev + LOW_PASS_FILTER_ALPHA*((360 + current) - prev);
            if (res > 180) {
                res = res - 360;
            }
        } else if (current - prev > 180){
            res = prev + LOW_PASS_FILTER_ALPHA*((current - 360) - prev);
            if (res < -180) {
                res = 360 - res;
            }
        } else {
            res = prev + LOW_PASS_FILTER_ALPHA*(current - prev);
        }

        return res;
    }


    public void updateAccelerometerData(float x, float y, float z){
        accelX = x;
        accelY = y;
        accelZ = z;
        //updateAngle();
//        Log.d("accelerometer","x"+x+" y"+y+" z"+z);
    }

    private double[] values = null;
    private double angleXY = 0.0;
    private double angleZ = 0.0;

    private void updateAngle(){
        if (!initComplete) {
            return;
        }
        double norm = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        if (Math.abs(norm)<0.0001){
            norm = norm<0 ? -1 : 1;//Math.signum can return 0;
        }
        double inclination = Math.toDegrees(Math.acos(accelZ / norm));
        double rotation = Math.toDegrees(Math.atan2(-accelX / norm, accelY / norm));

        inclination = Utils.filterNaN(inclination);
        rotation = Utils.filterNaN(rotation);

        if ((inclination < LYING_ANGLE) || (inclination > 180 - LYING_ANGLE)) {
            if (abs(angleXY -180)>abs(angleXY)){
                rotation = 0;
            } else {
                rotation = 180;
            }
        }

        angleXY = lowPass(angleXY, rotation);
        angleZ = lowPass(angleZ, inclination);

        angleXY = Utils.filterNaN(angleXY);
        angleZ = Utils.filterNaN(angleZ);

        double deltaXY = rotation-angleXY;
        double deltaZ = inclination-angleZ;

        if (deltaXY<-270){
            deltaXY = deltaXY + 360;
        } else if (deltaXY>270){
            deltaXY = deltaXY - 360;
        }

        deltaXY = Utils.filterNaN(deltaXY);
        deltaZ = Utils.filterNaN(deltaZ);

        for (int i = 0; i < columns.length; i++) {
            if (random() > SHAKE_ON_FLIP_FACTOR) {
                columns[i].speed += (float) deltaZ * FLIP_SPEED_MULTIPLIER;
            }
        }


        if (SHAKE_ON_ROTATION) {
            int correction = (int) Math.round(Math.random() * SHAKE_NEIGHTBOURS);
            float delta = (float) Math.min(deltaXY * ROTATE_SPEED_MULTIPLIER, SHAKE_SPEED_MAX);
            if (deltaXY>0) {
                columns[columns.length - 1 - correction].speed += delta;
            } else {
                columns[correction].speed += -delta;
            }
        }
    }

    private void updateMatrix(){
        Matrix.setIdentityM(matrix, 0); //reset matrix
        Matrix.orthoM(matrix, 0, 0.0f, 1.0f, 0.0f, 1.0f, -1, 1);
        Matrix.translateM(matrix, 0, 0.5f, 0.5f, 0.0f);
        Matrix.scaleM(matrix, 0, scaleFactor, scaleFactor, scaleFactor);
        Matrix.rotateM(matrix, 0, (float) angleXY, 0.f, 0.f, 1.f);
        Matrix.translateM(matrix, 0, -0.5f, -0.5f, 0.0f);
    }

    private float[] lDeltas = null;
    private float[] rDeltas = null;

    private void updateWater(){
        int size = columns.length;
        long curTime = System.nanoTime();
        if (curTime - shakeOnIdlePrevTime > SHAKE_ON_IDLE_PERIOD) {
            shakeOnIdlePrevTime = curTime;
            int correction = (int) Math.round(Math.random() * SHAKE_NEIGHTBOURS);
            float dir = (Math.random()<0.5) ? 1.0f: -1.0f;
            int index = (Math.random()<0.5) ? correction: columns.length-1-correction;
            WaterColumn col = columns[index];
            //float detla = 1.0f * ((col.targetHeight < col.height) ? -1.0f : 1.0f);
            col.speed += SHAKE_ON_IDLE_SPEED*dir;
        }

        updateAngle();

        for (int i = 0; i < size; i++)
            columns[i].update(DAMPENING, TENSION);

        if (lDeltas==null || lDeltas.length<size){
            lDeltas = new float[size];
        }
        if (rDeltas==null || rDeltas.length<size){
            rDeltas = new float[size];
        }

        for (int j = 0; j < WAVE_NEIGHTBOURS; j++){
            for (int i = 0; i < size; i++){
                if (i > 0){
                    lDeltas[i] = SPREAD * (columns[i].height - columns[i - 1].height);
                    columns[i - 1].speed += lDeltas[i];
                }
                if (i < size - 1){
                    rDeltas[i] = SPREAD * (columns[i].height - columns[i + 1].height);
                    columns[i + 1].speed += rDeltas[i];
                }
            }
            for (int i = 0; i < size; i++){
                if (i > 0){
                    columns[i - 1].height += lDeltas[i];
                }
                if (i < size - 1){
                    columns[i + 1].height += rDeltas[i];
                }
            }
        }

    }

    private void updateVerts(){
        float magic_delta = MAGIC_TOP - MAGIC_BOTTOM;
        for (int i=0; i<columns.length-1;i++){
            float l = (i)*COLUMN_WIDTH;
            float r = (i+1)*COLUMN_WIDTH;
            float t1 = MAGIC_BOTTOM + magic_delta * columns[i].height;
            float t2 = MAGIC_BOTTOM + magic_delta * columns[i+1].height;
            float b = 0.f;
            Vertex.writeToArray(i * 6,vertsData, l, t1, 0, COLOR_LIGHT_BLUE);
            Vertex.writeToArray(i * 6+1,vertsData, l, b, 0, COLOR_BLUE);
            Vertex.writeToArray(i * 6+2,vertsData, r, t2, 0, COLOR_LIGHT_BLUE);

            Vertex.writeToArray(i * 6+3,vertsData, r, t2, 0, COLOR_LIGHT_BLUE);
            Vertex.writeToArray(i * 6+4,vertsData, l, b, 0, COLOR_BLUE);
            Vertex.writeToArray(i * 6+5,vertsData, r, b, 0, COLOR_BLUE);
        }
    }

    private void drawText(){
        GLES20.glUseProgram(textShader.getProgramId());

        GLES20.glEnableVertexAttribArray(textShader.getSlot("a_position"));
        GLES20.glEnableVertexAttribArray(textShader.getSlot("a_texcoord"));
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        textBuffer.position(Vertex.POSITION_OFFSET);
        GLES20.glVertexAttribPointer(textShader.getSlot("a_position"), Vertex.POSITION_SIZE, GLES20.GL_FLOAT,
                false, Vertex.BLOCK_SIZE_BYTES, textBuffer);
        textBuffer.position(Vertex.TEXTURE_OFFSET);
        GLES20.glVertexAttribPointer(textShader.getSlot("a_texcoord"), Vertex.TEXTURE_SIZE, GLES20.GL_FLOAT,
                false, Vertex.BLOCK_SIZE_BYTES, textBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glDisableVertexAttribArray(textShader.getSlot("a_position"));
        GLES20.glDisableVertexAttribArray(textShader.getSlot("a_texcoord"));
    }

    private void drawWater(){
        GLES20.glUseProgram(waterShader.getProgramId());
        verticesBuffer.clear();
        verticesBuffer.put(vertsData).position(0);
        GLES20.glEnableVertexAttribArray(waterShader.getSlot("a_position"));
        GLES20.glEnableVertexAttribArray(waterShader.getSlot("a_color"));

        verticesBuffer.position(Vertex.POSITION_OFFSET);
        GLES20.glVertexAttribPointer(waterShader.getSlot("a_position"), Vertex.POSITION_SIZE, GLES20.GL_FLOAT,
                false, Vertex.BLOCK_SIZE_BYTES, verticesBuffer);
        verticesBuffer.position(Vertex.COLOR_OFFSET);
        GLES20.glVertexAttribPointer(waterShader.getSlot("a_color"), Vertex.COLOR_SIZE, GLES20.GL_FLOAT,
                false, Vertex.BLOCK_SIZE_BYTES, verticesBuffer);
        GLES20.glUniformMatrix4fv(waterShader.getSlot("u_mvp"), 1, false, matrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, (columns.length-1)*6);
        GLES20.glDisableVertexAttribArray(waterShader.getSlot("a_position"));
        GLES20.glDisableVertexAttribArray(waterShader.getSlot("a_color"));
    }

    public void onDrawFrame(GL10 glUnused) {
        fpsCounter +=1;
        long current_time = System.nanoTime();
        if (current_time - fpsCounterPrevTime > 1000000000){
            if (TRACE_FPS) {
                Log.d("FPS", fpsCounter + "");
            }
            fpsCounter = 0;
            fpsCounterPrevTime = current_time;
        }
        updateWater();
        updateVerts();
        updateMatrix();

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        int glErr = GLES20.glGetError();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        glErr = GLES20.glGetError();

        GLES20.glEnable(GLES20.GL_BLEND);
        glErr = GLES20.glGetError();
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ZERO);
        glErr = GLES20.glGetError();
        drawWater();
        glErr = GLES20.glGetError();
        int a = 0;
        GLES20.glBlendFuncSeparate(GLES20.GL_DST_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE);
        drawText();

        GLES20.glBlendFunc(GLES20.GL_CONSTANT_COLOR, GLES20.GL_ONE);
        GLES20.glBlendColor(COLOR_TEXT[0],COLOR_TEXT[1],COLOR_TEXT[2],COLOR_TEXT[3]);
        drawText();

    }

    public void onSurfaceChanged(GL10 glUnused, int w, int h){
        screenWidth = w;
        screenHeight = h;
        scaleFactor = Math.max(screenHeight / (float) screenWidth, screenWidth / (float) screenHeight)*1.5f;

        initComplete = true;
        fpsCounterPrevTime = System.nanoTime();
        columns = new WaterColumn[COLUMNS_COUNT];
        for (int i = 0; i < columns.length; i++){
            columns[i] = new WaterColumn(waterPercent, waterPercent, 0);
        }

        int size = columns.length;
        vertsData = new float[(size)*6*Vertex.BLOCK_SIZE];
        matrix = new float[16];

        verticesBuffer = ByteBuffer.allocateDirect(vertsData.length * Vertex.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        loadWaterShader();
        loadTextShader();

        GLES20.glViewport(0, 0, screenWidth, screenHeight);
        initText();
        updateText();

        if (onInit != null) {
            onInit.run();
        }
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config){

    }


    private void initText(){
        float width = screenWidth;
        float height = screenHeight;
        float textureSize = Utils.getClosestPOT(width);
        textureSize = Math.max(textureSize, Utils.getClosestPOT(height));
        textBmp = Bitmap.createBitmap((int) textureSize, (int) textureSize, Bitmap.Config.ARGB_4444);

        float[] textVertsData = new float[6*Vertex.BLOCK_SIZE];
        Vertex.writeToArray(0, textVertsData, -1.0f,  1.0f, 0.0f, .0f, .0f, .0f, 0.0f, 0.0f, 0.0f);
        Vertex.writeToArray(1, textVertsData,  1.0f,  1.0f, 0.0f, .0f, .0f, .0f, 0.0f, width/textureSize, 0.0f);
        Vertex.writeToArray(2, textVertsData, -1.0f, -1.0f, 0.0f, .0f, .0f, .0f, 0.0f, 0.0f, height / textureSize);

        Vertex.writeToArray(3, textVertsData,  1.0f,  1.0f, 0.0f, .0f, .0f, .0f, 0.0f, width/textureSize, 0.0f);
        Vertex.writeToArray(4, textVertsData, -1.0f, -1.0f, 0.0f, .0f, .0f, .0f, 0.0f, 0.0f, height / textureSize);
        Vertex.writeToArray(5, textVertsData,  1.0f, -1.0f, 0.0f, .0f, .0f, .0f, 0.0f, width/textureSize, height/textureSize);

        textBuffer = ByteBuffer.allocateDirect(textVertsData.length * Vertex.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        textBuffer.put(textVertsData);

        textCanvas = new Canvas(textBmp);
        font = Typeface.createFromAsset(cnx.getAssets(), FONT_NAME);
        fontTooltip = Typeface.createFromAsset(cnx.getAssets(), FONT_TOOLTIP_NAME);
        paintTootipText = new Paint();
        paintTootipText.setTextSize(height / 30);
        paintTootipText.setAntiAlias(true);
        paintTootipText.setColor(Color.argb(255, 255, 255, 255));
        paintTootipText.setStyle(Paint.Style.FILL);
        paintTootipText.setTypeface(fontTooltip);
        textPaintPercent = new Paint();
        textPaintPercent.setTextSize((int)(height/3.5));
        textPaintPercent.setAntiAlias(true);
        textPaintPercent.setColor(Color.argb(255, 255, 255, 255));
        textPaintPercent.setStyle(Paint.Style.FILL);
        textPaintPercent.setTypeface(font);
        textureId = new int[1];

        GLES20.glGenTextures(1, textureId, 0);
    }

    private void updateText() {
        text = Integer.toString(Math.round(textPercent * 100))+"%";
        textBmp.eraseColor(Color.argb(0, 0, 0, 0));
        Rect bounds = new Rect();
        textPaintPercent.getTextBounds(text, 0, text.length(), bounds);
        int x1 = (screenWidth - bounds.width())/2;
        Rect boundsTT = new Rect();
        paintTootipText.getTextBounds(tooltipText, 0, tooltipText.length(), boundsTT);
        int x2 = (screenWidth - boundsTT.width())/2;
        int sum = bounds.height() + (int)(boundsTT.height()*3);

        int y1 = (screenHeight/2 - sum/2 + bounds.height());
        int y2 = (screenHeight/2 + sum/2);

        textCanvas.drawText(text, x1, y1, textPaintPercent);
        textCanvas.drawText(tooltipText, x2, y2, paintTootipText);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textBmp, 0);
    }

    private void loadTextShader(){
        String vertex = "attribute vec4 a_position;\n" +
                "attribute vec2 a_texcoord;\n" +
                "varying vec2 v_texcoord;\n" +
                "void main() {\n" +
                "    v_texcoord = a_texcoord;\n" +
                "    gl_Position = a_position;\n" +
                "}";
        String fragment = "uniform sampler2D u_texture1;\n" +
                "varying lowp vec2 v_texcoord;\n" +
                "precision lowp float;\n" +
                "void main() {\n" +
                "    gl_FragColor = texture2D(u_texture1, v_texcoord);\n" +
                "}";
        textShader = new Shader(fragment, vertex, "a_position a_texcoord","");
    }

    private void loadWaterShader(){
        String vertex = "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "uniform mat4 u_mvp;\n" +
                "varying vec4 v_color;\n" +
                "void main() {\n" +
                "    v_color = a_color;\n" +
                "    gl_Position = u_mvp * a_position;\n" +
                "}";
        String fragment = "varying lowp vec4 v_color;\n" +
                "precision lowp float;\n" +
                "void main() {\n" +
                "   gl_FragColor = v_color;\n" +
                "}";
        waterShader = new Shader(fragment, vertex, "a_position a_color","u_mvp");
    }

}