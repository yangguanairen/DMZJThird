package com.sena.dmzjthird.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.view.LoginActivity;
import com.sena.dmzjthird.account.view.SettingActivity;
import com.sena.dmzjthird.account.view.UpdateProfileActivity;
import com.sena.dmzjthird.account.view.UserCommentActivity;
import com.sena.dmzjthird.account.view.UserDownloadActivity;
import com.sena.dmzjthird.account.view.UserHistoryActivity;
import com.sena.dmzjthird.account.view.UserSubscribedActivity;
import com.sena.dmzjthird.databinding.FragmentAccountHomeBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.XPopUpUtil;

/**
 * account偶数次点击白屏
 * 或许导向错误
 * comic未出现此现象
 * 或许页面复杂度过高，或者comic是start页面
 *
 */

public class AccountHomeFragment extends Fragment {

    private FragmentAccountHomeBinding binding;

    public static AccountHomeFragment newInstance() {
        return new AccountHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccountHomeBinding.inflate(inflater, container, false);

        binding.accountSetting.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingActivity.class)));

        binding.accountLogin.setOnClickListener(v -> {
            long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
            if (uid == 0L) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            } else {
                // Edit Profile
                startActivity(new Intent(getContext(), UpdateProfileActivity.class));
            }
        });

        binding.accountFavorite.setOnClickListener(v -> {
            long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
            if (uid == 0L) {
                XPopUpUtil.showCustomErrorToast(getContext(), getString(R.string.not_login));
            } else {
                startActivity(new Intent(getContext(), UserSubscribedActivity.class));
            }
        });

        binding.accountHistory.setOnClickListener(v -> {
            long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
            if (uid == 0L) {
                XPopUpUtil.showCustomErrorToast(getContext(), getString(R.string.not_login));
            } else {
                startActivity(new Intent(getContext(), UserHistoryActivity.class));
            }
        });

        binding.accountMessage.setOnClickListener(v -> {
            long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
            if (uid == 0L) {
                XPopUpUtil.showCustomErrorToast(getContext(), getString(R.string.not_login));
            } else {
                startActivity(new Intent(getContext(), UserCommentActivity.class));
            }
        });

        binding.accountExit.setOnClickListener(v -> cancelLogin());

        binding.accountDownload.setOnClickListener(v ->
                startActivity(new Intent(getContext(), UserDownloadActivity.class)));


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        setUserInfo();
    }

    // 已登录，加载信息
    private void setUserInfo() {

        long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        if (uid != 0L) {
            LogUtil.e(MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_AVATAR, ""));
            Context context = getContext();
            if (context == null) return ;
            Glide.with(context)
                    .load(MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_AVATAR, ""))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
                    .into(binding.avatar);
            binding.nickname.setText(MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_NICKNAME, ""));
        }

    }

    private void cancelLogin() {

        long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        if (uid == 0L) {
            XPopUpUtil.showCustomErrorToast(getContext(), getString(R.string.not_login));
            return ;
        }

        new XPopup.Builder(getContext())
                .borderRadius(10.0f)
                .isDestroyOnDismiss(true)
                .asConfirm("", "确定退出登录吗？", () -> {
                    binding.nickname.setText(getString(R.string.click_login));
                    binding.avatar.setImageResource(R.drawable.layer_avatar);
                    MyDataStore.clearUserData(getContext());
                })
                .setConfirmText("确定")
                .setCancelText("取消")
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}