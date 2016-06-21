package SonR.Interaction;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by samferguson on 11/11/2015.
 */
public class Traversal {


    float pointerPos = 0f;
    float[] pointerRange = new float[2];
    long traverseTime = 0;
    float positionStep = 0.001f;
    float traversalPeriod = 120.0f;

    Timer traversalTimer;
    TraversalMode tm;



    public static abstract class traversalListener {

        // We set the pointer Pos
        public void setPointerPos(float pointerPos){};
        // We can also add other things here
        // such as progress to completion

    }

    public Set<traversalListener> listeners = new HashSet<traversalListener>();

    public void addListener(traversalListener listener) {
        listeners.add(listener);
    }

    public void removeListener(traversalListener listener) {
        listeners.remove(listener);
    }


    public enum TraversalMode{

        linear,
        sampling,
        interactive

    }

    public Traversal(TraversalMode tmIn){

        // set traversal mode from Enumeration
        tm = tmIn;

        // set Timer and Listener
        traversalTimer = new Timer();

    }

    public void fireListenerWithCurrentData(){
        // here we fire the listener

        //pass data on to listeners from other classes
        for(traversalListener listener : listeners) {
            traversalListener tListener = (traversalListener)listener;
            tListener.setPointerPos(pointerPos);
        }

    }

    public float getPointerPos() {
        // returns pointer Pos manually
        // use listener pattern and avoid this if possible.
        return pointerPos;
    }

    public void setPointerPos(float pointerPosInput) {

        // set variable to input value
        pointerPos = pointerPosInput;
    }

    public float[] getPointerRange() {
        // return Pointer Range (two numbers)

        return pointerRange;
    }

    public void setPointerRange(float[] pointerRangeInput) {
        // set Pointer Range (two numbers)
        pointerRange = pointerRangeInput;
    }

    public void setPositionStepInput(float positionStepInput){
        // set the speed at which the graph is traversed.
        positionStep = positionStepInput;
    }

    public void setTraversalPeriodInput(float traversalPeriodInput){
        // set the amount of time the traversal takes
        traversalPeriod = traversalPeriodInput;
    }

    public void traverseGraphSpace() {
        // Set up the traversal, with a timer that fires a listener

        TimerTask task; // a task to complete
        task = new TimerTask() { // encapsulate task
            @Override
            public void run() {
                if (pointerPos >= 0f &  pointerPos <= 1.0f ) {
                    pointerPos = pointerPos + positionStep; // add posStep to pointer Pos.
                    setPointerPos(pointerPos);  // this sets the data.
                    fireListenerWithCurrentData();// this fires the listener with the new data.
                } else {
                    traversalTimer.cancel(); // once you are done you are done.
                }
                System.out.println(pointerPos);
            }
        };
        traverseTime = (long) ((traversalPeriod * 1000) / (1 / positionStep)); // how long between traversal calls.
        traversalTimer.schedule(task, 0,  traverseTime); // turn on the timer.
    }

}
