package eu.h2020.symbiote;

import com.mongodb.MongoException;
import eu.h2020.symbiote.core.model.internal.CoreResource;
import eu.h2020.symbiote.model.RegistryPlatform;
import eu.h2020.symbiote.repository.RegistryPlatformRepository;
import eu.h2020.symbiote.repository.RepositoryManager;
import eu.h2020.symbiote.repository.ResourceRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static eu.h2020.symbiote.TestSetupConfig.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mateuszl on 17.05.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RepositoryManagerTests {

    RepositoryManager repositoryManager;
    RegistryPlatformRepository registryPlatformRepository;
    ResourceRepository resourceRepository;

    @Before
    public void setup() {
        registryPlatformRepository = Mockito.mock(RegistryPlatformRepository.class);
        resourceRepository = Mockito.mock(ResourceRepository.class);
        repositoryManager = new RepositoryManager(registryPlatformRepository, resourceRepository);
    }

    @After
    public void teardown() {
    }

    @Test
    public void testSaveResourceTriggersRepository() {
        CoreResource resource = generateCoreResource();
        addIdToCoreResource(resource);
        when(resourceRepository.save(resource)).thenReturn(resource);

        repositoryManager.saveResource(resource);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(resourceRepository).save(resource);
    }

    @Test
    public void testModifyResourceTriggersRepository() {
        CoreResource resource = generateCoreResource();
        addIdToResource(resource);
        when(resourceRepository.save(resource)).thenReturn(resource);
        when(resourceRepository.findOne("101")).thenReturn(resource);

        repositoryManager.modifyResource(resource);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(resourceRepository).save(resource);
    }

    @Test
    public void testRemoveResourceTriggersRepository() {
        CoreResource resource = generateCoreResource();
        addIdToCoreResource(resource);
        when(resourceRepository.findOne("101")).thenReturn(resource);
        repositoryManager.removeResource(resource);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(resourceRepository).delete("101");
    }


    @Test
    public void testSaveResourceReturnsStatus200() throws Exception {
        CoreResource coreResource = generateCoreResource();
        when(resourceRepository.save(coreResource)).thenReturn(addIdToCoreResource(coreResource));
        Assert.assertEquals(200,repositoryManager.saveResource(coreResource).getStatus());
    }

    @Test
    public void testSaveResourceWithWrongId() throws Exception {
        CoreResource coreResource = generateCoreResource();
        Assert.assertNotEquals(200,repositoryManager.saveResource(coreResource).getStatus());
    }

    @Test
    public void testSaveResourceMongoError(){
        CoreResource coreResource = generateCoreResource();
        when(resourceRepository.save(coreResource)).thenThrow(new MongoException("MONGO ERROR"));
        Assert.assertNotEquals(200,repositoryManager.saveResource(coreResource).getStatus());
    }

    @Test
    public void testModifyResourceWithWrongId() throws Exception {
        CoreResource coreResource = generateCoreResource();
        Assert.assertNotEquals(200,repositoryManager.modifyResource(coreResource).getStatus());
    }

    @Test
    public void testRemoveResourceWithWrongId() throws Exception {
        CoreResource coreResource = generateCoreResource();
        addIdToCoreResource(coreResource);
        Assert.assertNotEquals(200,repositoryManager.removeResource(coreResource).getStatus());
    }

    @Test
    public void testSavePlatformTriggersRepository() {
        RegistryPlatform platform = generateRegistryPlatformB();
        when(registryPlatformRepository.save(platform)).thenReturn(platform);

        repositoryManager.savePlatform(platform);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(registryPlatformRepository).save(platform);
    }

    @Test
    public void testModifyPlatformTriggersRepository() {
        RegistryPlatform platform = generateRegistryPlatformB();
        when(registryPlatformRepository.save(platform)).thenReturn(platform);
        when(registryPlatformRepository.findOne(PLATFORM_B_ID)).thenReturn(platform);

        repositoryManager.modifyPlatform(platform);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(registryPlatformRepository).save(platform);
    }

    @Test
    public void testRemovePlatformTriggersRepository() {
        RegistryPlatform platform = generateRegistryPlatformB();
        when(registryPlatformRepository.findOne(PLATFORM_B_ID)).thenReturn(platform);

        repositoryManager.removePlatform(platform);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(registryPlatformRepository).delete(PLATFORM_B_ID);
    }

    @Test
    public void testSavePlatformReturnsStatus200() throws Exception {
        RegistryPlatform platform = generateRegistryPlatformB();
        when(registryPlatformRepository.save(platform)).thenReturn(platform);

        Assert.assertEquals(200,repositoryManager.savePlatform(platform).getStatus());
    }

    @Test
    public void testSavePlatformWithWrongId() throws Exception {
        RegistryPlatform platform = new RegistryPlatform();
        Assert.assertNotEquals(200,repositoryManager.savePlatform(platform).getStatus());
    }

    @Test
    public void testSavePlatformMongoError(){
        RegistryPlatform platform = generateRegistryPlatformB();
        when(registryPlatformRepository.save(platform)).thenThrow(new MongoException("MONGO ERROR"));
        Assert.assertNotEquals(200,repositoryManager.savePlatform(platform).getStatus());
    }

    @Test
    public void testRemovePlatformWithWrongId() throws Exception {
        RegistryPlatform platform = new RegistryPlatform();
        Assert.assertNotEquals(200,repositoryManager.removePlatform(platform).getStatus());
    }

    @Test
    public void testModifyPlatformWithWrongId() throws Exception {
        RegistryPlatform platform = new RegistryPlatform();
        Assert.assertNotEquals(200,repositoryManager.modifyPlatform(platform).getStatus());
    }

    @Test
    public void testRemovePlatformWithResourcesFail(){
        RegistryPlatform platform = generateRegistryPlatformB();
        when(resourceRepository.findByInterworkingServiceURL(platform.getId())).thenReturn(Arrays.asList(new CoreResource()));
        Assert.assertNotEquals(200,repositoryManager.removePlatform(platform).getStatus());
    }
}

