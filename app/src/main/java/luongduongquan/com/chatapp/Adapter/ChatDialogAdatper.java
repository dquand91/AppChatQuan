package luongduongquan.com.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

import luongduongquan.com.chatapp.R;

/**
 * Created by luong.duong.quan on 3/15/2018.
 */

public class ChatDialogAdatper extends BaseAdapter {

	private Context mContext;
	private ArrayList<QBChatDialog> listQBChatDialog;

	public ChatDialogAdatper(Context mContext, ArrayList<QBChatDialog> listQBChatDialog) {
		this.mContext = mContext;
		this.listQBChatDialog = listQBChatDialog;
	}

	@Override
	public int getCount() {
		return listQBChatDialog.size();
	}

	@Override
	public Object getItem(int i) {
		return listQBChatDialog.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		View viewRow = view;
		if (view == null){

			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewRow = inflater.inflate(R.layout.item_chat_dialog, null);

			TextView tvTitle, tvMessage;
			ImageView imgChatDialog;

			tvMessage = viewRow.findViewById(R.id.tv_item_Message_ChatDialog);
			tvTitle = viewRow.findViewById(R.id.tv_item_Tile_ChatDialog);;
			imgChatDialog = viewRow.findViewById(R.id.img_item_ChatDialog);

			tvMessage.setText(listQBChatDialog.get(i).getLastMessage());
			tvTitle.setText(listQBChatDialog.get(i).getLastMessage());


			ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
			int randomColor = colorGenerator.getRandomColor();

			TextDrawable.IBuilder textBuilder = TextDrawable.builder().beginConfig()
					.withBorder(4)
					.endConfig()
					.round();

			// Lấy ký tự đầu tiên của Title đoạn Chat để làm Image của item.
			TextDrawable textDrawable = textBuilder.build(tvTitle.getText().toString().substring(0,1).toUpperCase(), randomColor);

			imgChatDialog.setImageDrawable(textDrawable);

		}


		return viewRow;
	}
}
