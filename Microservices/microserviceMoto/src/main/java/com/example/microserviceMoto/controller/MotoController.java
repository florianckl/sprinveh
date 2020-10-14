package com.example.microserviceMoto.controller;

import com.example.microserviceMoto.exception.MotoException;
import com.example.microserviceMoto.model.FileResponse;

import java.io.FileNotFoundException;
import java.util.List;

import com.example.microserviceMoto.model.Moto;
import com.example.microserviceMoto.repository.MotoRepository;
import com.example.microserviceMoto.stockage.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;

import org.springframework.hateoas.IanaLinkRelations;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class MotoController {

    @Autowired
    private final MotoRepository repository;
    @Autowired
    private FileSystemStorageService fileSytemStorage;

    private final MotoModelAssembler assembler;

    public MotoController(MotoRepository repository, MotoModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/motos")
    List<Moto> all() {
        List<Moto> motos = (List<Moto>) repository.findAll();
        return motos;
    }

    @GetMapping("/motos/sortByPrix")
    List<Moto> allSortByPrix() {
        List<Moto> motos = (List<Moto>) repository.findByOrderByPrix();
        return motos;
    }

    @GetMapping("/motos/sortByMarque")
    List<Moto> allSortByMarque() {
        List<Moto> motos = (List<Moto>) repository.findByOrderByMarque();
        return motos;
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws FileNotFoundException {

        Resource resource = fileSytemStorage.loadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/add")
    ResponseEntity<?> newmoto(@RequestBody Moto nouvMoto) {

        EntityModel<Moto> entityModel = assembler.toModel(repository.save(nouvMoto));

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

    @PutMapping("/motos/{id}")
    ResponseEntity<?> replaceVoiture(@RequestBody Moto nouvMoto, @PathVariable Long id) {

         Moto majMoto = repository.findById(id) //
                .map(moto -> {
                    moto.setMarque(nouvMoto.getMarque());
                    moto.setNom(nouvMoto.getNom());
                    moto.setPrix(nouvMoto.getPrix());
                    return repository.save(moto);
                }) //
                .orElseGet(() -> {
                    nouvMoto.setId(id);
                    return repository.save(nouvMoto);
                });

        EntityModel<Moto> entityModel = assembler.toModel(majMoto);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @GetMapping("/motos/{id}")
    EntityModel<Moto> one(@PathVariable Long id) {
         Moto moto = repository.findById(id) //
                .orElseThrow(() -> new MotoException(id));
        return assembler.toModel(moto);
    }

    @DeleteMapping("/motos/delete/{id}")
    ResponseEntity<?> suppMoto(@PathVariable Long id) {
        fileSytemStorage.deleteFile(repository.findById(id).get().getImage());
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}