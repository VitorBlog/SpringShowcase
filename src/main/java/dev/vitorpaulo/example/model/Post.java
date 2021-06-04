package dev.vitorpaulo.example.model;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Lob
    @NotBlank(message = "Invalid title")
    private String description;

    @Nullable
    private String image;

    private int votes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private User author;

    private String url;

    private Instant created;

    public ObjectNode toJson() {

        ObjectNode json = JsonMapper.builder().findAndAddModules().build().createObjectNode();

        json.put("id", this.getId());
        json.put("description", this.getDescription());
        json.put("image", this.getImage());
        json.put("votes", this.getVotes());
        json.put("author", this.getAuthor().toJson(false));
        json.put("url", this.getUrl());
        json.put("created", this.getCreated().toEpochMilli());
        json.put("userVote", false);

        return json;

    }

}
