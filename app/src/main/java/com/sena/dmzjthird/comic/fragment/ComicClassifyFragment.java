package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.custom.ProgressWheel;
import com.sena.dmzjthird.databinding.FragmentComicClassifyBinding;


public class ComicClassifyFragment extends Fragment {

    private FragmentComicClassifyBinding binding;

//    private ProgressWheel pwOne, pwTwo;
//    boolean wheelRunning;
//    int wheelProgress = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentComicClassifyBinding.inflate(inflater, container, false);
//
//        pwOne = (ProgressWheel) findViewById(R.id.progress_bar_one);
//        binding.on.spin();
//        pwTwo = (ProgressWheel) findViewById(R.id.progress_bar_two);
//        new Thread(r).start();
//
//        mPieProgress1 = (PieProgress) findViewById(R.id.pie_progress1);
//        mPieProgress2 = (PieProgress) findViewById(R.id.pie_progress2);
//        new Thread(indicatorRunnable).start();
//
//        Button startBtn = (Button) findViewById(R.id.btn_start);
//        binding.startBtn.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (!wheelRunning) {
//                    wheelProgress = 0;
//                    pwTwo.resetCount();
//                    new Thread(r).start();
//                }
//            }
//        });
//
//        new Thread(r).start();


        return binding.getRoot();
    }

//    final Runnable r = new Runnable() {
//        public void run() {
//            wheelRunning = true;
//            while (wheelProgress < 361) {
//                pwTwo.incrementProgress();
//                wheelProgress++;
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            wheelRunning = false;
//        }
//    };
}