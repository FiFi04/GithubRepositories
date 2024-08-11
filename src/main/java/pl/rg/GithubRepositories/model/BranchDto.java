package pl.rg.GithubRepositories.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchDto {

    private String name;

    private String sha;

    @JsonProperty("commit")
    private void setLastCommitSha(JsonNode commit) {
        this.sha = commit.get("sha").asText();
    }
}
