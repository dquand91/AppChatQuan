package luongduongquan.com.chatapp.Holder;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luong.duong.quan on 3/16/2018.
 */

public class ChatMessageHolder {

	private static ChatMessageHolder instance;
	private HashMap<String, ArrayList<QBChatMessage>> chatMessageArray;

	public static synchronized ChatMessageHolder getInstance(){

		ChatMessageHolder chatMessageHolder;
		synchronized (ChatMessageHolder.class){
			if (instance == null){
				instance = new ChatMessageHolder();
			}
		}
		return instance;
	}

	private ChatMessageHolder(){
		this.chatMessageArray = new HashMap<>();
	}

	public void putMessages(String dialogId, ArrayList<QBChatMessage> listChatMessage){
		this.chatMessageArray.put(dialogId, listChatMessage);
	}

	public void putMessage(String dialogId, QBChatMessage chatMessage){
		List<QBChatMessage> listResult = (List) this.chatMessageArray.get(dialogId);
		listResult.add(chatMessage);
		ArrayList<QBChatMessage> listAdded = new ArrayList<>(listResult.size());
		listAdded.addAll(listResult);
		putMessages(dialogId, listAdded);
	}

	public ArrayList<QBChatMessage> getChatMessagesByDialogId(String dialogId){
		return (ArrayList<QBChatMessage>) this.chatMessageArray.get(dialogId);
	}

}
