package blacktokkies.toquiz.domain.activeinfo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "toquiz-member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActiveInfo {
    @Id
    private String id;

    @Field("active_panels")
    private Map<String, ActivePanel> activePanels = new HashMap<>();
}
