
package org.finra.datagenerator.samples.transformer;

import org.apache.log4j.Logger;

import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.DataTransformer;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import java.util.Map;
import java.util.Random;


/**
 *  * A simple transformer replacing the reserved string "customplaceholder" with a random integer.
 *   */
public class KmeansTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(KmeansTransformer.class);
    private Random gen = new Random(1000);
    private double rangeMax, rangeMin;
    private double c1, c2, c3,c4,c5, noise;
    /**
 *      * The transform method for this DataTransformer
 *           *
 *                * @param cr a reference to DataPipe from which to read the current map
 *                     */
    public void setKmean(String centroid){
        switch(centroid){
            case "Vec0":
                c1 = 45.00;
                c2 = -0.10;
                c3 = 45.00;
                c4 = -1.00;
                c5 = 10.00;
                rangeMax = 100;
                rangeMin = -10;
                break;
            case "Vec1":
                c1 = 100.00;
                c2 = 50.10;
                c3 = -50.00;
                c4 = 90.00;
                c5 = 110.00;
                rangeMax = 150;
                rangeMin = -70;
                break;
            case "Vec2":
                c1 = -110.00;
                c2 = -500.10;
                c3 = -60.00;
                c4 = -3.00;
                c5 = 70.00;
                rangeMax =90;
                rangeMin = -600;
                break;
            case "Vec3":
                c1 = 1.00;
                c2 = 0.10;
                c3 = 2.00;
                c4 = 1.00;
                c5 = 1.00;
                rangeMax = 5;
                rangeMin = -1;
                break;
            case "Vec4":
                c1 = 900.00;
                c2 = 500.00;
                c3 = 200.00;
                c4 = 400.00;
                c5 = 300.00;
                rangeMax = 1000;
                rangeMin = 150;
                break;
        }
    }

    public double addNoise(double centroid)
    {   double f_sigma = 0.1, f_mu = 0.1, sigma,mu;
        /*Noise is assumed to be proportional to the size of the centroid.
 *         i.e., if x cord of centroid is large, the noise along x-axis is also large.
 *                 This can be changed as required
 *                          */
        noise = gen.nextGaussian();
        sigma = f_sigma * centroid;
        mu = f_mu * centroid;
        noise = noise * sigma + mu; //standard normal to normal
        centroid = centroid + noise;
        return centroid;
    } 
    
   public void transform(DataPipe cr) {
        Map<String, String> state = cr.getDataMap();
        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String value = entry.getValue();
            String whichCentroid = state.get("centroids");
            setKmean(whichCentroid);
            switch (value) {
                case "#{feat1}":
                    entry.setValue(String.valueOf(addNoise(c1)));
                    break;
                case "#{feat2}":
                    entry.setValue(String.valueOf(addNoise(c2)));
                    break;
                case "#{feat3}":
                    entry.setValue(String.valueOf(addNoise(c3)));
                    break;

                case "#{feat4}":
                    entry.setValue(String.valueOf(addNoise(c4)));
                    break;

                case "#{feat5}":
                    entry.setValue(String.valueOf(addNoise(c5)));
                    break;
            }
        }
    }
}

