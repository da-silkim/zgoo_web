package zgoo.cpos.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.payment.ChgPaymentInfoDto;
import zgoo.cpos.dto.payment.ChgPaymentSummaryDto;
import zgoo.cpos.service.ChargingPaymentInfoService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/calc")
public class CalcController {

    private final ChargingPaymentInfoService chargingPaymentInfoService;

    @GetMapping("/chgpayment/excel")
    public void chgPaymentExcel(
            @RequestParam(required = false, value = "companyIdSearch") Long reqCompanyId,
            @RequestParam(required = false, value = "startMonthSearch") String reqStartMonth,
            @RequestParam(required = false, value = "endMonthSearch") String reqEndMonth,
            @RequestParam(required = false, value = "opSearch") String reqOp,
            @RequestParam(required = false, value = "contentSearch") String reqContent,
            HttpServletResponse response) {

        log.info(
                "=== Excel download request: companyId={}, startMonthSearch={}, endMonthSearch={}, searchOp={}, searchContent={} ===",
                reqCompanyId, reqStartMonth, reqEndMonth, reqOp, reqContent);

        try {
            // 검색 조건에 따른 데이터 조회
            List<ChgPaymentInfoDto> chgPaymentInfoList = chargingPaymentInfoService
                    .findAllChgPaymentInfoListWithoutPagination(reqStartMonth, reqEndMonth, reqOp,
                            reqContent, reqCompanyId);

            log.info("=== Total records found: {} ===", chgPaymentInfoList.size());

            // 합계 데이터 조회
            ChgPaymentSummaryDto summary = chargingPaymentInfoService.calculatePaymentSummary(
                    reqStartMonth, reqEndMonth, reqOp, reqContent, reqCompanyId);

            // 충전량 합계 계산
            double totalChgAmount = chgPaymentInfoList.stream()
                    .filter(dto -> dto.getChgAmount() != null)
                    .mapToDouble(dto -> dto.getChgAmount().doubleValue())
                    .sum();

            // 기존 summary 객체에 충전량 합계 설정
            summary.setTotalChgAmount(totalChgAmount);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("충전기 결제이력정보");

            // 스타일 설정
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

            // 그룹 헤더 스타일
            CellStyle groupHeaderStyle = workbook.createCellStyle();
            Font groupHeaderFont = workbook.createFont();
            groupHeaderFont.setBold(true);
            groupHeaderStyle.setFont(groupHeaderFont);
            groupHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
            groupHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            groupHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            groupHeaderStyle.setBorderTop(BorderStyle.THIN);
            groupHeaderStyle.setBorderBottom(BorderStyle.THIN);
            groupHeaderStyle.setBorderLeft(BorderStyle.THIN);
            groupHeaderStyle.setBorderRight(BorderStyle.THIN);

            // 데이터 스타일 설정
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // 합계 스타일 설정
            CellStyle summaryStyle = workbook.createCellStyle();
            Font summaryFont = workbook.createFont();
            summaryFont.setBold(true);
            summaryStyle.setFont(summaryFont);
            summaryStyle.setAlignment(HorizontalAlignment.CENTER);
            summaryStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            summaryStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            summaryStyle.setBorderTop(BorderStyle.THIN);
            summaryStyle.setBorderBottom(BorderStyle.THIN);
            summaryStyle.setBorderLeft(BorderStyle.THIN);
            summaryStyle.setBorderRight(BorderStyle.THIN);

            // 그룹 헤더 행 생성
            Row groupHeaderRow = sheet.createRow(0);

            // 관제 결제 데이터 그룹 헤더
            Cell groupCell1 = groupHeaderRow.createCell(0);
            groupCell1.setCellValue("관제 결제 데이터");
            groupCell1.setCellStyle(groupHeaderStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // PG 거래 데이터 그룹 헤더
            Cell groupCell2 = groupHeaderRow.createCell(8);
            groupCell2.setCellValue("PG 거래 데이터");
            groupCell2.setCellStyle(groupHeaderStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 14));

            // 빈 셀에도 스타일 적용
            for (int i = 1; i < 8; i++) {
                Cell cell = groupHeaderRow.createCell(i);
                cell.setCellStyle(groupHeaderStyle);
            }
            for (int i = 9; i < 15; i++) {
                Cell cell = groupHeaderRow.createCell(i);
                cell.setCellStyle(groupHeaderStyle);
            }

            // 헤더 행 생성
            Row headerRow = sheet.createRow(1);
            String[] headers = { "결제시간", "사업자", "충전소명", "충전기ID", "충전량(kWh)", "승인금액(원)", "취소금액(원)", "결제금액(원)",
                    "승인번호", "승인일시", "지불수단", "승인금액(원)", "취소금액(원)", "결제금액(원)", "카드번호" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000); // 열 너비 설정
            }

            // 데이터 행 생성
            int rowNum = 2;
            for (ChgPaymentInfoDto chgPaymentInfo : chgPaymentInfoList) {
                Row row = sheet.createRow(rowNum++);

                // 셀 데이터 설정
                createCell(row, 0, chgPaymentInfo.getPaymentTime(), dataStyle);
                createCell(row, 1, chgPaymentInfo.getCompanyName(), dataStyle);
                createCell(row, 2, chgPaymentInfo.getStationName(), dataStyle);
                createCell(row, 3, chgPaymentInfo.getChargerId(), dataStyle);
                createCell(row, 4, formatDecimal(chgPaymentInfo.getChgAmount()), dataStyle);
                createCell(row, 5, formatInteger(chgPaymentInfo.getChgPrice()), dataStyle);
                createCell(row, 6, formatInteger(chgPaymentInfo.getCancelCost()), dataStyle);
                createCell(row, 7, formatInteger(chgPaymentInfo.getRealCost()), dataStyle);
                createCell(row, 8, chgPaymentInfo.getPgAppNum(), dataStyle);
                createCell(row, 9, chgPaymentInfo.getPgAppTime(), dataStyle);
                createCell(row, 10, chgPaymentInfo.getPgPayType(), dataStyle);
                createCell(row, 11, formatInteger(chgPaymentInfo.getPgAppAmount()), dataStyle);
                createCell(row, 12, formatInteger(chgPaymentInfo.getPgCancelAmount()), dataStyle);
                createCell(row, 13, formatInteger(chgPaymentInfo.getPgPaymentAmount()), dataStyle);
                createCell(row, 14, chgPaymentInfo.getCardNumber(), dataStyle);
            }

            // 빈 행 추가
            sheet.createRow(rowNum++);

            // 합계 행 추가 - 관제 결제 데이터
            Row summaryRow1 = sheet.createRow(rowNum++);
            Cell summaryLabelCell1 = summaryRow1.createCell(0);
            summaryLabelCell1.setCellValue("관제 결제 데이터 합계");
            summaryLabelCell1.setCellStyle(summaryStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));

