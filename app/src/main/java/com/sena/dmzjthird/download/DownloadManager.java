package com.sena.dmzjthird.download;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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

/**
 * FileName: DownloadManager
 * Author: JiaoCan
 * Date: 2022/3/15 11:11
 */

public class DownloadManager {

    private static  Context mContext;

    private static final AtomicReference<DownloadManager> INSTANCE = new AtomicReference<>();
    private HashMap<String, Call> downCalls;
    private OkHttpClient mClient;

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

    public void download(List<DownloadUrlBean> urlList, String comicId, String chapterId, String comicName, String chapterName, DownloadObserver downloadObserver) {
        Observable.just(urlList)
                .filter(s -> !downCalls.containsKey(s))
                .flatMap(s -> Observable.just(createDownInfo(urlList, comicId + "-" + chapterId, comicName, chapterName)))
                .map(this::checkFolder)
                .flatMap(downloadInfo -> Observable.create(new DownloadSubscribe(downloadInfo)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(downloadObserver);
    }


    public void cancel(String url) {
        Call call = downCalls.get(url);
        if (call != null) {
            call.cancel();
        }
        downCalls.remove(url);
    }

    private DownloadInfo createDownInfo(List<DownloadUrlBean> list, String tag, String comicName, String chapterName) {

        DownloadInfo downloadInfo = new DownloadInfo(list);
        List<String> fileNameList = new ArrayList<>();
        for (DownloadUrlBean url: list) {
            fileNameList.add(url.getUrl().replace("https://images.dmzj.com/l/", "").replace("/","-"));
        }
        downloadInfo.setFileNameList(fileNameList);
        downloadInfo.setTotal(list.size());
        downloadInfo.setTag(tag);
        downloadInfo.setComicName(comicName);
        downloadInfo.setChapterName(chapterName);

        return downloadInfo;
    }


    private DownloadInfo checkFolder(DownloadInfo downloadInfo) {
        String folder = "/" + downloadInfo.getComicName() + "/" + downloadInfo.getChapterName();
        File file = new File(mContext.getCacheDir(), folder);
        if (!file.exists()) file.mkdirs();
        return downloadInfo;
    }
//    private DownloadInfo getRealFileName(DownloadInfo downloadInfo) {
//        String fileName = downloadInfo.getFileName();
//        long downloadLength = 0;
//        long contentLength = downloadInfo.getTotal();
//        File file = new File(mContext.getCacheDir(), fileName)   ;
//        if (file.exists()) {
//            downloadLength = file.length();
//        }
//        int i = 1;
//        while (downloadLength >= contentLength) {
//            int dotIndex = fileName.lastIndexOf(".");
//            String fileNameOther;
//            if (dotIndex == -1) {
//                fileNameOther = fileName + "(" + i + ")";
//            } else {
//                fileNameOther = fileName.substring(0, dotIndex)
//                         + "(" + i + ")" + fileName.substring(dotIndex);
//            }
//            File newFile = new File(mContext.getCacheDir(), fileNameOther);
//            file = newFile;
//            downloadLength = newFile.length();
//            i++;
//        }
//        downloadInfo.setProgress(downloadLength);
//        downloadInfo.setFileName(file.getName());
//        return downloadInfo;
//    }

    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
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

        private DownloadInfo downloadInfo;

        public DownloadSubscribe(DownloadInfo downloadInfoList) {
            this.downloadInfo = downloadInfoList;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<DownloadInfo> emitter) throws Throwable {


            List<DownloadUrlBean> urlList = downloadInfo.getUrlList();
            List<String> fileNameUrlList = downloadInfo.getFileNameList();

            emitter.onNext(downloadInfo);

            for (int i = 0; i < urlList.size(); i++) {
                DownloadUrlBean url = urlList.get(i);
                String fileName = fileNameUrlList.get(i);

                Request request = new Request.Builder().url(url.getUrl()).build();
                Call call = mClient.newCall(request);

                downCalls.put(downloadInfo.getTag(), call);
                Response response = call.execute();

                File file = new File(mContext.getCacheDir(), fileName);
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
                    downloadInfo.setDownloadedNum(downloadInfo.getDownloadedNum() + 1);
                    emitter.onNext(downloadInfo);
                    fos.flush();
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread.sleep(1000);
            }
            downCalls.remove(downloadInfo.getTag());
            emitter.onComplete();


//            String url = downloadInfo.getUrl();
//            long downloadLength = downloadInfo.getProgress();
//            long contentLength = downloadInfo.getTotal();
//
//            emitter.onNext(downloadInfo);
//
//            Request request = new Request.Builder()
//                    .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
//                    .url(url).build();
//
//            Call call = mClient.newCall(request);
//            downCalls.put(url, call);
//            Response response = call.execute();
//
//            File file = new File(mContext.getCacheDir(), downloadInfo.getFileName());
//            InputStream is;
//            FileOutputStream fos;
//            try {
//                is = response.body().byteStream();
//                fos = new FileOutputStream(file, true);
//                byte[] buffer = new byte[1024];
//                int len;
//                while ((len = is.read(buffer)) != -1) {
//                    fos.write(buffer, 0, len);
//                    downloadLength += len;
//                    downloadInfo.setProgress(downloadLength);
//                    emitter.onNext(downloadInfo);
//                }
//                fos.flush();
//                fos.close();
//                is.close();
//                downCalls.remove(url);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//
//            }
        }
    }
}
