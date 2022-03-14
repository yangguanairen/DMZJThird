package com.sena.dmzjthird.custom.readerComic;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * FileName: ComicViewViewModel
 * Author: JiaoCan
 * Date: 2022/3/4 14:45
 */

public class ComicViewVM extends ViewModel {

    public MutableLiveData<List<Integer>> chapterIdList = new MutableLiveData<>();
    public MutableLiveData<List<String>> chapterNameList = new MutableLiveData<>();

    public MutableLiveData<Integer> currentChapterId = new MutableLiveData<>();
    public MutableLiveData<String> currentChapterName = new MutableLiveData<>();

    public MutableLiveData<Integer> totalPage = new MutableLiveData<>();
    public MutableLiveData<Integer> currentPage = new MutableLiveData<>();

    public MutableLiveData<Boolean> isShowToolView = new MutableLiveData<>();

    public MutableLiveData<Boolean> isUserOperate = new MutableLiveData<>();

    public MutableLiveData<Boolean> isUseSysBright = new MutableLiveData<>();
    public MutableLiveData<Integer> sysMaxBright = new MutableLiveData<>();
    public MutableLiveData<Integer> seekbarBright = new MutableLiveData<>();
    public MutableLiveData<Boolean> isVerticalMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> isKeepLight = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFullMode = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShowState = new MutableLiveData<>();



    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
