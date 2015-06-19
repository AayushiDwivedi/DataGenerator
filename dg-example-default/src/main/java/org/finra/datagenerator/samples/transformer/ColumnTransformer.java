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
public class ColumnTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(ColumnTransformer.class);
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
            String value = entry.getValue();
            if (value.startsWith("#")) {
                String macro;
                String expr;

                int param = value.indexOf("{");
                if (param != -1) {
                    macro = value.substring(1, param);
                    expr = value.substring(param + 1, value.length() - 1);
                } else {
                    macro = value.substring(1);
                    expr = "";
                }
                //System.out.println("macro:"+macro+" expr:"+expr);
                for(int i= 0; i<=100 ; i++){
                    if(i<10 && i == Integer.parseInt(expr)){
                        entry.setValue(String.valueOf(Math.random()));
                        break;
                    }
                    else if (i>=10 && i<30 && i==Integer.parseInt(expr)){
                        if(i%2==0){
                                entry.setValue(getRandomString(gen.nextInt(10-4)+4));

                        }
                        else{
                            entry.setValue(String.valueOf(gen.nextBoolean()));

                        }
                    }
                    else if (i>=30 && i<50 && i==Integer.parseInt(expr)){
                        entry.setValue(String.valueOf((100.0 - 30.0) * gen.nextDouble() + 30.0));
                    }
                    else if (i>=50 && i<=100 && i==Integer.parseInt(expr)){
                        int label = gen.nextInt(2);
                        entry.setValue(String.valueOf(label));
                    }
                }
            }
        }
    }
    public String getRandomString(int len){
        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWZYZ";
        final int alphaLen= alphabet.length();
        StringBuilder str = new StringBuilder(len);
        for(int i=0; i<len; i++){
             str.append(alphabet.charAt(gen.nextInt(alphaLen)));
        }
        return str.toString();
    }
}
