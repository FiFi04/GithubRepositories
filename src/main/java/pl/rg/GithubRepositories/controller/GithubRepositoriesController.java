package pl.rg.GithubRepositories.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rg.GithubRepositories.service.GithubRepositoriesService;

@RestController
@RequestMapping("/api/github")
public class GithubRepositoriesController {

    private final GithubRepositoriesService githubService;

    @Autowired
    public GithubRepositoriesController(GithubRepositoriesService githubRepositoriesService) {
        this.githubService = githubRepositoriesService;
    }

    @GetMapping(value = "/repositories/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserRepositories(@PathVariable String username, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {
        return ResponseEntity.ok(githubService.getRepositories(username, acceptHeader));
    }
}
