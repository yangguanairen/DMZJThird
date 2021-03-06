package com.sena.dmzjthird.account.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ItemDownloadChildBinding;
import com.sena.dmzjthird.databinding.ItemDownloadGroupBinding;
import com.sena.dmzjthird.download.DownloadBean;
import com.sena.dmzjthird.download.DownloadInfo;
import com.sena.dmzjthird.download.DownloadManager;
import com.sena.dmzjthird.download.DownloadObserver;
import com.sena.dmzjthird.room.RoomHelper;
import com.sena.dmzjthird.room.chapter.Chapter;
import com.sena.dmzjthird.room.comic.Comic;
import com.sena.dmzjthird.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import pokercc.android.expandablerecyclerview.ExpandableAdapter;

/**
 * FileName: UserDownloadAdapter
 * Author: JiaoCan
 * Date: 2022/3/16 15:05
 */

public class UserDownloadAdapter extends ExpandableAdapter<ExpandableAdapter.ViewHolder> {

    private final Context mContext;
    private final List<DownloadBean> mDataList;


    private final RoomHelper roomHelper;

    public UserDownloadAdapter(Context context, List<DownloadBean> dataList) {
        mContext = context;
        if (dataList == null) dataList = new ArrayList<>();
        mDataList = dataList;
        roomHelper = RoomHelper.getInstance(context);
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (groupPosition < mDataList.size()) return mDataList.get(groupPosition).getChapterList().size();
        return 0;
    }

    @Override
    public int getGroupCount() {
        return mDataList.size();
    }

    @Override
    protected void onBindChildViewHolder(@NonNull ViewHolder viewHolder, int groupPosition, int childPosition, @NonNull List<?> payloads) {
        ChildViewHolder holder = (ChildViewHolder) viewHolder;
        ItemDownloadChildBinding binding = holder.binding;
        Chapter chapter = mDataList.get(groupPosition).getChapterList().get(childPosition);

        binding.chapterName.setText(chapter.chapterName);
        binding.progress.setMaxProgress(chapter.totalPage);
        binding.progress.setProgress(chapter.finishPage);
        binding.pageNum.setText(chapter.finishPage + "/" + chapter.totalPage);
        if (chapter.finishPage == chapter.totalPage) {
            binding.control.setImageResource(R.drawable.ic_check);
        }
        binding.control.setOnClickListener(v -> {

            if (holder.binding.progress.getProgress() == holder.binding.progress.getMaxProgress()) {
                Toast.makeText(mContext, "??????????????????????????????!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            boolean tag;
            if (binding.control.getTag() == null) {
                tag = false;
            } else {
                tag = (boolean) binding.control.getTag();
            }

            // false: ????????????????????????????????????
            // true:  ????????????????????????????????????
            if (!tag) {
                DownloadManager.getInstance(mContext)
                        .download(chapter.urlList, chapter.folder_name, new DownloadObserver() {

                            @Override
                            public void onSubscribe(Disposable d) {
                                super.onSubscribe(d);
                            }

                            @Override
                            public void onNext(DownloadInfo downloadInfo) {
                                super.onNext(downloadInfo);
                                roomHelper.updateChapterStatus(chapter.comicId, chapter.chapterId, RoomHelper.STATUS_DOWNLOADING);
                                roomHelper.updateChapterFinishPage(chapter.comicId, chapter.chapterId, downloadInfo.getFinishPage());
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    holder.binding.progress.setProgress(downloadInfo.getFinishPage());
                                    holder.binding.pageNum.setText(downloadInfo.getFinishPage() + "/" + downloadInfo.getTotalPage());
                                });
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                                roomHelper.updateChapterStatus(chapter.comicId, chapter.chapterId, RoomHelper.STATUS_FINISH);
                                roomHelper.updateComicFinishChapter(chapter.comicId);
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    holder.binding.control.setImageResource(R.drawable.ic_check);
                                    Toast.makeText(mContext, downloadInfo.getTag() + "????????????",Toast.LENGTH_SHORT).show();
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        });
            } else {
                DownloadManager.getInstance(mContext).cancel(chapter.folder_name);
            }

            binding.control.setTag(!tag);
            binding.control.setImageResource(tag ? R.drawable.ic_pause : R.drawable.ic_start);




        });

    }

    @Override
    protected void onBindGroupViewHolder(@NonNull ViewHolder viewHolder, int groupPosition, boolean b, @NonNull List<?> payloads) {
        GroupViewHolder holder = (GroupViewHolder) viewHolder;
        ItemDownloadGroupBinding binding = holder.binding;

        Comic comic = mDataList.get(groupPosition).getComic();
        GlideUtil.loadImage(mContext, comic.comicCover, holder.binding.cover);
        binding.title.setText(comic.comicName);

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
