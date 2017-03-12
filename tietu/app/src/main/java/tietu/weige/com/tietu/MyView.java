package tietu.weige.com.tietu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by RWF on 2017/3/8.
 */

public class MyView extends View {
    Paint paint;
    Bitmap bitmap;
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initBitmap();
    }

    public void initBitmap(){
        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.phone);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(15);
        canvas.drawBitmap(bitmap,100,20,paint);
        canvas.save();

        Matrix m1 = new Matrix();
        m1.setTranslate(1200,10);
        Matrix m2 = new Matrix();
        m2.setRotate(15);
        Matrix m3 = new Matrix();
        m3.setConcat(m1,m2);
        m2.setScale(0.8f,0.8f);
        m2.setConcat(m3,m2);
        canvas.drawBitmap(bitmap,m2,paint);
        canvas.restore();
        canvas.save();

        paint.setAlpha(180);
        m1.setTranslate(500,450);
        m2.setScale(1.3f,1.3f);
        m3.setConcat(m1,m2);
        canvas.drawBitmap(bitmap,m3,paint);
        canvas.restore();
        canvas.save();
    }
}
