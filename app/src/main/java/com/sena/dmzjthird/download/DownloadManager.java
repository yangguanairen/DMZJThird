package com.sena.dmzjthird.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import com.sena.dmzjthird.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.Url;

/**
 * FileName: DownloadManager
 * Author: JiaoCan
 * Date: 2022/3/15 11:11
 */

public class DownloadManager {

    @SuppressLint("StaticFieldLeak")
    private static  Context mContext;

    private static final AtomicReference<DownloadManager> INSTANCE = new AtomicReference<>();
    private final HashMap<String, Call> downCalls;
    private final OkHttpClient mClient;

    public static DownloadManager getInstance(Context context) {
        mContext = context;
        for (;;) {
            DownloadManager current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new DownloadManager();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private DownloadManager() {
        downCalls = new HashMap<>();
        mClient = new Builder().build();
    }

    public void download(List<String> urlList, String folderName, DownloadObserver downloadObserver) {
        Observable.just(urlList)
                .filter(s -> !downCalls.containsKey(folderName))
                .flatMap(s -> Observable.just(createDownInfo(s, folderName)))
//                .map(this::checkFolder)
                .flatMap(downloadInfo -> Observable.create(new DownloadSubscribe(downloadInfo)))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(downloadObserver);
    }


    public void cancel(String tag) {
        Call call = downCalls.get(tag);
        if (call != null) {
            call.cancel();
        }
        downCalls.remove(tag);
    }

    private DownloadInfo createDownInfo(List<String> list, String folderName) {

        DownloadInfo downloadInfo = new DownloadInfo();
        List<String> urlList = new ArrayList<>();  // 需要下载的url
        List<String> nameList = new ArrayList<>();

        String finalFolderName = "/漫画下载/" + folderName;
        File folder = new File(mContext.getExternalCacheDir(), finalFolderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }


        for (String url: list) {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            File file = new File(finalFolderName, fileName);
            if (file.exists() && file.length() == getContentLength(url)) {
                continue;
            }
            urlList.add(url);
            nameList.add(folder.getPath() + "/" + fileName);
        }

        downloadInfo.setUrlList(urlList);
        downloadInfo.setNameList(nameList);
        downloadInfo.setTotalPage(list.size());
        downloadInfo.setFinishPage(list.size() - urlList.size());
        downloadInfo.setTag(folderName);

        return downloadInfo;
    }

    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength == 0 ? DownloadInfo.TOTAL_ERROR : contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DownloadInfo.TOTAL_ERROR;
    }

    private class DownloadSubscribe implements ObservableOnSubscribe<DownloadInfo> {

        private final DownloadInfo downloadInfo;

        public DownloadSubscribe(DownloadInfo downloadInfoList) {
            this.downloadInfo = downloadInfoList;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<DownloadInfo> emitter) throws Throwable {


            List<String> urlList = downloadInfo.getUrlList();
            List<String> nameList = downloadInfo.getNameList();


            for (int i = 0; i < urlList.size(); i++) {
                String url = urlList.get(i);
                String fileName = nameList.get(i);

                Request request = new Request.Builder().url(url).addHeader("Referer", "http://imgsmall.dmzj.com").build();
                Call call = mClient.newCall(request);

                downCalls.put(downloadInfo.getTag(), call);
                Response response = call.execute();

                File file = new File(fileName);
                InputStream is;
                FileOutputStream fos;
                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file, true);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    downloadInfo.setFinishPage(downloadInfo.getFinishPage() + 1);
                    emitter.onNext(downloadInfo);
                    fos.flush();
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    LogUtil.e("图片下载出错!!");
                    e.printStackTrace();
                }
            }
            downCalls.remove(downloadInfo.getTag());
            emitter.onComplete();

        }
    }
}
