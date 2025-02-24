package zgoo.cpos.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.ConditionCode;
import zgoo.cpos.domain.member.ConditionVersionHist;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;
import zgoo.cpos.dto.member.ConditionDto.ConditionVersionHistBaseDto;
import zgoo.cpos.mapper.ConditionMapper;
import zgoo.cpos.repository.member.ConditionCodeRepository;
import zgoo.cpos.repository.member.ConditionVersionHistRepository;
import zgoo.cpos.util.FileNameUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConditionService {

    @Value("${file.dir}")
    private String filepath;

    private final ConditionCodeRepository conditionCodeRepository;
    private final ConditionVersionHistRepository conditionVersionHistRepository;

    // 약관 전체조회
    public List<ConditionCodeBaseDto> findConditionCodeAll() {
        try {
            List<ConditionCodeBaseDto> conList = this.conditionCodeRepository.findAllCustom();
            return conList;
        } catch (Exception e) {
            log.error("[findConditionCode] error : {}", e.getMessage());
            return null;
        }
    }

    // 약관별 개정 히스토리 전체조회
    public List<ConditionVersionHistBaseDto> findHistAllByConditionCode(String conditionCode) {

        try {
            List<ConditionVersionHistBaseDto> conList = this.conditionVersionHistRepository.findAllByConditionCode(conditionCode);
            return conList;
        } catch (Exception e) {
            log.error("[findHistByConditionCode] error : {}", e.getMessage());
            return null;
        }
    }

    // 약관 개정 단건 조회
    public ConditionVersionHistBaseDto findHistOne(Long id) {
        ConditionVersionHistBaseDto con = null;
        try {
            con = this.conditionVersionHistRepository.findByIdCustom(id);
        } catch (Exception e) {
            log.error("[findHistOne] error : {}", e.getMessage());
        }
        return con;
    }

    // 약관 저장
    public void saveConditionCode(ConditionCodeBaseDto dto) {
        try {
            ConditionCode condition = ConditionMapper.toEntityConditionCode(dto);
            this.conditionCodeRepository.save(condition);
            log.info("[saveConditionCode] complete}");
        } catch (Exception e) {
            log.error("[saveConditionCode] error: {}", e.getMessage());
        }
    }

    // 약관 개정 저장
    @Transactional
    public void saveConditionVersionHist(ConditionVersionHistBaseDto dto) {
        try {
            // System.out.println("conditionCode: " + dto.getConditionCode());
            ConditionCode condition = this.conditionCodeRepository.findByConditionCode(dto.getConditionCode());

            if (condition == null) {
                log.error("[saveConditionVersionHist] condition is null");
                return;
            }

            String applyDtString = dto.getApplyDtString();

            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate applyDate = LocalDate.parse(applyDtString, dateFormatter);
                LocalDateTime formattedApplyDt = applyDate.atStartOfDay();
                dto.setApplyDt(formattedApplyDt);
            } catch (DateTimeParseException e) {
                log.error("[saveConditionVersionHist] Invalid date format: {}", applyDtString);
            }

            // 파일 업로드
            if(!dto.getFile().isEmpty()) {
                try {
                    String originalFileName = dto.getFile().getOriginalFilename();
                    String saveFileName = FileNameUtils.fileNameConver(originalFileName);
                    String fullPath = filepath + saveFileName;
                    dto.getFile().transferTo(new File(fullPath));
                    dto.setFilePath(fullPath);
                    dto.setOriginalName(originalFileName);
                    dto.setStoredName(saveFileName);
                    log.info("[saveConditionVersionHist] File saved: {}", fullPath);
                } catch (IOException e) {
                    log.info("[saveConditionVersionHist] file save failure: {}", e.getMessage());
                    return;
                }
            }

            ConditionVersionHist conditionHist = ConditionMapper.toEntityConditionHist(dto, condition);
            this.conditionVersionHistRepository.save(conditionHist);
            log.info("[saveConditionVersionHist] save complete");

            updateApplyYn(dto.getConditionCode());
        } catch (Exception e) {
            log.error("[saveConditionVersionHist] error: {}", e.getMessage());
        }
    }

    // 적용일시에 따른 약관 적용여부 업데이트
    public void updateApplyYn(String conditionCode) {
        try {
            ConditionVersionHist condition = this.conditionVersionHistRepository.findApplyYesByConditionCode(conditionCode);

            if (condition != null) {
                condition.updateConditionVersionHistInfo("N");
            }

            LocalDateTime applyDt = LocalDate.now().atStartOfDay();
            ConditionVersionHist conHist = this.conditionVersionHistRepository.findRecentHistByConditionCode(conditionCode, applyDt);
            log.info("=== before update: {}", conHist.toString());
            conHist.updateConditionVersionHistInfo("Y");
            log.info("=== after update: {}", conHist.toString());
            
        } catch (Exception e)      {
            log.error("[updateApplyYn] error: {}", e.getMessage());
        }
    }

    // 약관 삭제
    @Transactional
    public void deleteConditionCode(String conditionCode) {

        try {
            ConditionCode condition = this.conditionCodeRepository.findByConditionCode(conditionCode);
            
            if (condition == null) {
                log.error("[deleteConditionCode] error");
                return;
            }

            this.conditionVersionHistRepository.deleteByConditionCode(conditionCode);
            this.conditionCodeRepository.deleteByConditionCode(conditionCode);
            log.info("[deleteConditionCode] delete complete}");
        } catch (Exception e) {
            log.error("[deleteConditionCode] error: {}", e.getMessage());
        }
    }

    // 약관 개정 삭제
    @Transactional
    public void deleteConditionHist(Long conditionVersionHistId) {
        try {
            this.conditionVersionHistRepository.deleteById(conditionVersionHistId);
            log.info("[deleteConditionHist] delete complete}");
        } catch (Exception e) {
            log.error("[deleteConditionHist] error: {}", e.getMessage());
        }
    }

    // 약관명 조회
    public String findConditionName(String conditionCode) {
        ConditionCode condition = this.conditionCodeRepository.findByConditionCode(conditionCode);
        if (condition == null) {
            log.error("[findConditionName] error");
            throw new IllegalArgumentException(conditionCode + "코드에 해당하는 파일 정보를 찾을 수 없습니다.");
        }
        return condition.getConditionName();
    }
}
