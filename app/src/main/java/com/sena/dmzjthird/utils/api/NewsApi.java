package com.sena.dmzjthird.utils.api;

import com.example.application.ComicDetailRes;
import com.example.application.NewsListRes;
import com.sena.dmzjthird.utils.RsaUtil;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * FileName: NewsApi
 * Author: JiaoCan
 * Date: 2022/2/25 9:28
 */

public class NewsApi {

    /**
     * 获取新闻列表
     */
    public static Observable<List<NewsListRes.NewsListItemResponse>> getNewsList(int sort, int page) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/news/list/" + sort + "/" + (sort == 0 ? 2 : 3) + "/" + page).build();
            Response response = client.newCall(request).execute();

            String bodyStr = response.body().string();

            byte[] decryptByte = RsaUtil.decrypt(bodyStr);

            List<NewsListRes.NewsListItemResponse> data;
            try {
                NewsListRes.NewsListResponse tmp = NewsListRes.NewsListResponse.parseFrom(decryptByte);
                data = tmp.getDataList();
            } catch (Exception e) {
                data = null;
            }

            emitter.onNext(data);
        });
    }

}
