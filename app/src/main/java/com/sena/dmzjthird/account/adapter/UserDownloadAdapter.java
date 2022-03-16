package com.sena.dmzjthird.account.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ItemDownloadChildBinding;
import com.sena.dmzjthird.databinding.ItemDownloadGroupBinding;
import com.sena.dmzjthird.download.DownloadChapterBean;
import com.sena.dmzjthird.download.DownloadComicBean;
import com.sena.dmzjthird.download.DownloadInfo;
import com.sena.dmzjthird.download.DownloadManager;
import com.sena.dmzjthird.download.DownloadObserver;
import com.sena.dmzjthird.novel.adapter.NovelChapterAdapter;
import com.sena.dmzjthird.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableAdapter;

/**
 * FileName: UserDownloadAdapter
 * Author: JiaoCan
 * Date: 2022/3/16 15:05
 */

public class UserDownloadAdapter extends ExpandableAdapter<ExpandableAdapter.ViewHolder> {

    private final Context mContext;
    private final List<DownloadComicBean> mDataList;

    public UserDownloadAdapter(Context context, List<DownloadComicBean> dataList) {
        mContext = context;
        if (dataList == null) dataList = new ArrayList<>();
        mDataList = dataList;
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (groupPosition < mDataList.size()) return mDataList.get(groupPosition).getTotalChapter();
        return 0;
    }

    @Override
    public int getGroupCount() {
        return mDataList.size();
    }

    @Override
    protected void onBindChildViewHolder(@NonNull ViewHolder viewHolder, int groupPosition, int childPosition, @NonNull List<?> payloads) {
        ChildViewHolder holder = (ChildViewHolder) viewHolder;

        DownloadChapterBean chapterBean = mDataList.get(groupPosition).getChapterList().get(childPosition);

        holder.binding.chapterName.setText(chapterBean.getChapterName());
        holder.binding.progress.setMaxProgress(chapterBean.getTotalPages());
        holder.binding.control.setOnClickListener(v -> {
            DownloadManager.getInstance(mContext)
                    .download(chapterBean.getUrlList(), mDataList.get(groupPosition).getComicId(), chapterBean.getChapterId(),
                            mDataList.get(groupPosition).getComicName(), chapterBean.getChapterName(), new DownloadObserver() {
                                @Override
                                public void onNext(DownloadInfo downloadInfo) {
                                    super.onNext(downloadInfo);
                                    holder.binding.progress.setProgress(downloadInfo.getDownloadedNum());
                                }
                            });
        });
    }

    @Override
    protected void onBindGroupViewHolder(@NonNull ViewHolder viewHolder, int groupPosition, boolean b, @NonNull List<?> payloads) {
        GroupViewHolder holder = (GroupViewHolder) viewHolder;

        DownloadComicBean comicBean = mDataList.get(groupPosition);
        GlideUtil.loadImage(mContext, comicBean.getComicCover(), holder.binding.cover);
        holder.binding.title.setText(comicBean.getComicName());

    }

    @NonNull
    @Override
    protected ViewHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int groupPosition) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemDownloadChildBinding binding = ItemDownloadChildBinding.inflate(inflater, viewGroup, false);
        return new ChildViewHolder(binding);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateGroupViewHolder(@NonNull ViewGroup viewGroup, int groupPosition) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemDownloadGroupBinding binding = ItemDownloadGroupBinding.inflate(inflater, viewGroup, false);
        return new GroupViewHolder(binding);
    }

    @Override
    protected void onGroupViewHolderExpandChange(@NonNull ViewHolder viewHolder, int groupPosition, long animDuration, boolean expand) {
        GroupViewHolder holder = (GroupViewHolder) viewHolder;
        float targetRotation = expand ? 180f : 0f;

        ObjectAnimator.ofFloat((View) holder.binding.icon, View.ROTATION, targetRotation)
                .setDuration(animDuration)
                .start();

        // 动态改变svg图片颜色
        VectorDrawableCompat drawableCompat = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_down_keyboard, mContext.getTheme());
        assert drawableCompat != null;
        drawableCompat.setTint(expand ? Color.parseColor("#ff2196F3") : Color.parseColor("#89000000"));
        holder.binding.icon.setImageDrawable(drawableCompat);
        // rotation属性保存在ImageView上，更改图片会自动修正图片的旋转角度
//        holder.binding.icon.setRotation(targetRotation);
//        holder.binding.tvGroup.setTextColor(expand ? Color.parseColor("#ff2196F3") : Color.parseColor("#ff000000"));
        holder.binding.divider.setVisibility(expand ? View.VISIBLE : View.INVISIBLE);
    }

    private static class GroupViewHolder extends ExpandableAdapter.ViewHolder {

        private final ItemDownloadGroupBinding binding;

        public GroupViewHolder(ItemDownloadGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ChildViewHolder extends ExpandableAdapter.ViewHolder {

        private final ItemDownloadChildBinding binding;

        public ChildViewHolder(ItemDownloadChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
