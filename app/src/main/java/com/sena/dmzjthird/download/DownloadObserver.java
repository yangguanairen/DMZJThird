package com.sena.dmzjthird.download;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * FileName: DownloadObserver
 * Author: JiaoCan
 * Date: 2022/3/15 11:16
 */

public class DownloadObserver implements Observer<DownloadInfo> {

    protected Disposable d;
    protected DownloadInfo downloadInfo;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(@NonNull DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
