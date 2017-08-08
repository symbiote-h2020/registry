package eu.h2020.symbiote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.h2020.symbiote.core.internal.CoreResourceRegistryRequest;
import eu.h2020.symbiote.core.internal.DescriptionType;
import eu.h2020.symbiote.core.model.InterworkingService;
import eu.h2020.symbiote.core.model.Platform;
import eu.h2020.symbiote.core.model.RDFFormat;
import eu.h2020.symbiote.core.model.internal.CoreResource;
import eu.h2020.symbiote.core.model.internal.CoreResourceType;
import eu.h2020.symbiote.core.model.resources.Resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mael on 23/01/2017.
 */
public class TestSetupConfig {

    public static final String MOCKED_TOKEN = "eyJhbGciOiJFUzI1NiJ9.eyJTWU1CSU9URV9Pd25lZFBsYXRmb3JtIjoidGVzdDFQbGF0IiwiU1lNQklPVEVfUm9sZSI6IlBMQVRGT1JNX09XTkVSIiwidHR5cCI6IkNPUkUiLCJzdWIiOiJUZXN0MSIsImlwayI6Ik1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRXI2OXZEV0pzT3duYW9CM0FDRVJPdnRETWtmNjh5aUd6c3lmR1duOWZnSnJHT2ZoTGJkM2Q5NEMxay9TUW1hRWdTakVOUWI4ZEljME9FYWRSRzFtWGR3PT0iLCJpc3MiOiJTeW1iSW9UZV9Db3JlX0FBTSIsImV4cCI6MTQ5NTExNTExMiwiaWF0IjoxNDk1MTExNTEyLCJqdGkiOiI0ODY1MTQ2NTIiLCJzcGsiOiJNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUV5RVJnYXhnQUUzSmUwand6RDBMdjluby9wQVQyUjV0Njc0MzJrcTQxaHNGTWZRSXdyQ212RVRZbW9lekgxYWU2WSsyV0ZCQVdiMHhiMGVUd1ozeWZZdz09In0.M7fIyUsr0GfUN7IyaKMG9T41dabvkFS_UNGeq3RCzzyzA-ttEdHnDNV3oImX7eMS_vvp-prBlnSsVl0dhL131Q";

    public static final String PLATFORM_EXCHANGE_NAME = "symbIoTe.platform";
    public static final String PLATFORM_CREATION_REQUESTED_RK = "symbIoTe.platform.creationRequested";
    public static final String PLATFORM_MODIFICATION_REQUESTED_RK = "symbIoTe.platform.modificationRequested";
    public static final String PLATFORM_REMOVAL_REQUESTED_RK = "symbIoTe.platform.removalRequested";
    public static final String RESOURCE_EXCHANGE_NAME = "symbIoTe.resource";
    public static final String RESOURCE_CREATION_REQUESTED_RK = "symbIoTe.resource.creationRequested";
    public static final String RESOURCE_MODIFICATION_REQUESTED_RK = "symbIoTe.resource.modificationRequested";
    public static final String RESOURCE_REMOVAL_REQUESTED_RK = "symbIoTe.resource.removalRequested";

    public static final String PLATFORM_CREATED_ROUTING_KEY = "symbIoTe.platform.created";
    public static final String PLATFORM_MODIFIED_ROUTING_KEY = "symbIoTe.platform.updated";
    public static final String PLATFORM_REMOVED_ROUTING_KEY = "symbIoTe.platform.deleted";
    public static final String RESOURCE_CREATED_ROUTING_KEY = "symbIoTe.resource.created";
    public static final String RESOURCE_REMOVED_ROUTING_KEY = "symbIoTe.resource.deleted";
    public static final String RESOURCE_MODIFIED_ROUTING_KEY = "symbIoTe.resource.updated";

    public static final String PLATFORM_REMOVAL_REQUESTED_QUEUE = "symbIoTe-Registry-platformRemovalRequestedQueue";
    public static final String RESOURCE_CREATION_REQUESTED_QUEUE = "symbIoTe-Registry-resourceCreationRequestedQueue";
    public static final String RESOURCE_MODIFICATION_REQUESTED_QUEUE = "symbIoTe-Registry-resourceModificationRequestedQueue";
    public static final String PLATFORM_CREATION_REQUESTED_QUEUE = "symbIoTe-Registry-platformCreationRequestedQueue";
    public static final String PLATFORM_MODIFICATION_REQUESTED_QUEUE = "symbIoTe-Registry-platformModificationRequestedQueue";
    public static final String RESOURCE_REMOVAL_REQUESTED_QUEUE = "symbIoTe-Registry-resourceRemovalRequestedQueue";
    public static final String PLATFORM_RESOURCES_REQUESTED_QUEUE = "symbIoTe-Registry-platformResourcesRequestedQueue";

