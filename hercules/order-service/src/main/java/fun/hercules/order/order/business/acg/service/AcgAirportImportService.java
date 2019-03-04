package fun.hercules.order.order.business.acg.service;

import fun.hercules.order.order.business.acg.domain.AcgAirport;
import fun.hercules.order.order.business.acg.exception.AcgAirportFileNotFoundException;
import fun.hercules.order.order.business.acg.exception.CSVImportException;
import fun.hercules.order.order.business.acg.repository.AirportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class AcgAirportImportService {
    private AirportRepository airportRepository;

    public AcgAirportImportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    /*
    * Initialise / update the airport info in db
    *
    * @param airportInfoFile: csv format (header) - 城市中文名称,城市英文名称,城市拼音,城市拼音缩写,机场三字码,国家名称,所属洲,国家英文名
    * @param airportNameFile: csv format (header) - 机场三字码,机场名称,机场英文名,机场拼音,机场拼音缩写,是否为国内(1: 国内, 2: 国外)
    *
    * Initial airport csv files locate in /src/main/resources/*.csv
    */
    public void execute(MultipartFile airportInfoFile, MultipartFile airportNameFile) {
        try {
            byte[] airportInfoFileBytes = airportInfoFile.getBytes();
            ArrayList<String> airportInfo = new ArrayList<>(Arrays.asList(new String(airportInfoFileBytes, Charset.defaultCharset()).split("\n")));
            removeHeader(airportInfo);

            byte[] airportNameFileBytes = airportNameFile.getBytes();
            ArrayList<String> airportNames = new ArrayList<>(Arrays.asList(new String(airportNameFileBytes, Charset.defaultCharset()).split("\n")));
            removeHeader(airportNames);

            getAcgAirports(airportNames, airportInfo);
        } catch (IOException e) {
            throw new CSVImportException("invalid format", e);
        }
    }

    private void getAcgAirports(ArrayList<String> airportNames, ArrayList<String> airportInfo) {
        airportNames.stream().forEach(airportNameInfo -> {
            String[] columns = airportNameInfo.split(",");
            String airportId = columns[0];
            AcgAirport acgAirport = getByAirportId(airportId);
            boolean isInternational = isInternational(columns[5]);
            acgAirport.setAirportName(columns[1]);
            acgAirport.setAbroad(isInternational);
            acgAirport.setDelivery(!isInternational);
            acgAirport.setPickup(!isInternational);
            acgAirport.setAirportId(airportId);
            acgAirport.setName(columns[3]);

            Optional<String[]> matchedAirportInfo = airportInfo.stream()
                    .map(airportInfoItem -> airportInfoItem.split(","))
                    .filter(airportInfoLine -> airportInfoLine[4].equals(airportId))
                    .findFirst();

            if (!matchedAirportInfo.equals(Optional.empty())) {
                acgAirport.setCity(matchedAirportInfo.get()[0]);
            } else {
                acgAirport.setCity(columns[1]);
            }
            
            try {
                airportRepository.save(acgAirport);
            } catch (Exception e) {
                log.error("import airport error: city - {}, id - {}", acgAirport.getCity(), acgAirport.getAirportId());
            }
        });
    }

    @Profile("!test")
    @Component
    public static class Injector {
        private AcgAirportImportService importService;

        public Injector(AcgAirportImportService importService) {
            this.importService = importService;
        }

        @PostConstruct
        public void initialDataImport() {
            try {
                ArrayList<String> airportNames = readFromFile("/airport_names.csv");
                ArrayList<String> airportInfo = readFromFile("/airport_info.csv");
                importService.getAcgAirports(airportNames, airportInfo);
            } catch (FileNotFoundException e) {
                throw new AcgAirportFileNotFoundException("file not found", e);
            } catch (IOException e) {
                throw new AcgAirportFileNotFoundException("invalid format", e);
            }
        }

        private ArrayList<String> readFromFile(String filePath) throws IOException {
            InputStream info = this.getClass().getResourceAsStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(info, Charset.forName("UTF-8")));
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            ArrayList<String> dataRow = new ArrayList<>();
            while (line != null) {
                dataRow.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            return dataRow;
        }
    }

    private void removeHeader(ArrayList<String> dataRows) {
        dataRows.remove(0);
    }

    private AcgAirport getByAirportId(String airportId) {
        return Optional.ofNullable(airportRepository.findByAirportIdIs(airportId)).orElse(new AcgAirport());
    }

    private boolean isInternational(String symbol) {
        return "2".equals(symbol);
    }
}