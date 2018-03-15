package luongduongquan.com.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	static final String APP_ID = "69407";
	static final String AUTH_KEY = "nOzJV5Oy4HekhKV";
	static final String AUTH_SECRET = "JepfmhW3YuP8OUr";
	static final String ACCOUNT_KEY = "WBJHu5bAH9QzskoukPwT";

	Button btnLogin, btnSignUp;
	EditText edtUser, edtPassword;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initializeFramworkQuickBlox();

		btnLogin = findViewById(R.id.main_btnLogin);
		btnSignUp = findViewById(R.id.main_btnSignup);
		edtUser = findViewById(R.id.main_edtLogin);
		edtPassword = findViewById(R.id.main_edtPassword);

		btnLogin.setOnClickListener(this);
		btnSignUp.setOnClickListener(this);




	}

	private void initializeFramworkQuickBlox() {

		QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
		QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

	}


	@Override
	public void onClick(View view) {

		switch (view.getId()){
			case R.id.main_btnLogin:

				final String user = edtUser.getText().toString();
				final String password = edtPassword.getText().toString();

				QBUser qbUser = new QBUser(user, password);
				QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
					@Override
					public void onSuccess(QBUser qbUser, Bundle bundle) {
						Toast.makeText(MainActivity.this, "Login success! User = " + qbUser.getLogin().toString() + "", Toast.LENGTH_SHORT ).show();
						Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
						intent.putExtra("user", user);
						intent.putExtra("password", password);
						startActivity(intent);
					}

					@Override
					public void onError(QBResponseException e) {

						Toast.makeText(MainActivity.this, "Fail!!!!!!!!!!", Toast.LENGTH_SHORT ).show();

					}
				});

				break;

			case R.id.main_btnSignup:
//				startActivity(new Intent(MainActivity.this, SignUpActivity.class));
				startActivity(new Intent(MainActivity.this, TestActivity.class));
				break;
		}

	}
}