    public static final String RESOURCES_FOR_PLATFORM_REQUESTED_RK = "symbIoTe.platform.resourcesRequested";

    public static final String RESOURCE_TRANSLATION_REQUESTED_RK = "symbIoTe.resource.instance.translationRequested";
    public static final String RESOURCE_VALIDATION_REQUESTED_RK = "symbIoTe.resource.instance.validationRequested";

    public static final String PLATFORM_A_ID = "1";
    public static final String PLATFORM_A_NAME = "Platform1";
    public static final String PLATFORM_A_DESCRIPTION = "11desc";
    public static final String PLATFORM_A_URI = "http://www.symbiote-h2020.eu/ontology/platforms/1";
    public static final String PLATFORM_A_SERVICE_URI = "http://www.symbiote-h2020.eu/ontology/platforms/1/service/somehost1.com/resourceAccessProxy";
    public static final String PLATFORM_A_URL = "http://somehost1.com/resourceAccessProxy";
    public static final String INTERWORKING_SERVICE_URL_A = "http://somehost1.com/platformA";
    public static final String INFORMATION_MODEL_ID_A = "IM_1";

    public static final String PLATFORM_A_NAME_UPDATED = "Platform1Updated";
    public static final String PLATFORM_A_MODEL_ID_UPDATED = "11Updated";
    public static final String PLATFORM_A_DESCRIPTION_UPDATED = "11descUpdated";
    public static final String PLATFORM_A_URL_UPDATED = "http://somehost1.com/resourceAccessProxyUpdated";

    public static final String PLATFORM_B_ID = "PlatB";
    public static final String PLATFORM_B_NAME = "PlatformB";
    public static final String PLATFORM_B_DESCRIPTION = "21desc";
    public static final String INTERWORKING_SERVICE_URL_B = "http://somehost1.com/platformB";
    public static final String INFORMATION_MODEL_ID_B = "IM_2";

    public static final String RESOURCE_PREDICATE = "http://www.symbiote-h2020.eu/ontology/resources/";

    public static final String RESOURCE_101_LABEL = "Resource 101";
    public static final String RESOURCE_101_COMMENT = "Resource 101 comment";
    public static final String RESOURCE_101_ID = "101";

    public static final String RESOURCE_STATIONARY_FILENAME = "/exampleStationarySensor.json";
    public static final String RESOURCE_STATIONARY_FILENAME_MODIFIED = "/exampleStationarySensorModified.json";
    public static final String RESOURCE_STATIONARY_LABEL = "Stationary 1";
    public static final String RESOURCE_STATIONARY_LABEL_MODIFIED = "New sensor 1";
    public static final String RESOURCE_STATIONARY_COMMENT = "This is stationary 1";
    public static final String RESOURCE_STATIONARY_URI = RESOURCE_PREDICATE + "stationary1";
    public static final String RESOURCE_STATIONARY_ID = "stationary1";


    public static Platform generateSymbiotePlatformA() {
        Platform platform = new Platform();
        platform.setId(PLATFORM_A_ID);
        platform.setComments(Arrays.asList(PLATFORM_A_DESCRIPTION));
        platform.setLabels(Arrays.asList(PLATFORM_A_NAME));
        InterworkingService interworkingService = new InterworkingService();
        interworkingService.setInformationModelId(INFORMATION_MODEL_ID_A);
        interworkingService.setUrl(INTERWORKING_SERVICE_URL_A);
        platform.setInterworkingServices(Arrays.asList(interworkingService));
        platform.setRdf("http://www.symbIoTe.com/");
        platform.setRdfFormat(RDFFormat.JSONLD);
        return platform;
    }

    public static Platform generatePlatformB() {
        Platform platform = new Platform();
        platform.setId(PLATFORM_B_ID);
        platform.setComments(Arrays.asList(PLATFORM_B_DESCRIPTION));
        platform.setLabels(Arrays.asList(PLATFORM_B_NAME));
        InterworkingService interworkingService = new InterworkingService();
        interworkingService.setInformationModelId(INFORMATION_MODEL_ID_B);
        interworkingService.setUrl(INTERWORKING_SERVICE_URL_B);
        platform.setInterworkingServices(Arrays.asList(interworkingService));
        platform.setRdf("http://www.symbIoTe.com/");
        platform.setRdfFormat(RDFFormat.JSONLD);
        return platform;
    }

