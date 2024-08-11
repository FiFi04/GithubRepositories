package pl.rg.GithubRepositories.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepositoryDto {

    private String name;

    private String login;

    private List<BranchDto> branches;

    @JsonProperty("fork")
    private boolean isFork;

    @JsonProperty("owner")
    private void setLogin(JsonNode owner) {
        this.login = owner.get("login").asText();
    }
}
