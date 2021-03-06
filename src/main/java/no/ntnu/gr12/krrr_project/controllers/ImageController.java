package no.ntnu.gr12.krrr_project.controllers;

import io.swagger.models.Response;
import no.ntnu.gr12.krrr_project.models.Image;
import no.ntnu.gr12.krrr_project.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for image handling.
 */
@RestController
@CrossOrigin
public class ImageController {

  Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

  @Autowired
  ImageService imgService;

  @PostMapping("/images")
  public ResponseEntity<String> upload(@RequestParam("fileContent")MultipartFile multipartFile, String productName) {
    ResponseEntity<String> response = null;
    int imgId = imgService.saveImage((multipartFile), productName);
    if(imgId > 0) {
      response = new ResponseEntity<>("" + imgId, HttpStatus.OK);
    } else {
      response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return response;
  }

  /**
   * Return image content from database
   * @param id ID of the image to locate
   * @return Image content and correct content type, or NOT FOUND.
   */
  @GetMapping(value = "/api/images/{id}",
  produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> get(@PathVariable Integer id) {
    logger.info("Calling ImageController.GetMapping() with id: " + id);
    ResponseEntity<byte[]> response;
    Image img = imgService.getImageByID(id);
    if(img != null) {
      response = ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, img.getContentType())
        .body(img.getData());
    } else {
      response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    logger.info(response.toString());
    return response;
  }

  @CrossOrigin
  @GetMapping(value = "/api/images/{productName}",
  produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> get(@PathVariable String productName) {
    logger.info("Calling ImageController.getMapping() with productName: " + productName);
    ResponseEntity<byte[]> response;
    Image img = imgService.getImageByProductName(productName);
    if(img != null) {
      response = ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, img.getContentType())
        .body(img.getData());
    } else {
      response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    logger.info(response.toString());
    return response;
  }

  /**
   * Delete image from database
   * @param id ID of image to be deleted
   * @return HTTP OK on success, NOT FOUND if image not found.
   */
  @DeleteMapping("/images/{id}")
  public ResponseEntity<String> delete(@PathVariable Integer id) {
    ResponseEntity<String> response;
    if(imgService.deleteImageByID(id)) {
      response = ResponseEntity.ok("");
    } else {
      response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    return response;
  }
}
