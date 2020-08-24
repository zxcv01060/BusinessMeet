package tw.com.businessmeet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.dao.FriendCustomizationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.service.FriendCustomizationService;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditMemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMemoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //floating button
    private FloatingActionButton floatingActionButton;

    private Button confirmButton, cancelButton;
    private EditText addColumnMemo;

    private FriendCustomizationDAO friendCustomizationDAO;
    private FriendCustomizationService friendCustomizationService;
    private AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean>() {
                @Override
                public Call<ResponseBody<FriendCustomizationBean>> request(FriendCustomizationBean... friendCustomizationBean) {
                    return friendCustomizationService.add(friendCustomizationBean[0]);
                }

                @Override
                public void onSuccess(FriendCustomizationBean friendCustomizationBean) {

                }

                @Override
                public void onFail(int status) {

                }
            };

    public EditMemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_editMemo.
     */
    // TODO: Rename and change types and number of parameters
    public static EditMemoFragment newInstance(String param1, String param2) {
        EditMemoFragment fragment = new EditMemoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_memo, container, false);
        Log.d("EditMemoFragment", "friendNo:" + getActivity().getIntent().getIntExtra("friendNo", 0));
        //floating button
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.memo_addColumn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog)
                Intent intent = new Intent(getActivity(), AddColumnMemoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("friendId", getActivity().getIntent().getStringExtra("friendId"));
                bundle.putInt("friendNo", getActivity().getIntent().getIntExtra("friendNo", 0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }
}
