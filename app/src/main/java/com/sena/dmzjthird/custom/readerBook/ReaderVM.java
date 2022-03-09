package com.sena.dmzjthird.custom.readerBook;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * FileName: ReaderVM
 * Author: JiaoCan
 * Date: 2022/3/9 10:16
 */

public class ReaderVM extends ViewModel {

    MutableLiveData<Boolean> isShowToolView = new MutableLiveData<>();
    MutableLiveData<Boolean> isSubscribe = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
