package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class UserData {
    private String name;
    private String password;
    private String email;
    private String job;
}
