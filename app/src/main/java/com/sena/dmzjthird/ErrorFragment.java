package com.sena.dmzjthird;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sena.dmzjthird.databinding.FragmentErrorBinding;


public class ErrorFragment extends Fragment {

    private FragmentErrorBinding binding;

    private static final String COMIC_ID = "comic_id";

    private String mComicId;

    public static ErrorFragment newInstance(String comicId) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString(COMIC_ID, comicId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mComicId = getArguments().getString(COMIC_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentErrorBinding.inflate(inflater, container, false);

        if (mComicId == null) {
            binding.noData.setText(getString(R.string.no_data));
        } else {
            binding.noData.setText("漫画ID:"+mComicId+"\n"+getString(R.string.copyright_error));
        }

        return binding.getRoot();
    }
}