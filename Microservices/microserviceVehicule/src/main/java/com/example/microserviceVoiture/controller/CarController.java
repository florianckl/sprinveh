package com.example.microserviceVoiture.controller;

import com.example.microserviceVoiture.exception.CarException;
import com.example.microserviceVoiture.model.Car;
import com.example.microserviceVoiture.model.FileResponse;

import java.io.FileNotFoundException;
import java.util.List;

import com.example.microserviceVoiture.stockage.FileSystemStorageService;
import com.example.microserviceVoiture.repository.CarRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;

import org.springframework.hateoas.IanaLinkRelations;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CarController {

    @Autowired
    private final CarRepository repository;
    @Autowired
    private FileSystemStorageService fileSytemStorage;

    private final CarModelAssembler assembler;

    public CarController(CarRepository repository, CarModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/vehicules")
    JSONArray all() {
        RestTemplate restTemplate = new RestTemplate();
        JSONArray result1 = restTemplate.getForObject("http://localhost:8081/cars", JSONArray.class);
        JSONArray result2 = restTemplate.getForObject("http://localhost:8082/motos", JSONArray.class);
        result1.addAll(result2);
        return result1;
    }

    @GetMapping("/cars/sortByPrix")
    JSONArray allSortByPrix() {
        RestTemplate restTemplate = new RestTemplate();
        JSONArray result1 = restTemplate.getForObject("http://localhost:8081/cars/sortByPrix", JSONArray.class);
        JSONArray result2 = restTemplate.getForObject("http://localhost:8082/motos/sortByPrix", JSONArray.class);
        result1.addAll(result2);
        return result1;
    }

    @GetMapping("/cars/sortByMarque")
    JSONArray allSortByMarque() {
        RestTemplate restTemplate = new RestTemplate();
        JSONArray result1 = restTemplate.getForObject("http://localhost:8081/cars/sortByMarque", JSONArray.class);
        JSONArray result2 = restTemplate.getForObject("http://localhost:8082/motos/sortByMarque", JSONArray.class);
        result1.addAll(result2);
        return result1;
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws FileNotFoundException {

        Resource resource = fileSytemStorage.loadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/add")
    ResponseEntity<?> newcar(@RequestBody Object nouvVeh) {
        Logger log = LoggerFactory.getLogger(CarController.class);
        RestTemplate restTemplate = new RestTemplate();
        if (nouvVeh.toString().contains("type:voiture")){
            log.info("voiture");
            return restTemplate.postForEntity ("http://localhost:8081/add",nouvVeh,Object.class);
        }
        else{
            return restTemplate.postForEntity("http://localhost:8082/add", nouvVeh, Object.class);
        }
    }

    @PostMapping("/uploadfile")
    public ResponseEntity<FileResponse> uploadSingleFile (@RequestParam("file") MultipartFile file) {
        String upfile = fileSytemStorage.saveFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(upfile)
                .toUriString();

        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(upfile,fileDownloadUri,"File uploaded with success!"));
    }

    @PutMapping("/cars/{id}")
    ResponseEntity<?> replaceVoiture(@RequestBody Car nouvCar, @PathVariable Long id) {

         Car majCar = repository.findById(id) //
                .map(car -> {
                    car.setMarque(nouvCar.getMarque());
                    car.setNom(nouvCar.getNom());
                    car.setPrix(nouvCar.getPrix());
                    return repository.save(car);
                }) //
                .orElseGet(() -> {
                    nouvCar.setId(id);
                    return repository.save(nouvCar);
                });

        EntityModel< Car> entityModel = assembler.toModel(majCar);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @GetMapping("/cars/{id}")
    EntityModel<?> one(@PathVariable String id) {
        String type=id.substring(0, 1);
        long idV=Long.parseLong(id.substring(1,2));
        RestTemplate restTemplate = new RestTemplate();
        if (type=="v"){
            return restTemplate.getForObject("http://localhost:8081/cars/{id}" ,EntityModel.class);
        }
        else{
            return restTemplate.getForObject("http://localhost:8082/motos/{id}",, EntityModel.class);
        }
    }

    @DeleteMapping("/cars/delete/{id}")
    ResponseEntity<?> suppVoiture(@PathVariable String id) {
        String type=id.substring(0, 1);
        long idV=Long.parseLong(id.substring(1,2));
        RestTemplate restTemplate = new RestTemplate();
        if (type=="v"){
            restTemplate.delete("http://localhost:8081/cars/delete/{id}",idV);
            return ResponseEntity.noContent().build();
        }
        else{
            restTemplate.delete("http://localhost:8082/cars/delete/{id}", idV);
            return ResponseEntity.noContent().build();
        }

    }
}