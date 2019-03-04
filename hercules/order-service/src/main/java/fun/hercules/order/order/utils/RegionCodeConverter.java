package fun.hercules.order.order.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static fun.hercules.order.order.utils.ResourceUtils.loadResource;

public class RegionCodeConverter {

    private static Object getCountryInfos() {
        return JsonUtils.unmarshal(loadResource("/fixtures/countryCode.json"),
                new TypeReference<List<CountryInfo>>() {
                });
    }

    private static Object getDistrictInfo() {
        return JsonUtils.unmarshal(loadResource("/fixtures/districtCode.json"),
                new TypeReference<List<DistrictInfo>>() {
                });
    }

    public static String country2Code(String countryName) {
        for (CountryInfo countryInfo : (ArrayList<CountryInfo>) getCountryInfos()) {
            if (countryInfo.getCountry().equals(countryName)) {
                return countryInfo.getCode();
            }
        }
        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }

    public static String code2Country(String countryCode) {
        for (CountryInfo countryInfo : (ArrayList<CountryInfo>) getCountryInfos()) {
            if (countryInfo.getCode().equals(countryCode)) {
                return countryInfo.getCountry();
            }
        }
        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }

    public static String province2Code(String provinceName) {
        for (DistrictInfo provinceInfo : (ArrayList<DistrictInfo>) getDistrictInfo()) {
            if (provinceInfo.getLabel().equals(provinceName)) {
                return provinceInfo.getValue();
            }
        }
        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }

    public static String code2Province(String provinceCode) {
        for (DistrictInfo provinceInfo : (ArrayList<DistrictInfo>) getDistrictInfo()) {
            if (provinceInfo.getValue().equals(provinceCode)) {
                return provinceInfo.getLabel();
            }
        }
        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }

    public static String city2Code(String cityName) {
        for (DistrictInfo provinceInfo : (ArrayList<DistrictInfo>) getDistrictInfo()) {
            for (DistrictInfo cityInfo : provinceInfo.getChildren()) {
                if (cityInfo.getLabel().equals(cityName)) {
                    return cityInfo.getValue();
                }
            }
        }
        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }

    public static String code2City(String cityCode) {
        String provinceCode = cityCode.substring(0, 2);
        List<DistrictInfo> cityInfos = new ArrayList<>();

        for (DistrictInfo districtInfo : (ArrayList<DistrictInfo>) getDistrictInfo()) {
            if (districtInfo.getValue().equals(provinceCode)) {
                cityInfos = districtInfo.getChildren();
            }
        }

        for (DistrictInfo cityInfo : cityInfos) {
            if (cityInfo.getValue().equals(cityCode)) {
                return cityInfo.getLabel();
            }
        }
        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }

    public static String district2Code(String districtName) {
        for (DistrictInfo provinceInfo : (ArrayList<DistrictInfo>) getDistrictInfo()) {
            for (DistrictInfo cityInfo : provinceInfo.getChildren()) {
                for (DistrictInfo districtInfo : cityInfo.getChildren()) {
                    if (districtInfo.getLabel().equals(districtName)) {
                        return districtInfo.getValue();
                    }
                }
            }
        }
        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }

    public static String code2District(String districtCode) {
        String provinceCode = districtCode.substring(0, 2);
        String cityCode = districtCode.substring(0, 4);
        List<DistrictInfo> cityInfos = new ArrayList<>();
        List<DistrictInfo> districtInfos = new ArrayList<>();

        for (DistrictInfo provinceInfo : (ArrayList<DistrictInfo>) getDistrictInfo()) {
            if (provinceInfo.getValue().equals(provinceCode)) {
                cityInfos = provinceInfo.getChildren();
            }
        }

        for (DistrictInfo cityInfo : cityInfos) {
            if (cityInfo.getValue().equals(cityCode)) {
                districtInfos = cityInfo.getChildren();
            }
        }

        for (DistrictInfo districtInfo : districtInfos) {
            if (districtInfo.getValue().equals(districtCode)) {
                return districtInfo.getLabel();
            }
        }

        throw new NotFoundException(ErrorCode.REGION_CODE_NOT_FOUND);
    }
}