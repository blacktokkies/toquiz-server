package blacktokkies.toquiz.domain.activeinfo.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "toquiz-member")
@Getter
@NoArgsConstructor
public class ActiveInfo {
    @Id
    String id;
    private final Map<String, ActivePanel> activePanels = new HashMap<>();
}
