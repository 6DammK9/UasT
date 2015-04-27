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
        // initialize Login View
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        // Identify Objects
		reset = (Button) this.findViewById(R.id.reset);
		login = (Button) this.findViewById(R.id.login);
		set = (Button) this.findViewById(R.id.set);
		email = (EditText) this.findViewById(R.id.email);
		code = (EditText) this.findViewById(R.id.code);
		name = (EditText) this.findViewById(R.id.name);
        address = (EditText) this.findViewById(R.id.address);

        // Change "code" button's name, switching between Login and Validate
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

        // Reset function: Clear email and code block
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				email.setText("");
				code.setText("");
                address.setText("");
			}
		});

        // Login function: Login / Validate
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
                    // Press "Login"
                    // Send request to server
					ApiManager.login(email.getText().toString(),
							new ApiHandler<ApiResponseBase>() {
								@Override
								public void onSuccess(ApiResponseBase response) {
									dialog.dismiss();
                                    // Bubble Message
									Toast.makeText(LoginActivity.this,
                                            "Login Success: " + response.getMessage(),
											Toast.LENGTH_LONG).show();
                                    // Store User' and its ITSC in SharedPreferences
									Database.getUser().setEmail(
											email.getText().toString());
									Database.commitUser();
									code.forceLayout();
								}

								@Override
								public void onFailure(String message) {
									dialog.dismiss();
									Toast.makeText(LoginActivity.this, "Login Fail: " + message,
											Toast.LENGTH_LONG).show();
								}

							});
				} else {
                    // Press "Validate"
                    // Send request to server
					ApiManager.validate(email.getText().toString(), code
							.getText().toString(), MainActivity.regid,
							new ApiHandler<ApiResponseValidate>() {
								@Override
								public void onSuccess(
										ApiResponseValidate response) {
									Toast.makeText(LoginActivity.this,
											"Validate Success: " + response.getMessage(),
											Toast.LENGTH_LONG).show();
                                    // Store Users Email, Auth
									Database.getUser().setEmail(
											email.getText().toString());
									Database.getUser().setAuth(
											response.getAuth());
									Database.commitUser();
									dialog.dismiss();

                                    // Retrue true back to parent
									Intent result = new Intent();
									result.putExtra("result", true);
									LoginActivity.this.setResult(100, result);
                                    // End LoginActivity
									LoginActivity.this.finish();
								}

								@Override
								public void onFailure(String message) {
									dialog.dismiss();
									Toast.makeText(LoginActivity.this, "Validate Fail: " + message,
											Toast.LENGTH_LONG).show();
								}
							});
				}
			}
		});

        // Rename these two textfield with values stored in local sp
		email.setText(Database.getUser().getEmail());
		name.setText(Database.getUser().getName());

        // Change user's nickname
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
