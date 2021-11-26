package com.sena.dmzjthird;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/9/7
 * Time: 19:28
 */
public class Heap {

    @Test
    public void main() {

//        int[] data = {1, 2, 5, 7, 4, 5, 23, 55, 34, 86};

        int[] data = {84, 55, 34, 23, 7, 5, 5, 4, 3, 1};
        heapSort(data);
        System.out.println("main: " + Arrays.toString(data));
//        bubbleSort(data);



    }


    int a = 0;

    void bubbleSort(int[] data) {
        int count = data.length - 1;
        int flag = 1;
        while (count > 0 && flag == 1) {
            flag = 0;
            for (int i = 0; i < count; i++) {
                a++;
                if (data[i] > data[i+1]) {
                    flag = 1;
                    int tmp = data[i];
                    data[i] = data[i+1];
                    data[i+1] = tmp;
                }
            }
            System.out.println(Arrays.toString(data));
            count--;
        }
        System.out.println(a);
        System.out.println(Arrays.toString(data));
    }

    private void heapSort(int[] data) {

        // 构建大顶堆
        for (int i = data.length / 2 - 1; i >= 0; i--) {
            heapAdjust(data, i, data.length);
        }

        for (int j = data.length - 1; j >= 0; j--) {
            // 交换堆顶和堆底的元素
            int tmp = data[0];
            data[0] = data[j];
            data[j] = tmp;

            // 调整堆, 长度为j, 之后的元素已经排序
            heapAdjust(data, 0, j);
        }

        System.out.println(Arrays.toString(data));


    }

    private void heapAdjust(int[] data, int start, int length) {

        int tmp = data[start];

        // 从指定位置找到最后非叶节点
        // 再找到其子树的最大值
        // 比较，交换
        for (int k = start * 2 + 1; k < length; k = k * 2 + 1) {
            if (k + 1 < length && data[k] < data[k + 1]) {
                k = k + 1;
            }
            if (data[k] > tmp) {
                data[start] = data[k];
                start = k;
            } else {
                break;
            }
        }

        data[start] = tmp;

    }

}
