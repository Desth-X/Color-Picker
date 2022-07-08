package tools.color_picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorPicker extends View implements View.OnTouchListener {

    String TAG = "APPMSG";
    int intWidth;
    int intSpacing;
    int intBarWidth;
    int intSquareBorderSize;
    int intHeight;
    Canvas canvas;
    Paint paint;
    Rect barRect;
    int selectedColor;

    public ColorPicker(Context context) {
        super(context);
        selectedColor = Color.RED;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        paint = new Paint();
        intWidth = getWidth();
        intSpacing = 20;
        intBarWidth = 70;
        intSquareBorderSize = intWidth - 3 * intSpacing - intBarWidth;
        Log.d(TAG, "onDraw: " + intSquareBorderSize);
        intHeight = 2 * intSpacing + intSquareBorderSize;
        createArea();
        createBar();
        setOnTouchListener(this);
    }

    private void createArea(){
        Bitmap bitmap = Bitmap.createBitmap(intSquareBorderSize, intSquareBorderSize, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < intSquareBorderSize; i++) {
            for( int j = 0; j < intSquareBorderSize; j++){
                bitmap.setPixel(j, i, getAreaColorAt(j, i));
            }
        }
        Rect rect = new Rect(0,0,intSquareBorderSize, intSquareBorderSize);
        Rect rectPos = new Rect(intSpacing,intSpacing, intSpacing + intSquareBorderSize, intSpacing + intSquareBorderSize);
        canvas.drawBitmap(bitmap, rect, rectPos, paint);
        Log.d(TAG, "createArea: AREA CREATED");
    }

    private void createBar(){
        int lMargin = 2 * intSpacing + intSquareBorderSize;
        barRect = new Rect(lMargin, intSpacing, lMargin + intBarWidth, intSpacing + intSquareBorderSize);
        Bitmap bitmap = Bitmap.createBitmap(intBarWidth, intSquareBorderSize, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < intSquareBorderSize; i++) {
            int index = (int) Math.round((255*6)*i/(double)intSquareBorderSize);
            for (int j = 0; j < intBarWidth; j++) {
                bitmap.setPixel(j, i, getColorAt(index));
            }

            //paint.setColor(getColorAt(index));
            //canvas.drawRect(barRect.left, barRect.top + i, barRect.right, barRect.top + i+1, paint);
        }
        canvas.drawBitmap(bitmap, new Rect(0,0,intBarWidth,intSquareBorderSize), barRect, paint);
    }

    private int getColorAt(int index){
        if(index <= 255){
            return Color.rgb(255, 0, index);
        } else if (index <= 510){
            return Color.rgb(255 - (index - 255), 0, 255);
        } else if(index <= 765){
            return Color.rgb(0, index  - 510, 255);
        } else if(index <= 1020){
            return Color.rgb(0, 255, 255 - (index - 1020));
        } else if(index <= 1275){
            return Color.rgb(index - 1020, 255, 0);
        } else if(index <= 1530){
            return Color.rgb(255, 255 - (index - 1275), 0);
        }
        return Color.rgb(0,0,0);
    }

    private int getAreaColorAt(int x, int y){
        double yVal = Math.round((255*y/(double)intSquareBorderSize));
        double fact = (255.0 - yVal) / 255.0;
        int dR = 255 - Color.red(selectedColor);
        int dG = 255 - Color.green(selectedColor);
        int dB = 255 - Color.blue(selectedColor);
        int r = 255 - (dR * x / intSquareBorderSize);
        int g = 255 - (dG * x / intSquareBorderSize);
        int b = 255 - (dB * x / intSquareBorderSize);
        r = (int) Math.round(r * fact);
        g = (int) Math.round(g * fact);
        b = (int) Math.round(b * fact);
        return Color.rgb(r,g,b);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if(x >= barRect.left && x <= barRect.right && y >= barRect.top && y <= barRect.bottom){
            int i = (int) y - barRect.top;
            int index = (int) Math.round((255*6)*i/(double)intSquareBorderSize);
            int color = getColorAt(index);
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            Log.d(TAG, "onTouch: " + r + " "+ g + " " + b );
            selectedColor = color;
            invalidate();
        }
        return false;
    }
}
