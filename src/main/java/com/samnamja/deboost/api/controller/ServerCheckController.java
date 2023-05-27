package com.samnamja.deboost.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServerCheckController {
    private final Environment env;
    @Value("${riot.txt}")
    private String riotTxt;

    @GetMapping("/profile")
    public String profile(){
        List<String> profile = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profile.isEmpty() ? "default" : profile.get(0);

        return profile.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

    @GetMapping("/riot.txt")
    public String riotTxt(){
        return riotTxt;
    }
}
