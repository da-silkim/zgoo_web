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
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.cp.CpMaintainDto.CpInfoDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.mapper.CpMaintainMapper;
import zgoo.cpos.repository.cp.CpMaintainRepository;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.users.UsersRepository;
import zgoo.cpos.util.FileNameUtils;
import zgoo.cpos.util.MenuConstants;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpMaintainService {

    public final CpMaintainRepository cpMaintainRepository;
    public final UsersRepository usersRepository;
    public final MenuAuthorityRepository menuAuthorityRepository;

    @Value("${file.img}")
    private String filepath;

    // 조회
    public Page<CpMaintainListDto> findCpMaintainInfoWithPagination(Long companyId, String searchOp, String searchContent,
            String processStatus, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CpMaintainListDto> cpList;

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
        try {
            if (file != null && !file.isEmpty()) {
                System.out.println("fileLoc: " + file);
                String originalFileName = file.getOriginalFilename();
                System.out.println("originalFileName: " + originalFileName);
                String saveFileName = FileNameUtils.fileNameConver(originalFileName);
                String fullPath = filepath + saveFileName;
                file.transferTo(new File(fullPath));
                return fullPath;
            }
            return null;
        } catch (Exception e) {
            log.info("[saveImageFile] {} save failure: {}", logName, e.getMessage());
            return null;
        }
    }

    // 충전기 장애정보 수정
    @Transactional
    public void updateMaintain(Long cpmaintainId, CpMaintainRegDto dto, String loginUserId) {
        CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
            .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));

        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot update cpmaintain info without login user ID."); 
            }

            boolean isMod = checkUpdateAndDeleteAuthority(cpmaintainId, loginUserId);
            if (!isMod) return;

            if ("FSTATFINISH".equals(dto.getProcessStatus())) {
                cpMaintain.updateProcessInfo(dto);
            }
        } catch (Exception e) {
            log.error("[updateMaintain] error: {}", e.getMessage());
        }
    }

    // @Transactional
    // public void updateMaintain2(Long cpmaintainId, CpMaintainRegDto dto) {
    //     CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
    //         .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));

    //         try {
    //             if (dto.getFileLoc1() != null && !dto.getFileLoc1().isEmpty()) {
    //                 String originalFileName = dto.getFileLoc1().getOriginalFilename();
    //                 System.out.println("originalFileName1: " + originalFileName);
    //                 String saveFileName = FileNameUtils.fileNameConver(originalFileName);
    //                 System.out.println("saveFileName1: " + saveFileName);
    //                 String fullPath = filepath + saveFileName;
    //                 System.out.println("fullPath1: " + fullPath);
    //             }

    //             if (dto.getFileLoc2() != null && !dto.getFileLoc2().isEmpty()) {
    //                 String originalFileName = dto.getFileLoc2().getOriginalFilename();
    //                 System.out.println("originalFileName2: " + originalFileName);
    //                 String saveFileName = FileNameUtils.fileNameConver(originalFileName);
    //                 System.out.println("saveFileName2: " + saveFileName);
    //                 String fullPath = filepath + saveFileName;
    //                 System.out.println("fullPath2: " + fullPath);
    //             }

    //             if (dto.getFileLoc3() != null && !dto.getFileLoc3().isEmpty()) {
    //                 String originalFileName = dto.getFileLoc3().getOriginalFilename();
    //                 System.out.println("originalFileName3: " + originalFileName);
    //                 String saveFileName = FileNameUtils.fileNameConver(originalFileName);
    //                 System.out.println("saveFileName3: " + saveFileName);
    //                 String fullPath = filepath + saveFileName;
    //                 System.out.println("fullPath3: " + fullPath);
    //             }

    //             if ("FSTATFINISH".equals(dto.getProcessStatus())) {
    //                 cpMaintain.updateProcessInfo(dto);
    //             }

    //             cpMaintain.updateCpMaintainInfo(dto);
    //         } catch (Exception e) {
    //             log.error("[updateMaintain] error: {}", e.getMessage());
    //         }
    // }

    // 충전기 장애정보 삭제
    @Transactional
    public void deleteMaintain(Long cpmaintainId, String loginUserId) {
        CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
            .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));

        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot delete cpmaintain info without login user ID."); 
            }

            boolean isMod = checkUpdateAndDeleteAuthority(cpmaintainId, loginUserId);
            if (!isMod) return;

            // image delete
            deleteImage(cpMaintain.getPictureLoc1());
            deleteImage(cpMaintain.getPictureLoc2());
            deleteImage(cpMaintain.getPictureLoc3());
            this.cpMaintainRepository.deleteById(cpmaintainId);
            log.info("=== cpmaintainId: {} is deleted..", cpmaintainId);
        } catch (IllegalArgumentException e) {
            log.error("[deleteMaintain] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[deleteMaintain] error: {}", e.getMessage());
        }
    }

    private void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                boolean deleted = imageFile.delete();
                if (deleted) {
                    log.info("=== Image at path: {} is deleted successfully.", imagePath);
                } else {
                    log.warn("=== Failed to delete image at path: {}", imagePath);
                }
            } else {
                log.warn("=== Image file not found at path: {}", imagePath);
            }
        } 
    }

    // edit & delete button control
    public boolean buttonControl(Long cpmaintainId, String loginUserId) {
        CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
            .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));

        try {
            String writer = cpMaintain.getRegUserId();

            Users user = this.usersRepository.findUserOne(writer);
            String userAuthority = user.getAuthority();

            Users loginUser = this.usersRepository.findUserOne(loginUserId);
            String loginUserAuthority = loginUser.getAuthority();

            if (loginUserAuthority.equals("SU")) {
                return true;
            }

            if (loginUserAuthority.equals("AD")) {
                return !userAuthority.equals("SU");
            }

            return writer.equals(loginUserId);
        } catch (Exception e) {
            log.error("[CpMaintainService >> buttonControl] error: {}", e.getMessage());
            return false;
        }
    }

    // update & delete check
    public boolean checkUpdateAndDeleteAuthority(Long cpmaintainId, String loginUserId) {
        Users loginUser = this.usersRepository.findUserOne(loginUserId);
        String loginUserAuthority = loginUser.getAuthority();

        if (loginUserAuthority.equals("SU")) {
            log.info("[CpMaintainService >> checkUpdateAndDeleteAuthority] Super Admin");
            return true;
        }

        CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
            .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));
        String writer = cpMaintain.getRegUserId();
        Users user = this.usersRepository.findUserOne(writer);
        String userAuthority = user.getAuthority();

        MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(loginUser.getCompany().getId(),
            loginUserAuthority, MenuConstants.MAINTEN_ERR);
        String modYn = dto.getModYn();

        if (modYn.equals("Y")) {
            if (loginUserAuthority.equals("AD")) {
                log.info("[CpMaintainService >> checkUpdateAndDeleteAuthority] Admin");
                return !userAuthority.equals("SU");
            }
            return writer.equals(loginUserId);
        }
        log.info("[CpMaintainService >> checkUpdateAndDeleteAuthority] update & delete no permission");
        return false;
    }
}
