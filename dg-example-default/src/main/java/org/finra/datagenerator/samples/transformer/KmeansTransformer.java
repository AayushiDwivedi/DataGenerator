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
public class KmeansTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(KmeansTransformer.class);
    private Random gen = new Random(1000);
    private double rangeMax, rangeMin, mean, variance;
    private double m1, m2, m3,m4,m5;
    /**
     * The transform method for this DataTransformer
     *
     * @param cr a reference to DataPipe from which to read the current map
     */
    public void transform(DataPipe cr) {

        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String value = entry.getValue();

            switch (value) {
                case "#{feat1}":
                    // Generate a random number
                    //int ran = rand.nextInt();
                    rangeMax = 75.00000;
                    rangeMin = 55.000000;
                    mean = 45.0000;
                    variance = (rangeMax - rangeMin) * gen.nextDouble() + rangeMin-mean;
                    double f1 = mean + (gen.nextGaussian() * Math.sqrt(Math.abs(variance)) + mean);
                    entry.setValue(String.valueOf(f1));
                    break;
                case "#{feat2}":
                    // Generate a random number
                    //int ran = rand.nextInt();
                    rangeMax = 20.00000;
                    rangeMin = -7.000000;
                    mean = -0.100;
                    variance = (rangeMax - rangeMin) * gen.nextDouble() + rangeMin-mean;
                    double f2 = mean + (gen.nextGaussian() * Math.sqrt(Math.abs(variance)) + mean);
                    entry.setValue(String.valueOf(f2));
                    break;
                case "#{feat3}":
                    rangeMax = 60.00000;
                    rangeMin = 15.000000;
                    mean = 45.0000;
                    variance = (rangeMax - rangeMin) * gen.nextDouble() + rangeMin - mean;
                    double f3 = mean + (gen.nextGaussian() * Math.sqrt(Math.abs(variance)) + mean);
                    entry.setValue(String.valueOf(f3));
                    break;

                case "#{feat4}":
                    rangeMax = 100.00000;
                    rangeMin = -7.000000;
                    mean = -1.0;
                    variance = (rangeMax - rangeMin) * gen.nextDouble() + rangeMin - mean;
                    double f4 = mean + (gen.nextGaussian() * Math.sqrt(Math.abs(variance)) + mean);
                    entry.setValue(String.valueOf(f4));
                    break;

                case "#{feat5}":
                    rangeMax = 80.00;
                    rangeMin = 7.000000;
                    mean = 10.00;
                    variance = (rangeMax - rangeMin) * gen.nextDouble() + rangeMin-mean;
                    double f5 = mean + (gen.nextGaussian() * Math.sqrt(Math.abs(variance)) + mean);
                    entry.setValue(String.valueOf(f5));
                    break;
            }
        }
    }
}




