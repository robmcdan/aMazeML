package aMazeML.Statistics;

import java.util.*;


/**
 * from stackoverflow
 * https://stackoverflow.com/questions/6409652/random-weighted-selection-in-java
 * @param <E>
 */

public class WeightedSelection<E> {
    // red-black tree implementation of sortedMap
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public WeightedSelection() {
        this(new Random());
    }

    public WeightedSelection(Random random) {

        this.random = random;
        this.random.setSeed(7);
    }

    public WeightedSelection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public ArrayList<E> shuffle(int n){
        ArrayList<E> retVal = new ArrayList<>();

        for(int i = 0; i < n; i ++) {
            while(true) {
                E e = this.next();
                if (!retVal.contains(e)) {
                    retVal.add(e);
                    break;
                }
            }

        }
        return retVal;
    }

    public static void main(String[] args) {
        WeightedSelection<String> selection = new WeightedSelection<>();
        selection.add(.20, "cat");
        selection.add(.20, "dog");
        selection.add(.40, "bird");
        selection.add(.20, "fish");

        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < 1000; i ++){
            list.add(selection.next());
        }
        int cats = Collections.frequency(list, "bird");

        System.out.println(cats/1000f);
    }
}