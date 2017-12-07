# aMazeML
aMazeML is a stats-based game based on designing strategies to more quickly solve randomly generated mazes. The catch?  
The random maze generation is biased, and your job is to predict the weights given to each direction in the maze.

  
### Basic Components
**MazeGenerator**  
The maze generator creates mazes using a common recursive backtracking algorithm. It accepts a Preference object during  
construction, which defines the weights for each direction taken, and generates mazes via its generateMaze() method.  
It can also display a maze graphically.
  
**MazeSolver**  
The maze solver is the generator in reverse; it accepts a Preference object which defines the directional weights to  
use while solving the maze; i.e. go north more often then south, etc. It also will display the maze solution graphically.
  
**Preference**  
Preferences define the bias of the generation or solver algorithm. They are implemented by defining a weighted distrib-  
ution to emulate when generating directions. 
  
  
#### StrategyHarness  

The StrategyHarness can be used to demonstrate the capability of a MazeSolver Preference. In this example, we test a  
Preference generated by least squares regression, and also another generated by a simple frequency distribution.  The  
maze is generated anew 100,000 times and we test the two solvers against a random solver, and compute p-values to  
measure the outcomes. 

```
treatment 1:
median solve time: 0.003
variance: 4.0E-5
std: 0.00618
mean solve time: 0.0037
p=0.0

treatment 2:
median solve time: 0.003
variance: 5.7E-4
std: 0.02393
mean solve time: 0.00416
p=0.0

null hypothesis (random):
median solve time: 0.005
variance: 3.0E-5
std: 0.00541
mean solve time: 0.00522
```

#### WeightedSelection
The weighted selection of Directions is performed by loading the weights as frequency counts into a red-black tree. We  
then grab a random, uniformly distributed double (between 0 and 1), multiply that by the total weight and use the red-black  
tree to quickly identify the correct bucket.

```
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

```
