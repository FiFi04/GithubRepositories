package pl.rg.GithubRepositories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.rg.GithubRepositories.exception.InvalidHeaderException;
import pl.rg.GithubRepositories.exception.UserNotFoundException;
import pl.rg.GithubRepositories.model.BranchDto;
import pl.rg.GithubRepositories.model.RepositoryDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GithubRepositoriesService {

    private final static String GITHUB_API_URL = "https://api.github.com";

    private final RestTemplate restTemplate;

    @Autowired
    public GithubRepositoriesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepositoryDto> getRepositories(String username, String acceptHeader) {
        if (!MediaType.APPLICATION_JSON_VALUE.equals(acceptHeader)) {
            throw new InvalidHeaderException("Not accepted response type. Only 'Accept: application/json' is supported");
        }
        try {
            String url = GITHUB_API_URL + "/users/" + username + "/repos";
            ResponseEntity<RepositoryDto[]> response = restTemplate.exchange(url, HttpMethod.GET, null, RepositoryDto[].class);
            return filterNonForkRepositories(List.of(response.getBody()));
        } catch (HttpClientErrorException.NotFound exception) {
            throw new UserNotFoundException("User not found: " + username, exception);
        }
    }

    private List<RepositoryDto> filterNonForkRepositories(List<RepositoryDto> repositories) {
        return repositories.stream()
                .filter(repository -> !repository.isFork())
                .peek(repository -> {
                    BranchDto[] branches = getBranches(repository.getLogin(), repository.getName());
                    repository.setBranches(List.of(branches));
                })
                .collect(Collectors.toList());
    }

    private BranchDto[] getBranches(String owner, String repository) {
        String url = GITHUB_API_URL + "/repos/" + owner + "/" + repository + "/branches";
        ResponseEntity<BranchDto[]> response = restTemplate.exchange(url, HttpMethod.GET, null, BranchDto[].class);
        return response.getBody();
    }
}