package SonR.Data;

import joinery.DataFrame;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import javax.script.ScriptException;
import java.util.*;

/**
 * Created by samferguson on 2/02/2016.
 */

public class Connect {

    public RConnection c;
    public DataFrame df;

    public Connect(String[] args, String dfName) throws RserveException, REXPMismatchException, ScriptException {
        c = new RConnection();
        load(dfName);
    }

    public DataFrame load(String datasetName) throws ScriptException, RserveException, REXPMismatchException {
    // load a DataFrame

        RList data = c.eval(datasetName).asList();
        //System.out.println(data.keyAt(0).toString());
        int colNums = data.size(); // numColumns is the length of the list.
        int colNum = 0;
        int rowNum = 0;
        List<List> cols = null;
        List<String> myNames = Arrays.asList(data.keys());
        Collection myCols = data.values();
        ListIterator<String> myNamesIt = myNames.listIterator();
        boolean debug = false;
        df = new DataFrame();
        while(myNamesIt.hasNext()) {
            String colName = myNamesIt.next();
            df.add(colName);
            if (debug) {
                System.out.println("Adding " + colName);
            }
        }

        Object lenCol = myCols.iterator().next();
        double[] len = ((REXPDouble) lenCol).asDoubles();
        int dfLength = len.length;

        for (int i = 0; i < dfLength; i++){

            // New row and columnIterator
            List<Object> row = new ArrayList<Object>();
            Iterator<Object> colIt = myCols.iterator();

            while(colIt.hasNext()) {
                if (debug){
                    System.out.println("hasNext " + i);
                }
                // get the next column
                Object col = colIt.next();
                if (col.getClass().toString().equals("class org.rosuda.REngine.REXPDouble")) {
                    double[] myDoubleCol = ((REXPDouble) col).asDoubles(); // extract the list
                    float[] myfloatCol = new float[myDoubleCol.length];
                    for (int j = 0; j < myDoubleCol.length; j++) {myfloatCol[j] = (float) myDoubleCol[j];}
                    row.add(myfloatCol[rowNum]);
                } else if ((col.getClass().toString().equals("class org.rosuda.REngine.REXPFactor"))) {
                    String[] myFactorCol = ((REXPFactor) col).asStrings();
                    row.add(myFactorCol[rowNum]);
                }
            }
            rowNum = rowNum + 1;
            df.append(row);
            if (debug) {
                System.out.println(row);
            }
        }
        return df;
    }

    public int numRows(){
        int dflen = this.df.length();
        return(dflen);
    }


}