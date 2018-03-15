package luongduongquan.com.chatapp.Common;

import com.quickblox.users.model.QBUser;

import java.util.List;

import luongduongquan.com.chatapp.Holder.QBUserHolder;

/**
 * Created by luong.duong.quan on 3/15/2018.
 */

public class Common {

	public static String createChatDialogName(List<Integer> qbUsers){
		List<QBUser> qbUserList = QBUserHolder.getInstance().getUserByIds(qbUsers);
		StringBuilder name = new StringBuilder();
		for (QBUser user : qbUserList){
			name.append(user.getFullName()).append(" ");
		}
		if (name.length() > 30){
			name = name.replace(30,name.length()-1, "...");
		}
		return name.toString();
	}


}
