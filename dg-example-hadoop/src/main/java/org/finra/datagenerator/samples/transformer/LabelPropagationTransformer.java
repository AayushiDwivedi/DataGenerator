package org.finra.datagenerator.samples.transformer;

import org.apache.log4j.Logger;

import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.DataTransformer;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class LabelPropagationTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger(LabelPropagationTransformer.class);
    private final Random rand = new Random(System.currentTimeMillis());
    private Random gen = new Random(100000000);
    private float label[] = {0, 0, 0};

    public void getLabels() {

        float prob = gen.nextFloat();
        if (prob < 0.1) {

            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    label[i] = gen.nextFloat();
                } else {
                    int sum = 0;

                    for (float index : label) {
                        sum += index;
                    }

                    if (sum == 1) {
                        label[i] = 0;
                    } else {
                        float temp = gen.nextFloat();
                        while (temp + sum > 1) {
                            temp = gen.nextFloat();
                        }
                        label[i] = temp;
                    }
                }

            }
	 }
	 else {
        	    for (int i = 0; i < 3; i++) {
                	label[i] = -1;
            		}
        	}
    }
    public void transform(DataPipe cr) {
        Map<String, String> state = cr.getDataMap();
        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String value = entry.getValue();
            String rowno = state.get("rows");
            double rowa = Double.parseDouble(rowno);
            int row = (int) rowa;
            String temp = state.get("total");
            double temp1 = Double.parseDouble(temp);
            int total = (int) temp1;
            switch (value) {
                case "#{dest1}":
                    int lower = row + 1;
                    int upper = row + 5;
                    if(lower > total || upper > total){
                        lower = 1+gen.nextInt(total/2);
                        upper = lower + 6;
                    }
                    int dest1 = lower + gen.nextInt(upper - lower);
                    entry.setValue(String.valueOf(dest1));
                    break;
                case "#{dest2}":
                    lower = row + 5;
                    upper = row + 8;
                    if(lower > total || upper > total){
                        lower = 1+gen.nextInt(total/2);
                        upper = lower + 6;
                    }
                    int dest2 = lower + gen.nextInt(upper - lower);

                    entry.setValue(String.valueOf(dest2));
                    break;
                case "#{weight1}":
                    entry.setValue(String.valueOf(gen.nextFloat()));
                    break;
                case "#{weight2}":
                    entry.setValue(String.valueOf(gen.nextFloat()));
                    break;
                case "#{label1}":
                    getLabels();
                    entry.setValue(String.valueOf(Arrays.toString(label)));
                    break;
                case "#{label2}":
                    getLabels();
                    entry.setValue(String.valueOf(Arrays.toString(label)));
                    break;
            }
        }
    }
}

