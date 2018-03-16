package luongduongquan.com.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import luongduongquan.com.chatapp.Adapter.ChatDialogAdatper;

public class ChatDialogActivity extends AppCompatActivity {

	FloatingActionButton floatingActionButton;
	ListView listChatDialogs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_dialog);

		listChatDialogs = findViewById(R.id.listChatDialogs);

		crateSessionforChat();
		
		loadChatDialogs();

		floatingActionButton = findViewById(R.id.chatDialog_btnAddUser);
		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ChatDialogActivity.this, ListUserChatActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadChatDialogs();

	}

	private void crateSessionforChat() {

		final ProgressDialog mDialog = new ProgressDialog(ChatDialogActivity.this);
		mDialog.setMessage("Please wait...");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		String user, password;
		user = getIntent().getStringExtra("user");
		password = getIntent().getStringExtra("password");

		final QBUser qbUser = new QBUser(user, password);
		QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
			@Override
			public void onSuccess(QBSession qbSession, Bundle bundle) {
				qbUser.setId(qbSession.getUserId());
				try {
					qbUser.setPassword(BaseService.getBaseService().getToken());
				} catch (BaseServiceException e) {
					e.printStackTrace();
				}

				QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
					@Override
					public void onSuccess(Object o, Bundle bundle) {
						mDialog.dismiss();
					}

					@Override
					public void onError(QBResponseException e) {
						Log.d("Error", ""+e.getMessage().toString());
						mDialog.dismiss();

					}
				});
			}

			@Override
			public void onError(QBResponseException e) {

			}
		});

	}

	private void loadChatDialogs() {

		QBRequestGetBuilder qbRequestGetBuilder = new QBRequestGetBuilder();
		qbRequestGetBuilder.setLimit(100); // mỗi lần chỉ lấy 100 records trả về thôi

		QBRestChatService.getChatDialogs(null, qbRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
			@Override
			public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
				ChatDialogAdatper adatper = new ChatDialogAdatper(getBaseContext(), qbChatDialogs);
				listChatDialogs.setAdapter(adatper);
				adatper.notifyDataSetChanged();
			}

			@Override
			public void onError(QBResponseException e) {
				Log.e("ERROR", "" + e.getMessage());
			}
		});


	}


}
