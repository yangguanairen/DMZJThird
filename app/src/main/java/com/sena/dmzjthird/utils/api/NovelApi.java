package com.sena.dmzjthird.utils.api;

import com.example.application.NovelChapterRes;
import com.example.application.NovelDetailRes;
import com.sena.dmzjthird.utils.RsaUtil;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * FileName: NovelApi
 * Author: JiaoCan
 * Date: 2022/2/28 10:09
 */

public class NovelApi {

    /**
     * 获取轻小说详情
     */
    public static Observable<NovelDetailRes.NovelDetailResponse> getNovelInfo(String novelId) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/novel/detail/" + novelId).build();
            Response response = client.newCall(request).execute();

            byte[] decryptByte = RsaUtil.decrypt(response.body().string());

            NovelDetailRes.NovelDetailResponse data;
            try {
                data = NovelDetailRes.NovelDetailResponse.parseFrom(decryptByte);
            } catch (Exception e) {
                data = null;
            }
            emitter.onNext(data);
        });
    }

    public static Observable<List<NovelChapterRes.NovelChapterVolumeResponse>> getNovelChapter(String novelId) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/novel/chapter/" + novelId).build();
            Response response = client.newCall(request).execute();

            String bodyStr = response.body().string();
            byte[] decryptByte = RsaUtil.decrypt(bodyStr);

            List<NovelChapterRes.NovelChapterVolumeResponse> data;
            try {
                NovelChapterRes.NovelChapterResponse tmp = NovelChapterRes.NovelChapterResponse.parseFrom(decryptByte);
                data = tmp.getDataList();
            } catch (Exception e) {
                data = null;
            }
            emitter.onNext(data);
        });
    }

//    public static Observable<String> getNovelContent(String novelId, String chapterId) {
//
//        return Observable.create(emitter -> {
//
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url().build();
//            Response response = client.newCall(request).execute();
//
//            byte[] decryptByte = RsaUtil.decrypt(response.body().toString());
//
//        })
//
//    }


}
