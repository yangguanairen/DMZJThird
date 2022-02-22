package com.sena.dmzjthird.comic.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicInfoAdapter;
import com.sena.dmzjthird.comic.bean.ComicClassifyFilterBean;
import com.sena.dmzjthird.comic.bean.ComicInfoBean;
import com.sena.dmzjthird.databinding.FragmentComicInfoBinding;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ComicInfoFragment extends Fragment {

    private static final String COMIC_ID = "comic_id";

    private Callbacks mCallbacks;

    private FragmentComicInfoBinding binding;
    private String comicId;
    private boolean isError = false;
    private ComicInfoAdapter adapter;

    private String coverUrl = "";
    private String title = "";
    private String authorName = "";
    private String updateTime = "";
    private String description = "";
    private List<String> tags = new ArrayList<>();
    private String jsonData = "[{\"title\": \"\", \"data\":[{}]}]";


    public interface Callbacks {
        void loadingDataFinish(String title, String cover, String author);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static ComicInfoFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(COMIC_ID, id);

        ComicInfoFragment fragment = new ComicInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comicId = getArguments().getString(COMIC_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicInfoBinding.inflate(inflater, container, false);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicInfoAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        binding.cover.setOnClickListener(v -> IntentUtil.goToLargeImageActivity(getActivity(), coverUrl));


        getResponse();

        return binding.getRoot();
    }

    private void getResponse() {
        Observable<List<ComicInfoBean>> observable = Observable.create(emitter -> {
            Document doc;
            try {
                doc = Jsoup.connect("https://m.dmzj.com/info/" + comicId + ".html").get();
                Element introduct = doc.getElementsByClass("Introduct_Sub autoHeight").get(0);
                description = doc.getElementsByClass("txtDesc autoHeight").text().replace("介绍:", "");
                Element pic = introduct.getElementsByClass("pic").get(0);
                coverUrl = pic.getElementsByTag("img").attr("src");
                title = pic.getElementsByTag("img").attr("title");
                Elements sub_r = doc.getElementsByClass("sub_r").get(0).getElementsByClass("txtItme");
                authorName = sub_r.get(0).getElementsByTag("a").text();

                for (Element e: doc.getElementsByClass("pd")) {
                    if (e.attr("class").equals("pd introName")) {
                        continue;
                    }
                    tags.add(e.text());
                }

                updateTime = sub_r.get(3).getElementsByClass("date").text();


                Elements javascript = doc.getElementsByTag("script");
                for (Element e : javascript) {
                    if (e.toString().contains("initIntroData")) {
                        for (String s : e.toString().split("\n")) {
                            if (s.contains("initIntroData")) {
                                int startIndex = s.indexOf("[");
                                int endIndex = s.lastIndexOf("]");
                                jsonData = s.substring(startIndex, endIndex + 1);
                                break;
                            }
                        }
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                isError = true;
            }

            emitter.onNext(new Gson().fromJson(jsonData, new TypeToken<List<ComicInfoBean>>() {
            }.getType()));
        });

        Consumer<List<ComicInfoBean>> observer = comicInfoBeans -> {
            if (isError) {
                mCallbacks.loadingDataFinish(null, null, null);
                return;
            }
            adapter.setList(comicInfoBeans);
            GlideUtil.loadImageWithCookie(getActivity(), coverUrl, binding.cover);

            binding.title.setText(title);
            binding.author.setText(authorName);
            binding.updateTime.setText(updateTime);
            binding.description.setText(description);

            initComicTag();


            mCallbacks.loadingDataFinish(title, coverUrl, authorName);
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void initComicTag() {
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getComicClassifyFilter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicClassifyFilterBean>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<ComicClassifyFilterBean> beans) {
                        for (String tag: tags) {

                            TextView textView = new TextView(getActivity());
                            textView.setText(tag);
                            textView.setBackgroundResource(R.drawable.shape_filter_tag);
                            textView.setPadding(20, 10, 20, 10);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.rightMargin = 20;
                            textView.setLayoutParams(params);

                            if (tags.indexOf(tag) < 3) {
                                binding.tag1.addView(textView);
                            } else {
                                binding.tag2.addView(textView);
                            }

                            for (ComicClassifyFilterBean bean: beans) {
                                for (ComicClassifyFilterBean.Items item: bean.getItems()) {
                                    if (item.getTag_name().contains(tag) || tag.contains(item.getTag_name())) {
                                        textView.setOnClickListener(v ->
                                                IntentUtil.goToComicClassifyActivity(getActivity(), item.getTag_id()));

                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}