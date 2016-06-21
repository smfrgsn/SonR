package SonR;

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
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

import javax.script.ScriptException;
import java.util.Set;

/**
 * Created by samferguson on 18/06/2016.
 */
public class SonifyDataFrameColumn {
    // A
    AudioIOManager AIO;


    public static void main(String[] args) throws REXPMismatchException, RserveException, ScriptException {

        // write code to sonify the data frame
        // instantiate the connection
        DataBead db = new DataBead();

        String dfName = "iris";
        String[] Rargs = {"--save"};
        Connect dc = new Connect(Rargs, dfName);

        // set up the traversal
        Traversal t = new Traversal(Traversal.TraversalMode.linear);

        // Create Mapping, choose data frame and load into mapping
        Mapping map = new Mapping(t, dc.df, dfName);
        Set<Object> cols = map.getColumns();
        map.setColumn((String) cols.toArray()[0]);




        // set Pitch Function
        map.setMappingFunction(x -> x);

        FrequencyModulationContinuousTone fm = new FrequencyModulationContinuousTone((Mapping) map);

        float pitch = map.getOutput();
        db.put("Frequency", 440.0f);
        db.put("Gain", -10.0f);
        db.put("Timbre", 0.5f);
        db.put("Shape", "Triangle");
        fm.sendData(db);


        // setup the audio synthesis
        AudioContext ac = new AudioContext();
        Gain g = new Gain(ac, 1, 0.1f);
        Envelope freqEnv = new Envelope(ac, 500);
        WavePlayer wp = new WavePlayer(ac, freqEnv, Buffer.SINE);
        g.addInput(wp);
        ac.out.addInput(g);
        ac.start();


        // setup the Listener in the Mapping
        map.addListener(new Mapping.mappingListener() {
            @Override
            public void getPointerPos(float dataPoint){
                // System.out.println("Data1 is" + dataPoint);
            }
            @Override
            public void setOutputValue(float dataPoint){
                System.out.println(dataPoint*200);
                freqEnv.setValue(dataPoint*200);
            }
        });



        // trigger the traversal
        t.traverseGraphSpace();

    }
}
