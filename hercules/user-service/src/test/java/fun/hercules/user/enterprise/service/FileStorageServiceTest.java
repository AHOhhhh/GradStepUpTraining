package fun.hercules.user.enterprise.service;

import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.utils.file.FileStorageService;
import org.junit.Test;


public class FileStorageServiceTest {

    FileStorageService fileStorageService = new FileStorageService("./pictures");

    @Test(expected = NotFoundException.class)
    public void shouldGetNotFoundExceptionWhenNoFile() throws Exception {
        fileStorageService.loadAsResource("XXX");
    }
}