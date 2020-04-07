import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCount {

	public static void main(String[] args)throws IOException, ClassNotFoundException, InterruptedException
	{
		if(args.length != 2)
		{
			System.err.println("Usage: Word Count <input path> <output path>");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(WordCount.class);
		job.setJobName("Word Count");
		FileInputFormat.addInputPath(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.waitForCompletion(true);
	}
}
class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable>
{
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{
		String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line);
		while(tokenizer.hasMoreTokens()) {
			word.set(tokenizer.nextToken());
			context.write(word, one);
		}
	}
}
class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>
{
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
		throws IOException, InterruptedException
		{
			int sum = 0;
			for (IntWritable value : values)
			{
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));
		}
}
