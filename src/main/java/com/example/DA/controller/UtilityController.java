package com.example.DA.controller;


import com.example.DA.dto.UtilityDTO;
import com.example.DA.model.Utility;
import com.example.DA.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{id}")
    public ResponseEntity<UtilityDTO> updateUtility(@PathVariable Integer id, @RequestBody UtilityDTO utilityDTO) {
        UtilityDTO updatedUtility = utilityService.updateUtility(id, utilityDTO);
        return updatedUtility != null ? ResponseEntity.ok(updatedUtility) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtility(@PathVariable Integer id) {
        utilityService.deleteUtility(id);
        return ResponseEntity.noContent().build();
    }
}
