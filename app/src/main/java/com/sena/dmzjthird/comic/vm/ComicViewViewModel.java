package com.sena.dmzjthird.comic.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * FileName: ComicViewViewModel
 * Author: JiaoCan
 * Date: 2022/3/4 14:45
 */

public class ComicViewViewModel extends ViewModel {

    public MutableLiveData<String> currentChapterId = new MutableLiveData<>();
    public MutableLiveData<String> currentChapterName = new MutableLiveData<>();

    public MutableLiveData<Integer> totalPage = new MutableLiveData<>();
    public MutableLiveData<Integer> currentPage = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
