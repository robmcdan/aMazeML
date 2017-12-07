package aMazeML.GamePlay.Analysis;

import aMazeML.Direction;
import aMazeML.GamePlay.Preferences.Preference;

import java.util.ArrayList;

public class FrequencyDistributions {
    public static void main(String[] args) {
        ArrayList<Direction> N = new ArrayList<>();
        ArrayList<Direction> S = new ArrayList<>();
        ArrayList<Direction> E = new ArrayList<>();
        ArrayList<Direction> W = new ArrayList<>();
        Preference preference = new Preference(5, 10, 20, 15);
        for(int i = 0; i < 50; i++){
            Direction d = preference.getNext();
            switch (d){
                case N:
                    N.add(d);
                    break;
                case S:
                    S.add(d);
                    break;
                case E:
                    E.add(d);
                    break;
                case W:
                    W.add(d);
                    break;
            }
        }
        int x = 3;
    }

    }
