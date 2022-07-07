package tools.color_picker;

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

    public ColorPicker(Context context) {
        super(context);
    }

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
            double yFact = (255/(double) intSquareBorderSize);
            double sR = 255;
            double sG = 127;
            double sB = 0  ;
            double r = 255  ;
            double g = 255 ;
            double b = 255;
            double dR = r - sR;
            double dG = g - sG;
            double dB = b - sB;
            double vR = dR / intSquareBorderSize;
            double vG = dG / intSquareBorderSize;
            double vB = dB / intSquareBorderSize;
            for( int j = 0; j < intSquareBorderSize; j++){
                r = dR * (r-vR*j)/ (double) intSquareBorderSize; //255 - (int) Math.round(255.0  *  j / intSquareBorderSize);
                g = dG * (g-vG*j)/ (double) intSquareBorderSize;
                b = dB * (b-vB*j)/ (double) intSquareBorderSize;
                bitmap.setPixel(j, i, Color.rgb((int) r, (int) g, (int)b));
            }
        }
        Rect rect = new Rect(0,0,intSquareBorderSize, intSquareBorderSize);
        Rect rectPos = new Rect(intSpacing,intSpacing, intSpacing + intSquareBorderSize, intSpacing + intSquareBorderSize);
        canvas.drawBitmap(bitmap, rect, rectPos, paint);

    }

    private void createBar(){
        double r = 255;
        double g = 0;
        double b = 0;
        boolean rMaxed = true;
        boolean gMaxed = false;
        boolean bMaxed = false;
        int lMargin = 2 * intSpacing + intSquareBorderSize;
        barRect = new Rect(lMargin, intSpacing, lMargin + 70, intSpacing + intSquareBorderSize);
        for (int i = intSquareBorderSize; i > 0; i--) {
            int intColorCount = 6 * 255;
            double val = intColorCount / (double) intSquareBorderSize;
            Log.d(TAG, "createBar: " + val);
            if(rMaxed && !gMaxed && !bMaxed){
                g += val;
                if(g >= 255){
                    g = 255;
                    gMaxed = true;
                }
            } else if (rMaxed && gMaxed){
                r -= val;
                if(r <= 0){
                    r = 0;
                    rMaxed = false;
                }
            } else if(gMaxed && !bMaxed){
                b += val;
                if(b >= 255){
                    b = 255;
                    bMaxed = true;
                }
            } else if(gMaxed && bMaxed){
                g -= val;
                if(g <= 0){
                    g = 0;
                    gMaxed = false;
                }
            } else if(bMaxed && !rMaxed){
                r += val;
                if(r >= 255){
                    r = 255;
                    rMaxed = true;
                }
            } else if(bMaxed && rMaxed){
                b -= val;
                if(b <= 0){
                    b = 0;
                    bMaxed = false;
                }
            }
            //Log.d("APPMSG:", r + " " + g + " " + b);
            paint.setColor(Color.rgb((int) r,(int) g, (int)b));
            canvas.drawRect(barRect.left, barRect.top + i, barRect.right, barRect.top + i+1, paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if(x >= barRect.left && x <= barRect.right && y >= barRect.top && y <= barRect.bottom){
            Log.d(TAG, "onTouch: here we go");
        }
        return false;
    }
}
