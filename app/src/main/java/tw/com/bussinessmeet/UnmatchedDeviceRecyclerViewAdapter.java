package tw.com.bussinessmeet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tw.com.bussinessmeet.Bean.UserInformationBean;


public class UnmatchedDeviceRecyclerViewAdapter extends RecyclerView.Adapter<UnmatchedDeviceRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private  List<UserInformationBean> userInformationBeanList;
    private SearchClickListener searchClickListener;
    UnmatchedDeviceRecyclerViewAdapter(Context context, List<UserInformationBean> userInformationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInformationBeanList = userInformationBeanList;
    }

    @NonNull
    @Override
    public UnmatchedDeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_matched_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnmatchedDeviceRecyclerViewAdapter.ViewHolder holder, int position) {
        UserInformationBean ufb = userInformationBeanList.get(position);
        holder.bindInformation(ufb.getBlueTooth(),ufb.getAvatar());
    }

    @Override
    public int getItemCount() {
        return userInformationBeanList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView search_pro_pic_small;
        TextView search_name;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            search_pro_pic_small = itemView.findViewById(R.id.search_pro_pic_small);
            search_name = itemView.findViewById(R.id.search_name);
        }

        void bindInformation(String userName, String avatar){
            search_name.setText(userName);

        }

        @Override
        public void onClick(View v) {
                if(searchClickListener != null){
                    searchClickListener.onSearchClick(v,getAdapterPosition());
                }
        }

    }
    void  setClickListener(SearchClickListener searchClickLinster){
        this.searchClickListener = searchClickLinster;
    }
    public void dataInsert(UserInformationBean userInformationBean){
        Log.d("resultDataInsert",userInformationBean.getBlueTooth());
        userInformationBeanList.add(userInformationBean);
        notifyItemInserted(getItemCount());
    }
    public interface SearchClickListener{
        void onSearchClick(View view, int position);
    }

}