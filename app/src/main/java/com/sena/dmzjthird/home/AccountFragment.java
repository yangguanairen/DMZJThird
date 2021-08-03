package com.sena.dmzjthird.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.SettingActivity;
import com.sena.dmzjthird.databinding.FragmentAccountBinding;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);

        binding.accountSetting.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingActivity.class)));


        return binding.getRoot();
    }
}