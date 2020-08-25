package tw.com.businessmeet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.dao.FriendCustomizationDAO;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.FriendCustomizationService;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;


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

    //dialog
    private Button confirm, cancel;
    private EditText addColumnMemo;

    private FriendCustomizationDAO friendCustomizationDAO;
    private DBHelper dh = null;
    private FriendCustomizationServiceImpl friendCustomizationServiceImpl = new FriendCustomizationServiceImpl();
    private FriendServiceImpl friendServiceImpl = new FriendServiceImpl();
    private AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean>() {
                @Override
                public Call<ResponseBody<FriendCustomizationBean>> request(FriendCustomizationBean... friendCustomizationBean) {
                    return friendCustomizationServiceImpl.add(friendCustomizationBean[0]);
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
        //floating button
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.memo_addColumn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog
                Log.d("EditMemoFragment", "friendNo:" + getActivity().getIntent().getIntExtra("friendNo", 0));
                LayoutInflater inflater = getActivity().getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = inflater.inflate(R.layout.add_column, null);
                builder.setView(view);
                builder.create();
                AlertDialog alertDialog = builder.show();

                addColumnMemo = (EditText) view.findViewById(R.id.addColumn_dialog_Input);
                confirm = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendCustomizationBean fcb = new FriendCustomizationBean();
                        fcb.setName(addColumnMemo.getText().toString());
                        FriendBean fb = new FriendBean();
                        fcb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                        openDB();
                        friendCustomizationDAO.add(fcb);
                        AsyncTasKHelper.execute(addResponseListener, fcb);
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
                cancel = (Button) view.findViewById(R.id.addColumn_dialog_cancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        return view;
    }

    private void openDB() {
        dh = new DBHelper(getContext());
        friendCustomizationDAO = new FriendCustomizationDAO(dh);
    }
}
