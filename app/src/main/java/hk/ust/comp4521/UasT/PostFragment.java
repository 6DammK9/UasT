package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.ThreadItemInfo;
import hk.ust.comp4521.UasT.json.ApiResponseBase;
import hk.ust.comp4521.UasT.json.ApiResponseIMG;

public class PostFragment extends BaseFragment implements IMGUpload {

	ThreadItemInfo info;
	String code;
	EditText title, details, content, attachment;
	RatingBar rating;
    ImageButton camera, image;

    View view;


	public PostFragment() {

	}

	public void setCode(ThreadItemInfo info, String code) {
		this.info = info;
		this.code = code;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_post, container, false);
		title = (EditText) view.findViewById(R.id.title);
		details = (EditText) view.findViewById(R.id.details);
		content = (EditText) view.findViewById(R.id.content);
		rating = (RatingBar) view.findViewById(R.id.rating);

        attachment = (EditText) view.findViewById(R.id.attachment);
        camera = (ImageButton) view.findViewById(R.id.camera);
        image = (ImageButton) view.findViewById(R.id.image);

		if (info.type().equals("comments")) {
			rating.setVisibility(View.VISIBLE);
            attachment.setVisibility(View.GONE);
            camera.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
		} else if (info.type().equals("groups")) {
            attachment.setVisibility(View.GONE);
            camera.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Multimedia_photo cam = new Multimedia_photo();
                if (!Multimedia_photo.checkCameraHardware(view.getContext())) {
                    Toast.makeText(view.getContext(), "This feature needs camera!", Toast.LENGTH_LONG).show();
                    return;
                }
                cam.setParam(PostFragment.this);
                MainActivity main = (MainActivity) getActivity();
                main.gotoFragment(0, cam);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Multimedia_image img = new Multimedia_image();
                img.setParam(PostFragment.this);
                MainActivity main = (MainActivity) getActivity();
                main.gotoFragment(0, img);
            }
        });
		return view;
	}

	@Override
	public String getTitle() {
		return "New " + info.typeName();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.post, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.confirm: {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.post(info.type(), code, title.getText().toString(),
					details.getText().toString(), content.getText().toString(),
					rating.getProgress(), attachment.getText().toString(), new ApiHandler<ApiResponseBase>() {

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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public void UploadIMG(final String img) {
        File file = new File(img);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ProgressBar bar = new ProgressBar(view.getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        bar.setIndeterminate(true);
        builder.setCancelable(true).setTitle("Sending request to server").setView(bar);
        final AlertDialog dialog = builder.show();

        ApiManager.upIMG(file, new ApiHandler<ApiResponseIMG>() {

            @Override
            public void onSuccess(ApiResponseIMG response) {
                dialog.dismiss();
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "USTasUST");
                File tmp_file = new File(img);
                File target_file = new File(mediaStorageDir.getPath() + File.separator + response.getIMG());
                attachment.setText(response.getIMG());
                tmp_file.renameTo(target_file);
            }

            @Override
            public void onFailure(String message) {
                dialog.dismiss();
                Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
            }

        });
    }
}
