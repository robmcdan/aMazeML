package aMazeML.GamePlay.Preferences;

import aMazeML.Direction;
import aMazeML.Statistics.WeightedSelection;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Preference {
    private WeightedSelection<Direction> selector = new WeightedSelection<>();

    public Preference(){

    }

    public Preference(double N, double S, double E, double W){
        selector.add(N, Direction.N);
        selector.add(S, Direction.S);
        selector.add(E, Direction.E);
        selector.add(W, Direction.W);
    }

    public Direction getNext(){
        return selector.next();
    }

    public ArrayList<Direction> getStack(){
        return selector.shuffle(4);
    }



}
