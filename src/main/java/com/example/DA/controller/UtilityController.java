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


    @GetMapping("/near/{propertyId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UtilityDTO>> getNearUtilities(
            @PathVariable Integer propertyId,
            @RequestParam("type") String type) {

        List<UtilityDTO> utilityDTOs = utilityService.getNearbyUtilities(propertyId, type);

        if (utilityDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content if no utilities are found
        }

        return new ResponseEntity<>(utilityDTOs, HttpStatus.OK);
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

    @GetMapping("/nearby-schools/{propertyId}")
    public ResponseEntity<List<UtilityDTO>> getNearbySchools(@PathVariable Integer propertyId) {
        List<UtilityDTO> nearbySchools = utilityService.getNearbySchools(propertyId);
        return ResponseEntity.ok(nearbySchools);
    }

    @GetMapping("/nearby-hospitals/{propertyId}")
    public ResponseEntity<List<UtilityDTO>> getNearbyHospitals(@PathVariable Integer propertyId) {
        List<UtilityDTO> nearbyHospitals = utilityService.getNearbyHospitals(propertyId);
        return ResponseEntity.ok(nearbyHospitals);
    }

}
