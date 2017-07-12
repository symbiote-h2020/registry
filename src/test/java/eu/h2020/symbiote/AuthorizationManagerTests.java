package eu.h2020.symbiote;

import eu.h2020.symbiote.core.model.resources.Resource;
import eu.h2020.symbiote.model.RegistryPlatform;
import eu.h2020.symbiote.repository.RegistryPlatformRepository;
import eu.h2020.symbiote.security.InternalSecurityHandler;
import eu.h2020.symbiote.security.enums.ValidationStatus;
import eu.h2020.symbiote.utils.AuthorizationManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static eu.h2020.symbiote.TestSetupConfig.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by mateuszl on 18.05.2017.
 */
public class AuthorizationManagerTests {

    AuthorizationManager authorizationManager;
    RegistryPlatformRepository mockedRegistryPlatformRepository;
    InternalSecurityHandler mockedSecurityHandler;

    @Before
    public void setup() throws IOException, TimeoutException {
        mockedSecurityHandler = Mockito.mock(InternalSecurityHandler.class);
        mockedRegistryPlatformRepository = Mockito.mock(RegistryPlatformRepository.class);
        authorizationManager = new AuthorizationManager(mockedSecurityHandler, mockedRegistryPlatformRepository);
    }

    @After
    public void teardown() {

    }

    @Test
    public void testResourceOperationAccessPass() {
        when(mockedRegistryPlatformRepository.findOne("test1Plat")).thenReturn(generateRegistryPlatformB());
        when(mockedSecurityHandler.verifyHomeToken(any())).thenReturn(ValidationStatus.VALID);
        Assert.assertTrue("Access test failed!", authorizationManager.checkResourceOperationAccess(MOCKED_TOKEN, "test1Plat").isValidated());
    }

    @Test
    public void testCheckToken(){
        when(mockedSecurityHandler.verifyHomeToken(any())).thenReturn(ValidationStatus.VALID);
        Assert.assertTrue(authorizationManager.checkToken(MOCKED_TOKEN).isValidated());
    }

    @Test
    public void testCheckTokenFail(){
        when(mockedSecurityHandler.verifyHomeToken(any())).thenReturn(ValidationStatus.VALID);
        Assert.assertFalse(authorizationManager.checkToken("wrong_token").isValidated());
    }

    @Test
    public void testResourceOperationAccessFailWithInvalidStatus() {
        when(mockedRegistryPlatformRepository.findOne("test1Plat")).thenReturn(generateRegistryPlatformB());
        when(mockedSecurityHandler.verifyHomeToken(any())).thenReturn(ValidationStatus.INVALID);
        Assert.assertFalse("Access test passed!", authorizationManager.checkResourceOperationAccess(MOCKED_TOKEN, "test1Plat").isValidated());
    }

    @Test
    public void testResourceOperationAccessFailWithWrongPlatformOwner() {
        when(mockedRegistryPlatformRepository.findOne("wrongOwner")).thenReturn(generateRegistryPlatformB());
        when(mockedSecurityHandler.verifyHomeToken(any())).thenReturn(ValidationStatus.VALID);
        Assert.assertFalse("Access test passed!", authorizationManager.checkResourceOperationAccess(MOCKED_TOKEN, "wrongOwner").isValidated());
    }

    @Test
    public void testResourceOperationAccessFailWithWrongPlatformId() {
        when(mockedSecurityHandler.verifyHomeToken(any())).thenReturn(ValidationStatus.VALID);
        Assert.assertFalse("Access test passed!", authorizationManager.checkResourceOperationAccess(MOCKED_TOKEN, "wrongPlatformId").isValidated());
    }

    @Test
    public void testResourceOperationAccessFailWithWrongIssuer() {
        when(mockedSecurityHandler.verifyHomeToken(any())).thenReturn(ValidationStatus.VALID);
        Assert.assertFalse("Access test passed!", authorizationManager.checkResourceOperationAccess(MOCKED_TOKEN, "wrongIssuer").isValidated());
    }

    @Test
    public void testIfResourceBelongsToPlatform(){
        Resource resource = generateResource();
        RegistryPlatform platform = generateRegistryPlatformB();
        when(mockedRegistryPlatformRepository.findOne(PLATFORM_B_ID)).thenReturn(platform);
        List<Resource> resources = Arrays.asList(resource);

        Assert.assertTrue(authorizationManager.checkIfResourcesBelongToPlatform(resources, PLATFORM_B_ID).isValidated());
    }

    @Test
    public void testIfResourceDoesNotBelongToPlatform(){
        Resource resource = generateResource();
        resource.setInterworkingServiceURL("http://other_url.com/");
        RegistryPlatform platform = generateRegistryPlatformB();
        when(mockedRegistryPlatformRepository.findOne(PLATFORM_B_ID)).thenReturn(platform);
        List<Resource> resources = Arrays.asList(resource);

        Assert.assertFalse(authorizationManager.checkIfResourcesBelongToPlatform(resources, PLATFORM_B_ID).isValidated());
    }

}
