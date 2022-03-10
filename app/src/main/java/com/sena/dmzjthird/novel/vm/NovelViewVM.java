package com.sena.dmzjthird.novel.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * FileName: NovelViewVM
 * Author: JiaoCan
 * Date: 2022/3/10 11:32
 */

public class NovelViewVM extends ViewModel {

    public MutableLiveData<Integer> currentChapterId = new MutableLiveData<>();
    public MutableLiveData<String> currentChapterName = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShowToolView = new MutableLiveData<>();
    public MutableLiveData<Integer> totalPageNum = new MutableLiveData<>();
    public MutableLiveData<Integer> currentPageIndex = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
