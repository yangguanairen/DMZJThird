//package com.sena.dmzjthird.custom;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.DialogFragment;
//
//import com.sena.dmzjthird.R;
//import com.sena.dmzjthird.utils.IntentUtil;
//import com.sena.dmzjthird.utils.LogUtil;
//
///**
// * Created by Android Studio.
// * User: Sena
// * Date: 2021/8/26
// * Time: 20:07
// */
//public class DialogComment extends AlertDialog {
//
//    TextView userTV;
//    private TextView likeTV;
//    private TextView replyTV;
//    private TextView copyTV;
//
//    private String uid;
//    private static final String UID_ARG = "uid_arg";
//
//    protected DialogComment(@NonNull Context context) {
//        super(context);
//    }
//
//
//    public static DialogComment newInstance(String uid) {
//
//        Bundle args = new Bundle();
//
//        DialogComment fragment = new DialogComment();
//        args.putString(UID_ARG, uid);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public static DialogFragment newInstance(String uid, String username, String content
//            , String obj_id, String to_uid, String to_comment_id, String comment_id) {
//
//        Bundle args = new Bundle();
//
//        DialogFragment fragment = new DialogFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            uid = getArguments().getString(UID_ARG);
//        }
//    }
//
//
//    o
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.dialog_comment, container, false);
//        userTV = view.findViewById(R.id.username);
//
//        userTV.setOnClickListener(v -> {
//            IntentUtil.goToUserInfoActivity(getActivity(), uid);
//            dismiss();
//        });
//
//        return view;
//    }
//
//
//    @Override
//    public void onStart() {
//
//        getWindow().getDecorView().setPadding(0, 0, 0, 0);
//
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        getWindow().setAttributes(params);
//
//        super.onStart();
//
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        LogUtil.e("dialogFragment is stop");
//    }
//}
