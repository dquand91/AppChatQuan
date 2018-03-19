package luongduongquan.com.chatapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

import luongduongquan.com.chatapp.Holder.UserHolder;
import luongduongquan.com.chatapp.R;

/**
 * Created by luong.duong.quan on 3/16/2018.
 */

public class ChatMessageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<QBChatMessage> mListChatMessage;

	public ChatMessageAdapter(Context mContext, ArrayList<QBChatMessage> mListChatMessage) {
		this.mContext = mContext;
		this.mListChatMessage = mListChatMessage;
	}

	private class ViewHolderChatMessage{
		private BubbleTextView bubbleTextView;
		private TextView tvSender;
	}

	@Override
	public int getCount() {
		return mListChatMessage.size();
	}

	@Override
	public Object getItem(int i) {
		return mListChatMessage.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		View itemView = view;
		ViewHolderChatMessage viewHolder = new ViewHolderChatMessage();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		QBChatMessage qbChatMessage = (QBChatMessage) getItem(i);


		Log.d("QUANTest", "i = " + i + " ----- "
				+ "Sender = " + UserHolder.getInstance().getUserById(mListChatMessage.get(i).getSenderId()).getFullName()
				+ " ------ "
				+ "Body = " + qbChatMessage.getBody());

		Log.d("QUANTest", "i = " + i + " ----- " + "SenderID = " + qbChatMessage.getSenderId());

			if (qbChatMessage.getSenderId().equals(QBChatService.getInstance().getUser().getId())){
				itemView = inflater.inflate(R.layout.list_send_message, null);
				viewHolder.bubbleTextView = itemView.findViewById(R.id.message_content);

			} else {
				itemView = inflater.inflate(R.layout.list_receive_message, null);
				viewHolder.bubbleTextView = itemView.findViewById(R.id.message_content_receive);
				viewHolder.tvSender = itemView.findViewById(R.id.message_sender_name);
			}
			itemView.setTag(viewHolder);


//			itemView.setTag(i, viewHolder);

		viewHolder.bubbleTextView.setText(qbChatMessage.getBody());
		if (viewHolder.tvSender != null && !qbChatMessage.getSenderId().equals(QBChatService.getInstance().getUser().getId())){
			viewHolder.tvSender.setText(UserHolder.getInstance().getUserById(mListChatMessage.get(i).getSenderId()).getFullName());
		}



//		if (itemView == null){
//
//
//			if(mListChatMessage.get(i).getSenderId().equals(QBChatService.getInstance().getUser().getId())){
//				itemView = inflater.inflate(R.layout.list_send_message, null);
//				BubbleTextView bubbleTextSend = itemView.findViewById(R.id.message_content);
//				bubbleTextSend.setText(mListChatMessage.get(i).getBody());
//			} else {
//				itemView = inflater.inflate(R.layout.list_receive_message, null);
//				BubbleTextView bubbleTextReceive = itemView.findViewById(R.id.message_content_receive);
//				bubbleTextReceive.setText(mListChatMessage.get(i).getBody());
//				TextView tvSender = itemView.findViewById(R.id.message_sender_name);
//				tvSender.setText(UserHolder.getInstance().getUserById(mListChatMessage.get(i).getSenderId()).getFullName());
//			}
//
//		}


		return itemView;
	}
}
