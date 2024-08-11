package pl.rg.GithubRepositories.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.rg.GithubRepositories.exception.GlobalExceptionHandler;
import pl.rg.GithubRepositories.exception.UserNotFoundException;
import pl.rg.GithubRepositories.model.RepositoryDto;
import pl.rg.GithubRepositories.service.GithubRepositoriesService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GithubRepositoriesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GithubRepositoriesService githubService;

    @InjectMocks
    private GithubRepositoriesController githubController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(githubController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void shouldReturnNonForkRepositoriesWhenAcceptHeaderIsJson() throws Exception {
        List<RepositoryDto> repositories = List.of(new RepositoryDto("JDBC", "FiFi04", new ArrayList<>(), false));

        when(githubService.getRepositories("FiFi04", MediaType.APPLICATION_JSON_VALUE)).thenReturn(repositories);

        mockMvc.perform(get("/api/github/repositories/FiFi04")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("JDBC"));

        verify(githubService, times(1)).getRepositories("FiFi04", MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void shouldReturnNotAcceptableWhenAcceptHeaderIsNotJson() throws Exception {
        mockMvc.perform(get("/api/github/repositories/FiFi04")
                        .header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotAcceptable());

        verify(githubService, times(0)).getRepositories(anyString(), anyString());
    }

    @Test
    public void shouldReturnNotFoundWhenUserNotExist() throws Exception {
        when(githubService.getRepositories("DX_NonExistingUser_PX", MediaType.APPLICATION_JSON_VALUE))
                .thenThrow(new UserNotFoundException("User not found: DX_NonExistingUser_PX", new RuntimeException()));

        mockMvc.perform(get("/api/github/repositories/DX_NonExistingUser_PX")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("User not found: DX_NonExistingUser_PX"));

        verify(githubService, times(1)).getRepositories("DX_NonExistingUser_PX", MediaType.APPLICATION_JSON_VALUE);
    }
}