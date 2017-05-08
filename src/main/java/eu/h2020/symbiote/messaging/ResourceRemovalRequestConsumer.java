package eu.h2020.symbiote.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import eu.h2020.symbiote.core.internal.CoreResourceRegistryRequest;
import eu.h2020.symbiote.core.internal.CoreResourceRegistryResponse;
import eu.h2020.symbiote.core.internal.DescriptionType;
import eu.h2020.symbiote.core.model.resources.Resource;
import eu.h2020.symbiote.model.RegistryPersistenceResult;
import eu.h2020.symbiote.repository.RepositoryManager;
import eu.h2020.symbiote.utils.AuthorizationManager;
import eu.h2020.symbiote.utils.RegistryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RabbitMQ Consumer implementation used for Resource Removal actions
 * <p>
 * Created by mateuszl
 */
public class ResourceRemovalRequestConsumer extends DefaultConsumer {

    private static Log log = LogFactory.getLog(ResourceRemovalRequestConsumer.class);
    private AuthorizationManager authorizationManager;
    private RepositoryManager repositoryManager;
    private RabbitManager rabbitManager;
    private List<RegistryPersistenceResult> resourceRemovalResultList;
    private List<Resource> resourcesRemoved;
    private List<Resource> resources;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     * Managers beans passed as parameters because of lack of possibility to inject it to consumer.
     *
     * @param channel           the channel to which this consumer is attached
     * @param rabbitManager     rabbit manager bean passed for access to messages manager
     * @param repositoryManager repository manager bean passed for persistence actions
     */
    public ResourceRemovalRequestConsumer(Channel channel,
                                          RepositoryManager repositoryManager,
                                          RabbitManager rabbitManager,
                                          AuthorizationManager authorizationManager) {
        super(channel);
        this.repositoryManager = repositoryManager;
        this.rabbitManager = rabbitManager;
        this.authorizationManager = authorizationManager;
        resourceRemovalResultList = new ArrayList<>();
        resourcesRemoved = new ArrayList<>();
        resources = new ArrayList<>();
    }

    /**
     * Called when a <code><b>basic.deliver</b></code> is received for this consumer.
     *
     * @param consumerTag the <i>consumer tag</i> associated with the consumer
     * @param envelope    packaging data for the message
     * @param properties  content header data for the message
     * @param body        the message body (opaque, client-specific byte array)
     * @throws IOException if the consumer encounters an I/O error while processing the message
     * @see Envelope
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CoreResourceRegistryRequest request = null;
        CoreResourceRegistryResponse response = new CoreResourceRegistryResponse();
        RegistryPersistenceResult resourceRemovalResult = new RegistryPersistenceResult();

        String message = new String(body, "UTF-8");
        log.info(" [x] Received resource to remove: '" + message + "'");

        try {
            request = mapper.readValue(message, CoreResourceRegistryRequest.class);
        } catch (JsonSyntaxException e) {
            log.error("Error occured during getting Operation Request from Json", e);
            response.setStatus(400);
            response.setMessage("Error occured during getting Operation Request from Json");
        }

        if (request != null) {
            if (authorizationManager.checkAccess(request.getToken(), request.getPlatformId())) {
                try {
                    resources = mapper.readValue(request.getBody(), new TypeReference<List<Resource>>() {
                    });
                } catch (JsonSyntaxException e) {
                    log.error("Error occured during getting Resources from Json", e);
                    response.setStatus(400);
                    response.setMessage("Error occured during getting Resources from Json");
                }
            } else {
                log.error("Token invalid");
                response.setStatus(400);
                response.setMessage("Token invalid");
            }
        }

        for (Resource resource : resources) {

            if (resource == null) {
                log.error("Resources list contains a NULL resource!");
                response.setMessage("Resources list contains a NULL resource!");
                response.setStatus(410);
            } else {
                if (resource.getId() != null || !resource.getId().isEmpty()) {
                    resourceRemovalResult = this.repositoryManager.removeResource(resource);
                } else {
                    log.error("Given Resource has id null or empty");
                    resourceRemovalResult.setMessage("Given Resource has ID null or empty");
                    resourceRemovalResult.setStatus(400);
                }
                resourceRemovalResultList.add(resourceRemovalResult);
            }
        }

        if (checkIfRemovalWasSuccessful()) {
            sendFanoutMessage();

            response.setMessage("Success");
            response.setStatus(200);
            response.setDescriptionType(DescriptionType.BASIC);
        } else {
            response.setMessage("Operation not performed");
            response.setStatus(410);
            response.setDescriptionType(DescriptionType.BASIC);
        }

        response.setBody(mapper.writerFor(new TypeReference<List<Resource>>() {
                }).writeValueAsString(resourceRemovalResultList.stream()
                        .map(registryPersistenceResult ->
                                RegistryUtils.convertCoreResourceToResource
                                        (registryPersistenceResult.getResource()))
                        .collect(Collectors.toList())
                )
        );

        rabbitManager.sendRPCReplyMessage(this, properties, envelope, mapper.writeValueAsString(response));
        log.info("- rpc response message sent. Content: " + response);
        resourceRemovalResultList.clear();
        resourcesRemoved.clear();
    }

    private void sendFanoutMessage() {
        List<String> removedResourcesIds = resourceRemovalResultList.stream()
                .map(registryPersistenceResult ->
                        registryPersistenceResult.getResource().getId())
                .collect(Collectors.toList());
        rabbitManager.sendResourcesRemovedMessage(removedResourcesIds);
        log.info("- List with removed resources id's sent (fanout). Content: " + removedResourcesIds);
    }

    private boolean checkIfRemovalWasSuccessful() {
        for (RegistryPersistenceResult result : resourceRemovalResultList) {
            if (result.getStatus() == 200) {
                resourcesRemoved.add(result.getResource());
            }
        }
        System.out.println("Resources removed: " + resourcesRemoved);
        if (resourcesRemoved.size() != resources.size()) {
            rollback();
            return false;
        }
        return true;
    }

    private void rollback() {
        for (Resource resource : resourcesRemoved) {
            this.repositoryManager.saveResource(RegistryUtils.convertResourceToCoreResource(resource));
            log.info("Removed resources rollback performed.");
        }
    }
}