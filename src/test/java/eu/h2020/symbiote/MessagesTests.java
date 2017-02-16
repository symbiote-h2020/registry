package eu.h2020.symbiote;

import eu.h2020.symbiote.messaging.RabbitManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mateuszl on 16.02.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessagesTests {

    private static Logger log = LoggerFactory.getLogger(MessagesTests.class);

    public static final String PLATFORM_EXCHANGE_NAME = "symbiote.platform";
    public static final String PLATFORM_CREATED = "platform.creationRequested";
    public static final String PLATFORM_MODIFIED = "platform.modificationRequested";
    public static final String PLATFORM_DELETED = "platform.removalRequested";
    public static final String RESOURCE_EXCHANGE_NAME = "symbiote.resource";
    public static final String RESOURCE_CREATED = "resource.creationRequested";
    public static final String RESOURCE_MODIFIED = "resource.modificationRequested";
    public static final String RESOURCE_DELETED = "resource.removalRequested";

    private Random rand;

    @InjectMocks
    private RabbitManager rabbitManager;

    @Before
    public void setup() throws IOException, TimeoutException {
        ReflectionTestUtils.setField(rabbitManager, "rabbitHost", "localhost");
        ReflectionTestUtils.setField(rabbitManager, "rabbitUsername", "guest");
        ReflectionTestUtils.setField(rabbitManager, "rabbitPassword", "guest");

        ReflectionTestUtils.setField(rabbitManager, "platformExchangeName", PLATFORM_EXCHANGE_NAME);
        ReflectionTestUtils.setField(rabbitManager, "platformExchangeType", "topic");
        ReflectionTestUtils.setField(rabbitManager, "plaftormExchangeDurable", true);
        ReflectionTestUtils.setField(rabbitManager, "platformExchangeAutodelete", false);
        ReflectionTestUtils.setField(rabbitManager, "platformExchangeInternal", false);

        ReflectionTestUtils.setField(rabbitManager, "resourceExchangeName", RESOURCE_EXCHANGE_NAME);
        ReflectionTestUtils.setField(rabbitManager, "resourceExchangeType", "topic");
        ReflectionTestUtils.setField(rabbitManager, "resourceExchangeDurable", true);
        ReflectionTestUtils.setField(rabbitManager, "resourceExchangeAutodelete", false);
        ReflectionTestUtils.setField(rabbitManager, "resourceExchangeInternal", false);

        ReflectionTestUtils.setField(rabbitManager, "platformCreationRequestedRoutingKey", PLATFORM_CREATED);
        ReflectionTestUtils.setField(rabbitManager, "platformModificationRequestedRoutingKey", PLATFORM_MODIFIED);
        ReflectionTestUtils.setField(rabbitManager, "platformRemovalRequestedRoutingKey", PLATFORM_DELETED);
        ReflectionTestUtils.setField(rabbitManager, "resourceCreationRequestedRoutingKey", RESOURCE_CREATED);
        ReflectionTestUtils.setField(rabbitManager, "resourceModificationRequestedRoutingKey", RESOURCE_MODIFIED);
        ReflectionTestUtils.setField(rabbitManager, "resourceRemovalRequestedRoutingKey", RESOURCE_DELETED);

        rand = new Random();
    }

    @Test
    public void PlatformCreationTest() throws Exception {
//        rabbitManager.init();
//
//        Platform platform = new Platform ();
//        String platformId = Integer.toString(rand.nextInt(50));
//        String name = "platform" + rand.nextInt(50000);
//
//        platform.setPlatformId(platformId);
//        platform.setName(name);
//        platform.setDescription("platform_description");
//        platform.setUrl("http://www.symbIoTe.com");
//        platform.setInformationModelId("platform_info_model");
//
//        Gson gson = new Gson();
//        String message = gson.toJson(platform);
//
//        rabbitManager.sendCustomMessage(PLATFORM_EXCHANGE_NAME, PLATFORM_CREATED, message);

        // Sleep to make sure that the platform has been saved to the repo before querying
        TimeUnit.SECONDS.sleep(1);

    }

}
