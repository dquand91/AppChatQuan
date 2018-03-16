package luongduongquan.com.chatapp.Holder;

import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luong.duong.quan on 3/15/2018.
 */

public class UserHolder {

	private static UserHolder instance;

	private SparseArray<QBUser> qbUserSparseArray;

	public static synchronized UserHolder getInstance(){
		if (instance == null ){
			instance = new UserHolder();
		}
		return instance;
	}

	private UserHolder(){
		qbUserSparseArray = new SparseArray<>();
	}

	public void putUsers(List<QBUser>users){
		for(QBUser user : users){
			putUser(user);
		}
	}

	private void putUser(QBUser user) {
		qbUserSparseArray.put(user.getId(), user);
	}

	public QBUser getUserById(int id) {
		return qbUserSparseArray.get(id);
	}

	public List<QBUser> getUserByIds(List<Integer> listIds){
		List<QBUser> listResult = new ArrayList<>();
		for(Integer i : listIds){
			QBUser user = getUserById(i);
			if(user != null){
				listResult.add(user);
			}
		}
		return listResult;
	}



}
