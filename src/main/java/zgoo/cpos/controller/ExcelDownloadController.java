package zgoo.cpos.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoListDto;
import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.CsService;
import zgoo.cpos.service.MemberService;
import zgoo.cpos.service.PurchaseService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/excel")
public class ExcelDownloadController {

    private final MemberService memberService;
    private final CsService csService;
    private final PurchaseService purchaseService;
    private final ComService comService;

    /*
     * member list excel download
     */
    @GetMapping("/download/member_list")
    public void downloadMemberList(
            @RequestParam(required = false, value = "companyIdSearch") Long companyIdSearch,
            @RequestParam(required = false, value = "idTagSearch") String idTagSearch,
            @RequestParam(required = false, value = "nameSearch") String nameSearch,
            HttpServletResponse response, Principal principal) throws IOException {
        log.info("=== member list excel download info ===");

        boolean isExcel = this.comService.checkExcelPermissions(principal, MenuConstants.MEMBER_LIST);
        if (!isExcel) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"An abnormal access has occurred.\"}");
            return;
        }

        List<MemberListDto> memberList = this.memberService.findAllMemberListWithoutPagination(companyIdSearch,
                idTagSearch, nameSearch, principal.getName());

        String[] headers = { "사업자", "사용자명", "사용자ID", "회원카드번호", "휴대전화", "이메일", "개인/법인", "가입일자" };
        Function<MemberListDto, Object[]> dataMapper = member -> new Object[] {
                member.getCompanyName(), member.getName(), member.getMemLoginId(),
                member.getIdTag(), member.getPhoneNo(), member.getEmail(),
                member.getBizTypeName(),
                member.getJoinedDt() != null ? member.getJoinedDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : ""
        };

        Workbook workbook = createExcelFile(memberList, "회원리스트", headers, dataMapper);
        writeExcelToResponse(response, workbook, "member_list");
    }

    /*
     * member tag excel download
     */
    @GetMapping("/download/member_tag")
    public void downloadMemberTag(
            @RequestParam(required = false, value = "idTagSearch") String idTagSearch,
            @RequestParam(required = false, value = "nameSearch") String nameSearch,
            HttpServletResponse response, Principal principal) throws IOException {
        log.info("=== member tag excel download info ===");

        boolean isExcel = this.comService.checkExcelPermissions(principal, MenuConstants.MEMBER_TAG);
        if (!isExcel) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"An abnormal access has occurred.\"}");
            return;
        }

        List<MemberAuthDto> memtagList = this.memberService.findAllMemberTagWithoutPagination(idTagSearch, nameSearch,
                principal.getName());

        String[] headers = { "사업자", "회원명", "휴대전화", "회원카드번호", "부모IDTAG", "사용여부", "누적충전량(kWh)", "누적충전금액(원)", "상태" };
        Function<MemberAuthDto, Object[]> dataMapper = memtag -> new Object[] {
                memtag.getCompanyName(), memtag.getName(), memtag.getPhoneNo(), memtag.getIdTag(),
                memtag.getParentIdTag(), memtag.getUseYn(), memtag.getTotalChargingPower(),
                memtag.getTotalChargingPrice(), memtag.getStatus()
        };

        Workbook workbook = createExcelFile(memtagList, "회원카드관리", headers, dataMapper);
        writeExcelToResponse(response, workbook, "member_tag");
    }

    /*
     * station list excel download
     */
    @GetMapping("/download/station")
    public void downloadStation(
            @RequestParam(required = false, value = "companyIdSearch") Long companyIdSearch,
            @RequestParam(required = false, value = "opSearch") String opSearch,
            @RequestParam(required = false, value = "contentSearch") String contentSearch,
            HttpServletResponse response, Principal principal) throws IOException {
        log.info("=== station list excel download info ===");

        boolean isExcel = this.comService.checkExcelPermissions(principal, MenuConstants.STATION_LIST);
        if (!isExcel) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"An abnormal access has occurred.\"}");
            return;
        }

        List<CsInfoListDto> csList = this.csService.findAllStationWithoutPagination(companyIdSearch, opSearch,
                contentSearch);

        String[] headers = { "사업자", " 충전소명", "충전소ID", "설치주소", "충전기수", "운영상태", "운영시작시간", "운영종료시간" };
        Function<CsInfoListDto, Object[]> dataMapper = station -> new Object[] {
                station.getCompanyName(), station.getStationName(), station.getStationId(),
                station.getAddress(), station.getCpCount(), station.getOpStatusName(),
                station.getOpenStartTime(), station.getOpenEndTime()
        };

        Workbook workbook = createExcelFile(csList, "충전소리스트", headers, dataMapper);
        writeExcelToResponse(response, workbook, "station_list");
    }

    /*
     * purchase excel download
     */
    @GetMapping("/download/purchase")
    public void downloadPurchase(
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            HttpServletResponse response, Principal principal) throws IOException {
        log.info("=== purchase excel download info ===");

        boolean isExcel = this.comService.checkExcelPermissions(principal, MenuConstants.CALC_PURCHASE);
        if (!isExcel) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"An abnormal access has occurred.\"}");
            return;
        }

        List<PurchaseListDto> purList = this.purchaseService.findAllPurchaseWithoutPagination(searchOp, searchContent,
                startDate, endDate);

        String[] headers = { "충전소명", "거래처명", "계정과목", "공급가액", "부가세", "부담금", "가산금", "절사액", "미납금액", "합계", "지불방법", "승인번호",
                "매입일자" };
        Function<PurchaseListDto, Object[]> dataMapper = purchase -> new Object[] {
                purchase.getStationName(), purchase.getBizName(), purchase.getAccountCodeName(),
                purchase.getSupplyPrice(), purchase.getVat(), purchase.getCharge(), purchase.getSurcharge(),
                purchase.getCutoffAmount(), purchase.getUnpaidAmount(), purchase.getTotalAmount(),
                purchase.getPaymentMethodName(), purchase.getApprovalNo(),
                purchase.getPurchaseDate() != null
                        ? purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : ""
        };

        Workbook workbook = createExcelFile(purList, "매입관리", headers, dataMapper);
        writeExcelToResponse(response, workbook, "purchase");
    }

    private <T> Workbook createExcelFile(List<T> data, String sheetName, String[] headers,
            Function<T, Object[]> dataMapper) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 5000);
        }

        // 데이터 행 생성
        int rowNum = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowNum++);
            Object[] rowData = dataMapper.apply(item);

            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                if (rowData[i] instanceof String string) {
                    cell.setCellValue(string);
                } else if (rowData[i] instanceof Number number) {
                    cell.setCellValue(number.doubleValue());
                } else {
                    cell.setCellValue(rowData[i] != null ? rowData[i].toString() : "");
                }
                cell.setCellStyle(dataStyle);
            }
        }

        return workbook;
    }

    /*
     * 헤더 스타일 설정
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /*
     * 데이터 스타일 설정
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void writeExcelToResponse(HttpServletResponse response, Workbook workbook, String baseFileName) {
        try {
            // 파일 다운로드 설정
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = baseFileName + "_" + timestamp + ".xlsx";

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 엑셀 파일 출력
            workbook.write(response.getOutputStream());
            workbook.close();
            log.info("=== Excel download completed: {} ===", fileName);
        } catch (IOException e) {
            log.error("=== Excel download failed: {} ===", e.getMessage(), e);
            try {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "엑셀 파일 생성 중 오류가 발생했습니다.");
            } catch (IOException ex) {
                log.error("=== Failed to send error response: {} ===", ex.getMessage());
            }
        }
    }
}
