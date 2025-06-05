import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class IngredientFrequency {

    public static class IngredientMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text ingredient = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();

            String[] columns = line.split("\",\"");
            if (columns.length > 0) {
                String rawIngredients = columns[columns.length - 1].replace("\"", "").trim();

                // Split ingredients by comma 
                String[] ingList = rawIngredients.split(",");
                for (String ing : ingList) {
                    String cleaned = ing.trim().toLowerCase();
                    if (!cleaned.isEmpty()) {
                        ingredient.set(cleaned);
                        context.write(ingredient, one);
                    }
                }
            }
        }
    }

    public static class IngredientReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values)
                sum += val.get();
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "ingredient frequency");

        job.setJarByClass(IngredientFrequency.class);
        job.setMapperClass(IngredientMapper.class);
        job.setCombinerClass(IngredientReducer.class);
        job.setReducerClass(IngredientReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
