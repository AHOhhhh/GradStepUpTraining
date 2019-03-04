package fun.hercules.order.order.business.acg.repository;

import fun.hercules.order.order.business.acg.domain.AcgAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<AcgAirport, String> {
    AcgAirport findByAirportIdIs(String airportId);
}
