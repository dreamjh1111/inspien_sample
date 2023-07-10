package inspien.weather.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CitiesList {
    @Id
    private Long id;
    private String name;
    private Float lon;
    private Float lat;
    private String country;
}



