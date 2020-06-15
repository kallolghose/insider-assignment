package com.insider.assignment.kafka;

import com.insider.assignment.pojo.response.Comment;
import com.insider.assignment.pojo.response.Story;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InsiderKafkaClient {

    private static InsiderKafkaClient instance = null;

    private InsiderKafkaClient(){}

    public static InsiderKafkaClient getInstance(){
        if(instance != null)
            return instance;
        else{
            synchronized (InsiderKafkaClient.class){
                instance = new InsiderKafkaClient();
                return instance;
            }
        }
    }

    public void storeStory(List<Story> stories){
        try {
            KafkaConfiguration kafkaConfiguration = KafkaConfiguration.getInstance();
            Producer<Long, Story> producer = kafkaConfiguration.createStoryProducer();
            for (int i = 0; i < stories.size(); i++) {
                ProducerRecord<Long, Story> producerRecord = new ProducerRecord<Long, Story>("top-stories", (long) i, stories.get(i));
                RecordMetadata metadata = producer.send(producerRecord).get();
                System.out.println(metadata.partition());
            }
            producer.commitTransaction();
            producer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Story> getStory(){
        try{
            Consumer<Long, Story> consumer = KafkaConfiguration.getInstance().createStoryConsumer();
            ConsumerRecords<Long, Story> records = consumer.poll(Duration.ofMillis(1));
            List<Story> stories = new ArrayList<>();
            for(ConsumerRecord<Long, Story> record : records){
                Story story = record.value();
                stories.add(story);
            }
            consumer.close();
            return stories;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void storeComment(Long storyId, List<Comment> comments){
        try{
            KafkaConfiguration kafkaConfiguration = KafkaConfiguration.getInstance();
            Producer<Long, List<Comment>> producer = kafkaConfiguration.createCommentProducer();
            ProducerRecord<Long, List<Comment>> producerRecord = new ProducerRecord<Long, List<Comment>>("comments", storyId,
                    comments);
            RecordMetadata metadata = producer.send(producerRecord).get();
            System.out.println("Partitiion ::::::::::: " + metadata.partition());
            producer.commitTransaction();
            producer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Comment> getComments(Long storyId){
        try{
            Consumer<Long, List<Comment>> consumer = KafkaConfiguration.getInstance().createCommentConsumer();
            //consumer.seekToBeginning(consumer.assignment());
            while (true) {
                ConsumerRecords<Long, List<Comment>> records = consumer.poll(Duration.ofMillis(1));
                List<Comment> comments = null;
                for (ConsumerRecord<Long, List<Comment>> record : records) {
                    if (record.key() == storyId) {
                        comments = record.value();
                        break;
                    }
                }
                if(comments!=null){
                    break;
                }
            }
            consumer.close();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
