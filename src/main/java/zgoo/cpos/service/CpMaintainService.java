package zgoo.cpos.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainDetailDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainRegDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.mapper.CpMaintainMapper;
import zgoo.cpos.repository.company.CompanyRepository;
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
    public final ComService comService;
    public final CompanyRepository companyRepository;

    @Value("${file.img}")
    private String filepath;

    // 조회
    public Page<CpMaintainListDto> findCpMaintainInfoWithPagination(Long companyId, String searchOp,
            String searchContent,
            String processStatus, LocalDate startDate, LocalDate endDate, int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CpMaintainListDto> cpList;

            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            if (companyId == null && (searchOp == null || searchOp.isEmpty())
                    && (searchContent == null || searchContent.isEmpty()) &&
                    (processStatus == null || processStatus.isEmpty()) && startDate == null && endDate == null) {
                log.info("Executing the [findCpMaintainWithPagination]");
                cpList = this.cpMaintainRepository.findCpMaintainWithPagination(pageable, levelPath, isSuperAdmin);
            } else {
                log.info("Executing the [searchCpMaintainWithPagination]");
                cpList = this.cpMaintainRepository.searchCpMaintainWithPagination(companyId, searchOp, searchContent,
                        processStatus, startDate, endDate, pageable, levelPath, isSuperAdmin);
            }

            return cpList;
        } catch (Exception e) {
            log.error("[findCpMaintainInfoWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 충전소, 충전기 조회
    public CpInfoDto searchCsCpInfo(String chargerId, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }
            CpInfoDto dto = this.cpMaintainRepository.searchCsCpInfoWithChargerId(chargerId, levelPath, isSuperAdmin);
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
                dto.setPictureLoc1(convertToWebPath(dto.getPictureLoc1()));
                System.out.println("pic1 (after): " + dto.getPictureLoc1());
            }

            if (dto.getPictureLoc2() != null) {
                System.out.println("pic2 (before): " + dto.getPictureLoc2());
                dto.setPictureLoc2(convertToWebPath(dto.getPictureLoc2()));
                System.out.println("pic2 (after): " + dto.getPictureLoc2());
            }

            if (dto.getPictureLoc3() != null) {
                System.out.println("pic3 (before): " + dto.getPictureLoc3());
                dto.setPictureLoc3(convertToWebPath(dto.getPictureLoc3()));
                System.out.println("pic3 (after): " + dto.getPictureLoc3());
            }

            return dto;
        } catch (Exception e) {
            log.error("[findMaintainOne] error: {}", e.getMessage());
            return null;
        }
    }

    // 충전기 장애정보 단건 조회(detail)
    public CpMaintainDetailDto findMaintainDetailOne(Long cpmaintainId) {
        CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
                .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));

        try {
            CpMaintainDetailDto dto = this.cpMaintainRepository.findMaintainDetailOne(cpmaintainId);

            // 이미지 경로를 웹 경로로 수정 (null 체크 추가)
            if (dto.getPictureLoc1() != null) {
                System.out.println("pic1 (before): " + dto.getPictureLoc1());
                dto.setPictureLoc1(convertToWebPath(dto.getPictureLoc1()));
                System.out.println("pic1 (after): " + dto.getPictureLoc1());
            }

            if (dto.getPictureLoc2() != null) {
                System.out.println("pic2 (before): " + dto.getPictureLoc2());
                dto.setPictureLoc2(convertToWebPath(dto.getPictureLoc2()));
                System.out.println("pic2 (after): " + dto.getPictureLoc2());
            }

            if (dto.getPictureLoc3() != null) {
                System.out.println("pic3 (before): " + dto.getPictureLoc3());
                dto.setPictureLoc3(convertToWebPath(dto.getPictureLoc3()));
                System.out.println("pic3 (after): " + dto.getPictureLoc3());
            }

            return dto;
        } catch (Exception e) {
            log.error("[findMaintainDetailOne] error: {}", e.getMessage());
            return null;
        }
    }

    private String convertToWebPath(String path) {
        if (path == null || !path.contains("/images/"))
            return null;
        String webPath = path.substring(path.indexOf("/images"));
        return webPath.replace("\\", "/");
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

                String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String uploadDir = filepath + datePath + "/";
                String fullPath = uploadDir + saveFileName;

                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

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
                throw new IllegalArgumentException(
                        "User ID is missing. Cannot update cpmaintain info without login user ID.");
            }

            boolean isMod = checkUpdateAndDeleteAuthority(cpmaintainId, loginUserId);
            if (!isMod || "FSTATFINISH".equals(cpMaintain.getProcessStatus()))
                return; // 권한이 없거나 이미 처리된 건

            List<String> picturePaths = new ArrayList<>();
            String pictureLoc1 = saveImageFile(dto.getFileLoc1(), "picture1");
            String pictureLoc2 = saveImageFile(dto.getFileLoc2(), "picture2");
            String pictureLoc3 = saveImageFile(dto.getFileLoc3(), "picture3");

            log.info("[updateMaintain] getExistingPictureLoc1 >> {}", dto.getExistingPictureLoc1());
            log.info("[updateMaintain] getExistingPictureLoc2 >> {}", dto.getExistingPictureLoc2());
            log.info("[updateMaintain] getExistingPictureLoc3 >> {}", dto.getExistingPictureLoc3());

            handlePicture(cpMaintain.getPictureLoc1(), dto.getExistingPictureLoc1(), pictureLoc1, "pictureLoc1",
                    picturePaths);
            handlePicture(cpMaintain.getPictureLoc2(), dto.getExistingPictureLoc2(), pictureLoc2, "pictureLoc2",
                    picturePaths);
            handlePicture(cpMaintain.getPictureLoc3(), dto.getExistingPictureLoc3(), pictureLoc3, "pictureLoc3",
                    picturePaths);

            dto.setPictureLoc1(!picturePaths.isEmpty() ? picturePaths.get(0) : null);
            dto.setPictureLoc2(picturePaths.size() > 1 ? picturePaths.get(1) : null);
            dto.setPictureLoc3(picturePaths.size() > 2 ? picturePaths.get(2) : null);

            log.info("[updateMaintain] PictureLoc1 update>> {}", dto.getPictureLoc1());
            log.info("[updateMaintain] PictureLoc2 update>> {}", dto.getPictureLoc2());
            log.info("[updateMaintain] PictureLoc3 update>> {}", dto.getPictureLoc3());

            cpMaintain.updateCpMaintainInfo(dto);
            if ("FSTATFINISH".equals(dto.getProcessStatus())) {
                cpMaintain.updateProcessInfo(dto);
            }

        } catch (IllegalArgumentException e) {
            log.error("[updateMaintain] IllegalArgumentException: {}", e.getMessage());
        } catch (NullPointerException e) {
            log.error("[updateMaintain] NullPointerException: {}", e.getMessage());
        } catch (Exception e) {
            log.error("[updateMaintain] error: {}", e.getMessage());
        }
    }

    /*
     * 1) 기존 이미지가 저장되어 있는 상태일 때
     * 1. 이미지 웹경로가 null 또는 empty가 아닐 경우 => 이미지 유지
     * 2. 이미지 웹경로가 null 또는 empty이면 => 이미지 삭제
     * 
     * 2) 기존에 이미지가 저장되어 있지 않은 상태일 때
     * 1. 새로운 이미지가 있는지 확인
     * 2. null 아니면 새로운 이미지 경로 추가
     */
    private void handlePicture(String pictureLoc, String existingPictureLoc, String uploadFileLoc, String label,
            List<String> picturePaths) {
        if (pictureLoc != null) {
            log.info("[handlePicture] {} >> {}", label, pictureLoc);

            if (existingPictureLoc != null && !existingPictureLoc.isEmpty()) {
                picturePaths.add(pictureLoc);
            } else {
                log.info("[handlePicture] {} delete", label);
                deleteImage(pictureLoc);
            }
        } else {
            log.info("[handlePicture] {} null");
            if (uploadFileLoc != null) {
                picturePaths.add(uploadFileLoc);
            }
        }
    }

    // 충전기 장애정보 삭제
    @Transactional
    public void deleteMaintain(Long cpmaintainId, String loginUserId) {
        CpMaintain cpMaintain = this.cpMaintainRepository.findById(cpmaintainId)
                .orElseThrow(() -> new IllegalArgumentException("cpmaintain not found with id: " + cpmaintainId));

        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException(
                        "User ID is missing. Cannot delete cpmaintain info without login user ID.");
            }

            boolean isMod = checkUpdateAndDeleteAuthority(cpmaintainId, loginUserId);
            if (!isMod)
                return;

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
