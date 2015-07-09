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

package org.finra.datagenerator.samples.consumer;

import org.apache.hadoop.mapreduce.Mapper.Context;
import org.finra.datagenerator.consumer.DataConsumer;
import org.finra.datagenerator.reporting.ReportingHandler;
import org.finra.datagenerator.samples.manager.LineCountManager;
import org.finra.datagenerator.samples.transformer.SampleMachineTransformer;
import org.finra.datagenerator.samples.transformer.KmeansTransformer;
import org.finra.datagenerator.samples.transformer.StateMachineTransformer;
import org.finra.datagenerator.samples.transformer.ColumnTransformer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Sample consumer which uses messaging to a host to coordinate
 * a global stopping condition based on total line count
 */
public class SampleMachineConsumer extends DataConsumer {
    private long lastReportedLineCount, currentLineCount;
    private JenkinsReportingHandler handler;
    private AtomicBoolean exit;
    private long nextReport;
    private long reportGap;
   // private String[] template = new String[]{"var_out_V1", "var_out_V2", "var_out_V3",
   //         "var_out_V4", "var_out_V5", "var_out_V6",
   //         "var_out_V7", "var_out_V8", "var_out_V9"};
    
    private String[] template = new String[]{"userid", "left", "movieid", "rating", "dataset"};
    private String[] template2 = new String[]{"movieid", "right", "userid", "rating", "dataset"};

   // private String[] template = new String[]{"x1", "x2", "x3", "x4", "x5"};
//   private String[] template = new String[]{"x0","x1","x2","x3","x4","x5","x6","x7","x8","x9","x10","x11","x12","x13","x14","x15","x16","x17","x18","x19","x20","x21","x22","x23","x24","x25","x26","x27","x28","x29","x30","x31","x32","x33","x34","x35","x36","x37","x38","x39","x40","x41","x42","x43","x44","x45","x46","x47","x48","x49","x50","x51","x52","x53","x54","x55","x56","x57","x58","x59","x60","x61","x62","x63","x64","x65","x66","x67","x68","x69","x70","x71","x72","x73","x74","x75","x76","x77","x78","x79","x80","x81","x82","x83","x84","x85","x86","x87","x88","x89","x90","x91","x92","x93","x94","x95","x96","x97","x98","x99"};
    private long currentRow, finalRow;

    private class JenkinsReportingHandler implements ReportingHandler {
        private final AtomicBoolean exit;

        public JenkinsReportingHandler(final AtomicBoolean exit) {
            this.exit = exit;
        }

        public void handleResponse(String response) {
            if (response.contains("exit")) {
                exit.set(true);
            }
        }
    }

    private void setReportGap(long reportGap) {
        this.reportGap = reportGap;
    }

    /**
     * Constructor for SampleMachineConsumer - needs the Mapper Context
     *
     * @param context A Hadoop MapReduce Mapper.Context to which this consumer
     *                should writer
     */
    public SampleMachineConsumer(final Context context) {
        super();

        ContextWriter contextWrite = new ContextWriter(context, template);
        ContextWriter contextWrite2 = new ContextWriter(context, template2);

        this.addDataWriter(contextWrite);
	this.addDataWriter(contextWrite2);
        this.addDataTransformer(new StateMachineTransformer());
        //this.addDataTransformer(new KmeansTransformer());

        exit = new AtomicBoolean(false);
        handler = new JenkinsReportingHandler(exit);

        currentRow = -1;
        finalRow = -2;

        setReportGap(1000);
    }

    private void makeReport(boolean force) {
        long time = System.currentTimeMillis();
        if (time > nextReport || force) {
            long delta = currentLineCount - lastReportedLineCount;
            lastReportedLineCount = currentLineCount;

            sendRequest("this/report/" + String.valueOf(delta), handler);

            nextReport = time + reportGap;
        }
    }

    /**
     * Exit reporting up to distributor, using information gained from status reports to the LineCountManager
     *
     * @return a boolean of whether this consumer should immediately exit
     */
    public boolean isExit() {
        if (currentRow > finalRow) { //request new block of work
            String newBlock = this.sendRequestSync("this/request/block");
            LineCountManager.LineCountBlock block = new LineCountManager.LineCountBlock(0, 0);

            if (newBlock.contains("exit")) {
                getExitFlag().set(true);
                makeReport(true);
                return true;
            } else {
                block.buildFromResponse(newBlock);
            }

            currentRow = block.getStart();
            finalRow = block.getStop();
        } else { //report the number of lines written
            makeReport(false);

            if (exit.get()) {
                getExitFlag().set(true);
                return true;
            }
        }

        return false;
    }

    /**
     * Consume a data set (Map of Variable names and their Values)
     *
     * @param initialVars a map containing the initial variables assignments
     * @return Number of rows written; 0 rows if exiting 1 row otherwise
     */
    public int consume(Map<String, String> initialVars) {
        if (isExit()) {
            return 0;
        }

        currentLineCount++;
        currentRow++;
        return super.consume(initialVars);
    }
}
