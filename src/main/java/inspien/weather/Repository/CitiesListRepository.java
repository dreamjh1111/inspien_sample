package inspien.weather.Repository;

import inspien.weather.Entity.CitiesList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitiesListRepository extends JpaRepository<CitiesList, Long> {
}
