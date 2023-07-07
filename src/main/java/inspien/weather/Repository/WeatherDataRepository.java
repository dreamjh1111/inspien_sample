package inspien.weather.Repository;

import inspien.weather.Entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    Optional<WeatherData> findTopByCityOrderByCreatedAtDesc(String city);
}
