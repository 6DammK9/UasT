package hk.ust.comp4521.UasT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Darren on 22/5/2015.
 */

/**
 * I almost forgot this. Zooming function is desired.
 */
public class Multimedia_showIMG extends BaseFragment {
    String path;
    View view;
    ImageView img;
    final static String TAG = "UasT.showIMG";

    public void setParam(String path) {
        this.path = path;
    }

    @Override
    public String getTitle() {
        return "ShowIMG";
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.multimedia_showimg, null);

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
}
