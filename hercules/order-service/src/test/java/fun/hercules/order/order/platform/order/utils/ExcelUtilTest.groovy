package fun.hercules.order.order.platform.order.utils

import fun.hercules.order.order.common.exceptions.PayloadTooLargeException
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import spock.lang.Specification

class ExcelUtilTest extends Specification {


    def "should raise exception when upload file too large"() {
        setup:
        Sheet sheet = Stub(Sheet.class)
        sheet.getLastRowNum() >> {
            return 10020
        }

        Workbook workbook = Stub(Workbook.class)
        workbook.getSheetAt(0) >> {
            return sheet
        }

        when:
        ExcelUtil.parseExcelBody(workbook, [], 1, 10000)

        then:
        PayloadTooLargeException exception = thrown()
        exception.getMessage() == 'Payload too large. permitted is 10,000 but actual is 10,020'
    }
}
