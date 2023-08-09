package blacktokkies.toquiz.activeinfo;

import blacktokkies.toquiz.activeinfo.domain.ActiveInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveInfoRepository extends MongoRepository<ActiveInfo, String> {
}
