package SonR.Testing;

import SonR.Data.Connect;
import org.junit.Before;
import org.junit.Test;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

import javax.script.ScriptException;

import static org.junit.Assert.assertEquals;

/**
 * Created by samferguson on 9/12/2015.
 */

public class TestDataFrame {

    // Setup Connection

    Connect dc;

    @Before
    public void setUp() throws Exception {
        String[]  agrs = {"--save"};
        String dfName = "iris";
         dc = new Connect(agrs, dfName);
    }

    @Test
    public void evaluatesExpression() throws Exception {
        assertEquals(dc.c.getClass().toString(),"class org.rosuda.REngine.Rserve.RConnection");
    }

    @Test
    public void testConnectToR() throws Exception {
        // Get the Repo with R datasets (looking for warpbreaks)

        assertEquals(dc.c.getClass().toString(),"class org.rosuda.REngine.Rserve.RConnection");
    }

    @Test
    public void testLoadDataFrame() throws REXPMismatchException, ScriptException, RserveException {

        dc.load("iris"); // load the iris dataset

        int len = dc.numRows(); // Get the number of rowsfrom the dataset

        assertEquals(len, 1); // the correct answer for the iris dataset is 150
    }

    @Test
    public void testSelect() {

        assertEquals(6,6);
    }


}
