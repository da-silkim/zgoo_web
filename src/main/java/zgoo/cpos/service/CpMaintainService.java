package zgoo.cpos.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.cp.CpMaintain;
import zgoo.cpos.dto.cp.CpMaintainDto.CpInfoDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;
import zgoo.cpos.mapper.CpMaintainMapper;
import zgoo.cpos.repository.cp.CpMaintainRepository;
import zgoo.cpos.util.FileNameUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpMaintainService {

    public final CpMaintainRepository cpMaintainRepository;

    @Value("${file.img}")
    private String filepath;


    // 조회
    public Page<CpMaintainListDto> findCpMaintainInfoWithPagination(Long companyId, String searchOp, String searchContent,
            String processStatus, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CpMaintainListDto> cpList = Page.empty(pageable);

            if (companyId == null && (searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty()) &&
                     (processStatus == null || processStatus.isEmpty()) && startDate == null && endDate == null) {
                log.info("Executing the [findCpMaintainWithPagination]");
                cpList = this.cpMaintainRepository.findCpMaintainWithPagination(pageable);
            } else {
                log.info("Executing the [searchCpMaintainWithPagination]");
                cpList = this.cpMaintainRepository.searchCpMaintainWithPagination(companyId, searchOp, searchContent,
                    processStatus, startDate, endDate, pageable);
            }

            return cpList;
        } catch (Exception e) {
            log.error("[findCpMaintainInfoWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 충전소, 충전기 조회
    public CpInfoDto searchCsCpInfo(String chargerId) {
        try {
            CpInfoDto dto = this.cpMaintainRepository.searchCsCpInfoWithChargerId(chargerId);
            if (dto == null) {
                dto = new CpInfoDto();
                dto.setCompanyId(null);
                dto.setCompanyName("");
                dto.setStationId("");
                dto.setStationName("");
                dto.setAddress("");
                dto.setChargerId(null);
            }
            return dto;
        } catch (Exception e) {
            log.error("[searchCsCpInfo] error: {}", e.getMessage());
            return null;
        }
    }

    // 충전기 장애정보 등록
    public void saveMaintain(CpMaintainRegDto dto, String regUserId) {
        try {
            if ("FSTATFINISH".equals(dto.getProcessStatus())) {
                dto.setProcessDate(LocalDateTime.now());
            }

            // image file upload
            dto.setPictureLoc1(saveImageFile(dto.getFileLoc1(), "picture1"));
            dto.setPictureLoc2(saveImageFile(dto.getFileLoc2(), "picture2"));
            dto.setPictureLoc3(saveImageFile(dto.getFileLoc3(), "picture3"));

            CpMaintain cpMaintain = CpMaintainMapper.toEntity(dto, regUserId);
            this.cpMaintainRepository.save(cpMaintain);
            log.info("[saveMaintain] save complete");
        } catch (Exception e) {
            log.error("[saveMaintain] error: {}", e.getMessage());
        }
    }

    private String saveImageFile(MultipartFile file, String logName) {
        if (file != null && !file.isEmpty()) {
                try {
                    String originalFileName = file.getOriginalFilename();
                    String saveFileName = FileNameUtils.fileNameConver(originalFileName);
                    String fullPath = filepath + saveFileName;
                    file.transferTo(new File(fullPath));
                    return fullPath;
                } catch (Exception e) {
                    log.info("[saveMaintain] {} save failure: {}", logName, e.getMessage());
                }
            }
        return null;
    }
}
