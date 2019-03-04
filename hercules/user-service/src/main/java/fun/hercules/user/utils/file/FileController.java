package fun.hercules.user.utils.file;

import com.google.common.io.Files;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.InternalServerError;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.InternalServerError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping(value = "/file")
@Api(value = "fileProcess", description = "file process")
@Slf4j
public class FileController {

    private static final int IMG_WIDTH = 180;

    private static final int IMG_HEIGHT = 128;

    private static final int PICTURE_MAX_SIZE = 3 * 1024 * 1024;

    private FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/pictures")
    @ApiOperation(value = "upload picture")
    @ApiResponses(value = {
            @ApiResponse(code = 202, response = ResponseEntity.class, message = "upload picture successfully"),
            @ApiResponse(code = 500, message = "failed to store picture")
    })
    public ResponseEntity<String> uploadPicture(@RequestParam("filename") MultipartFile file) {
        checkPicture(file);
        String fileName = fileStorageService.store(file);
        return ResponseEntity.accepted().body(fileName);
    }

    @GetMapping("/pictures/{filename:.+}")
    @ApiOperation(value = "get the file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "search file successfully"),
            @ApiResponse(code = 404, message = "fileName not right"),
            @ApiResponse(code = 500, message = "resized file failed")
    })
    public void serveFile(@PathVariable String filename,
                          @RequestParam(required = false) boolean isResize,
                          HttpServletResponse response) {
        Resource file = fileStorageService.loadAsResource(filename);
        try {
            BufferedImage resizedImage = null;
            OutputStream outputStream = response.getOutputStream();
            if (isResize) {
                resizedImage = fileStorageService.createResizedCopy(ImageIO.read(file.getInputStream()), IMG_WIDTH, IMG_HEIGHT);
            } else {
                resizedImage = ImageIO.read(file.getInputStream());
            }
            ImageIO.write(resizedImage, Files.getFileExtension(filename), outputStream);
        } catch (IOException e) {
            log.warn("Failed to resized file {}, errorMessage {}", filename, e.getMessage());
            throw new InternalServerError(ErrorCode.FAILED_TO_RESIZED_IMAGE, "Failed to resized file " + filename, e);
        }
    }

    @PostMapping("/pictures/{filename:.+}/deletion")
    @ApiOperation(value = "delete the file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "delete the file successfully"),
            @ApiResponse(code = 404, message = "file not found"),
            @ApiResponse(code = 500, message = "io error")
    })
    public void deleteFile(@PathVariable String filename) {
        fileStorageService.deleteFile(filename);
    }

    private void checkPicture(MultipartFile multipartPicture) {
        if (!isValidPictureSuffix(multipartPicture)) {
            throw new InternalServerError(ErrorCode.INVALID_UPLOAD_FILE);
        }
        if (!isValidPictureSize(multipartPicture)) {
            throw new InternalServerError(ErrorCode.UPLOAD_FILE_SIZE_TOO_LARGE);
        }
    }

    private boolean isValidPictureSuffix(MultipartFile multipartPicture) {
        String suffix = Files.getFileExtension(multipartPicture.getOriginalFilename());
        return suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpg")
                || suffix.equalsIgnoreCase("jpeg") || suffix.equalsIgnoreCase("bmp");
    }

    private boolean isValidPictureSize(MultipartFile multipartPicture) {
        return multipartPicture.getSize() <= PICTURE_MAX_SIZE;
    }

}
