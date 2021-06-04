package dev.vitorpaulo.example.model;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Invalid name")
    private String name;

    @NotBlank(message = "Invalid username")
    private String username;

    @NotBlank(message = "Invalid password")
    private String password;

    @Email @NotBlank(message = "Invalid email")
    private String email;

    private Instant created;

    public ObjectNode toJson(Boolean showPassword) {

        ObjectNode json = JsonMapper.builder().findAndAddModules().build().createObjectNode();

        json.put("id", this.getId());
        json.put("name", this.getName());
        json.put("username", this.getUsername());
        json.put("email", this.getEmail());
        json.put("created", this.getCreated().toEpochMilli());

        if (showPassword) json.put("password", this.getPassword());

        return json;

    }

}
