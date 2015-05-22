package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.ChatItem;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.ThreadItem;
import hk.ust.comp4521.UasT.json.ApiResponseBase;

public class ChatCardView extends ThreadCardView {
    private static final String TAG = "UasT.ChatCardView";

    public ChatCardView(Context context) {
        super(context);
    }

    public ChatCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ViewHolder createViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void initViews() {
        super.initViews();
        final ViewHolder vh = (ViewHolder) getTag();
        vh.bg = findViewById(R.id.bg);
        vh.match_img = findViewById(R.id.match_img);
        vh.view_img = (ImageButton) findViewById(R.id.view_img);

        vh.view_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //CHECK IF IT IS DOWNLOADED
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "USTasUST");
                File test = new File(mediaStorageDir.getPath() + File.separator + vh.chat.getIMGName());

                if (test.exists()) {
                    //Toast.makeText(ChatCardView.this.getContext(), "Downloaded.", Toast.LENGTH_SHORT).show();
                    MainActivity main = (MainActivity) ChatCardView.this.getContext();
                    Multimedia_showIMG img = new Multimedia_showIMG();
                    img.setParam(test.getAbsolutePath());

                    main.gotoFragment(2, img);
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatCardView.this.getContext());
                    ProgressBar bar = new ProgressBar(ChatCardView.this.getContext(), null,
                            android.R.attr.progressBarStyleHorizontal);
                    bar.setIndeterminate(true);
                    builder.setCancelable(true).setTitle("Sending request to server").setView(bar);
                    final AlertDialog dialog = builder.show();

                    ApiManager.downIMG(vh.chat.getIMGId(), vh.chat.getAuthorId(), new ApiHandler<ApiResponseBase>() {

                        @Override
                        public void onSuccess(ApiResponseBase response) {
                            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES), "USTasUST");
                            File temp_img = new File(mediaStorageDir.getPath() + File.separator + "TEMP.jpg");
                            File target_img = new File(mediaStorageDir.getPath() + File.separator + response.getMessage());
                            try {
                                CopyFile(temp_img, target_img);
                            } catch (IOException ioe) {
                                Log.i(TAG, ioe.toString());
                            } finally {
                                dialog.dismiss();
                                //Toast.makeText(ChatCardView.this.getContext(), response.getMessage(),
                                //        Toast.LENGTH_LONG).show();
                                Bitmap thumbnail = Multimedia_image.getThumbnail(response.getMessage());
                                if (thumbnail != null) {
                                    vh.view_img.setImageBitmap(thumbnail);
                                } else {
                                    Log.i(TAG, "FAIL AFTER DOWNLOADING");
                                }
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            dialog.dismiss();
                            Toast.makeText(ChatCardView.this.getContext(), message,
                                    Toast.LENGTH_LONG).show();
                        }

                    });

                }
            }
        });

    }

    @Override
    public void setThread(ThreadItem thread) {
        ChatItem chat = (ChatItem) thread;
        ViewHolder vh = (ViewHolder) getTag();
        boolean isMine = chat.getAuthorId()
                .equals(Database.getUser().getITSC());
        Drawable bg = getResources().getDrawable(
                isMine ? R.drawable.chat_isuser : R.drawable.chat_notuser);
        vh.bg.setBackground(bg);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vh.bg
                .getLayoutParams();
        params.gravity = isMine ? Gravity.RIGHT : Gravity.LEFT;
        vh.bg.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) vh.title
                .getLayoutParams();
        params2.setMargins(isMine ? 0 : 10, 8, isMine ? 10 : 0, 0);
        params2.gravity = isMine ? Gravity.RIGHT : Gravity.LEFT;
        vh.title.setLayoutParams(params2);

        boolean hasIMG = (chat.getIMGId() != null);
        vh.match_img.setVisibility(hasIMG ? View.VISIBLE : View.GONE);
        if (hasIMG) {
            if (chat.getIMGName() != null) {

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "USTasUST");
                File test = new File(mediaStorageDir.getPath() + File.separator + chat.getIMGName());

                if (test.exists()) {
                    Bitmap thumbnail = Multimedia_image.getThumbnail(chat.getIMGName());
                    if (thumbnail != null) {
                        vh.view_img.setImageBitmap(thumbnail);
                    } else {
                        Log.i(TAG, "FAIL WHEN LOADING INITIALLY");
                        Log.i(TAG, chat.getIMGName());
                    }
                }
            }
        }

        vh.chat = chat;

        super.setThread(thread);
    }

    protected static class ViewHolder extends ThreadCardView.ViewHolder {
        public View bg, match_img;
        public ImageButton view_img;
        public ChatItem chat;
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
