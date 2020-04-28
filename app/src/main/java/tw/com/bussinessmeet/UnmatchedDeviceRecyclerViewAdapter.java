package tw.com.bussinessmeet;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import tw.com.bussinessmeet.helper.AvatarHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;


public class UnmatchedDeviceRecyclerViewAdapter extends RecyclerView.Adapter<UnmatchedDeviceRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private String un;
    private Bitmap avatar;
    private UserInformationDAO userInformationDAO;
    private List<UserInformationBean> userInformationBeanList;
    private UserInformationBean userInformationBean;
    private MatchedClickListener matchedClickListener;
    private AvatarHelper avatarHelper;

    UnmatchedDeviceRecyclerViewAdapter(Context context, List<UserInformationBean> userInformationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInformationBeanList = userInformationBeanList;
    }

    private UserInformationBean ufb;
    private UserInformationServiceImpl userInformationApi;
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>> searchBluetoothResponseListener =
            new AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>>() {
                @Override
                public Call<ResponseBody<List<UserInformationBean>>> request(UserInformationBean... userInformationBeans) {
                    return userInformationApi.search(userInformationBeans[0]);
                }

                @Override
                public void onSuccess(List<UserInformationBean> userInformationBeanList) {
                    System.out.println("List" + userInformationBeanList.size());
                    if (userInformationBeanList.size() != 0) {
                        ufb = userInformationBeanList.get(0);
                        un = ufb.getUserName();
                        avatarHelper = new AvatarHelper();
                        avatar = avatarHelper.getImageResource(ufb.getAvatar());
                        Log.d("unmatched-username", un);
                    }
                }

                @Override
                public void onFail(int status) {

                }
            };

    @NonNull
    @Override
    public UnmatchedDeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_unmatched_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnmatchedDeviceRecyclerViewAdapter.ViewHolder holder, int position) {
        //UserInformationBean ufb = userInformationBeanList.get(position);
        ufb = userInformationBeanList.get(position);
        holder.bindInformation(un, avatar);
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
            itemView.setOnClickListener(this);
        }

        void bindInformation(String userName, Bitmap avatar) {
            search_name.setText(userName);
            search_pro_pic_small.setImageBitmap(avatar);
        }

        @Override
        public void onClick(View v) {
            if (matchedClickListener != null) {
                matchedClickListener.onMatchedClick(v, getAdapterPosition());
            }
        }

    }

    void setClickListener(MatchedClickListener searchClickLinster) {
        this.matchedClickListener = searchClickLinster;
    }

    public UserInformationBean getUserInformation(int position) {
        return userInformationBeanList.get(position);
    }

    public void dataInsert(UserInformationBean userInformationBean) {
        Log.d("resultDataInsert", userInformationBean.getBlueTooth());
        userInformationBeanList.add(userInformationBean);
        AsyncTasKHelper.execute(searchBluetoothResponseListener, userInformationBean);
        notifyItemInserted(getItemCount());
    }

    public interface MatchedClickListener {
        void onMatchedClick(View view, int position);
    }


}