package org.finra.datagenerator.samples.transformer;
import org.apache.log4j.Logger;
import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.DataTransformer;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import java.util.Map;
import java.util.Random;


/*A multipurpose transformer.
 * Can be used to tranform:
 * movieStateMachine.xml: #{movieID},#{rating}, #{dataset}
 * logisticRegression.xml: #{pos}, #{neg}
 */
public class StateMachineTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(StateMachineTransformer.class);
    private final Random rand = new Random(System.currentTimeMillis());
    private Random gen = new Random(System.currentTimeMillis());
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


