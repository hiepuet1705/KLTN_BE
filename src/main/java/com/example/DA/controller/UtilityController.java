package com.example.DA.controller;


import com.example.DA.dto.UtilityDTO;
import com.example.DA.model.Utility;
import com.example.DA.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilities")
public class UtilityController {
    @Autowired
    private UtilityService utilityService;

    @GetMapping
    public List<UtilityDTO> getAllUtilities() {
        return utilityService.getAllUtilities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilityDTO> getUtilityById(@PathVariable Integer id) {
        UtilityDTO utilityDTO = utilityService.getUtilityById(id);
        return utilityDTO != null ? ResponseEntity.ok(utilityDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public UtilityDTO createUtility(@RequestBody UtilityDTO utilityDTO) {
        return utilityService.createUtility(utilityDTO);
    }

    @GetMapping("/near/{propertyId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UtilityDTO>> getNearUtilities(@PathVariable Integer propertyId) {
        List<UtilityDTO> utilityDTOs = utilityService.getNearbyUtilities(propertyId);
        return new ResponseEntity<>(utilityDTOs, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtility(@PathVariable Integer id) {
        utilityService.deleteUtility(id);
        return ResponseEntity.noContent().build();
    }
}
