package com.sena.dmzjthird;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/9/7
 * Time: 19:47
 */
public class TopK {

    private String TAG = "";

    @Test
    public void main() {
//        int[] data = {1, 2, 5, 7,4, 5, 23, 55, 34, 86};
        int[] data = {84, 55, 34, 23, 7, 5, 5, 4, 3, 1};

        getTopKByHeapSort(data, 3);

    }




    /**
     * 时间复杂度: n*log2(n)
     */

    int count = 1;

    private void getTopKByHeapSort(int[] data, int k) {

        int[] topK = new int[k];
        for (int i = 0; i < k; i++) {
            topK[i] = data[i];
        }
        // 使用前k个元素构建小顶堆
        for (int i = k/2-1; i >= 0; i--) {
            TAG = "construct";
            heapAdjust(topK, i, k);
        }

        // 从k+1位置开始遍历
        // 大于堆顶的元素替换, 重新构建小顶堆
        for (int j = k+1; j < data.length; j++) {
            TAG  = "adjust";

            if (data[j] > topK[0]) {
                count++;
                topK[0] = data[j];
                for (int i = k/2-1; i >= 0; i--) {
                    heapAdjust(topK, i, k);
                }
            }

        }
        System.out.println(count);

        System.out.println(Arrays.toString(topK));



    }


    private int[] heapAdjust(int[] data, int start, int length) {

        int tmp = data[start];

        for (int k = start*2+1; k < length; k = k*2+1) {
            if (k+1 < length && data[k] > data[k+1]) {
                count++;
                k++;
            }
            if (data[k] < tmp) {
                count++;
                data[start] = data[k];
                start = k;
            } else {
                break;
            }
        }

        data[start] = tmp;

//        System.out.println("count: " + count++  + "tag: " + TAG + Arrays.toString(data));
        return data;
    }

}
