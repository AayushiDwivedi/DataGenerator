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
public class MultivarLRTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(MultivarLRTransformer.class);
    private final Random rand = new Random(System.currentTimeMillis());
    private Random gen = new Random(1000);
    private int rangeMin, rangeMax;

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
            switch (value){
                case "#{age}":
                    rangeMax = 80;
                    rangeMin = 18;
                    int randVar = gen.nextInt(rangeMax-rangeMin)+rangeMin;
                    entry.setValue(String.valueOf(randVar));
                    break;
                case "#{income}":
                    long rangeMax = 600000L;
                    long rangeMin = 10000L;
                    long rand= rangeMin + (long)(Math.random() *(rangeMax -rangeMin));
                    entry.setValue(String.valueOf(rand));
                    break;
                case "#{maritalstatus}":
                    entry.setValue(getMaritalStatus());
                    break;
                case "#{label}":
                    int age = Integer.parseInt(state.get("age"));
                    long income = Long.parseLong(state.get("income"));
                    String maritalstatus = state.get("maritalstatus");
                    if((age <=28 && income >= 30000 ) ||(income >= 20000&& maritalstatus.equals("YES"))){
                        entry.setValue(String.valueOf("1"));

                    }
                    else{
                        entry.setValue(String.valueOf("0"));

                    }
                    break;


            }
        }
    }

    private String getMaritalStatus()
    {
        if(Math.random() < 0.5){
            return "NO";

        }
        else{
            return "YES";
        }
    }
}