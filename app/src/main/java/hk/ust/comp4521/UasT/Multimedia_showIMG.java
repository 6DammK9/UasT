package hk.ust.comp4521.UasT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Darren on 22/5/2015.
 * Main purpose: To show the selected image.
 */

/**
 * I almost forgot this. Zooming function is desired.
 */
public class Multimedia_showIMG extends BaseFragment {
    final static String TAG = "UasT.showIMG";
    String path;
    View view;
    ImageView img;
    private PointF startPoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix currentMaritx = new Matrix();

    private int mode = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private float startDis = 0;
    private PointF midPoint;

    public void setParam(String path) {
        this.path = path;
    }

    @Override
    public String getTitle() {
        return "ShowIMG";
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.multimedia_showimg, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        mode = DRAG;
                        currentMaritx.set(img.getImageMatrix());
                        startPoint.set(motionEvent.getX(),motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (mode == DRAG) {
                            float dx = motionEvent.getX() - startPoint.x;
                            float dy = motionEvent.getY() - startPoint.y;
                            matrix.set(currentMaritx);
                            matrix.postTranslate(dx, dy);

                        } else if(mode == ZOOM){
                            float endDis = distance(motionEvent);
                            if(endDis > 10f){
                                float scale = endDis / startDis;
                                matrix.set(currentMaritx);
                                matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = 0;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = 0;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = ZOOM;
                        startDis = distance(motionEvent);

                        if(startDis > 10f){
                            midPoint = mid(motionEvent);
                            currentMaritx.set(img.getImageMatrix());
                        }

                        break;
                }
                img.setImageMatrix(matrix);
                return true;
            }
        });

        img = (ImageView) view.findViewById(R.id.img);

        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size); //int width = size.x; int height = size.y;

        Bitmap thumbnail = Multimedia_image.decodeSampledBitmapFromUri(path, size.x, size.y);
        if (thumbnail != null) {
            img.setImageBitmap(thumbnail);
        } else {
            Log.i(TAG, "FAIL WHEN LOADING");
        }

        return view;
    }

    private static float distance(MotionEvent event){
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return FloatMath.sqrt(dx*dx + dy*dy);
    }

    private static PointF mid(MotionEvent event){
        float midx = event.getX(1) + event.getX(0);
        float midy = event.getY(1) + event.getY(0);

        return new PointF(midx/2, midy/2);
    }
}
