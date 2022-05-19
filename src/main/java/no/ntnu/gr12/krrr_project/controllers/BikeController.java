package no.ntnu.gr12.krrr_project.controllers;

import no.ntnu.gr12.krrr_project.services.BikeService;
import no.ntnu.gr12.krrr_project.models.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @GetMapping("/bikes")
    public List<Bike> getBikes(){
        return StreamSupport
                        .stream(bikeService.readBikes()
                        .spliterator(),false)
                        .collect(Collectors.toList());
    }

    @GetMapping("/bikes/{id}")
    public Bike getBike(@PathVariable String id){
        for (Bike bikeFound : bikeService.readBikes()) {
            if (bikeFound.getBikeId().equals(id)) {
                return bikeFound;
            }
        }
        return null;
    }

    @GetMapping("/bikes/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) throws IOException{
        File img = new File("src/main/resources/"+id+".jpg");
        if(!img.exists()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
        }

    }

    @PostMapping("/bikes/addBike")
    public void addBike(@RequestBody Bike bike){
        bikeService.createBike(bike);
    }

    @PutMapping("bikes/updateBike/")
    public void updateBike(@RequestBody Bike bike){
        bikeService.updateBike(bike);
        }

    @DeleteMapping("bikes/deleteBike")
    public void deleteBike(@RequestBody Bike bike){
                bikeService.deleteBike(bike);
    }
}