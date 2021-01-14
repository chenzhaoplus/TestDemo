package com.smcaiot.bigdata.demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.util.Collector;
import org.apache.flink.util.Preconditions;

public class WordCount {

//    private static Logger LOG = LoggerFactory.getLogger(Kafka2Hdfs.class);

//    public static void main(String[] args) throws Exception {
//        try {
//            if(args!=null && args.length>0){
//                for (int i = 0; i < args.length; i++) {
//                    System.out.println("args"+i+" = "+args[i]);
//                }
//            }
//
//            final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//            DataStream<String> text = null;
//            System.out.println("Executing WordCount example with default input data set.");
//            System.out.println("Use --input to specify file input.");
//            // get default test text data
//            text = env.fromElements(WordCountData.WORDS);
//
//            DataStream<Tuple2<String, Integer>> counts =
//                    // split up the lines in pairs (2-tuples) containing: (word,1)
//                    text.flatMap(new Tokenizer())
//                            // group by the tuple field "0" and sum up tuple field "1"
//                            .keyBy(value -> value.f0)
//                            .sum(1);
//
//            System.out.println("Printing result to stdout. Use --output to specify output path.");
////        counts.print();
//            counts.writeAsText("/ops/app/flinkexecutordemo//hello-output.log");
//
//            env.execute("Streaming WordCount");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("----------- hello begin ------------------");
            if(args!=null && args.length>0){
                for (int i = 0; i < args.length; i++) {
                    System.out.println("args"+i+" = "+args[i]);
                }
            }

            System.out.println("----------- MultipleParameterTool.fromArgs begin ------------------");
            final MultipleParameterTool params = MultipleParameterTool.fromArgs(args);

            // set up the execution environment
            System.out.println("----------- ExecutionEnvironment.getExecutionEnvironment begin ------------------");
            final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

            // make parameters available in the web interface
            env.getConfig().setGlobalJobParameters(params);

            // get input data
            DataSet<String> text = null;
            if (params.has("input")) {
                System.out.println("get input "+params.get("input"));
                // union all the inputs from text files
                for (String input : params.getMultiParameterRequired("input")) {
                    if (text == null) {
                        text = env.readTextFile(input);
                    } else {
                        text = text.union(env.readTextFile(input));
                    }
                }
                Preconditions.checkNotNull(text, "Input DataSet should not be null.");
            } else {
                // get default test text data
                System.out.println("Executing WordCount example with default input data set.");
                System.out.println("Use --input to specify file input.");
                text = WordCountData.getDefaultTextLineDataSet(env);
            }

            DataSet<Tuple2<String, Integer>> counts =
                    // split up the lines in pairs (2-tuples) containing: (word,1)
                    text.flatMap(new Tokenizer())
                            // group by the tuple field "0" and sum up tuple field "1"
                            .groupBy(0)
                            .sum(1);
            System.out.println("counts = " + counts);

            // emit result
            if (params.has("output")) {
                System.out.println("get output "+params.get("output"));
//                counts.writeAsCsv(params.get("output"), "\n", " ");
                counts.writeAsText(params.get("output"));
                // execute program
                env.execute("WordCount Example");
            } else {
                System.out.println("Printing result to stdout. Use --output to specify output path.");
                counts.print();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Implements the string tokenizer that splits sentences into words as a user-defined
     * FlatMapFunction. The function takes a line (String) and splits it into multiple pairs in the
     * form of "(word,1)" ({@code Tuple2<String, Integer>}).
     */
    public static final class Tokenizer
            implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // normalize and split the line
            String[] tokens = value.toLowerCase().split("\\W+");

            // emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }

}
