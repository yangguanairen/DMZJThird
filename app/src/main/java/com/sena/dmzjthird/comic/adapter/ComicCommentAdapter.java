package com.sena.dmzjthird.comic.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicCommentBean;
import com.sena.dmzjthird.custom.CommentTextContent;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/23
 * Time: 16:41
 */
public class ComicCommentAdapter extends BaseQuickAdapter<ComicCommentBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;
    private final int classify;


    public ComicCommentAdapter(Context context, int classify) {
        super(R.layout.item_comic_comment);
        this.mContext = context;
        this.classify = classify;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicCommentBean bean) {

        holder.setIsRecyclable(false);

        // https://images.dmzj1.com//commentImg/13/20210316125657_608_small.png
        // 此处folder为其中数字13
        // 至关重要
        int tmpNum = (Integer.parseInt(bean.getObj_id().substring(2, 3))) % 5;
        String folder = (tmpNum == 0 ? "" : tmpNum) + bean.getObj_id().substring(bean.getObj_id().length() - 2);

        Glide.with(mContext).load(bean.getCover())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(180)))
                .placeholder(R.drawable.selector_default_picture)
                .error(R.drawable.selector_default_picture)
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.username, bean.getNickname());
        CommentTextContent content = holder.getView(R.id.content);
        content.setContent(bean.getContent());
        if (bean.getUpload_images() != null) {
            content.setUploadImages(mContext, bean.getUpload_images(), folder, bean.getId());
        }

        holder.setText(R.id.time, TimeUtil.millConvertToDate(bean.getCreate_time() * 1000));
        holder.setText(R.id.likeAmount, mContext.getString(R.string.like_amount, bean.getLike_amount()));


        if (bean.getMasterCommentNum() > 0) {

            List<View> viewList = new ArrayList<>();
            for (ComicCommentBean.MasterComment comment : bean.getMasterComments()) {

                CommentTextContent commentView = new CommentTextContent(mContext);
                commentView.setParentBg(R.color.layout_bg);
                commentView.setParentPadding(10, 20, 10, 20);
                commentView.setContent(comment.getNickname() + ": " + comment.getContent());
                commentView.setUsername(comment.getNickname());
                if (comment.getUpload_images() != null) {
                    commentView.setUploadImages(mContext, comment.getUpload_images(), folder, bean.getId());
                }

                commentView.setParentClickListener(v -> {
                    AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                    View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment, null, false);
                    TextView usernameTV = view.findViewById(R.id.username);
                    usernameTV.setText(comment.getNickname());
                    usernameTV.setOnClickListener(v1 -> {
                        IntentUtil.goToUserInfoActivity(mContext, comment.getSender_uid());
                        dialog.dismiss();
                    });
                    // 我是有点绝望，它的点赞存储在本地
                    TextView likeTV = view.findViewById(R.id.like);
                    likeTV.setOnClickListener(v1 -> {
                        Toast.makeText(mContext, "没做呢", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                    // 跳转评论页面
                    TextView replyTV = view.findViewById(R.id.reply);
                    replyTV.setOnClickListener(v1 -> {
                        if (PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_UID) == null ||
                                PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_TOKEN) == null) {
                            Toast.makeText(mContext, mContext.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        IntentUtil.goToCommentReplyActivity(mContext, classify, comment.getObj_id(), comment.getSender_uid(),
                                comment.getId(), comment.getNickname(), comment.getContent());
                        dialog.dismiss();
                    });
                    // 复制评论到剪贴板
                    TextView copyTV = view.findViewById(R.id.copy);
                    copyTV.setOnClickListener(v1 -> {
                        ClipboardManager manager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        manager.setPrimaryClip(ClipData.newPlainText(null, comment.getContent()));
                        dialog.dismiss();
                    });


                    dialog.setView(view);

                    // 设置横向铺满整个屏幕，无需style
                    dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
                    dialog.getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    dialog.getWindow().setGravity(Gravity.BOTTOM);

                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setAttributes(params);

                    dialog.show();
                });

                viewList.add(commentView);

            }


            // 评论大于2条，折叠处理
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 10;
            params.topMargin = 10;
            if (viewList.size() > 2) {
                content.addOtherContentView(viewList.get(0), params);

                // 折叠时显示的提示
                TextView textView = new TextView(mContext);
                textView.setText(mContext.getString(R.string.fold_comment_count, viewList.size() - 2));
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.color.layout_bg);
                textView.setPadding(20, 20, 20, 20);
                content.addOtherContentView(textView, params);

                textView.setOnClickListener(v -> {
                    content.removeOtherContentView(v);
                    for (int i = 1; i < viewList.size() - 1; i++) {
                        content.addOtherContentViewByIndex(viewList.get(i), i, params);
                    }
                });

                content.addOtherContentView(viewList.get(viewList.size() - 1), params);

            } else {
                for (View v : viewList) {
                    content.addOtherContentView(v, params);
                }
            }
        }
    }

}
