package com.sena.dmzjthird.utils.api;

import com.example.application.NovelChapterRes;
import com.example.application.NovelDetailRes;
import com.sena.dmzjthird.utils.RsaUtil;

import java.security.MessageDigest;
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
    public static Observable<NovelDetailRes.NovelDetailInfoResponse> getNovelInfo(String novelId) {

        return Observable.create(emitter -> {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/novel/detail/" + novelId).build();
            Response response = client.newCall(request).execute();

            byte[] decryptByte = RsaUtil.decrypt(response.body().string());

            NovelDetailRes.NovelDetailInfoResponse data;
            try {
                NovelDetailRes.NovelDetailResponse tmp = NovelDetailRes.NovelDetailResponse.parseFrom(decryptByte);
                data = tmp.getData();
            } catch (Exception e) {
                data = null;
            }
            emitter.onNext(data);
        });
    }

    /**
     * 获取轻小说章节列表
     */
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

    /**
     * 获取轻小说某一卷章节列表
     */
    public static Observable<List<NovelChapterRes.NovelChapterItemResponse>> getNovelVolumeChapter(String novelId, int volumeId) {

        return Observable.create(emitter -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://nnv4api.muwai.com/novel/chapter/" + novelId).build();
            Response response = client.newCall(request).execute();

            String bodyStr = response.body().string();
            byte[] decryptByte = RsaUtil.decrypt(bodyStr);

            List<NovelChapterRes.NovelChapterItemResponse> data = null;
            try {
                NovelChapterRes.NovelChapterResponse tmp = NovelChapterRes.NovelChapterResponse.parseFrom(decryptByte);
                for (NovelChapterRes.NovelChapterVolumeResponse volumeResponse: tmp.getDataList()) {
                    if (volumeResponse.getVolumeId() == volumeId) {
                        data = volumeResponse.getChaptersList();
                        break;
                    }
                }
            } catch (Exception e) {

            }
            if (data == null) {
                emitter.onError(new Exception());
            } else {
                emitter.onNext(data);
            }
        });
    }

    /**
     * 获取轻小说章节正文
     */
    public static Observable<String> getNovelContent(int volumeId, int chapterId) {

        return Observable.create(emitter -> {

            String url = getNovelContentUrl(volumeId, chapterId);
            if (url == null) {
                emitter.onError(new Exception());
                return ;
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            emitter.onNext(response.body().string());
        });

    }

    private static String getNovelContentUrl(int volumeId, int chapterId) {

        String path = "/lnovel/" + volumeId + "_" + chapterId + ".txt";
        long ts = System.currentTimeMillis() / 1000;
        String key = "IBAAKCAQEAsUAdKtXNt8cdrcTXLsaFKj9bSK1nEOAROGn2KJXlEVekcPssKUxSN8dsfba51kmHM" + path + ts;
        try {
            String finalKey = md5Encrypt(key);
            return "http://jurisdiction.muwai.com" + path + "?t=" + ts + "&k=" + finalKey;
        } catch (Exception e) {
            return null;
        }

    }

    private static String md5Encrypt(String content) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(content.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            String tmp = Integer.toHexString(b & 0xff);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            builder.append(tmp);
        }
        return builder.toString();
    }


}
