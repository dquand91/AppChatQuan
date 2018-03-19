package luongduongquan.com.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;

import luongduongquan.com.chatapp.Adapter.ChatMessageAdapter;
import luongduongquan.com.chatapp.Common.Common;
import luongduongquan.com.chatapp.Holder.ChatMessageHolder;

public class ChatMessageActivity extends AppCompatActivity implements QBChatDialogMessageListener{

	QBChatDialog qbChatDialog;
	ListView listChatMessage;
	ImageButton btnSubmitButton;
	EditText edtContent;

	ChatMessageAdapter adapter;


	@Override
	protected void onDestroy() {
		super.onDestroy();
		qbChatDialog.removeMessageListrener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		qbChatDialog.removeMessageListrener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_message);

		initView();

		initChatDialogs();

		retrieveMessage();

		btnSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				QBChatMessage chatMessage = new QBChatMessage();
				chatMessage.setBody(edtContent.getText().toString());
				chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
				chatMessage.setSaveToHistory(true);

				try {
					qbChatDialog.sendMessage(chatMessage);
				} catch (SmackException.NotConnectedException e) {
					e.printStackTrace();
				}

//				ChatMessageHolder.getInstance().putMessage(qbChatDialog.getDialogId(), chatMessage);
//
//				ArrayList<QBChatMessage> messages = ChatMessageHolder.getInstance().getChatMessagesByDialogId(qbChatDialog.getDialogId());
//
//				refreshAdapter(messages);

				if(qbChatDialog.getType() == QBDialogType.PRIVATE){
					ChatMessageHolder.getInstance().putMessage(qbChatDialog.getDialogId(), chatMessage);
					ArrayList<QBChatMessage> messages = ChatMessageHolder.getInstance().getChatMessagesByDialogId(chatMessage.getDialogId());
					refreshAdapter(messages);
				}

				//Remove inputed value
				edtContent.setText("");
				edtContent.setFocusable(true);


			}
		});



	}

	private void refreshAdapter(ArrayList<QBChatMessage> messages){

		adapter = new ChatMessageAdapter(getBaseContext(), messages);
		listChatMessage.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	private void retrieveMessage() {

		QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
		messageGetBuilder.setLimit(500); // limit 500 messages per get

		if (qbChatDialog != null){
			QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
				@Override
				public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
					// put Message to cache
					ChatMessageHolder.getInstance().putMessages(qbChatDialog.getDialogId(), qbChatMessages);

					refreshAdapter(qbChatMessages);

//					adapter = new ChatMessageAdapter(getBaseContext(), qbChatMessages);
//					listChatMessage.setAdapter(adapter);
//					adapter.notifyDataSetChanged();

				}

				@Override
				public void onError(QBResponseException e) {

				}
			});
		}

	}

	private void initChatDialogs() {

		qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(Common.DIALOG_CHAT_EXTRA);
		qbChatDialog.initForChat(QBChatService.getInstance());

		//Register listener Incoming Message
		QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
		incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
			@Override
			public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

			}

			@Override
			public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

			}
		});


		// Add Join group to enable group chat
		if(qbChatDialog.getType() == QBDialogType.GROUP
				|| qbChatDialog.getType() == QBDialogType.PUBLIC_GROUP){
			DiscussionHistory discussionHistory = new DiscussionHistory();
			discussionHistory.setMaxStanzas(0);

			qbChatDialog.join(discussionHistory, new QBEntityCallback() {
				@Override
				public void onSuccess(Object o, Bundle bundle) {

				}

				@Override
				public void onError(QBResponseException e) {
					Log.e("ERROR", "" +  e.getMessage());
				}
			});
		}

		qbChatDialog.addMessageListener(this);

	}

	private void initView() {

		listChatMessage = findViewById(R.id.list_message_chat_room);
		btnSubmitButton = findViewById(R.id.btn_send_message);
		edtContent = findViewById(R.id.edt_content_input_message);


	}

	@Override
	public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
		// Cache Message
		ChatMessageHolder.getInstance().putMessage(qbChatMessage.getDialogId(), qbChatMessage);
		ArrayList<QBChatMessage> messages = ChatMessageHolder.getInstance().getChatMessagesByDialogId(qbChatMessage.getDialogId());

		refreshAdapter(messages);
	}

	@Override
	public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
		Log.e("Error", " " + e.getMessage());
	}
}