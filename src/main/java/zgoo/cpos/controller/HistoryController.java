package zgoo.cpos.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.history.ChargingHistDto;
import zgoo.cpos.service.ChargingHistService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/history")
public class HistoryController {

    private final ChargingHistService chargingHistService;

    @GetMapping("/charging/excel")
    public void chargingHistory(
            @RequestParam(required = false, value = "companyIdSearch") Long companyIdSearch,
            @RequestParam(required = false, value = "chgStartTimeFrom") String chgStartTimeFrom,
            @RequestParam(required = false, value = "chgStartTimeTo") String chgStartTimeTo,
            @RequestParam(required = false, value = "opSearch") String opSearch,
            @RequestParam(required = false, value = "contentSearch") String contentSearch,
            HttpServletResponse response) {

        log.info(
                "=== Excel download request: companyId={}, chgStartTimeFrom={}, chgStartTimeTo={}, opSearch={}, contentSearch={} ===",
                companyIdSearch, chgStartTimeFrom, chgStartTimeTo, opSearch, contentSearch);

        try {
            // 조건에 따른 전체 충전기 조회
            List<ChargingHistDto> chargingHistList = chargingHistService
                    .findAllChargingHistListWithoutPagination(companyIdSearch, chgStartTimeFrom, chgStartTimeTo,
                            opSearch,
                            contentSearch);

            log.info("=== Total records found: {} ===", chargingHistList.size());

            // 엑셀 파일 생성
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("실시간충전List");

            // 헤더 스타일 설정
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // 데이터 스타일 설정
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // 날짜 스타일 설정
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(dataStyle);
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));

            // 헤더 행 생성
            Row headerRow = sheet.createRow(0);
            String[] headers = { "사업자", "충전소명", "충전소ID", "충전기ID", "커넥터ID", "충전기구분", "회원명", "회원카드번호", "회원구분",
                    "충전시작일시", "충전종료일시", "충전시간", "종료사유", "SOC", "충전량(kWh)", "충전금액", "적용단가", "최초결제금액", "취소금액", "실결제금액",
                    "결제여부" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000); // 열 너비 설정
            }

            // 데이터 행 생성
            int rowNum = 1;
            for (ChargingHistDto chargingHist : chargingHistList) {
                Row row = sheet.createRow(rowNum++);

                // 셀 데이터 설정
                createCell(row, 0, chargingHist.getCompanyName(), dataStyle);
                createCell(row, 1, chargingHist.getStationName(), dataStyle);
                createCell(row, 2, chargingHist.getStationId(), dataStyle);
                createCell(row, 3, chargingHist.getChargerId(), dataStyle);
                createCell(row, 4, chargingHist.getConnectorId().toString(), dataStyle);
                createCell(row, 5, chargingHist.getCpType(), dataStyle);
                createCell(row, 6, chargingHist.getMemberName(), dataStyle);
                createCell(row, 7, chargingHist.getIdTag(), dataStyle);
                createCell(row, 8, chargingHist.getMemberType(), dataStyle);
                createCell(row, 9,
                        chargingHist.getChgStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        dataStyle);
                createCell(row, 10,
                        chargingHist.getChgEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        dataStyle);
                createCell(row, 11, chargingHist.getChgTime().toString(), dataStyle);
                createCell(row, 12, chargingHist.getChgEndReason().toString(), dataStyle);
                createCell(row, 13, chargingHist.getSoc().toString(), dataStyle);
                createCell(row, 14, chargingHist.getChgAmount().toString(), dataStyle);
                createCell(row, 15, chargingHist.getChgPrice().toString(), dataStyle);
                createCell(row, 16, chargingHist.getUnitCost().toString(), dataStyle);
                createCell(row, 17, chargingHist.getPrepayCost().toString(), dataStyle);
                createCell(row, 18, chargingHist.getCancelCost().toString(), dataStyle);
                createCell(row, 19, chargingHist.getRealCost().toString(), dataStyle);
                createCell(row, 20, chargingHist.getPaymentYn().toString(), dataStyle);
            }

            // 파일 다운로드 설정
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "charging_hist_list_" + timestamp + ".xlsx";

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 엑셀 파일 출력
            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("=== Excel download completed: {} ===", fileName);

        } catch (Exception e) {
            log.error("=== Excel download failed: {} ===", e.getMessage(), e);
            try {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "엑셀 파일 생성 중 오류가 발생했습니다.");
            } catch (IOException ex) {
                log.error("=== Failed to send error response: {} ===", ex.getMessage());
            }
        }
    }

    /**
     * 셀 생성 헬퍼 메서드
     */
    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }
}
