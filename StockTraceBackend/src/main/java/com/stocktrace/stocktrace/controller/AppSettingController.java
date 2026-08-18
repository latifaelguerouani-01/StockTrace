package com.stocktrace.stocktrace.controller;

import com.stocktrace.stocktrace.entity.AppSetting;
import com.stocktrace.stocktrace.repository.AppSettingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class AppSettingController {

    private final AppSettingRepository appSettingRepository;

    public AppSettingController(AppSettingRepository appSettingRepository) {
        this.appSettingRepository = appSettingRepository;
    }

    @GetMapping
    public ResponseEntity<List<AppSetting>> getAllSettings() {
        return ResponseEntity.ok(appSettingRepository.findAll());
    }

    @PutMapping
    public ResponseEntity<AppSetting> updateSetting(@RequestBody AppSetting setting) {
        return ResponseEntity.ok(appSettingRepository.save(setting));
    }
}