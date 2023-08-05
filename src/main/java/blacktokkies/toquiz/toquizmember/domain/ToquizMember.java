package blacktokkies.toquiz.toquizmember.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "toquiz-member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToquizMember {
    @Id
    private String id;

    private List<ActivePanel> activePanels;
}
