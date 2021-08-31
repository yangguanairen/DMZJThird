package com.sena.dmzjthird.comic.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicCommentAdapter;
import com.sena.dmzjthird.comic.bean.ComicCommentBean;
import com.sena.dmzjthird.databinding.FragmentComicCommentBinding;
import com.sena.dmzjthird.tmp.TmpActivity;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ComicCommentFragment extends Fragment {

    private static final String CLASSIFY = "classify";
    private static final String OBJECT_ID = "comic_id";

    private FragmentComicCommentBinding binding;
    private String objectId;
    private int classify;

    private int page = 1;
    private int sort = 0;
    private RetrofitService service;
    private ComicCommentAdapter adapter;

    public static ComicCommentFragment newInstance(int classify, String id) {

        Bundle args = new Bundle();
        args.putInt(CLASSIFY, classify);
        args.putString(OBJECT_ID, id);

        ComicCommentFragment fragment = new ComicCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            objectId = getArguments().getString(OBJECT_ID);
            classify = getArguments().getInt(CLASSIFY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicCommentBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_COMMENT_URL);

        initAdapter();
        initControl();

        return binding.getRoot();
    }

    private void initAdapter() {

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicCommentAdapter(getActivity(), classify);
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            ComicCommentBean bean = ((ComicCommentBean) adapter.getData().get(position));
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_comment, null, false);
            TextView usernameTV = layoutView.findViewById(R.id.username);
            usernameTV.setText(bean.getNickname());
            usernameTV.setOnClickListener(v1 -> {
                IntentUtil.goToUserInfoActivity(getActivity(), bean.getSender_uid());
                dialog.dismiss();
            });
            // 我是有点绝望，它的点赞存储在本地
            TextView likeTV = layoutView.findViewById(R.id.like);
            likeTV.setOnClickListener(v1 -> {
                Toast.makeText(getActivity(), "没做呢", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            // 跳转评论页面
            TextView replyTV = layoutView.findViewById(R.id.reply);
            replyTV.setOnClickListener(v1 -> {
                if (PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_UID) == null ||
                        PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_TOKEN) == null) {
                    Toast.makeText(getActivity(), getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                    return;
                }
                IntentUtil.goToCommentReplyActivity(getActivity(), classify, bean.getObj_id(), bean.getSender_uid(),
                        bean.getId(), bean.getNickname(), bean.getContent());
                dialog.dismiss();
            });
            // 复制评论到剪贴板
            TextView copyTV = layoutView.findViewById(R.id.copy);
            copyTV.setOnClickListener(v1 -> {
                ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(null, bean.getContent()));
                dialog.dismiss();
            });


            dialog.setView(layoutView);

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

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        if (sort == 0) {
            page = 1;
        } else {
            page = 0;
        }
        getResponse();

    }

    private void getResponse() {

            Observable.create((ObservableOnSubscribe<List<ComicCommentBean>>) emitter -> {
                String data = "";
                try {
                    URL url = new URL(getString(R.string.comment_request, classify, sort, objectId, page));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream in = connection.getInputStream();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        LogUtil.e(connection.getResponseCode() + "");
                        return;
                    }
                    int byteRead;
                    byte[] buffer = new byte[1024];
                    while ((byteRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, byteRead);
                    }
                    out.close();
                    data = out.toString();
                    connection.disconnect();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                String commentCount = data.substring(data.lastIndexOf(",") + 1, data.length() - 1);
                data = data.replace("," + commentCount, "");

                List<ComicCommentBean> beans = generateData(data);
                if (beans != null) {
                    emitter.onNext(beans);
                    emitter.onComplete();
                }

            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(beans -> {
                        if (sort == 0) {
                            if (page == 1 && beans.size() == 0) {
                                // 无数据处理
                                return;
                            }
                            if (beans.size() == 0) {
                                adapter.getLoadMoreModule().loadMoreEnd();
                            } else {
                                adapter.getLoadMoreModule().loadMoreComplete();
                                page++;
                            }
                            if (page == 1) {
                                adapter.setList(beans);
                            } else {
                                adapter.addData(beans);
                            }
                        } else {
                            if (page == 0 && beans.size() == 0) {
                                // 无数据处理
                                return;
                            }
                            if (beans.size() == 0) {
                                adapter.getLoadMoreModule().loadMoreEnd();
                            } else {
                                adapter.getLoadMoreModule().loadMoreComplete();
                                page++;
                            }
                            if (page == 0) {
                                adapter.setList(beans);
                            } else {
                                adapter.addData(beans);
                            }
                        }

                    });

    }

    private void initControl() {
        binding.sort.setOnClickListener(v -> {

            String[] items = new String[]{getString(R.string.latest_comment), getString(R.string.hottest_comment)};

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setSingleChoiceItems(items, sort, (dialog1, which) -> {
                        sort = which;
                        initAdapter();
                        dialog1.dismiss();
                        binding.sort.setText(items[which]);
                    })
                    .create();
            dialog.show();
        });

        binding.publicComment.setOnClickListener(v -> {
            if (PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_UID) == null ||
                    PreferenceHelper.findStringByKey(getActivity(), PreferenceHelper.USER_TOKEN) == null) {
                Toast.makeText(getActivity(), getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                return;
            }
            IntentUtil.goToCommentReplyActivity(getActivity(), classify, objectId, "0", "0", null, null);
//            Intent intent = new Intent(getActivity(), TmpActivity.class);
//            intent.putExtra("abc", objectId);
//            startActivity(intent);


        });
    }

    private List<ComicCommentBean> generateData(String data) {
        List<ComicCommentBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i< array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                ComicCommentBean bean = new ComicCommentBean();
                bean.setId(object.getString("id"));
                bean.setObj_id(object.getString("obj_id"));
                bean.setContent(object.getString("content"));
                if (!TextUtils.isEmpty(object.getString("upload_images"))) {
                    List<String> images = Arrays.asList(object.getString("upload_images").split(","));
                    bean.setUpload_images(images);
                } else {
                    bean.setUpload_images(null);
                }
                bean.setLike_amount(object.getInt("like_amount"));
                bean.setIs_passed(object.getInt("is_passed"));
                bean.setMasterCommentNum(object.getInt("masterCommentNum"));
                if (object.getInt("masterCommentNum") > 0) {

                    JSONArray masterArray = new JSONArray((array.getJSONObject(i).get("masterComment")).toString());
                    List<ComicCommentBean.MasterComment> comments = new ArrayList<>();
                    for (int j = 0; j < masterArray.length(); j++) {
                        JSONObject o = masterArray.getJSONObject(j);
                        ComicCommentBean.MasterComment comment = new ComicCommentBean.MasterComment();
                        comment.setId(o.getString("id"));
                        comment.setObj_id(o.getString("obj_id"));
                        comment.setContent(o.getString("content"));
                        if (!TextUtils.isEmpty(object.getString("upload_images"))) {
                            List<String> images = Arrays.asList(object.getString("upload_images").split(","));
                            bean.setUpload_images(images);
                        } else {
                            bean.setUpload_images(null);
                        }
                        comment.setLike_amount(o.getInt("like_amount"));
                        comment.setIs_passed(o.getInt("is_passed"));
                        comment.setCreate_time(o.getLong("create_time"));
                        comment.setSender_uid(o.getString("sender_uid"));
                        comment.setCover(o.getString("cover"));
                        comment.setNickname(o.getString("nickname"));
                        comment.setSex(o.getInt("sex"));

                        comment.setOrigin_comment_id(o.getString("origin_comment_id"));
                        comment.setTo_uid(o.getString("to_uid"));
                        comment.setTo_comment_id(o.getString("to_comment_id"));
                        comment.setIs_goods(o.getInt("is_goods"));

                        comments.add(comment);
                    }
                    bean.setMasterComments(comments);
                } else {
                    bean.setMasterComments(null);
                }
                bean.setCreate_time(object.getLong("create_time"));
                bean.setSender_uid(object.getString("sender_uid"));
                bean.setNickname(object.getString("nickname"));
                bean.setCover(object.getString("cover"));
                bean.setSex(object.getInt("sex"));

                bean.setOrigin_comment_id(object.getString("origin_comment_id"));
                bean.setTo_comment_id(object.getString("to_comment_id"));
                bean.setTo_uid(object.getString("to_uid"));
                bean.setIs_goods(object.getInt("is_goods"));



                beans.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (beans.size() == 0) {
            return null;
        } else {
            return beans;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}