package zgoo.cpos.service;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // 충전기 장애정보 단건 조회
    public CpMaintainRegDto findMaintainOne(Long cpmaintainId) {
        try {
            CpMaintainRegDto dto = this.cpMaintainRepository.findMaintainOne(cpmaintainId);

            // 이미지 경로를 웹 경로로 수정 (null 체크 추가)
            if (dto.getPictureLoc1() != null) {
                System.out.println("pic1 (before): " + dto.getPictureLoc1());
                dto.setPictureLoc1("/images/" + Paths.get(dto.getPictureLoc1()).getFileName());
                System.out.println("pic1 (after): " + dto.getPictureLoc1());
            }

            if (dto.getPictureLoc2() != null) {
                System.out.println("pic2 (before): " + dto.getPictureLoc2());
                dto.setPictureLoc2("/images/" + Paths.get(dto.getPictureLoc2()).getFileName());
                System.out.println("pic2 (after): " + dto.getPictureLoc2());
            }

            if (dto.getPictureLoc3() != null) {
                System.out.println("pic3 (before): " + dto.getPictureLoc3());
                dto.setPictureLoc3("/images/" + Paths.get(dto.getPictureLoc3()).getFileName());
                System.out.println("pic3 (after): " + dto.getPictureLoc3());
            }


            return dto;
        } catch (Exception e) {
            log.error("[findMaintainOne] error: {}", e.getMessage());
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
            List<String> picturePaths = new ArrayList<>();
            String pictureLoc1 = saveImageFile(dto.getFileLoc1(), "picture1");
            String pictureLoc2 = saveImageFile(dto.getFileLoc2(), "picture2");
            String pictureLoc3 = saveImageFile(dto.getFileLoc3(), "picture3");

            if (pictureLoc1 != null) {
                picturePaths.add(pictureLoc1);
            }
            if (pictureLoc2 != null) {
                picturePaths.add(pictureLoc2);
            }
            if (pictureLoc3 != null) {
                picturePaths.add(pictureLoc3);
            }

            dto.setPictureLoc1(!picturePaths.isEmpty() ? picturePaths.get(0) : null);
            dto.setPictureLoc2(picturePaths.size() > 1 ? picturePaths.get(1) : null);
            dto.setPictureLoc3(picturePaths.size() > 2 ? picturePaths.get(2) : null);

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

    // 충전기 장애정보 삭제
    @Transactional
    public void deleteMaintain(Long cpmaintainId) {
        CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
            .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));

        try {
            this.cpMaintainRepository.deleteById(cpmaintainId);
            log.info("=== cpmaintainId: {} is deleted..", cpmaintainId);
        } catch (Exception e) {
            log.error("[deleteMaintain] error: {}", e.getMessage());
        }
    }
}
