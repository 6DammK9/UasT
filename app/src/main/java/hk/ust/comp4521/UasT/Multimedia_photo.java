package hk.ust.comp4521.UasT;

/**
 * Created by Darren on 13/5/2015.
 * Main purpose: Photo, Store, Rename, Send.
 */
/**
 * Android tutorial rocks.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Multimedia_photo extends BaseFragment {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static Uri fileUri;
    private static String outFileUri;
    static final String TAG = "UasT.Multimedia_photo";
    View view;
    ChatFragment chatFragment;

    public void setParam(ChatFragment c){
        chatFragment = c;
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "USTasUST");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            outFileUri = mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg";
            mediaFile = new File(outFileUri);
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }



        return mediaFile;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.grid_view, container, false);

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        //Log.i(TAG, fileUri.toString());

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                if (data == null) {
                    // The picture was taken but not returned
                    if (chatFragment == null)
                    Toast.makeText(view.getContext(),
                            "The picture was taken and is located here: " + fileUri.toString(),
                           Toast.LENGTH_LONG).show();
                    else
                        chatFragment.UploadIMG(outFileUri);
                } else {
                    // The picture was returned
                    //Bundle extras = data.getExtras();
                    //ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
                    //imageView1.setImageBitmap((Bitmap) extras.get("data"));
                    Toast.makeText(view.getContext(), "Image saved to:\n" +
                            data.getData(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(view.getContext(), "Activity cancelled.", Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(view.getContext(), "ERROR: IMAGE CAPTURE FAILED: " + Integer.toString(resultCode), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(view.getContext(), "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
                Toast.makeText(view.getContext(), "Activity cancelled.", Toast.LENGTH_LONG).show();
            } else {
                // Video capture failed, advise user
                Toast.makeText(view.getContext(), "ERROR: VIDEO CAPTURE FAILED: " + Integer.toString(resultCode), Toast.LENGTH_LONG).show();
            }
        }

        ((MainActivity) getActivity()).popFragment();
    }

    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public String getTitle() {
        return "Taking Photo";
    }
}
