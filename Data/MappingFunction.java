package SonR.Data;

import java.util.function.Function;

/**
 * MAPPINGFUNCTION
 *
 * This class holds all the function types that can be used for mapping. Output is always 0-1 in floats.
 *
 * Created by samferguson on 10/05/2016.
 */
public class MappingFunction {


    Function mappingFunction;
    float[] inputRange;
    float[] outputRange;
    public MappingFunction(){


    }


    public enum functionTypes{
        linear,     //
        exponential,
        sigmoid,
        steps,
        sine,
        cosine
    }

    public void setFunction(functionTypes functionType){
        switch (functionType) {
            case linear:

                Function outFunc = (inValue) -> (((((float) inValue) - inputRange[0]) / (inputRange[1] - inputRange[0])) * (outputRange[1] - outputRange[0])) + outputRange[0];
                mappingFunction = outFunc;

            case exponential:
            break;
        }

    }

    public void setFloatRanges(float[] input, float[] output){

        inputRange = input;
        outputRange = output;

    }

    public Function getFunction(){

        return mappingFunction;

    }


}
