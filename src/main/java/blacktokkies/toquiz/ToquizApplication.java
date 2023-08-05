package blacktokkies.toquiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ToquizApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToquizApplication.class, args);
    }

}
