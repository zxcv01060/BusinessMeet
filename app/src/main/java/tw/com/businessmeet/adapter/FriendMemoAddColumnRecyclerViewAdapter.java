package tw.com.businessmeet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.UserInformationBean;

public class FriendMemoAddColumnRecyclerViewAdapter extends RecyclerView.Adapter<FriendMemoAddColumnRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<FriendCustomizationBean> friendCustomizationBeanList;
    private FriendsRecyclerViewAdapter.ClickListener clickLinster;

    //創建構造函數
    public FriendMemoAddColumnRecyclerViewAdapter(Context context, List<FriendCustomizationBean> friendCustomizationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.friendCustomizationBeanList = friendCustomizationBeanList;
    }

    @NonNull
    @Override
    public FriendMemoAddColumnRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 創建自定義布局
        View view = layoutInflater.inflate(R.layout.recycler_view_addcolumn_memo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendMemoAddColumnRecyclerViewAdapter.ViewHolder holder, int position) {
        FriendCustomizationBean data = friendCustomizationBeanList.get(position);
        holder.memoTitle.setText(data.getName());
    }

    // 得到總條數
    @Override
    public int getItemCount() {
        return friendCustomizationBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView memoTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memoTitle = (TextView) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_title);
        }
    }
}
