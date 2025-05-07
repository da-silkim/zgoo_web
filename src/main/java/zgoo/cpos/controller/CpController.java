package zgoo.cpos.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.ChargerDto.ChargerDetailListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;
import zgoo.cpos.dto.cp.ChargerDto.FacilityCountDto;
import zgoo.cpos.service.ChargerService;
import zgoo.cpos.service.ComService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/charger")
public class CpController {
    private final ChargerService chargerService;
    private final ComService comService;

    /*
     * 충전기 등록
     */
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createCpInfo(@Valid @RequestBody ChargerRegDto reqdto,
            BindingResult bindingResult, Principal principal) {
        log.info("=== create Charger info >> {}", reqdto.toString());

        Map<String, String> response = new HashMap<>();

        // valid error발생시 결과 리턴
        if (bindingResult.hasErrors()) {
            log.error("충전기등록 에러: {}", bindingResult.getAllErrors());

            // 클라이언트에게 400 Bad Request 상태와 함께 에러 메시지 반환
            bindingResult.getFieldErrors().forEach(error -> {
                response.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(response);
        }

        try {

            // html코드에서 악의적으로 '등록'버튼 권한체크 영억을 지우고 등록시도시 추가 권한체크 진행
            ResponseEntity<Map<String, String>> permissionCheck = this.comService.checkUserPermissionsMsg(principal,
                    MenuConstants.CHARGER_LIST);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            String result = chargerService.createCpInfo(reqdto);
            if (result == null) {
                response.put("message", "충전기 등록에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("chargerId", result);
            response.put("message", "충전기 정보가 정상적으로 등록되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("[createCpInfo] error: {}", e.getMessage());
            response.put("message", "오류 발생으로 충전기 등록에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /*
     * 충전기ID get
     */
    @GetMapping("/get/create/cpid")
    public ResponseEntity<Map<String, String>> createCpId(@RequestParam("stationId") String stationId,
            Principal principal) {
        log.info("=== Get new CPID ===");

        Map<String, String> respnose = new HashMap<>();

        try {
            ResponseEntity<Map<String, String>> permissionCheck = this.comService.checkUserPermissionsMsg(principal,
                    MenuConstants.CHARGER_LIST);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            String createdCpId = chargerService.createCpId(stationId);
            if (createdCpId.equals("")) {
                respnose.put("message", "충전기ID 생성에 실패하였습니다.");
                return ResponseEntity.badRequest().body(respnose);
            }
            respnose.put("cpid", createdCpId);
            return ResponseEntity.ok(respnose);

        } catch (Exception e) {
            log.error("[createCpid] error: {}", e.getMessage());
            respnose.put("message", "충전기ID 생성중 오류가 발생하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respnose);
        }
    }

    // 모뎀 시리얼번호 중복검사
    @GetMapping("/modem/serialnum/dupcheck")
    public ResponseEntity<Boolean> checkModemSerialNum(@RequestParam("serialNum") String serialNum) {
        log.info("=== duplicate check modem serial number ===");

        try {
            boolean response = chargerService.isModemSerialDuplicate(serialNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[checkModemSerialNumber] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 모뎀 번호 중복검사
    @GetMapping("/modem/modemNum/dupcheck")
    public ResponseEntity<Boolean> checkModemNum(@RequestParam("modemNum") String modemNum) {
        log.info("=== duplicate check modemNumber ===");

        try {
            boolean response = chargerService.isModemNumDuplicate(modemNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[checkModemNumber] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
     * 충전기 정보 상세 조회
     */
    @GetMapping("/detail/{chargerId}")
    public String detailCpInfo(Model model, @PathVariable("chargerId") String chargerId,
            @RequestParam(value = "companyIdSearch", required = false) Long reqCompanyId,
            @RequestParam(value = "manfCodeSearch", required = false) String reqManfCd,
            @RequestParam(value = "opSearch", required = false) String reqOpSearch,
            @RequestParam(value = "contentSearch", required = false) String reqSearchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        try {
            log.info("=== Charger detail info : {}, {}, {}, {}, {}, {}, {} ===", chargerId, reqCompanyId, reqManfCd,
                    reqOpSearch, reqSearchContent, page, size);

            ChargerDetailListDto chargerDetailListDto = chargerService.getChargerInfo(chargerId);
            model.addAttribute("cpInfo", chargerDetailListDto);

            // pagination 관련 파라미터 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedCompanyId", reqCompanyId);
            model.addAttribute("selectedManfCd", reqManfCd);
            model.addAttribute("selectedOpSearch", reqOpSearch);
            model.addAttribute("selectedContentSearch", reqSearchContent);

        } catch (Exception e) {
            log.error("[detailCpInfo] error: {}", e.getMessage());
        }

        return "pages/charge/cp_list_detail";

    }

    /**
     * 충전기 정보 조회
     * 
     * @param chargerId
     * @return
     */
    @GetMapping("/list/search/{chargerId}")
    public ResponseEntity<ChargerDetailListDto> getChargerInfo(@PathVariable("chargerId") String chargerId) {
        log.info("=== get Charger info >> {}", chargerId);

        try {
            ChargerDetailListDto response = chargerService.getChargerInfo(chargerId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[getChargerInfo] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
     * 충전기 정보 수정
     */
    @PatchMapping("/update")
    public ResponseEntity<Map<String, String>> updateCpInfo(@Valid @RequestBody ChargerRegDto reqdto,
            BindingResult bindingResult, Principal principal) {
        log.info("=== update Charger info >> {}", reqdto.toString());

        Map<String, String> response = new HashMap<>();

        // valid error발생시 결과 리턴
        if (bindingResult.hasErrors()) {
            log.error("충전기수정 에러: {}", bindingResult.getAllErrors());

            // 클라이언트에게 400 Bad Request 상태와 함께 에러 메시지 반환
            bindingResult.getFieldErrors().forEach(error -> {
                response.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(response);
        }

        try {
            ResponseEntity<Map<String, String>> permissionCheck = this.comService.checkUserPermissionsMsg(principal,
                    MenuConstants.CHARGER_LIST);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            boolean result = chargerService.updateCpInfo(reqdto);
            if (!result) {
                response.put("message", "충전기 정보 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("message", "충전기 정보가 정상적으로 수정되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("[updateCpInfo] error: {}", e.getMessage());
            response.put("message", "오류 발생으로 충전기 정보 수정에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /*
     * 충전기 정보 삭제
     */
    @DeleteMapping("/delete/{chargerId}")
    public ResponseEntity<Map<String, String>> deleteCpInfo(@PathVariable("chargerId") String chargerId,
            Principal principal) {
        log.info("=== delete Charger info >> {}", chargerId);

        Map<String, String> response = new HashMap<>();

        try {
            ResponseEntity<Map<String, String>> permissionCheck = this.comService.checkUserPermissionsMsg(principal,
                    MenuConstants.CHARGER_LIST);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            boolean result = chargerService.deleteCpInfo(chargerId);
            if (!result) {
                response.put("message", "충전기 정보 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("message", "충전기 정보가 정상적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[deleteCpInfo] error: {}", e.getMessage());
            response.put("message", "오류 발생으로 충전기 정보 삭제에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /*
     * 충전기 정보 엑셀 다운로드
     */
    @GetMapping("/excel/download")
    public void excelDownload(
            @RequestParam(required = false, value = "companyIdSearch") Long companyIdSearch,
            @RequestParam(required = false, value = "manfCodeSearch") String manfCodeSearch,
            @RequestParam(required = false, value = "opSearch") String opSearch,
            @RequestParam(required = false, value = "contentSearch") String contentSearch,
            HttpServletResponse response, Principal principal) {

        log.info("=== Excel download request: companyId={}, manufCd={}, searchOp={}, searchContent={} ===",
                companyIdSearch, manfCodeSearch, opSearch, contentSearch);

        try {
            // 조건에 따른 전체 충전기 조회
            List<ChargerListDto> chargerList = chargerService.findAllChargerListWithoutPagination(companyIdSearch,
                    manfCodeSearch, opSearch, contentSearch, principal.getName());

            log.info("=== Total records found: {} ===", chargerList.size());

            // 엑셀 파일 생성
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("충전기List");

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
            String[] headers = { "사업자", "충전소명", "충전기명", "충전기ID", "공용구분", "모델명", "요금제", "설치일", "제조사" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000); // 열 너비 설정
            }

            // 데이터 행 생성
            int rowNum = 1;
            for (ChargerListDto charger : chargerList) {
                Row row = sheet.createRow(rowNum++);

                // 셀 데이터 설정
                createCell(row, 0, charger.getCompanyName(), dataStyle);
                createCell(row, 1, charger.getStationName(), dataStyle);
                createCell(row, 2, charger.getChargerName(), dataStyle);
                createCell(row, 3, charger.getChargerId(), dataStyle);
                createCell(row, 4, charger.getCommonTypeName(), dataStyle);
                createCell(row, 5, charger.getModelName(), dataStyle);
                createCell(row, 6, charger.getPolicyName(), dataStyle);

                // 날짜 처리
                Cell dateCell = row.createCell(7);
                if (charger.getInstallDate() != null) {
                    dateCell.setCellValue(charger.getInstallDate().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else {
                    dateCell.setCellValue("");
                }
                dateCell.setCellStyle(dataStyle);

                createCell(row, 8, charger.getManufCdName(), dataStyle);
            }

            // 파일 다운로드 설정
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "charger_list_" + timestamp + ".xlsx";

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

    /*
     * 대시보드 - 사용용도
     */
    @GetMapping("/get/facility")
    public ResponseEntity<List<FacilityCountDto>> countFacility(@RequestParam("sido") String sido,
            @RequestParam("type") String type, Principal principal) {
        try {
            List<FacilityCountDto> facilityList = chargerService.countFacilityBySidoAndType(sido, type,
                    principal.getName());
            return ResponseEntity.ok(facilityList);
        } catch (Exception e) {
            log.error("[countFacility] error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
