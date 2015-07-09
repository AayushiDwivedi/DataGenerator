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
public class StateMachineTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(StateMachineTransformer.class);
    private final Random rand = new Random(System.currentTimeMillis());
    private Random gen = new Random(1000);
    private double rangeMin, rangeMax;

    /**
     * The transform method for this DataTransformer
     * @param cr a reference to DataPipe from which to read the current map
     */
    public void transform(DataPipe cr) {
        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String value = entry.getValue();
            if (value.equals("#{pos}")) {
                // Generate a random number
                int max = 100;
                int min = 0;
                int ranVar = gen.nextInt((max - min) + 1) + min;
                //long val = 1000;
                entry.setValue(String.valueOf(ranVar));
                //entry.setValue(String.valueOf(1));
            }
            else if (value.equals("#{neg}")) {
                // Generate a random number
                int max = 0;
                int min = -100;
                int ranVar = gen.nextInt((max - min) + 1) + min;
                //long val = 1000;
                entry.setValue(String.valueOf(ranVar));
                //entry.setValue(String.valueOf(1));
            }
            else if (value.equals("#{plabel}")) {
                entry.setValue(String.valueOf(0));
            }
            else if (value.equals("#{nlabel}")) {
                entry.setValue(String.valueOf(1));
            }

            else if (value.equals("#{unicode}")) {
                int max = 5000;
                int min = 0;
                int ranVar = gen.nextInt((max - min) + 1) + min;
                if(Character.isDefined(ranVar)){
                    entry.setValue(new String(Character.toChars(ranVar)));
                }

                }
            else if (value.equals("#{feat1}")) {
                // Generate a random number
                //int ran = rand.nextInt();
                rangeMax = 75.00000;
                rangeMin = 55.000000;
                //double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble()
                double f1 = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
                entry.setValue(String.valueOf(f1));
                }
            else if (value.equals("#{feat2}")) {
                // Generate a random number
                //int ran = rand.nextInt();
                rangeMax = 20.00000;
                rangeMin = -7.000000;
                //double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble()
                double f2 = rangeMin+ (rangeMax - rangeMin)* rand.nextDouble();
                entry.setValue(String.valueOf(f2));
            }
            else if (value.equals("#{feat3}")) {
                // Generate a random number
                //int ran = rand.nextInt();
                rangeMax = 60.00000;
                rangeMin = 15.000000;
                //double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble()
                double f3 = rangeMin+ (rangeMax - rangeMin)* rand.nextDouble();
                entry.setValue(String.valueOf(f3));
            }
            else if (value.equals("#{feat4}")) {
                // Generate a random number
                //int ran = rand.nextInt();
                rangeMax = 100.00000;
                rangeMin = -7.000000;
                //double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble()
                double f4 = rangeMin+ (rangeMax - rangeMin)* rand.nextDouble();
                rangeMin = -7.000000;
                //double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble()
                double f5 = rangeMin+ (rangeMax - rangeMin)* rand.nextDouble();
                entry.setValue(String.valueOf(f5));
            }
            else if (value.equals("#{feat6}")) {
                // Generate a random number
                //int ran = rand.nextInt();

                double n;
                int maxN = 100;
                int range = 1 << (maxN);
                rangeMax = 1000000;
                rangeMin = 1;
                //double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble()

                double f6 = rangeMin+ (rangeMax - rangeMin)* rand.nextDouble();
                n = Math.floor(Math.log10((f6 * range) + 1)/Math.log(2));
                entry.setValue(String.valueOf(n));
            }
            else if (value.equals("#{gaussian}")) {
                // Generate a random number
                //int ran = rand.nextInt();

                double g = rand.nextGaussian()+3;  //rand.nextGaussian()*sd +mean
                entry.setValue(String.valueOf(g));
            }
            else if (value.equals("#{logNormal}")) {
                // Generate a random number
                rangeMin = 0.0;
                rangeMax = 10;
                double ranVar = rangeMin+ (rangeMax - rangeMin)* rand.nextDouble();

                LogNormalDistribution ln= new LogNormalDistribution(0,10);
                entry.setValue(String.valueOf(ln.cumulativeProbability(ranVar)));
            }
            else if (value.equals("#{zipf}")) {
                // Generate a random number
                int ranVar = gen.nextInt(10);
                double exponent = 2;
                int noOfelements = 1000;
                ZipfDistribution zip= new ZipfDistribution(noOfelements,exponent);
                entry.setValue(String.valueOf(zip.cumulativeProbability(ranVar)));

            }

            else if (value.equals("#{rating}")) {
                // Generate a random number
                int ranVar = gen.nextInt(5) + 1;
                entry.setValue(String.valueOf(ranVar));

            }
            else if (value.equals("#{movieID}")) {
                // Generate a random number
                int max = 0;
                int min = -10000;
                int ranVar = gen.nextInt(max - min) + min;
                entry.setValue(String.valueOf(ranVar));

            }
            else if (value.equals("#{dataset}")) {
                // Generate a random number
                rangeMax = 0.0;
                rangeMin = 1.0;
                double ranVar = rangeMin + (rangeMax - rangeMin)* rand.nextDouble();
                // 30% of the data is for validation and the rest is for training
                if(ranVar < 0.3){
                    entry.setValue(String.valueOf("va"));
                }
                else{
                    entry.setValue(String.valueOf("tr"));

                }


            }

        }
    }

}


