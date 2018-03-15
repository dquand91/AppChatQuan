package luongduongquan.com.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

/**
 * Created by luong.duong.quan on 3/15/2018.
 */

public class ListUserAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<QBUser> listUser;

	public ListUserAdapter(Context mContext, ArrayList<QBUser> listUser) {
		this.mContext = mContext;
		this.listUser = listUser;
	}

	@Override
	public int getCount() {
		return listUser.size();
	}

	@Override
	public Object getItem(int i) {
		return listUser.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		View viewRow = view;
		if (viewRow == null){

			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewRow = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);
			TextView tvName = viewRow.findViewById(android.R.id.text1);
			tvName.setText(listUser.get(i).getLogin());


		}


		return viewRow;
	}
}
