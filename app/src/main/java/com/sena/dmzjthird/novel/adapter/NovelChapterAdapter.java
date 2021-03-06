package com.sena.dmzjthird.novel.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.application.NovelChapterRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ItemObjectExpandChildBinding;
import com.sena.dmzjthird.databinding.ItemObjectExpandGroupBinding;
import com.sena.dmzjthird.utils.IntentUtil;

import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableAdapter;

/**
 * FileName: NovelChapterAdpater
 * Author: JiaoCan
 * Date: 2022/2/28 17:07
 */

public class NovelChapterAdapter extends ExpandableAdapter<ExpandableAdapter.ViewHolder> {

    private final Context mContext;
    private final List<NovelChapterRes.NovelChapterVolumeResponse> mDataList;
    private final String mNovelId;
    private final String mNovelName;
    private final String mNovelCover;
    private final String mAuthorName;

    private int mGroupPosition = -1;
    private int mChildPosition = -1;

    public NovelChapterAdapter(Context context, List<NovelChapterRes.NovelChapterVolumeResponse> dataList, String novelId, String novelName, String novelCover, String authorName) {
        mContext = context;
        mDataList = dataList;
        mNovelId = novelId;
        mNovelName = novelName;
        mNovelCover = novelCover;
        mAuthorName = authorName;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mDataList.get(groupPosition).getChaptersCount();
    }

    @Override
    public int getGroupCount() {
        return mDataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onBindChildViewHolder(@NonNull ViewHolder viewHolder, int groupPosition, int childPosition, @NonNull List<?> payloads) {
        ChildViewHolder holder = (ChildViewHolder) viewHolder;
        holder.binding.tvChild.setText(mDataList.get(groupPosition).getChapters(childPosition).getChapterName());
        holder.binding.tvChild.setTextColor(mGroupPosition == groupPosition && mChildPosition == childPosition ? Color.parseColor("#ff2196F3") : Color.parseColor("#ff000000"));

        holder.binding.tvChild.setOnClickListener(v -> {
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
            notifyDataSetChanged();

            NovelChapterRes.NovelChapterVolumeResponse volumeData = mDataList.get(groupPosition);
            NovelChapterRes.NovelChapterItemResponse chapterData = volumeData.getChapters(childPosition);
            // ?????????????????????
            IntentUtil.goToNovelViewActivity(mContext, mNovelId, mNovelCover, mNovelName, mAuthorName,
                    volumeData.getVolumeId(), volumeData.getVolumeName(),
                    chapterData.getChapterId(), chapterData.getChapterName());
        });
    }

    @Override
    protected void onBindGroupViewHolder(@NonNull ViewHolder viewHolder, int groupPosition, boolean b, @NonNull List<?> payloads) {
        GroupViewHolder holder = (GroupViewHolder) viewHolder;
        holder.binding.tvGroup.setText(mDataList.get(groupPosition).getVolumeName());
    }

    @NonNull
    @Override
    protected ViewHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int groupPosition) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemObjectExpandChildBinding binding = ItemObjectExpandChildBinding.inflate(inflater, viewGroup, false);
        return new ChildViewHolder(binding);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateGroupViewHolder(@NonNull ViewGroup viewGroup, int groupPosition) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemObjectExpandGroupBinding binding = ItemObjectExpandGroupBinding.inflate(inflater, viewGroup, false);
        return new GroupViewHolder(binding);
    }

    @Override
    protected void onGroupViewHolderExpandChange(@NonNull ViewHolder viewHolder, int groupPosition, long animDuration, boolean expand) {
        GroupViewHolder holder = (GroupViewHolder) viewHolder;
        float targetRotation = expand ? 180f : 0f;

        ObjectAnimator.ofFloat(holder.binding.icon, View.ROTATION, targetRotation)
                .setDuration(animDuration)
                .start();

        // ????????????svg????????????
        VectorDrawableCompat drawableCompat = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_down_keyboard, mContext.getTheme());
        assert drawableCompat != null;
        drawableCompat.setTint(expand ? Color.parseColor("#ff2196F3") : Color.parseColor("#89000000"));
        holder.binding.icon.setImageDrawable(drawableCompat);
        // rotation???????????????ImageView??????????????????????????????????????????????????????
//        holder.binding.icon.setRotation(targetRotation);
        holder.binding.tvGroup.setTextColor(expand ? Color.parseColor("#ff2196F3") : Color.parseColor("#ff000000"));
        holder.binding.divider.setVisibility(expand ? View.VISIBLE : View.INVISIBLE);
    }

    private static class GroupViewHolder extends ExpandableAdapter.ViewHolder {

        private final ItemObjectExpandGroupBinding binding;

        public GroupViewHolder(ItemObjectExpandGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ChildViewHolder extends ExpandableAdapter.ViewHolder {

        private final ItemObjectExpandChildBinding binding;

        public ChildViewHolder(ItemObjectExpandChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
