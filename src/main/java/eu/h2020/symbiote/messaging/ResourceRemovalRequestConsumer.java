package eu.h2020.symbiote.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import eu.h2020.symbiote.model.Resource;
import eu.h2020.symbiote.model.ResourceResponse;
import eu.h2020.symbiote.repository.RepositoryManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * RabbitMQ Consumer implementation used for Resource Removal actions
 */
public class ResourceRemovalRequestConsumer extends DefaultConsumer {

    private static Log log = LogFactory.getLog(ResourceRemovalRequestConsumer.class);
    private RepositoryManager repositoryManager;
    private RabbitManager rabbitManager;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     * Managers beans passed as parameters because of lack of possibility to inject it to consumer.
     *
     * @param channel the channel to which this consumer is attached
     * @param rabbitManager rabbit manager bean passed for access to messages manager
     * @param repositoryManager repository manager bean passed for persistence actions
     */
    public ResourceRemovalRequestConsumer(Channel channel,
                                          RepositoryManager repositoryManager,
                                          RabbitManager rabbitManager) {
        super(channel);
        this.repositoryManager = repositoryManager;
        this.rabbitManager = rabbitManager;
    }

    /**
     * Called when a <code><b>basic.deliver</b></code> is received for this consumer.
     * @param consumerTag the <i>consumer tag</i> associated with the consumer
     * @param envelope packaging data for the message
     * @param properties content header data for the message
     * @param body the message body (opaque, client-specific byte array)
     * @throws IOException if the consumer encounters an I/O error while processing the message
     * @see Envelope
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        Gson gson = new Gson();
        String response = "";
        String message = new String(body, "UTF-8");
        log.info(" [x] Received resource to remove: '" + message + "'");

        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                .Builder()
                .correlationId(properties.getCorrelationId())
                .build();

        Resource resource;
        ResourceResponse resourceResponse = null;
        try {
            resource = gson.fromJson(message, Resource.class);
            resourceResponse = this.repositoryManager.removeResource(resource);
            if (resourceResponse.getStatus()==200) {
                rabbitManager.sendResourceRemovedMessage(resourceResponse.getResource());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            resourceResponse = new ResourceResponse();
            resourceResponse.setStatus(400);
        }

        response = gson.toJson(resourceResponse);

        this.getChannel().basicPublish("", properties.getReplyTo(), replyProps, response.getBytes());
        log.info("-> Message sent back");

        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }
}
