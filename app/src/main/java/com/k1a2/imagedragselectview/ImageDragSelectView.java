package com.k1a2.imagedragselectview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ImageDragSelectView extends View {

    public List<Position[]> points;//패스 저장
    private Bitmap scaledBitmap = null, sourceBitmap = null;
    private float xSize, ySize, widthBitmap = 0, heightBitmap = 0;
    private Paint paint = new Paint();
    private Path path  = new Path();
    private boolean isDraging = false;
    private List<Position> pointList = new ArrayList<>();//드래그 중인 패스 임시저장

    public ImageDragSelectView(Context context) {
        super(context);
        init();
    }

    public ImageDragSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttrs(attrs);
    }

    public ImageDragSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttrs(attrs, defStyleAttr);
    }

    //그리기
    @Override
    protected void onDraw(Canvas canvas) {
        if (scaledBitmap != null) {
            final float bitmapX = scaledBitmap.getWidth();
            final float bitmapY = scaledBitmap.getHeight();
            int positionX = 0, positionY = 0;

            //bitmap move to canter
            if (bitmapX < xSize) {
                positionX = (int)(xSize / 2 - bitmapX / 2);
            }
            if (bitmapY < ySize) {
                positionY = (int)(ySize / 2 - bitmapY / 2);
            }
            Paint paint1 = new Paint();
            ColorFilter colorFilter = new PorterDuffColorFilter(Color.argb(50,0, 0, 0), PorterDuff.Mode.DARKEN);
            paint1.setColorFilter(colorFilter);
            canvas.drawBitmap(scaledBitmap, positionX, positionY, paint1);
        }

        canvas.drawPath(path, paint);//저장된 path 그림
        super.onDraw(canvas);
    }

    //뷰 크기 측정
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        int viewHeight = height;
        int viewWidth = width;

        if (widthBitmap == 0||heightBitmap == 0) {
            if (sourceBitmap != null) {
                widthBitmap = sourceBitmap.getWidth();
                heightBitmap = sourceBitmap.getHeight();

                //bitmap scaled
                if(heightBitmap > viewHeight)  {
                    float per = (float)(widthBitmap / heightBitmap);
                    heightBitmap = height;
                    widthBitmap = heightBitmap * per;
                }

                if (widthBitmap > viewWidth) {
                    float per = (float)(heightBitmap / widthBitmap);
                    widthBitmap = width;
                    heightBitmap = widthBitmap * per;
                }
                scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, (int) widthBitmap, (int) heightBitmap, true);
            }
            xSize = width;
            ySize = height;
        } else {

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDraging) {
            pointList = new ArrayList<>();
        }
        float x = event.getX();
        float y = event.getY();

        Position point = new Position();
        point.x = x;
        point.y = y;

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                isDraging = true;
                path.moveTo(x, y);
                pointList.add(point);
                break;
            case MotionEvent.ACTION_MOVE :
                path.lineTo(x, y);
                pointList.add(point);
                break;
            case MotionEvent.ACTION_UP :
                isDraging = false;
                Position[] poinyArray = new Position[pointList.size()];

                for (int count = 0;count < pointList.size();count++) {
                    poinyArray[count] = pointList.get(count);
                }
                points.add(poinyArray);
                break;
        }
        invalidate();

        return true;
    }

    //초기화
    private void init() {
        this.setClickable(true);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setColor(Color.argb(180, 255, 255, 255));
        paint.setStrokeWidth(10f); // 선의 굵기 지정
        points = new ArrayList<Position[]>();
    }

    //attribute
    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageDragSelectView);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageDragSelectView, defStyle, 0);
        setTypeArray(typedArray);

    }

    private void setTypeArray(TypedArray typedArray) {

        int lineColor = typedArray.getColor(R.styleable.ImageDragSelectView_lineColor, 0);
        paint.setColor(lineColor);

        float linwWidth = typedArray.getFloat(R.styleable.ImageDragSelectView_lineWidth, 0);
        paint.setStrokeWidth(linwWidth);

        typedArray.recycle();

    }

    //선택된 영역이 몇개인지 카운트
    public int getSectionCount() {
        return points.size();
    }

    //선택된 영역중 하나만 가져옴
    public Bitmap getCropBitmap(int position) {
        return cropBitmap(position);
    }

    //선택 영역 잘라서 비트맵으로 리턴
    public Bitmap[] getCropBitmaps() {
        Bitmap[] results = new Bitmap[points.size()];

        for (int i = 0; i < points.size(); i++) {
            results[i] = cropBitmap(i);
        }

        return results;
    }

    //라인 색상
    public void setLineColor(int color) {
        paint.setColor(color);
    }

    //라인 두께
    public void setLineWidth(float lineWidth) {
        paint.setStrokeWidth(lineWidth);
    }

    //이미지 경로 일고 비트맵 변환
    public void setSourceImage(String path) {
        sourceBitmap = BitmapFactory.decodeFile(path);
        resizeBitmap();
        invalidate();
    }

    //비트맵
    public void setSourceImage(Bitmap bitmap) {
        sourceBitmap = bitmap;
        resizeBitmap();
        invalidate();
    }

    //비트맥 화면에 맞춰 리사이징
    private void resizeBitmap() {
        widthBitmap = sourceBitmap.getWidth();
        heightBitmap = sourceBitmap.getHeight();

        //bitmap scaled
        if(heightBitmap > ySize)  {
            float per = (float)(widthBitmap / heightBitmap);
            heightBitmap = ySize;
            widthBitmap = heightBitmap * per;
        }

        if (widthBitmap > xSize) {
            float per = (float)(heightBitmap / widthBitmap);
            widthBitmap = xSize;
            heightBitmap = widthBitmap * per;
        }

        scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, (int) widthBitmap, (int) heightBitmap, true);
    }

    //이미지 크롭
    private Bitmap cropBitmap(int position) {
        Bitmap originImage = scaledBitmap;

        Bitmap resultingImage = Bitmap.createBitmap((int)xSize, (int)ySize, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(resultingImage);

        final float bitmapX = scaledBitmap.getWidth();
        final float bitmapY = scaledBitmap.getHeight();
        int positionX = 0, positionY = 0;

        //bitmap move to canter
        if (bitmapX < xSize) {
            positionX = (int)(xSize / 2 - bitmapX / 2);
        }
        if (bitmapY < ySize) {
            positionY = (int)(ySize / 2 - bitmapY / 2);
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0XFF000000);
        Path path = new Path();

        Position[] poinyArray = points.get(position);
        for (int count = 0;count < poinyArray.length;count++) {
            Position p = poinyArray[count];
            if (count == 0) {
                path.moveTo(p.x, p.y);
            } else {
                path.lineTo(p.x, p.y);
            }
        }

        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(originImage, positionX, positionY, paint);

        return resultingImage;
    }
}

class Position {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
