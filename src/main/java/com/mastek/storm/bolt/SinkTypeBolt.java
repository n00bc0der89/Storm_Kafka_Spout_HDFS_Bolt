package com.mastek.storm.bolt;

import java.util.Map;

import org.json.JSONObject;

import scala.Option;
import scala.collection.immutable.List;
import scala.util.parsing.json.JSON;

import com.mastek.storm.Topology;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * @author mastek
 * This class parses the incoming messages and decided which bolt the message has to be passed on to
 * There are two cases in this example, first if of solr type and second is of hdfs type.
 */
public class SinkTypeBolt extends BaseRichBolt {


	  String tweet_created_at;
	  String tweet_id;
	  String tweet_id_str;
	  String tweet_text;
	  String tweet_source;
	  
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	

	public void execute(Tuple tuple) {
		String value = tuple.getString(0);
		System.out.println("Received in SinkType bolt : "+value);
		//int index = value.indexOf(" ");
		//if (index == -1)
		//	return;
		//String type = value.substring(0,index);
		System.out.println("Message : "+value);
		//value = value.substring(index);
		//if (type.equals("hdfs")) {
		
		 JSONObject object_tweet=new JSONObject(value);
		 this.tweet_created_at=object_tweet.getString("created_at");
         this.tweet_text=object_tweet.getString("text");
         this.tweet_source=object_tweet.getString("source");
         System.out.println("Tweets from Parser: "+ this.tweet_text);
		
		
			collector.emit(Topology.HDFS_STREAM,new Values(this.tweet_created_at,this.tweet_text,this.tweet_source));
			System.out.println("Emitted : "+this.tweet_text);
		//}
		collector.ack(tuple);	
	}

	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(Topology.HDFS_STREAM, new Fields( "created","tweet","source"));
	}

}
