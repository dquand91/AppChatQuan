package luongduongquan.com.chatapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import luongduongquan.com.chatapp.Adapter.ListUserAdapter;
import luongduongquan.com.chatapp.Common.Common;
import luongduongquan.com.chatapp.Holder.UserHolder;

public class ListUserChatActivity extends AppCompatActivity {

	ListView listViewUsers;
	Button btnCreateChat;
	private static String TAG = "ListUserChatActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_user_chat);

		listViewUsers =findViewById(R.id.listUsers_Chat);
		listViewUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		retrieveAllUser();




		btnCreateChat = findViewById(R.id.btn_createChat_ListUserChat);
		btnCreateChat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int countChoice = listViewUsers.getCount();
				if(listViewUsers.getCheckedItemCount() == 1){
					createPrivateChat(listViewUsers.getCheckedItemPositions());
				} else if (listViewUsers.getCheckedItemCount() > 1){
					createGroupChat(listViewUsers.getCheckedItemPositions());
				} else {
					Toast.makeText(ListUserChatActivity.this, "Please select an item", Toast.LENGTH_SHORT).show();
				}
			}
		});


	}

	private void createGroupChat(SparseBooleanArray checkedItemPositions) {
		final ProgressDialog mDialog = new ProgressDialog(ListUserChatActivity.this);
		mDialog.setMessage("Please wait...");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		int countChoice = listViewUsers.getCount();
		ArrayList<Integer> choiceIdsList = new ArrayList<>();
		for (int i = 0; i < countChoice; i++){
			if (checkedItemPositions.get(i)){
				QBUser user = (QBUser) listViewUsers.getItemAtPosition(i);
				choiceIdsList.add(user.getId());
			}
		}

		// Create Chat dialog
		QBChatDialog dialogChat = new QBChatDialog();
		dialogChat.setName(Common.createChatDialogName(choiceIdsList));
		Log.d(TAG, "QUAN choiceIdsList = " + Common.createChatDialogName(choiceIdsList));
		dialogChat.setType(QBDialogType.GROUP);
		dialogChat.setOccupantsIds(choiceIdsList);

		QBRestChatService.createChatDialog(dialogChat).performAsync(new QBEntityCallback<QBChatDialog>() {
			@Override
			public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
				mDialog.dismiss();
				Toast.makeText(getBaseContext(), "Create chat Dialog Successfully", Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onError(QBResponseException e) {
				mDialog.dismiss();
				Log.e("Error", "" + e.getMessage());
			}
		});


	}

	private void createPrivateChat(SparseBooleanArray checkedItemPositions) {

		final ProgressDialog mDialog = new ProgressDialog(ListUserChatActivity.this);
		mDialog.setMessage("Please wait...");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();

		int countChoice = listViewUsers.getCount();
		for(int i=0; i<countChoice; i++){
			if(checkedItemPositions.get(i)){
				QBUser user = (QBUser) listViewUsers.getItemAtPosition(i);
				QBChatDialog qbDialog = DialogUtils.buildPrivateDialog(user.getId());
				QBRestChatService.createChatDialog(qbDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
					@Override
					public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
						mDialog.dismiss();
						Toast.makeText(getBaseContext(), "Create chat Dialog Successfully", Toast.LENGTH_SHORT).show();
						finish();
					}

					@Override
					public void onError(QBResponseException e) {
						mDialog.dismiss();
						Log.e("Error", "" + e.getMessage());
					}
				});
			}
		}




	}

	private void retrieveAllUser() {

		QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
			@Override
			public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

				UserHolder.getInstance().putUsers(qbUsers);

				ArrayList<QBUser> listUserNoCurrentUser =new ArrayList<>();
				for(QBUser user : qbUsers){
					if (!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin())){
						listUserNoCurrentUser.add(user);
					}
				}
				Log.d(TAG, "retrieveAllUser ===>>> onSuccess");
				ListUserAdapter listUserAdapter = new ListUserAdapter(getBaseContext(), listUserNoCurrentUser);
				listViewUsers.setAdapter(listUserAdapter);
				listUserAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(QBResponseException e) {
				Log.e("Error", "" + e.getMessage());

			}
		});

	}
}
