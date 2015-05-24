/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.DatabaseLoad;
import hk.ust.comp4521.UasT.data.Post;
import hk.ust.comp4521.UasT.data.ThreadItemInfo;
import hk.ust.comp4521.UasT.data.ThreadPostItem;
import hk.ust.comp4521.UasT.json.ApiResponseAddChat;
import hk.ust.comp4521.UasT.json.ApiResponseBase;

public class ViewPostFragment extends BaseFragment {
	private final static String TAG = "UasT.ViewPost";
	private TextView content;
	private ImageView view_img;
	private Post postData;
	private View view;

	public ViewPostFragment() {

	}

	ThreadItemInfo info;
	private ThreadPostItem post;

	public void setCode(ThreadPostItem post) {
		this.info = post.getClass().getAnnotation(ThreadItemInfo.class);
		this.post = post;
	}

	@Override
	public String getTitle() {
		return post.getTitle();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_view_post, container, false);
		content = (TextView) view.findViewById(R.id.content);
        view_img = (ImageView) view.findViewById(R.id.view_img);

		Database.getDataSingle("posts", post.getKey(), Post.class,
				new DatabaseLoad<Post>() {
					@Override
					public void load(Post data) {
						postData = data;
						updateView();
					}
				});
		return view;
	}

	private void updateView() {
		if (postData != null) {
            content.setText(postData.getContent());
            if (!postData.getAttachment().equals("")) {
                view_img.setVisibility(View.VISIBLE);
                //CHECK IF IT IS DOWNLOADED
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "USTasUST");
                File test = new File(mediaStorageDir.getPath() + File.separator + postData.getAttachment());
                if (test.exists()) {
                    Bitmap thumbnail = Multimedia_image.getThumbnail(postData.getAttachment());
                    if (thumbnail != null) {
                        view_img.setImageBitmap(thumbnail);
                    } else {
                        Log.i(TAG, "FAIL AFTER DOWNLOADING");
                    }
                }

                view_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //CHECK IF IT IS DOWNLOADED
                        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "USTasUST");
                        File test = new File(mediaStorageDir.getPath() + File.separator + postData.getAttachment());

                        if (test.exists()) {

                            Bitmap thumbnail = Multimedia_image.getThumbnail(postData.getAttachment());
                            if (thumbnail != null) {
                                MainActivity main = (MainActivity) view.getContext();
                                Multimedia_showIMG img = new Multimedia_showIMG();
                                img.setParam(test.getAbsolutePath());

                                main.gotoFragment(0, img);
                            } else {
                                Log.i(TAG, "FAIL AFTER DOWNLOADING");
                            }
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            ProgressBar bar = new ProgressBar(view.getContext(), null,
                                    android.R.attr.progressBarStyleHorizontal);
                            bar.setIndeterminate(true);
                            builder.setCancelable(true).setTitle("Sending request to server").setView(bar);
                            final AlertDialog dialog = builder.show();

                            ApiManager.downIMG(postData.getAttachment(), post.getAuthorId(), new ApiHandler<ApiResponseBase>() {

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
                                            view_img.setImageBitmap(thumbnail);
                                        } else {
                                            Log.i(TAG, "FAIL AFTER DOWNLOADING");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(String message) {
                                    dialog.dismiss();
                                    Toast.makeText(view.getContext(), message,
                                            Toast.LENGTH_LONG).show();
                                }

                            });

                        }
                    }
                });
            } else {
                view_img.setVisibility(View.GONE);
            }
        }
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.view_post, menu);
		if (post.getAuthorId().equals(Database.getUser().getITSC())) {
			inflater.inflate(R.menu.view_post_del, menu);
		} else {
			inflater.inflate(R.menu.view_post_contact, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		ThreadListFragment f = (ThreadListFragment) ((MainActivity) getActivity())
				.getParentFragment();
		String key = f.getKey();

		switch (item.getItemId()) {
		case R.id.del: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.delPost(info.type(), key, post.getKey(),
					new ApiHandler<ApiResponseBase>() {

						@Override
						public void onSuccess(ApiResponseBase response) {
							dialog.dismiss();
							Toast.makeText(getActivity(),
									response.getMessage(), Toast.LENGTH_LONG)
									.show();
							MainActivity main = (MainActivity) getActivity();
							main.popFragment();
						}

						@Override
						public void onFailure(String message) {
							dialog.dismiss();
							Toast.makeText(getActivity(), message,
									Toast.LENGTH_LONG).show();
						}

					});
		}
			return true;

		case R.id.contact: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.addChat(post.getAuthorId(),
					new ApiHandler<ApiResponseAddChat>() {

						@Override
						public void onSuccess(ApiResponseAddChat response) {
							dialog.dismiss();
							Toast.makeText(getActivity(),
									response.getMessage(), Toast.LENGTH_LONG)
									.show();
							MainActivity main = (MainActivity) getActivity();
							ChatFragment post = new ChatFragment();
							post.setParam(response.getChat());
							main.popFragment(2, 1);
							main.gotoFragment(2, post);
						}

						@Override
						public void onFailure(String message) {
							dialog.dismiss();
							Toast.makeText(getActivity(), message,
									Toast.LENGTH_LONG).show();
						}

					});
		}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
