/*
 * Copyright 2014 DataGenerator Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.finra.datagenerator.samples.transformer;

import org.apache.log4j.Logger;

import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.DataTransformer;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import java.util.Map;
import java.util.Random;


/**
 * A simple transformer replacing the reserved string "customplaceholder" with a random integer.
 */
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

    private String getAge() {
        if (Math.random() > 0.3) {
            return "YES";

        } else {
            return "NO";
        }
    }

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
