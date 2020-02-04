package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
        String bootstrapServers = "127.0.0.1:9092";
       //create Producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootstrapServers);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i=0; i<10; i++){

            String key = "Key" + Integer.toString(i);
            //create a producer record
            ProducerRecord<String, String> record = new
                    ProducerRecord<String, String>("first_topic", key, "hello world" + Integer.toString(i));

            logger.info("Key: " + key);

            //send data
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        logger.info("Received new metadata. \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" + "offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp" + recordMetadata.timestamp());

                    } else {
                        logger.error("Error while producing", e);
                    }
                }
            });
        };

        producer.flush();
    }
}
