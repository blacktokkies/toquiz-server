package blacktokkies.toquiz.domain.panel.dto;

import blacktokkies.toquiz.domain.panel.domain.Panel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanelResponse {
    private String sid;
    private String title;
    private String description;
    private Author author;
    private long scarpNum;
    private boolean archived;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    public static PanelResponse from(Panel panel){
        return PanelResponse.builder()
            .sid(panel.getSid())
            .title(panel.getTitle())
            .description(panel.getDescription())
            .author(new Author(panel.getMember().getId(), panel.getMember().getNickname()))
            .archived(panel.isArchived())
            .scarpNum(panel.getScarpNum())
            .createdAt(panel.getCreatedDate())
            .updatedAt(panel.getUpdatedDate())
            .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Author{
        private Long id;
        private String nickname;
    }
}
