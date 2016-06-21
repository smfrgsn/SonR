package SonR.Data;

import SonR.Interaction.Traversal;
import joinery.DataFrame;
import net.beadsproject.beads.core.UGen;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/** This class is the core Mapping class and does a lot of the work.
 * Created by samferguson on 10/11/2015.
 *
 * TODO It will need some better organisation to strip out the different parts as classes
 * For instance a MappingFunction class would be good.
 * A
 *
 * TODO Better more consistent naming
 *
 */
public class Mapping {

    Hashtable<String, outputTypes> output;


    variableTypes inputType;
    outputTypes outputType;

    MappingFunction mapFunc;

    DataFrame df;           // This is obtained from R via Rserve library
    String dfName;          // Name for the dfName

    Traversal t;            // Traversal is
    float pointerPos = 0;   //


    boolean debug = false;
    Function mappingFunc;
    private String colname;

    public void setPointerPos(float pointerPosition) {
        pointerPos = pointerPosition;
    }

    /* A listener abstract class inside mapping it can be instantiated
    * in anything that wants to listen to a change in the data that is mapped
    * such as the audio generators or anything else. */

    public static abstract class mappingListener {

        public void setOutputValue(float outputValue) {};

    }

    public Set<mappingListener> listeners = new HashSet<mappingListener>();

    public void addListener(mappingListener listener) {
        listeners.add(listener);
    }

    public void removeListener(mappingListener listener) {
        listeners.remove(listener);
    }


    public enum variableTypes {
        // These are the types of statistical data, inherited directly from the DataFrame
        // TODO - these variableTypes should be in the data frame then.
        String, Numeric, Factor, Date
    }

    public enum perceptTypes{
        PITCH, GAIN
    }

    public enum outputTypes {
        // This is a list of outputTypes that are queried by the output object or any other object implementing the
        // interface correctly, when it wants to set up the mapping, so an output needs to have an outputType
        FREQUENCY, GAIN
    }


    public Mapping(Traversal tIn, DataFrame dfIn, String dfNameIn){
        // Create a Mapping
        //
        // connect the listener to the traversal for control
        t = tIn; // get the Traversal
        t.addListener(tListen); // add the listener

        // note that the dataframe is a reference, so it is read only
        // from the DataFrame you can read out the names of the columns
        //
        // This requires:
        // df -- the DataFrame
        // DFName -- the name for the data frame
        //
        df = dfIn; // save as a global DataFrame
        dfName = dfNameIn; // set the name

    }

    Traversal.traversalListener tListen = new Traversal.traversalListener() {
        // This is the listener that links to the Traversal.
        // Add code here that you want to trigger when the traversal point
        // Changes
        @Override
        public void setPointerPos(float pointerPosInput) {
            pointerPos = pointerPosInput;
            float outputValue = getOutput();

            if (debug) {
                System.out.println("the Mapping listener heard that the pointer Pos is now" + pointerPos);
                System.out.println("outputValue is " + outputValue);
            }
            update(); // main thing here is to update and fire the mapping listener
        }
    };


    public void update(){
        // here we fire the mapping listener, which is usually located in the main() function
        // to pass the output data on to listeners
        for(mappingListener listener : listeners) {
            mappingListener mapListener = (mappingListener)listener;
            mapListener.setOutputValue(this.getOutput());
        }
    }

    public void setColumn(String nameOfColumn){
        // This is where we set the name of the column we are interested in.
        colname = nameOfColumn;
        if (debug) {System.out.println(df.col(colname));}
    }

    public Set getColumns() {
        // return the Set of columns from the dataframe
        return df.columns();
    }

     public void getOutputs(){

        // Find out what outputs are available by querying the output object.
        // Eg look at an FM synthesizer and get the parameters of control, along with their ranges

    }

    public float getOutput(){
        // This is the main hook to the mapping.

        // Calculate the rowNumber
        int rowIndex = (int) Math.floor(pointerPos * df.col(0).toArray().length);
        // Retrieve the data value from the data frame
        float out =  (float) df.col(colname).get(rowIndex);
        // Apply the mapping function
        out = (float) mappingFunc.apply(out);

        if (debug) {
            System.out.println("pointerPos" + pointerPos + " size:" + df.col(0).toArray().length + " rowIndex: " + rowIndex + " out: " + out);
        }
        return out; // return float
    }


    public void setMappingFunction(Function func) {
        // This mapping function connects the input data to the
        // the output audio sonification
        // Usually this mapping is completely linear
        // The OTHER mapping at the output audio sonification
        // will convert linear data into logarithmic pitch (for instance)
        // some data is already logarithmic, and this mapping can be applied
        // if that is the case to linearise the data for the output audio sonification
        mappingFunc = func;
    }

    public void setMappingFunction(perceptTypes perceptType) {
        // the function is separate to the input and output
        // but here we assume the range

        switch (perceptType){
            case PITCH:
                //map range to midi note numbers
                float rangeMidpoint = 57;
                float rangeOut = 24;
                // range In
                float offsetIn = (float) df.describe().get("min",colname);
                float rangeIn = (float) df.describe().get("max",colname) - (float) df.describe().get("min",colname);
                float offsetOut = rangeMidpoint - rangeOut/2;

                Function outFunc = (inValue) -> (((((float) inValue) - offsetIn) / rangeIn) * rangeOut) + offsetOut;
                mappingFunc = outFunc;


            case GAIN:

            default:

        }
    }




    public List<UGen> outputParameters(){
        // Create a list of UGens
        return null;
    }

    public void printMapping(){

        // The Mapping takes,
        // the input with the df, colname pointer and pointerRange
        // the traversal t,
        // the Output destination

        String dfString = String.format("*** Mapping *** \nDataFrame: %s \n" +
                "Shape: %d rows and %d columns \nNames: %s",
                dfName,df.col(0).size() ,df.size(),df.columns().toString());

        String selString = String.format("Chosen column: %s",colname);
        System.out.println(dfString + "\n" + selString);

    }

    public String debug(){

        return null;

    }


}
