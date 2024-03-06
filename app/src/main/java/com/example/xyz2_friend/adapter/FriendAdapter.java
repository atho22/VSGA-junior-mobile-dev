package com.example.xyz2_friend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import com.example.xyz2_friend.R;
import com.example.xyz2_friend.dao.FriendDao;
import com.example.xyz2_friend.model.Friend;
public class FriendAdapter extends BaseAdapter implements FriendDao {
    private List<Friend> mList;
    private Context mContext;
    public FriendAdapter(List<Friend> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.row_layout, viewGroup, false);
        TextView tvID = convertView.findViewById(R.id.tv_id);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvAddress = convertView.findViewById(R.id.tv_address);
        tvID.setText(String.valueOf(mList.get(i).getId()));
        tvName.setText(mList.get(i).getName());
        tvAddress.setText(mList.get(i).getAddress());
        return convertView;
    }
    @Override
    public void insert(Friend f) {
        mList.add(f);
    }
    @Override
    public void update(int id, Friend f) {
        mList.set(id, f);
    }
    @Override
    public void delete(int id) {
        mList.remove(id);
    }
    @Override
    public Friend getAFriend(int id) {
        return mList.get(id);
    }
    @Override
    public List<Friend> getAllFriends() {
        return mList;
    }
}