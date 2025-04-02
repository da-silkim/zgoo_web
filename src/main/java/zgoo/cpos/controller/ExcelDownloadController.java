package zgoo.cpos.controller;

import java.io.IOException;
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
import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/excel")
public class ExcelDownloadController {

    private final MemberService memberService;

    /*
     * member list excel download
     */
    @GetMapping("/download/member_list")
    public void downloadMemberList(
            @RequestParam(required = false, value = "companyIdSearch") Long companyIdSearch,
            @RequestParam(required = false, value = "idTagSearch") String idTagSearch,
            @RequestParam(required = false, value = "nameSearch") String nameSearch,
            HttpServletResponse response) {
        log.info("=== member list excel download info ===");

        List<MemberListDto> memberList = this.memberService.findAllMemberListWithoutPagination(companyIdSearch,
                idTagSearch, nameSearch);

        String[] headers = { "사업자", "사용자명", "사용자ID", "회원카드번호", "휴대전화", "이메일", "개인/법인", "가입일자" };
        Function<MemberListDto, Object[]> dataMapper = member -> new Object[]{
                member.getCompanyName(), member.getName(), member.getMemLoginId(),
                member.getIdTag(), member.getPhoneNo(), member.getEmail(),
                member.getBizTypeName(),
                member.getJoinedDt() != null ? member.getJoinedDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : ""
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
            HttpServletResponse response) {
        log.info("=== member tag excel download info ===");

        List<MemberAuthDto> memtagList = this.memberService.findAllMemberTagWithoutPagination(idTagSearch, nameSearch);

        String[] headers = { "사업자", "회원명", "휴대전화", "회원카드번호", "부모IDTAG", "사용여부", "누적충전량", "누적충전금액", "상태" };
        Function<MemberAuthDto, Object[]> dataMapper = memtag -> new Object[] {
                memtag.getCompanyName(), memtag.getName(), memtag.getPhoneNo(), memtag.getIdTag(),
                memtag.getParentIdTag(), memtag.getUseYn(), memtag.getTotalChargingPower(),
                memtag.getTotalChargingPrice(), memtag.getStatus()
        };

        Workbook workbook = createExcelFile(memtagList, "회원카드관리", headers, dataMapper);
        writeExcelToResponse(response, workbook, "member_tag");
    }

    private <T> Workbook createExcelFile(List<T> data, String sheetName, String[] headers, Function<T, Object[]> dataMapper) {
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
