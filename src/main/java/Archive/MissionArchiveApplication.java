package Archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "Entities")
public class MissionArchiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(MissionArchiveApplication.class, args);
    }
}
