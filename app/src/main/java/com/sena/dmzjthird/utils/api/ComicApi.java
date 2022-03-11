package com.sena.dmzjthird.utils.api;

import com.example.application.ComicDetailRes;
import com.example.application.ComicRankListRes;
import com.example.application.ComicUpdateListRes;
import com.sena.dmzjthird.utils.RsaUtil;

import java.util.List;

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
    public static Observable<ComicDetailRes.ComicDetailInfoResponse> getComicInfo(String comicId) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/comic/detail/" + comicId).build();
            Response response = client.newCall(request).execute();

            String bodyStr = response.body().string();

            byte[] decryptByte = RsaUtil.decrypt(bodyStr);

            ComicDetailRes.ComicDetailInfoResponse data;
            try {
                ComicDetailRes.ComicDetailResponse tmp = ComicDetailRes.ComicDetailResponse.parseFrom(decryptByte);
                data = tmp.getData();
            } catch (Exception e) {
                data = null;
            }
            if (data == null) {
                emitter.onError(new Exception());
            } else {
                emitter.onNext(data);
            }
        });
    }

    /**
     * 获取漫画章节列表
     */
    public static Observable<ComicDetailRes.ComicDetailChapterResponse> getComicChapter(String comicId) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/comic/detail/" + comicId).build();
            Response response = client.newCall(request).execute();

            byte[] decryptByte = RsaUtil.decrypt(response.body().string());

            ComicDetailRes.ComicDetailChapterResponse data;
            try {
                ComicDetailRes.ComicDetailResponse tmp = ComicDetailRes.ComicDetailResponse.parseFrom(decryptByte);
                data = tmp.getData().getChaptersList().get(0);
            } catch (Exception e) {
                data = null;
            }
            if (data == null) {
                emitter.onError(new Exception());
            } else {
                emitter.onNext(data);
            }
        });
    }

    /**
     * 获取漫画更新列表
     * @param type
     * @param page 默认值 1
     * @return
     */
    public static Observable<List<ComicUpdateListRes.ComicUpdateListItemResponse>> getComicUpdate(int type, int page) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/comic/update/list/" + type + "/" + page).build();
            Response response = client.newCall(request).execute();

            byte[] decryptByte = RsaUtil.decrypt(response.body().string());

            List<ComicUpdateListRes.ComicUpdateListItemResponse> data;
            try {
                ComicUpdateListRes.ComicUpdateListResponse tmp = ComicUpdateListRes.ComicUpdateListResponse.parseFrom(decryptByte);
                data = tmp.getDataList();
            } catch (Exception e) {
                data = null;
            }
            if (data == null) {
                emitter.onError(new Exception());
            } else {
                emitter.onNext(data);
            }
        });
    }

    public static Observable<List<ComicRankListRes.ComicRankListItemResponse>> getComicRank(int tagId, int rankType, int time, int page) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(
                    "https://nnv4api.muwai.com/comic/rank/list"
                    + "?tag_id=" + tagId
                    + "&by_time=" + time
                    + "&rank_type=" + rankType
                    + "&page=" + page
            ).build();
            Response response = client.newCall(request).execute();

            byte[] decryptByte = RsaUtil.decrypt(response.body().string());

            List<ComicRankListRes.ComicRankListItemResponse> data;
            try {
                ComicRankListRes.ComicRankListResponse tmp = ComicRankListRes.ComicRankListResponse.parseFrom(decryptByte);
                data = tmp.getDataList();
            } catch (Exception e) {
                data = null;
            }
            if (data == null) {
                emitter.onError(new Exception());
            } else {
                emitter.onNext(data);
            }
        });
    }

}
