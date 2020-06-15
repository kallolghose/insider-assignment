package com.insider.assignment.kafka;

import com.insider.assignment.kafka.deserializer.PayLoadDeserializer;
import com.insider.assignment.kafka.serializer.PayLoadSerializer;
import com.insider.assignment.pojo.response.Comment;
import com.insider.assignment.pojo.response.Story;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KafkaConfiguration {

    private static String BOOTSTRAP_SERVERS = "localhost:9092";

    private static KafkaConfiguration instance = null;
    private KafkaConfiguration(){}

    public static KafkaConfiguration getInstance(){
        if(instance!=null){
            return instance;
        }
        else{
            synchronized (KafkaConfiguration.class){
                instance = new KafkaConfiguration();
                return instance;
            }
        }
    }

    private Properties getProperties(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "Insider");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                PayLoadSerializer.class.getName());
        props.put("key.deserializer",
                LongDeserializer.class.getName());
        props.put("value.deserializer",
                PayLoadDeserializer.class.getName());
        return props;
    }

    public Producer<Long, Story> createStoryProducer() {
        return new KafkaProducer<Long, Story>(getProperties());
    }

    public Consumer<Long, Story> createStoryConsumer(){
        Properties properties = getProperties();
        properties.put("group.id", "console-consumer-story");
        Consumer<Long, Story> consumer = new KafkaConsumer<Long, Story>(properties);
        consumer.subscribe(Arrays.asList("top-stories"));
        return consumer;
    }

    public Producer<Long, List<Comment>> createCommentProducer() {
        return new KafkaProducer<Long, List<Comment>>(getProperties());
    }

    public Consumer<Long, List<Comment>> createCommentConsumer(){
        Properties properties = getProperties();
        properties.put("group.id", "console-consumer-comment");
        Consumer<Long, List<Comment>> consumer = new KafkaConsumer<Long, List<Comment>>(properties);
        consumer.subscribe(Arrays.asList("comments"));
        return consumer;
    }
}
