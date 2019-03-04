package fun.hercules.order.order.business.acg.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import fun.hercules.order.order.business.acg.domain.AcgAirport;
import fun.hercules.order.order.business.acg.repository.AirportRepository;
import fun.hercules.order.order.common.cache.CacheNotLoadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AcgAirportService {
    private static String[] OTHER_AIRPORT_IDS = {"XXX"};

    private final AirportRepository airportRepository;

    private final Cache<String, AcgAirport> airportCache;


    public AcgAirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
        airportCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterAccess(60, TimeUnit.MINUTES)
                .build();
    }

    public List<AcgAirport> list() {
        return airportRepository.findAll().stream()
                .filter(acgAirport -> !acgAirport.isDeleted() && !Arrays.asList(OTHER_AIRPORT_IDS).contains(acgAirport.getAirportId()))
                .collect(Collectors.toList());
    }

    public String getAirportName(String airportId) {
        return findById(airportId)
                .map(airport -> airport.getAirportName())
                .orElse(airportId);
    }

    public Optional<AcgAirport> findById(String airportId) {
        try {
            return Optional.ofNullable(airportCache.get(airportId, () -> Optional.of(airportRepository.findByAirportIdIs(airportId))
                    .orElseThrow(CacheNotLoadException::new)));
        } catch (UncheckedExecutionException | CacheNotLoadException e) {
            return Optional.empty();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean getAboard(String airportId) {
        return findById(airportId)
                .map(airport -> airport.getAbroad())
                .orElse(false);
    }
}
