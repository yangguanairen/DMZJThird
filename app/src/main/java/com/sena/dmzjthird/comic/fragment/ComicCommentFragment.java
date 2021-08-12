package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.FragmentComicCommentBinding;
import com.sena.dmzjthird.databinding.FragmentComicInfoBinding;


public class ComicCommentFragment extends Fragment {

    private static final String COMIC_ID = "comic_id";

    private FragmentComicCommentBinding binding;
    private String comicId;

    public static ComicCommentFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(COMIC_ID, id);

        ComicCommentFragment fragment = new ComicCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comicId = getArguments().getString(COMIC_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicCommentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}