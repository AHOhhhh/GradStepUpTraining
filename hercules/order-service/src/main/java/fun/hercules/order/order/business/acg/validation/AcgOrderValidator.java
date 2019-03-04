package fun.hercules.order.order.business.acg.validation;

import fun.hercules.order.order.business.acg.domain.AcgAirport;
import fun.hercules.order.order.business.acg.domain.Goods;
import fun.hercules.order.order.business.acg.domain.PriceRequest;
import fun.hercules.order.order.business.acg.repository.AirportRepository;
import fun.hercules.order.order.common.dto.Contact;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
public class AcgOrderValidator {

    private AirportRepository airportRepository;

    public AcgOrderValidator(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public void validatePriceRequest(PriceRequest request) {
        validateGoods(request);
        validateDate(request.getEstimatedDeliveryTime());
        validateAirport(request);
        validateAddress(request);
    }

    private void validateDate(LocalDateTime estimatedDeliveryTime) {
        if (null == estimatedDeliveryTime) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "missing estimatedDeliveryTime");
        }
    }

    private void validateGoods(PriceRequest request) {
        List<Goods> goods = request.getGoods();
        if (goods == null) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "missing goods");
        }

        for (Goods good : goods) {
            if (good.getPackageQuantity() == null || good.getPackageQuantity() < 1) {
                throw new BadRequestException(ErrorCode.INVALID_REQUEST, "packageQuantity should greater than 0");
            }
        }
    }

    private void validateAirport(PriceRequest request) {
        validateAirport(request, PriceRequest::getDeparture, AcgAirport::getPickup, "pick up");
        validateAirport(request, PriceRequest::getArrival, AcgAirport::getDelivery, "drop off");
    }

    private void validateAirport(PriceRequest request,
                                 Function<PriceRequest, PriceRequest.Airport> airportGetter,
                                 Predicate<AcgAirport> needDelivery,
                                 String service) {
        PriceRequest.Airport airport = airportGetter.apply(request);
        Optional.ofNullable(airport).orElseThrow(() ->
                new BadRequestException(ErrorCode.INVALID_REQUEST, "missing airport"));

        String departureAirportId = airport.getAirportId();
        AcgAirport existedDeparture = airportRepository.findByAirportIdIs(departureAirportId);
        Optional.ofNullable(existedDeparture).orElseThrow(() ->
                new BadRequestException(ErrorCode.INVALID_REQUEST, String.format("Have no this airport %s", departureAirportId)));

        if (airport.isDelivery() && !needDelivery.test(existedDeparture)) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST,
                    String.format("Airport %s have no %s service", departureAirportId, service));
        }
    }

    private void validateAddress(PriceRequest request) {
        validateAddress(request, PriceRequest::getDeparture,
                PriceRequest::getPickUpAddress, "missing pick up address");
        validateAddress(request, PriceRequest::getArrival,
                PriceRequest::getDropOffAddress, "missing drop off address");
    }

    private void validateAddress(PriceRequest request,
                                 Function<PriceRequest, PriceRequest.Airport> airportGetter,
                                 Function<PriceRequest, Contact> addressGetter,
                                 String message) {
        if (airportGetter.apply(request).isDelivery()) {
            validate(addressGetter.apply(request), () -> new BadRequestException(ErrorCode.INVALID_REQUEST, message));
        }
    }

    private void validate(Contact address, Supplier<BadRequestException> exception) {
        Optional.ofNullable(address).orElseThrow(exception);
        validateAddressField(address);
    }

    private void validateAddressField(Contact contact) {
        ensureNotEmpty(contact.getCellphone(), "missing mobile phone number");
        ensureNotEmpty(contact.getName(), "missing name");
        ensureNotEmpty(contact.getPostcode(), "missing post code");
        ensureNotEmpty(contact.getCountryAbbr(), "missing country abbreviation");
        ensureNotEmpty(contact.getProvinceId(), "missing province id");
        ensureNotEmpty(contact.getCityId(), "missing city id");
        ensureNotEmpty(contact.getDistrictId(), "missing district id");
        ensureNotEmpty(contact.getAddress(), "missing address detail");
    }

    private void ensureNotEmpty(String content, String message) {
        if (StringUtils.isEmpty(content)) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, message);
        }
    }
}
