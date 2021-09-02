package com.sena.dmzjthird;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/9/2
 * Time: 18:20
 */
public class DataStructureTest {


    @Test
    public void main() {

        HashMap<Integer, LinkedList<Integer>> graph = new HashMap<>();
        HashMap<Integer, Integer> dist = new HashMap<>();

        LinkedList<Integer> list1 = new LinkedList<>();
        list1.add(1);
        list1.add(2);

        LinkedList<Integer> list2 = new LinkedList<>();
        list2.add(3);
        list2.add(4);

        LinkedList<Integer> list3 = new LinkedList<>();
        list3.add(4);
        list3.add(5);

        LinkedList<Integer> list4 = new LinkedList<>();
        list4.add(5);

        LinkedList<Integer> list5 = new LinkedList<>();
        list5.add(5);

        graph.put(0, list1);
        graph.put(1, list2);
        graph.put(2, list3);
        graph.put(3, list4);
        graph.put(4, list5);
        graph.put(5, new LinkedList<Integer>());

        bfs(graph, dist, 0);
        dfs(graph, 0);



    }

    private void bfs(HashMap<Integer, LinkedList<Integer>> graph, HashMap<Integer, Integer> dist, Integer num) {
        Queue<Integer> queue = new LinkedList<>();
        Integer start = num;
        dist.put(start, 0);
        ((LinkedList<Integer>) queue).add(start);
        while (queue != null) {
            Integer poll = queue.poll();
            if (poll == null) {
                break;
            }
            Integer distance = dist.get(poll);
            System.out.println("节点: " + poll + "; 距离: " + distance);
            distance++;
            for (Integer i: graph.get(poll)) {
                if (!dist.containsKey(i)) {
                    dist.put(i, distance);
                    queue.offer(i);                }
            }
        }

    }

    private Set<Integer> set = new HashSet<>();
    private void dfs(HashMap<Integer, LinkedList<Integer>> graph, int num) {

        set.add(num);
        System.out.println(num + "");

        for (int i = 0; i < graph.get(num).size(); i++) {
            if (!set.contains(graph.get(num).get(i))) {
                this.dfs(graph, graph.get(num).get(i));
            }
        }
    }

//    private void

}
