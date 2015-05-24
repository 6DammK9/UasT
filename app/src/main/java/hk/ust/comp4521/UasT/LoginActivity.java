/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

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

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.json.ApiResponseBase;
import hk.ust.comp4521.UasT.json.ApiResponseValidate;

public class LoginActivity extends Activity {
	private Button reset, login, set;
	private EditText email, code, name, address;

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
                    ApiManager.API_HOST = IPtoAPI(address.getText().toString());
                }

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoginActivity.this);
				ProgressBar bar = new ProgressBar(LoginActivity.this, null,
						android.R.attr.progressBarStyleHorizontal);
				bar.setIndeterminate(true);
				builder.setCancelable(true)
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
                                    Database.setAPIaddress(ApiManager.API_HOST);
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
                                    Database.setAPIaddress(ApiManager.API_HOST);
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

        address.setText(APItoIP(Database.getAPIaddress()));

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

    private String APItoIP (String API) { return API.substring(7).substring(0,API.length()-7-10); }
    private String IPtoAPI (String IP) {return "http://" + IP + ":5001/api/";}
}