            // 빈 셀에도 스타일 적용
            for (int i = 1; i <= 3; i++) {
                Cell cell = summaryRow1.createCell(i);
                cell.setCellStyle(summaryStyle);
            }

            createCell(summaryRow1, 4, "충전량 합계: " + formatDecimal(summary.getTotalChgAmount()) + "kWh", summaryStyle);
            createCell(summaryRow1, 5, "승인금액 합계: " + formatInteger(summary.getTotalChgPrice()) + "원", summaryStyle);
            createCell(summaryRow1, 6, "취소금액 합계: " + formatInteger(summary.getTotalCancelCost()) + "원", summaryStyle);
            createCell(summaryRow1, 7, "결제금액 합계: " + formatInteger(summary.getTotalRealCost()) + "원", summaryStyle);

            // 합계 행 추가 - PG 거래 데이터
            // Row summaryRow2 = sheet.createRow(rowNum++);
            Cell summaryLabelCell2 = summaryRow1.createCell(8);
            summaryLabelCell2.setCellValue("PG 거래 데이터 합계");
            summaryLabelCell2.setCellStyle(summaryStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 8, 10));

            // 빈 셀에도 스타일 적용
            for (int i = 9; i <= 10; i++) {
                Cell cell = summaryRow1.createCell(i);
                cell.setCellStyle(summaryStyle);
            }

            createCell(summaryRow1, 11, "승인금액 합계: " + formatInteger(summary.getTotalPgAppAmount()) + "원", summaryStyle);
            createCell(summaryRow1, 12, "취소금액 합계: " + formatInteger(summary.getTotalPgCancelAmount()) + "원",
                    summaryStyle);
            createCell(summaryRow1, 13, "결제금액 합계: " + formatInteger(summary.getTotalPgPaymentAmount()) + "원",
                    summaryStyle);
            createCell(summaryRow1, 14, "", summaryStyle);

            // 파일 다운로드 설정
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "chgPaymentInfo_list_" + timestamp + ".xlsx";

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

    /**
     * 소수점 형식 포맷팅 (소수점 2자리까지)
     */
    private String formatDecimal(Number value) {
        if (value == null)
            return "";
        return String.format("%.2f", value.doubleValue());
    }

    /**
     * 정수 형식 포맷팅 (천 단위 구분자)
     */
    private String formatInteger(Number value) {
        if (value == null)
            return "";
        return String.format("%,d", value.longValue());
    }
}
