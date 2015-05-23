/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT;

/**
 * Created by Darren on 14/5/2015.
 * To choose image through GUI and process with the image chosen.
 */
/**
 *  Tutorial rocks.
 *  Potentially more file directories are needed to included or investigated.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class Multimedia_image extends BaseFragment {

    private View view;
    GridView gridview;
    ImageAdapter myImageAdapter0, myImageAdapter1, myImageAdapter2, myImageAdapter3;
    Spinner FolderSpin;
    private static final String TAG = "UasT.Multimedia_image";
    IMGUpload chatFragment;
    final static int DEFAULT_IMAGE_SIZE = 240;
    final static int DEFAULT_IMAGE_PADDING = 8;
    final static int MAX_PICTURE_SHOW = 10;

    @Override
    public String getTitle() {
        return "Select Image";
    }

    public void setParam(IMGUpload c){
        chatFragment = c;
    }

    public class ImageAdapter extends BaseAdapter {

        private final Context mContext;

        final ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(
                        DEFAULT_IMAGE_SIZE + 2 * DEFAULT_IMAGE_PADDING,
                        DEFAULT_IMAGE_SIZE + 2 * DEFAULT_IMAGE_PADDING));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(DEFAULT_IMAGE_PADDING, DEFAULT_IMAGE_PADDING,
                        DEFAULT_IMAGE_PADDING, DEFAULT_IMAGE_PADDING);
            } else {
                imageView = (ImageView) convertView;
            }

            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position),
                    DEFAULT_IMAGE_SIZE, DEFAULT_IMAGE_SIZE);

            imageView.setImageBitmap(bm);
            return imageView;
        }

        public void load(String targetPath) {
            File dir = new File(targetPath);
            String extention = ".jpg";
            File[] listFile = dir.listFiles();
            if (listFile != null) {
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].isDirectory()) {
                        load(listFile[i].getAbsolutePath());
                    } else {
                        if (getCount() > MAX_PICTURE_SHOW) return;
                        if (listFile[i].getName().endsWith(extention)) {
                            add(listFile[i].getAbsolutePath());
                        }
                    }
                }
            }
        }
        /*    File targetDirector = new File(targetPath);
            File[] files = targetDirector.listFiles();
            if (files == null) {
                Log.i(TAG, "Fail to load folder: " + targetPath);
            } else {
                for (File file : files) {
                    add(file.getAbsolutePath());
                }
            }
        }*/
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        view = inflater.inflate(R.layout.multimedia_image, container, false);
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.grid_view);

        FolderSpin = (Spinner) view.findViewById(R.id.folderSpin);
        gridview = (GridView) view.findViewById(R.id.mygridview);

        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size); //int width = size.x; int height = size.y;

        gridview.setNumColumns(size.x/ DEFAULT_IMAGE_SIZE);


        File mediaStorageDir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        final String targetPath1 = mediaStorageDir1.getPath();

        File mediaStorageDir2 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "USTasUST");
        final String targetPath2 = mediaStorageDir2.getPath();

        File mediaStorageDir3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        final String targetPath3 = mediaStorageDir3.getPath();

        if (! mediaStorageDir2.exists()){
            if (! mediaStorageDir2.mkdirs()){
                Log.d(TAG, "failed to create directory");
            }
        }

        //Toast.makeText(view.getContext(), targetPath2, Toast.LENGTH_LONG).show();
        myImageAdapter0 = new ImageAdapter(view.getContext()); //Empty
        myImageAdapter1 = new ImageAdapter(view.getContext());
        myImageAdapter1.load(targetPath1);
        myImageAdapter2 = new ImageAdapter(view.getContext());
        myImageAdapter2.load(targetPath2);
        myImageAdapter3 = new ImageAdapter(view.getContext());
        myImageAdapter3.load(targetPath3);

        FolderSpin.setSelection(0);
        gridview.setAdapter(myImageAdapter0);

        FolderSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: gridview.setAdapter(myImageAdapter0); break;
                    case 1: gridview.setAdapter(myImageAdapter1); break;
                    case 2: gridview.setAdapter(myImageAdapter2); break;
                    case 3: gridview.setAdapter(myImageAdapter3); break;
                    default: gridview.setAdapter(myImageAdapter0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gridview.setAdapter(myImageAdapter0);
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (chatFragment != null)
                    if (gridview.getAdapter() == myImageAdapter1) {
                        //COPY FILE BEFORE UPLOADING
                        File temp_img = new File(myImageAdapter1.itemList.get(i));
                        File target_img = new File(targetPath2 + File.separator + "COPY.jpg");
                        try {
                            CopyFile(temp_img, target_img);
                            //UploadIMG
                            chatFragment.UploadIMG(target_img.getPath());
                        } catch (IOException ioe) {
                            Log.i(TAG, ioe.toString());
                        }
                    }
                    else if (gridview.getAdapter() == myImageAdapter2)
                        chatFragment.UploadIMG(myImageAdapter2.itemList.get(i));
                else if (gridview.getAdapter() == myImageAdapter3) {
                    //COPY FILE BEFORE UPLOADING
                    File temp_img = new File(myImageAdapter3.itemList.get(i));
                    File target_img = new File(targetPath2 + File.separator + "COPY.jpg");
                    try {
                        CopyFile(temp_img, target_img);
                        //UploadIMG
                        chatFragment.UploadIMG(target_img.getPath());
                    } catch (IOException ioe) {
                        Log.i(TAG, ioe.toString());
                    }
                }
                    else if (gridview.getAdapter() != myImageAdapter0)
                        Log.i(TAG, "Invalid Adapter; Exit directly");

                ((MainActivity) getActivity()).popFragment();
            }
        });

        return view;
    }

    public static Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    public static Bitmap getThumbnail(String fileName) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "USTasUST");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        File test = new File(mediaStorageDir.getPath() + File.separator + fileName);
        if (test.exists())
            return decodeSampledBitmapFromUri(mediaStorageDir.getPath() + File.separator + fileName, 192, 144);
        else
            return null;
    }

    private void CopyFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        try {
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            inStream.close();
            outStream.close();
        }
    }
}
