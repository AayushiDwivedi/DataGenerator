/**
 * A simple transformer for multiLRStateMachine.xml
 * Models data representing whether a patient is diabetic
 * based on her blood sugar level.
 * Classifications: NON-DIABETIC, TYPE1, TYPE2, CHILD w/ TYPE1, UNKOWN
 * Ranges for blood sugar level have been taken from a standard chart
 * for diabeties.
 */

package org.finra.datagenerator.samples.transformer;

import org.apache.log4j.Logger;

import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.DataTransformer;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import java.util.Map;
import java.util.Random;
public class MultinomialLRTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(MultinomialLRTransformer.class);
    private final Random rand = new Random(System.currentTimeMillis());
    private Random gen = new Random(1000);
    private double rangeMin, rangeMax;

    /**
     * The transform method for this DataTransformer
     *
     * @param cr a reference to DataPipe from which to read the current map
     */
    public void transform(DataPipe cr) {
        Map<String, String> state = cr.getDataMap();
        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String key = entry.getKey();
            // System.out.println("Printing key:"+ key);

            String value = entry.getValue();
            // System.out.println("Printing value:"+ value);
            switch (value) {
                case "#{beforemeal}":
                    rangeMax = 8.0;
                    rangeMin = 4.0;
                    double randVar = (rangeMax - rangeMin) * gen.nextDouble() + rangeMin;
                    entry.setValue(String.valueOf(randVar));
                    break;
                case "#{aftermeal}":
                    rangeMax = 10.0;
                    rangeMin = 6.0;
                    randVar = (rangeMax - rangeMin) * gen.nextDouble() + rangeMin;
                    entry.setValue(String.valueOf(randVar));
                    break;
                case "#{is}":
                    String a = getAge();
                    entry.setValue(a);
                    break;
                case "#{label}":
                    double beforemeal = Double.parseDouble(state.get("beforemeal"));
                    double aftermeal = Double.parseDouble(state.get("aftermeal"));
                    String isAdult = state.get("is");
                    entry.setValue(hasDiabetes(beforemeal, aftermeal, isAdult));
                    break;


            }
        }
    }

    /**Returns YES with probability greater than 0.3 indicating that the patient is an ADULT
     Returns NO with a probability less than 0.3 indicating that the patient is a CHILD
     */

    private String getAge() {
        if (Math.random() > 0.3) {
            return "YES";

        } else {
            return "NO";
        }
    }
    /**
     * Depending on the blood sugar levels before and after meal and age of the patient,
     * indicates type of label for the data: NON-DIABETIC, TYPE1, TYPE2, CHILD w/ TYPE1, UNKOWN
     */


    private String hasDiabetes(double beforemeal, double aftermeal, String isAdult) {
        if (isAdult.equals("YES")) {
            if (beforemeal <= 5.9 && aftermeal <= 7.8) {
                return "NON-DIABETIC";
            } else if (beforemeal <= 8 && aftermeal <= 8.5) {
                return "TYPE2";
            } else if (beforemeal <= 8 && aftermeal <= 10) {
                return "TYPE1";
            }
        } else if(isAdult.equals("NO")){
            if (beforemeal <= 8 && aftermeal <= 10) {
                return "CHILD w/ TYPE1";
            } else {
                return "NON-DIABETIC";
            }
        }
        return "UNKOWN";
    }
}
