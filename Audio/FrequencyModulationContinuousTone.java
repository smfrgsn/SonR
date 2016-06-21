package SonR.Audio;

import SonR.Data.Mapping;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.DataBeadReceiver;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

import java.util.List;

/**
 * FREQUENCYMODULATION is a class that sets up an FM synth to map data to.
 *
 *
 * Created by samferguson on 5/05/2016.
 */
public class FrequencyModulationContinuousTone extends PMSonify implements DataBeadReceiver{

    WavePlayer freqModulator;
    Function function ;
    WavePlayer wp;
    Gain g;

    public FrequencyModulationContinuousTone(Mapping map){
        super(map);
//        g = new Gain(AIO.ac, 1, 0.1f);
//        freqModulator = new WavePlayer(AIO.ac, 500, Buffer.SINE);
//        function = new Function(freqModulator) {
//            public float calculate() {return x[0] * 100.0f + 600.0f;
//            }
//        };
//        wp =  new WavePlayer(AIO.ac, function, Buffer.SINE);;
//        g.addInput(wp);
//
//        AIO.addToGraph(g);
    }


    public DataBeadReceiver sendData(DataBead db){
        // Read the parameters
        for (int i = 0; i < db.values().size(); i++) {
            System.out.println("key: " + db.keySet().toArray()[i].toString() + " value: " + db.values().toArray()[i].toString());
        }
        return this;
    }


    // Create UGen Glide from Mapping.
    public void interpolateMapping(float period){
        // interpolation over update period
        // interpolation over specified period
        // sample and hold

    }

    public void setAestheticMapping(DataBead aestheticMapping, List[] FM){

        /*
        * The aesthetic is the way in which each of the parameters maps to the parameters of the FM Synth
        *
        * Some examples --
        * Fundamental Frequency - Carrier Frequency
        * Loudness - Gain
        * Timbre - Modulation Index.
        *
        *
        *
        * Deal out the parameters in the DataBead to a Glide.
        * Attach each of the input Glides to a listener
        *
        * */

    }

    public void update(){

        // Every so often we want to update

        // Trigger the listeners to update each Glide

    }

}
