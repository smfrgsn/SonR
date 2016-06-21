package SonR.Testing;

import SonR.Audio.AudioIOManager;
import SonR.Audio.FrequencyModulationContinuousTone;
import SonR.Data.Connect;
import SonR.Data.Mapping;
import SonR.Interaction.Traversal;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;


/**
 * Created by samferguson on 1/05/2016.
 */
public class TestPipeline {



    @Before
    public void setUp() throws Exception {


    }

    public static void main(String[] args){

    }

    @Test
    public void evaluatesExpression() throws Exception {

        String dfName = "iris";
        String[]  agrs = {"--save"};
        Connect dc = new Connect(agrs, dfName);
        AudioIOManager AIO;
        Traversal t = new Traversal();
        Mapping map = new Mapping(t, dc.df, dfName);
        FrequencyModulationContinuousTone fm;
        DataBead db = new DataBead();

        AudioContext ac = new AudioContext();
        Gain g = new Gain(ac, 1, 0.1f);

        Envelope freqEnv = new Envelope(ac, 500);
        WavePlayer wp = new WavePlayer(ac, 200.0f, Buffer.SINE);
        g.addInput(wp);
        g.setGain(1.0f);
        ac.out.addInput(g);
        ac.start();
        ac.out.setGain(1.0f);



        dc.load(dfName);


        Set<Object> cols = map.getColumns();
        map.setColumn((String) cols.toArray()[0]);


        // setup the Traversal Listener
        map.addListener(new Mapping.mappingListener() {
            @Override
            public void getPointerPos(float pointerPos){
                map.setPointerPos(pointerPos);
                System.out.println("Pointer pos is" + pointerPos);
            }
//            @Override
//            public void getPointerRange(float pointerRange){
//                // range code goes here
//                System.out.println("Pointer range is" + pointerRange);
//            }

        });

        // use the traversal data to reference the correct part of the data
        map.setPointerPos(0.5f);

        // set Pitch Function
        map.setMappingFunction(x -> x);

        // update
        //map.update();

        fm = new FrequencyModulationContinuousTone((Mapping) map);

        // get the output data by calling the method that finds the data column in the data frame, looks for the
        // appropriate data point in the data column based on the pointer and then applies the function to that
        // datapoint
        float pitch = map.getOutput();


        db.put("Frequency", 440.0f);
        db.put("Gain", -10.0f);
        db.put("Timbre", 0.5f);
        db.put("Shape", "Triangle");
        fm.sendData(db);



    }

}
