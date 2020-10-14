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
    List<Car> allSortByPrix() {
        List<Car> cars = (List<Car>) repository.findByOrderByPrix();
        return cars;
    }

    @GetMapping("/cars/sortByMarque")
    List<Car> allSortByMarque() {
        List<Car> cars = (List<Car>) repository.findByOrderByMarque();
        return cars;
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws FileNotFoundException {

        Resource resource = fileSytemStorage.loadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/add")
    ResponseEntity<?> newcar(@RequestBody JSONPObject nouvCar) {
        if (nouvCar.getFunction().contains("type:voiture")){
            RestTemplate restTemplate = new RestTemplate();
            EntityModel<> entityModel
            restTemplate.patchForObject("http://localhost:8081/add",entityModel.class)
        }
        EntityModel<Car> entityModel = assembler.toModel(repository.save(nouvCar));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
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
    EntityModel< Car> one(@PathVariable Long id) {
         Car car = repository.findById(id) //
                .orElseThrow(() -> new CarException(id));
        return assembler.toModel(car);
    }

    @DeleteMapping("/cars/delete/{id}")
    ResponseEntity<?> suppVoiture(@PathVariable Long id) {
        fileSytemStorage.deleteFile(repository.findById(id).get().getImage());
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}