    public static CoreResource generateCoreResource() {
        return generateSensor(RESOURCE_101_LABEL, RESOURCE_101_COMMENT, null, INTERWORKING_SERVICE_URL_B,
                RESOURCE_STATIONARY_FILENAME, RDFFormat.JSONLD);
    }

    public static Resource generateResource() {
        return generateSensor(RESOURCE_101_LABEL, RESOURCE_101_COMMENT, null, INTERWORKING_SERVICE_URL_B);
    }

    public static CoreResource addIdToCoreResource(CoreResource coreResource) {
        coreResource.setId(RESOURCE_101_ID);
        return coreResource;
    }

    public static Resource addIdToResource(Resource resource) {
        resource.setId(RESOURCE_101_ID);
        return resource;
    }

    public static CoreResource generateStationarySensor() {
        return generateSensor(RESOURCE_STATIONARY_LABEL, RESOURCE_STATIONARY_COMMENT, RESOURCE_STATIONARY_ID,
                PLATFORM_A_URL, RESOURCE_STATIONARY_FILENAME, RDFFormat.JSONLD);
    }

    public static CoreResource generateModifiedStationarySensor() {
        return generateSensor(RESOURCE_STATIONARY_LABEL_MODIFIED, RESOURCE_STATIONARY_COMMENT, RESOURCE_STATIONARY_ID,
                PLATFORM_A_URL, RESOURCE_STATIONARY_FILENAME_MODIFIED, RDFFormat.JSONLD);
    }

    public static CoreResource generateSensor(String label, String comment, String id, String serviceUrl,
                                              String rdfFilename, RDFFormat format) {
        CoreResource res = new CoreResource();
        res.setComments(Arrays.asList(comment));
        res.setLabels(Arrays.asList(label));
        res.setId(id);
        res.setInterworkingServiceURL(serviceUrl);
        res.setRdf(rdfFilename);
        res.setRdfFormat(format);
        res.setType(CoreResourceType.STATIONARY_SENSOR);
        return res;
    }

    public static Resource generateSensor(String label, String comment, String id, String interworkingServiceUrl) {
        CoreResource res = new CoreResource();
        res.setComments(Arrays.asList(comment));
        res.setLabels(Arrays.asList(label));
        res.setId(id);
        res.setType(CoreResourceType.STATIONARY_SENSOR);
        res.setInterworkingServiceURL(interworkingServiceUrl);
        return res;
    }

    public static CoreResourceRegistryRequest generateCoreResourceRegistryRequest(Resource resource1, Resource resource2)
            throws JsonProcessingException {

        Map<String, Resource> resourceList = new HashMap<>();
        resourceList.put("1", resource1);
        resourceList.put("2", resource2);

        ObjectMapper mapper = new ObjectMapper();
        String resources = mapper.writerFor(new TypeReference<Map<String, Resource>>() {
        }).writeValueAsString(resourceList);

        CoreResourceRegistryRequest coreResourceRegistryRequest = new CoreResourceRegistryRequest();
        coreResourceRegistryRequest.setPlatformId(PLATFORM_B_ID);
        coreResourceRegistryRequest.setToken(MOCKED_TOKEN);
        coreResourceRegistryRequest.setDescriptionType(DescriptionType.BASIC);
        coreResourceRegistryRequest.setBody(resources);

        return coreResourceRegistryRequest;
    }


    public static CoreResourceRegistryRequest generateCoreResourceRegistryRequest()
            throws JsonProcessingException {

        Resource resource1 = generateResource();
        addIdToResource(resource1);
        Resource resource2 = generateResource();
        addIdToResource(resource2);

        Map<String, Resource> resourceList = new HashMap<>();
        resourceList.put("1", resource1);
        resourceList.put("2", resource2);

        ObjectMapper mapper = new ObjectMapper();
        String resources = mapper.writerFor(new TypeReference<Map<String, Resource>>() {
        }).writeValueAsString(resourceList);

        CoreResourceRegistryRequest coreResourceRegistryRequest = new CoreResourceRegistryRequest();
        coreResourceRegistryRequest.setPlatformId(PLATFORM_B_ID);
        coreResourceRegistryRequest.setToken(MOCKED_TOKEN);
        coreResourceRegistryRequest.setDescriptionType(DescriptionType.BASIC);
        coreResourceRegistryRequest.setBody(resources);

        return coreResourceRegistryRequest;
    }
}
