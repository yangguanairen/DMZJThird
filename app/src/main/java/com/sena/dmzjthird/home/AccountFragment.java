package com.sena.dmzjthird.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lxj.xpopup.XPopup;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.view.LoginActivity;
import com.sena.dmzjthird.account.view.SettingActivity;
import com.sena.dmzjthird.account.view.UpdateProfileActivity;
import com.sena.dmzjthird.account.view.UserCommentActivity;
import com.sena.dmzjthird.account.view.UserSubscribedActivity;
import com.sena.dmzjthird.databinding.FragmentAccountBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;

/**
 * account偶数次点击白屏
 * 或许导向错误
 * comic未出现此现象
 * 或许页面复杂度过高，或者comic是start页面
 *
 */

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    private ActivityResultLauncher getResult;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);

        initActivityResult();

        setUserInfo();

        binding.accountSetting.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingActivity.class)));

        binding.accountLogin.setOnClickListener(v -> {
            String uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
            if ("".equals(uid)) {
                getResult.launch(new Intent(getContext(), LoginActivity.class));
            } else {
                // Edit Profile
                IntentUtil.goToActivity(getActivity(), UpdateProfileActivity.class);
            }
        });

        binding.accountFavorite.setOnClickListener(v -> {
            String uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
            if (!"".equals(uid)) {
                Toast.makeText(getActivity(), getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            } else {
                IntentUtil.goToActivity(getActivity(), UserSubscribedActivity.class);
            }
        });

        binding.accountMessage.setOnClickListener(v -> {
            String uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
            if (!"".equals(uid)) {
                Toast.makeText(getActivity(), getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            } else {
                IntentUtil.goToActivity(getActivity(), UserCommentActivity.class);
            }
        });

        binding.accountExit.setOnClickListener(v -> cancelLogin());


        return binding.getRoot();
    }

    private void initActivityResult() {
        getResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> setUserInfo());
    }

    // 已登录，加载信息
    private void setUserInfo() {

        String uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
        if (!"".equals(uid)) {
            LogUtil.e(MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_AVATAR, ""));
            Glide.with(getActivity())
                    .load(MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_AVATAR, ""))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
                    .into(binding.avatar);
            binding.nickname.setText(MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_NICKNAME, ""));
        }

    }

    private void cancelLogin() {

        String uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
        if ("".equals(uid)) {
            Toast.makeText(getActivity(), getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            return ;
        }

        new XPopup.Builder(getContext())
                .borderRadius(10.0f)
                .isDestroyOnDismiss(true)
                .asConfirm("", "确定退出登录吗？", () -> {
                    binding.nickname.setText(getString(R.string.icon_login_title));
                    binding.avatar.setImageResource(R.drawable.layer_avatar);
                    MyDataStore.getInstance(getContext()).saveValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
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