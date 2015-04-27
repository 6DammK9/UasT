package hk.ust.comp4521.exust;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import hk.ust.comp4521.exust.data.ApiHandler;
import hk.ust.comp4521.exust.data.ApiManager;
import hk.ust.comp4521.exust.data.Database;
import hk.ust.comp4521.exust.json.ApiResponseBase;
import hk.ust.comp4521.exust.json.ApiResponseValidate;

public class LoginActivity extends Activity {
    Button reset, login, set;
    EditText email, code, name, address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		reset = (Button) this.findViewById(R.id.reset);
		login = (Button) this.findViewById(R.id.login);
		set = (Button) this.findViewById(R.id.set);
		email = (EditText) this.findViewById(R.id.email);
		code = (EditText) this.findViewById(R.id.code);
		name = (EditText) this.findViewById(R.id.name);
        address = (EditText) this.findViewById(R.id.address);

		code.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				login.setText((code.length() == 0) ? "Login" : "Validate");
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});

		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				email.setText("");
				code.setText("");
                address.setText("");
			}
		});

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if (!address.getText().toString().isEmpty()){
                    ApiManager.API_HOST = "http://" + address.getText().toString() + ":5001/api";
                }

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoginActivity.this);
				ProgressBar bar = new ProgressBar(LoginActivity.this, null,
						android.R.attr.progressBarStyleHorizontal);
				bar.setIndeterminate(true);
				builder.setCancelable(false)
						.setTitle("Sending request to server").setView(bar);
				final AlertDialog dialog = builder.show();

				if (code.length() == 0) {
					ApiManager.login(email.getText().toString(),
							new ApiHandler<ApiResponseBase>() {
								@Override
								public void onSuccess(ApiResponseBase response) {
									dialog.dismiss();
									Toast.makeText(LoginActivity.this,
											response.getMessage(),
											Toast.LENGTH_LONG).show();
									Database.getUser().setEmail(
											email.getText().toString());
									Database.commitUser();
									code.forceLayout();
								}

								@Override
								public void onFailure(String message) {
									dialog.dismiss();
									Toast.makeText(LoginActivity.this, message,
											Toast.LENGTH_LONG).show();
								}

							});
				} else {
					ApiManager.validate(email.getText().toString(), code
							.getText().toString(), MainActivity.regid,
							new ApiHandler<ApiResponseValidate>() {
								@Override
								public void onSuccess(
										ApiResponseValidate response) {
									Toast.makeText(LoginActivity.this,
											response.getMessage(),
											Toast.LENGTH_LONG).show();
									Database.getUser().setEmail(
											email.getText().toString());
									Database.getUser().setAuth(
											response.getAuth());
									Database.commitUser();
									dialog.dismiss();
									Intent result = new Intent();
									result.putExtra("result", true);
									LoginActivity.this.setResult(100, result);
									LoginActivity.this.finish();
								}

								@Override
								public void onFailure(String message) {
									dialog.dismiss();
									Toast.makeText(LoginActivity.this, message,
											Toast.LENGTH_LONG).show();
								}
							});
				}
			}
		});

		email.setText(Database.getUser().getEmail());

		name.setText(Database.getUser().getName());

		set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Database.getUser().setName(name.getText().toString());
				Database.commitUser();
				Toast.makeText(LoginActivity.this, "User name successfully set!",
						Toast.LENGTH_LONG).show();
			}
		});
		

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
}
