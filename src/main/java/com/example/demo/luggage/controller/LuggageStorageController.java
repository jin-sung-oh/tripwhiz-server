package com.example.demo.luggage.controller;

import com.example.demo.luggage.dto.LuggageStorageDTO;
import com.example.demo.luggage.entity.LuggageStorage;
import com.example.demo.luggage.entity.LuggageStorageStatus;
import com.example.demo.luggage.service.LuggageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storeowner/luggagestorage")
@RequiredArgsConstructor
public class LuggageStorageController {

    private final LuggageStorageService luggageStorageService;

    @PostMapping("/create")
    public ResponseEntity<LuggageStorageDTO> createLuggageStorage(@RequestBody LuggageStorage luggageStorage) {
        LuggageStorageDTO luggageStorageDTO = luggageStorageService.createLuggageStorage(luggageStorage);
        return ResponseEntity.ok(luggageStorageDTO);
    }

    @GetMapping("/{lsno}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<LuggageStorageDTO> getLuggageStorageDetails(@PathVariable Long lsno) {
        LuggageStorageDTO luggageStorageDTO = luggageStorageService.getLuggageStorageDetails(lsno);
        return ResponseEntity.ok(luggageStorageDTO);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<List<LuggageStorageDTO>> getAllLuggageStorages() {
        List<LuggageStorageDTO> luggageStorages = luggageStorageService.getAllLuggageStorages();
        return ResponseEntity.ok(luggageStorages);
    }

    @PutMapping("/{lsno}/status")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<Void> updateStorageStatus(@PathVariable Long lsno, @RequestParam LuggageStorageStatus status) {
        luggageStorageService.updateStorageStatus(lsno, status);
        return ResponseEntity.ok().build();
    }
}
