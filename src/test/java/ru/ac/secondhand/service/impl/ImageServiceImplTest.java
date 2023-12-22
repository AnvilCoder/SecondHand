package ru.ac.secondhand.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.exception.ImageNotFoundException;
import ru.ac.secondhand.exception.InvalidFileException;
import ru.ac.secondhand.repository.ImageRepository;
import ru.ac.secondhand.utils.TestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @TempDir
    Path tempDir;

    @Test
    public void whenGenerateUniqueFilename_thenStartsWithUUID() {
        String originalFilename = "test.jpg";
        String uniqueFilename = imageService.generateUniqueFilename(originalFilename);
        assertTrue(uniqueFilename.matches("[0-9a-fA-F\\-]{36}_.*"), TestUtils.UUID_START_MSG);
        assertTrue(uniqueFilename.endsWith(originalFilename), TestUtils.ORIGINAL_FILENAME_END_MSG);
    }

    @Test
    public void whenFileSizeIsValid_thenTrue() {
        MockMultipartFile file = new MockMultipartFile(
                TestUtils.FILE_CONTENT,
                TestUtils.FILE_NAME,
                TestUtils.TEXT_FILE_TYPE,
                new byte[10]);
        assertTrue(imageService.isValidSize(file), TestUtils.FILE_VALID_SIZE);
    }

    @Test
    public void whenFileSizeIsInvalid_thenFalse() {
        MockMultipartFile file = new MockMultipartFile(
                TestUtils.FILE_CONTENT,
                TestUtils.FILE_NAME,
                TestUtils.TEXT_FILE_TYPE,
                new byte[TestUtils.MAX_SIZE + 1]);
        assertFalse(imageService.isValidSize(file), TestUtils.INVALID_FILE_SIZE_MSG);
    }

    @Test
    public void whenFileTypeIsValid_thenTrue() {
        MockMultipartFile jpegFile = new MockMultipartFile(
                TestUtils.FILE_CONTENT,
                TestUtils.FILE_NAME,
                TestUtils.JPEG_FILE_TYPE,
                new byte[1]);
        assertTrue(imageService.isValidType(jpegFile), TestUtils.VALID_JPEG_TYPE_MSG);

        MockMultipartFile pngFile = new MockMultipartFile(
                TestUtils.FILE_CONTENT,
                TestUtils.FILE_NAME,
                TestUtils.PNG_FILE_TYPE,
                new byte[1]);
        assertTrue(imageService.isValidType(pngFile), TestUtils.VALID_PNG_TYPE_MSG);
    }

    @Test
    public void whenFileTypeIsInvalid_thenFalse() {
        MockMultipartFile gifFile = new MockMultipartFile(
                TestUtils.FILE_CONTENT,
                "filename.gif",
                TestUtils.GIF_FILE_TYPE,
                new byte[1]);
        assertFalse(imageService.isValidType(gifFile), TestUtils.INVALID_GIF_TYPE_MSG);
    }

    @Test
    public void whenFilenameIsValid_thenTrue() {
        assertTrue(imageService.isValidName(TestUtils.VALID_FILENAME), TestUtils.VALID_FILENAME_TRUE_MSG);
    }

    @Test
    public void whenFilenameIsInvalid_thenFalse() {
        assertFalse(imageService.isValidName(TestUtils.INVALID_FILENAME_1), "Filename with '..' should be invalid");
        assertFalse(imageService.isValidName(TestUtils.INVALID_FILENAME_2), "Filename with '<' should be invalid");
        assertFalse(imageService.isValidName(TestUtils.INVALID_FILENAME_3), "Filename with '>' should be invalid");
        assertFalse(imageService.isValidName(TestUtils.INVALID_FILENAME_4), "Filename with '\"' should be invalid");
    }

    @Test
    public void whenImageExists_thenRetrieveImageData() throws Exception {
        Integer imageId = 1;
        Image fakeImage = new Image();
        Path imagePath = tempDir.resolve(TestUtils.VALID_FILENAME);
        fakeImage.setImagePath(imagePath.toString());
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(fakeImage));

        byte[] expectedData = TestUtils.FILE_CONTENT.getBytes();
        Files.write(imagePath, expectedData);

        byte[] retrievedData = imageService.getImage(imageId);

        assertArrayEquals(expectedData, retrievedData,
                TestUtils.MATCH_EXPECTED_DATA_MSG);
    }

    @Test
    public void whenImageDoesNotExist_thenThrowException() {
        Integer imageId = 99;
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class,
                () -> imageService.getImage(imageId),
                TestUtils.IMAGE_NOT_FOUND_EXCEPTION_MSG);
    }

    @Test
    public void whenImageExists_thenDeleteImage() {
        Integer existingImageId = 1;
        when(imageRepository.existsById(existingImageId)).thenReturn(true);

        imageService.deleteImage(existingImageId);

        verify(imageRepository).deleteById(existingImageId);
        verify(imageRepository).existsById(existingImageId);
    }

    @Test
    public void whenImageDoesNotExist_thenNoDeletionAttempt() {
        Integer nonExistingImageId = 2;
        when(imageRepository.existsById(nonExistingImageId)).thenReturn(false);

        imageService.deleteImage(nonExistingImageId);

        verify(imageRepository, never()).deleteById(nonExistingImageId);
        verify(imageRepository).existsById(nonExistingImageId);
    }

    @Test
    public void whenAllValidationsPass_thenSaveImage() {
        byte[] content = TestUtils.FILE_CONTENT.getBytes();
        MockMultipartFile validImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                content);
        when(imageRepository.save(any(Image.class))).thenReturn(new Image());

        Image savedImage = imageService.saveImage(validImage);

        assertNotNull(savedImage, TestUtils.SAVED_IMAGE_NOT_NULL_MSG);
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    public void whenInvalidSize_thenThrowException() {
        byte[] largeContent = new byte[TestUtils.MAX_SIZE + 1];
        MockMultipartFile largeImage = new MockMultipartFile(
                "image",
                "large.jpg",
                "image/jpeg",
                largeContent);

        assertThrows(InvalidFileException.class,
                () -> imageService.saveImage(largeImage),
                TestUtils.LARGE_IMAGE_SIZE_EXCEPTION_MSG);
    }

    @Test
    public void whenInvalidType_thenThrowException() {
        byte[] content = TestUtils.FILE_CONTENT.getBytes();
        MockMultipartFile invalidTypeImage = new MockMultipartFile(
                "image",
                "test.gif",
                "image/gif",
                content);

        assertThrows(InvalidFileException.class,
                () -> imageService.saveImage(invalidTypeImage),
                TestUtils.INVALID_IMAGE_TYPE_EXCEPTION_MSG);
    }

    @Test
    public void whenInvalidName_thenThrowException() {
        byte[] content = TestUtils.FILE_CONTENT.getBytes();
        MockMultipartFile invalidNameImage = new MockMultipartFile(
                "image",
                "../test.jpg",
                "image/jpeg",
                content);

        assertThrows(InvalidFileException.class,
                () -> imageService.saveImage(invalidNameImage),
                TestUtils.INVALID_IMAGE_NAME_EXCEPTION_MSG);
    }
}
