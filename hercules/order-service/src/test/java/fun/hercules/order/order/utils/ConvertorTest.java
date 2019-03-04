package fun.hercules.order.order.utils;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class ConvertorTest extends OrderIntegrationTestBase {

    @Test
    public void should_convert_country_name_to_code() throws Exception {
        assertThat(RegionCodeConverter.country2Code("中国大陆"), is("CN"));
        assertThat(RegionCodeConverter.country2Code("香港"), is("HK"));
    }

    @Test
    public void should_convert_country_code_to_name() throws Exception {
        assertThat(RegionCodeConverter.code2Country("CN"), is("中国大陆"));
        assertThat(RegionCodeConverter.code2Country("HK"), is("香港"));
    }

    @Test
    public void should_convert_province_code_to_name() throws Exception {
        assertThat(RegionCodeConverter.code2Province("11"), is("北京市"));
        assertThat(RegionCodeConverter.code2Province("61"), is("陕西省"));
    }

    @Test
    public void should_convert_province_name_to_code() throws Exception {
        assertThat(RegionCodeConverter.province2Code("北京市"), is("11"));
        assertThat(RegionCodeConverter.province2Code("陕西省"), is("61"));
    }

    @Test
    public void should_convert_city_code_to_name() throws Exception {
        assertThat(RegionCodeConverter.code2City("4602"), is("三亚市"));
        assertThat(RegionCodeConverter.code2City("6101"), is("西安市"));
    }

    @Test
    public void should_convert_city_name_to_code() throws Exception {
        assertThat(RegionCodeConverter.city2Code("三亚市"), is("4602"));
        assertThat(RegionCodeConverter.city2Code("西安市"), is("6101"));
    }

    @Test
    public void should_convert_district_code_to_name() throws Exception {
        assertThat(RegionCodeConverter.code2District("460107"), is("琼山区"));
        assertThat(RegionCodeConverter.code2District("610113"), is("雁塔区"));
    }

    @Test
    public void should_convert_district_name_to_code() throws Exception {
        assertThat(RegionCodeConverter.district2Code("琼山区"), is("460107"));
        assertThat(RegionCodeConverter.district2Code("雁塔区"), is("610113"));
    }
}