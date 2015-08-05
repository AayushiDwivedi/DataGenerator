/*A simple transformer to replace macros in personalinfoStateMachine.xml
 * Replaces #{age}, #{income}, #{ms} with random integers
 */

package org.finra.datagenerator.samples.transformer;

import org.apache.log4j.Logger;

import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.DataTransformer;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import java.util.Map;
import java.util.Random;

public class PersonalInfo implements DataTransformer {

    private static final Logger log = Logger.getLogger(PersonalInfo.class);
    private final Random rand = new Random(System.currentTimeMillis());
    private Random gen = new Random(10000);
    private int rangeMin, rangeMax;

    public void transform(DataPipe cr) {
        Map<String, String> state = cr.getDataMap();
        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String value = entry.getValue();
            switch (value){
                case "#{age}":
                    //replace with a random integer within the range 15-100
                    rangeMax = 100;
                    rangeMin = 15;
                    int age = gen.nextInt(rangeMax-rangeMin)+ rangeMin;
                    entry.setValue(String.valueOf(age));
                    break;
                case "#{income}":
		    //replace with random integer within the range 100-10000
                    rangeMax = 10000;
                    rangeMin = 100;

                    int income = gen.nextInt(rangeMax-rangeMin)+ rangeMin;
                    entry.setValue(String.valueOf(income));
                    break;
                case "#{ms}":
		     //replace with either 1 or 0, indicating married and single respectively
                     rangeMax = 2;
                     rangeMin = 0;

                    int ms = gen.nextInt(rangeMax-rangeMin) + rangeMin;
                    entry.setValue(String.valueOf(ms));
                    break;

            }

        }
    }
}

