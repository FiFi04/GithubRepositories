package pl.rg.GithubRepositories.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {

    private int status;

    private String message;

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
