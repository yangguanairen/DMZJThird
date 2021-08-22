package com.sena.dmzjthird.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.LoginActivity;
import com.sena.dmzjthird.account.SettingActivity;
import com.sena.dmzjthird.account.UserCommentActivity;
import com.sena.dmzjthird.account.UserSubscribedActivity;
import com.sena.dmzjthird.databinding.FragmentAccountBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;

/**
 * account偶数次点击白屏
 * 或许导向错误
 * comic未出现此现象
 * 或许页面复杂度过高，或者comic是start页面
 *
 */

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);

        setUserInfo();

        binding.accountSetting.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingActivity.class)));

        binding.accountLogin.setOnClickListener(v -> {
            if (PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_UID) == null) {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1);
            } else {
                // 退出登录或者进入详细页面
                cancelLogin();
            }
        });

        binding.accountFavorite.setOnClickListener(v -> {
            String uid = PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_UID);
            if (uid == null) {
                Toast.makeText(getActivity(), getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            } else {
                IntentUtil.goToActivity(getActivity(), UserSubscribedActivity.class);
            }
        });

        binding.accountMessage.setOnClickListener(v -> {
            String uid = PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_UID);
            if (uid == null) {
                Toast.makeText(getActivity(), getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            } else {
                IntentUtil.goToActivity(getActivity(), UserCommentActivity.class);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            setUserInfo();
        }

    }

    // 已登录，加载信息
    private void setUserInfo() {
        if (PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_UID) != null) {
            LogUtil.e(PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_AVATAR));
            Glide.with(getActivity())
                    .load(PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_AVATAR))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
                    .into(binding.avatar);
            binding.nickname.setText(PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_NICKNAME));
        }
    }

    private void cancelLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出登录");
        builder.setMessage("确定退出登录吗？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            binding.nickname.setText(getActivity().getString(R.string.icon_login_title));
            binding.avatar.setImageResource(R.drawable.layer_avatar);

            PreferenceHelper.setStringByKey(getActivity(), PreferenceHelper.USER_UID, null);
            dialog.dismiss();
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}