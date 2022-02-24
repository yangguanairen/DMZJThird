package com.sena.dmzjthird.utils.api;

import com.example.application.ComicDetailInfo;
import com.sena.dmzjthird.utils.RsaUtil;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * FileName: ComicApi
 * Author: JiaoCan
 * Date: 2022/2/24 11:14
 */

public class ComicApi {

    /**
     * 获取漫画详情
     */
    public static Observable<ComicDetailInfo.ComicDetailInfoResponse> getComicInfo(String comicId) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/comic/detail/" + comicId).build();
            Response response = client.newCall(request).execute();

            String bodyStr = response.body().string();

            byte[] decryptByte = RsaUtil.decrypt(bodyStr);

            ComicDetailInfo.ComicDetailInfoResponse data;
            try {
                ComicDetailInfo.ComicDetailResponse tmp = ComicDetailInfo.ComicDetailResponse.parseFrom(decryptByte);
                data = tmp.getData();
            } catch (Exception e) {
                data = null;
            }

            emitter.onNext(data);
        });
    }

    /**
     * 获取漫画章节列表
     */
    public static Observable<ComicDetailInfo.ComicDetailChapterResponse> getComicChapter(String comicId) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/comic/detail/" + comicId).build();
            Response response = client.newCall(request).execute();

            byte[] decryptByte = RsaUtil.decrypt(response.body().string());

            ComicDetailInfo.ComicDetailChapterResponse data = null;
            try {
                ComicDetailInfo.ComicDetailResponse tmp = ComicDetailInfo.ComicDetailResponse.parseFrom(decryptByte);
                data = tmp.getData().getChaptersList().get(0);
            } catch (Exception e) {

            }
            emitter.onNext(data);
        });
    }

}
