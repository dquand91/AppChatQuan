package luongduongquan.com.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignUpActivity extends AppCompatActivity {

	Button btnSignUp, btnCancel;
	EditText edtUser, edtPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		registerFramworkSession();

		btnSignUp = findViewById(R.id.signup_btnSignup);
		btnCancel = findViewById(R.id.signup_btnCancel);

		edtUser = findViewById(R.id.signup_edtUser);
		edtPassword = findViewById(R.id.signup_edtPass);

		btnSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String user = edtUser.getText().toString();
				String password = edtPassword.getText().toString();

				QBUser qbUser = new QBUser(user, password);
				QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
					@Override
					public void onSuccess(QBUser qbUser, Bundle bundle) {
						Toast.makeText(SignUpActivity.this, "SignUp success! User = " + qbUser.getLogin().toString() + "", Toast.LENGTH_SHORT ).show();
					}

					@Override
					public void onError(QBResponseException e) {
						Toast.makeText(SignUpActivity.this, "Sign up fail!!!!!!!!!!", Toast.LENGTH_SHORT ).show();
					}
				});
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});


	}

	private void registerFramworkSession() {

		QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
			@Override
			public void onSuccess(QBSession qbSession, Bundle bundle) {

			}

			@Override
			public void onError(QBResponseException e) {
				Log.e("Error", e.getMessage() + " ");
			}
		});

	}
}
