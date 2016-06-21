package SonR.Testing;

import SonR.Data.Connect;
import SonR.Data.Mapping;
import SonR.Data.Mapping.mappingListener;
import SonR.Interaction.Traversal;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by samferguson on 1/05/2016.
 */
public class TestMapping {

    Connect dc;
    String dfName = "iris";
    Mapping map;

    @Before
    public void setUp() throws Exception {

        String[]  agrs = {"--save"};
        dc = new Connect(agrs, dfName);
        dc.load(dfName);
        Traversal t = new Traversal();
        map = new Mapping(t, dc.df, dfName);

    }

    @Test
    public void evaluatesExpression() throws Exception {

        // connect up the mapping with the data
        // setup some data from the iris dataset

        Set<Object> cols = map.getColumns();
        map.setColumn((String) cols.toArray()[0]);


        // setup the Traversal Listener
        map.addListener(new mappingListener() {
            @Override
            public void getPointerPos(float pointerPos){
                map.setPointerPos(pointerPos);
                System.out.println("Pointer pos is" + pointerPos);
            }
//            @Override
//            public void getPointerRange(float pointerRange){
//                System.out.println("Pointer range is" + pointerRange);
//            }

        });

        // use the traversal data to reference the correct part of the data
        map.setPointerPos(0.5f);

        // setup the Mapping from data to Pitch

        // set Pitch Function
        map.setMappingFunction(x -> x);

        // update
        //map.update();


        // get the output data by calling the method that finds the data column in the data frame, looks for the
        // appropriate data point in the data column based on the pointer and then applies the function to that
        // datapoint
        float pitch = map.getOutput();

        // check the results
        assertEquals(5.09, pitch, 0.1);

    }

    @Test
    public void testMappingToPitch() {


    }

}
