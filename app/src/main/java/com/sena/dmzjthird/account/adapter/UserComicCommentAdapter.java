package com.sena.dmzjthird.account.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.bean.UserComicCommentBean;
import com.sena.dmzjthird.account.bean.UserNovelCommentBean;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.TimeUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 18:22
 */
public class UserComicCommentAdapter extends BaseQuickAdapter<Object, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;
    private final int type;

    public UserComicCommentAdapter(Context context, int type) {
        super(R.layout.item_user_comment);
        this.mContext = context;
        this.type = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Object o) {
        if (type == 0) {
           UserComicCommentBean bean = (UserComicCommentBean) o;
            Glide.with(mContext)
                    .load(GlideUtil.addCookie(bean.getObj_cover()))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into((ImageView) holder.getView(R.id.cover));
            holder.setText(R.id.title, bean.getObj_name());
            holder.setText(R.id.content, bean.getContent());
            holder.setText(R.id.likeAmount, "点赞("+bean.getLike_amount()+")");
            holder.setText(R.id.replyAmount, "回复("+bean.getReply_amount()+")");
            holder.setText(R.id.time, TimeUtil.millConvertToDate(bean.getCreate_time()*1000));

            if (bean.getMasterComment() != null) {
                holder.setVisible(R.id.other_content, true);
                holder.setText(R.id.other_content, bean.getMasterComment().getNickname()+": " +
                        bean.getMasterComment().getContent());
            }

            holder.getView(R.id.cover).setOnClickListener(v -> {
                Intent intent = new Intent(mContext, ComicInfoActivity.class);
                intent.putExtra(mContext.getString(R.string.intent_comic_id), bean.getObj_id());
                mContext.startActivity(intent);
            });
        } else {
            UserNovelCommentBean bean = (UserNovelCommentBean) o;
            Glide.with(mContext)
                    .load(bean.getObj_cover())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into((ImageView) holder.getView(R.id.cover));
            holder.setText(R.id.title, bean.getObj_name());
            holder.setText(R.id.content, bean.getContent());
            holder.setText(R.id.likeAmount, "点赞("+bean.getLike_amount()+")");
            holder.setText(R.id.replyAmount, "回复("+bean.getReply_amount()+")");
            holder.setText(R.id.time, TimeUtil.millConvertToDate(bean.getCreate_time()*1000));

            if (bean.getMasterComment() != null) {
                holder.setVisible(R.id.other_content, true);
                holder.setText(R.id.other_content, bean.getMasterComment().getNickname()+": " +
                        bean.getMasterComment().getContent());
            }

            holder.getView(R.id.cover).setOnClickListener(v -> {
//                Intent intent = new Intent(mContext, ComicInfoActivity.class);
//                intent.putExtra(mContext.getString(R.string.intent_comic_id), bean.getObj_id());
//                mContext.startActivity(intent);
            });
        }




    }


}
