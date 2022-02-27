package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicClassifyFilterBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/14
 * Time: 17:00
 */
public class ComicFilterTagAdapter extends BaseQuickAdapter<ComicClassifyFilterBean, BaseViewHolder> {

    private final Context mContext;

    private String theme;
    private String readership;
    private String status;
    private String area;
    private String sort;

    private final Callbacks callbacks;


    public ComicFilterTagAdapter(Context context) {
        super(R.layout.item_object_filter_tag);
        this.mContext = context;
        callbacks = (Callbacks) context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicClassifyFilterBean bean) {
        String filterName = bean.getTitle();

        holder.setText(R.id.title, filterName);

        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicFilterTagChildAdapter adapter = new ComicFilterTagChildAdapter();
        recyclerView.setAdapter(adapter);

        // 设置初始筛选状态
        if (filterName.contains("排序")) {
            adapter.setTagId(sort);
        } else if (filterName.contains("题材"))  {
            adapter.setTagId(theme);
        } else if (filterName.contains("读者")) {
            adapter.setTagId(readership);
        } else if (filterName.contains("进度")) {
            adapter.setTagId(status);
        } else {
            adapter.setTagId(area);
        }

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            // 更新筛选状态
            String tagId = ((ComicClassifyFilterBean.Items)adapter1.getData().get(position)).getTag_id();
            if (filterName.contains("排序")) {
                sort = tagId;
                adapter.setTagId(sort);
            } else if (filterName.contains("题材"))  {
                theme = tagId;
                adapter.setTagId(theme);
            } else if (filterName.contains("读者")) {
                readership = tagId;
                adapter.setTagId(readership);
            } else if (filterName.contains("进度")) {
                status = tagId;
                adapter.setTagId(status);
            } else {
                area = tagId;
                adapter.setTagId(area);
            }
            String filter = "";
            if (!theme.equals("0")) {
                filter += theme+"-";
            }
            if (!readership.equals("0")) {
                filter += readership+"-";
            }
            if (!status.equals("0")) {
                filter += status+"-";
            }
            if (!area.equals("0")) {
                filter += area+"-";
            }
            if (filter.equals("")) {
                filter = "0";
            } else {
                // 最后一位为“-”
                filter = filter.substring(0, filter.length()-1);
            }
            // 通知Activity跟新数据
            callbacks.clickTag(filter, sort);
            // 重设数据已达到更新
            adapter.setList(bean.getItems());
        });

        adapter.setList(bean.getItems());
    }

    public interface Callbacks {
        void clickTag(String filter, String sort);
    }

    public void setFilterData(String data) {
        String[] tmp = data.split("-");
        sort = tmp[0];
        theme = tmp[1];
        readership = tmp[2];
        status = tmp[3];
        area = tmp[4];
    }
}
