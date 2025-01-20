package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.Member;
import zgoo.cpos.domain.member.Voc;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.VocDto.VocRegDto;
import zgoo.cpos.mapper.VocMapper;
import zgoo.cpos.dto.member.VocDto.VocListDto;
import zgoo.cpos.repository.member.MemberRepository;
import zgoo.cpos.repository.member.VocRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class VocService {
    private final VocRepository vocRepository;
    private final MemberRepository memberRepository;

    // 1:1문의 조회
    public Page<VocListDto> findVocInfoWithPagination(String type, String replyStat, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<VocListDto> vocList;

            if ((type == null || type.isEmpty()) && (replyStat == null || replyStat.isEmpty()) && (name == null || name.isEmpty())) {
                log.info("Executing the [findVocWithPagination]");
                vocList = this.vocRepository.findVocWithPagination(pageable);
            } else {
                log.info("Executing the [searchVocWithPagination]");
                vocList = this.vocRepository.searchVocWithPagination(type, replyStat, name, pageable);
            }

            return vocList;
        } catch (Exception e) {
            log.error("[findVocInfoWithPagination] error: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 1:1문의 단건 조회
    public VocRegDto findVocOne(Long vocId) {       
        try {
            VocRegDto voc = this.vocRepository.findVocOne(vocId);
            return voc;
        } catch (Exception e) {
            log.error("[findVocOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 전화문의 등록
    public void saveVocCall(VocRegDto dto, String regUserId) {
        try {
            Member member = this.memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("member not found with id: " + dto.getMemberId()));

            dto.setChannel("VOCTEL");   // 문의 경로(전화)

            if (dto.getReplyContent() == null || dto.getReplyContent().trim().isEmpty()) { // 답변X
                dto.setReplyStat("STANDBY");
                dto.setReplyDt(null);
                dto.setRegUserId(regUserId);
            } else {    // 답변O
                dto.setReplyStat("COMPLETE");
                dto.setReplyDt(LocalDateTime.now());
                dto.setRegUserId(regUserId);
                dto.setReplyUserId(regUserId);
            }

            Voc voc = VocMapper.toEntity(dto, member);
            this.vocRepository.save(voc);
        } catch (Exception e) {
            log.error("[saveVocCall] error: {}", e.getMessage());
        }
    }

    // 1:1문의 답변 등록
    @Transactional
    public Integer updateVocAnswer(Long vocId, VocRegDto dto, String replyUserId) {
        Voc voc = this.vocRepository.findById(vocId)
            .orElseThrow(() -> new IllegalArgumentException("voc not found with id: " + vocId));

        try {
            if (dto.getReplyContent() != null && !dto.getReplyContent().isEmpty()) {
                dto.setReplyUserId(replyUserId);
                dto.setReplyStat("COMPLETE");
                voc.updateVocAnswer(dto);
                return 1;
            }
            return -1;
        } catch (Exception e) {
            log.error("[updateVocAnswer] error: {}", e.getMessage());
            return null;
        }
    }

    // 회원정보 검색
    public List<MemberListDto> findMemberList(String name, String phoneNo) {
        try {
            List<MemberListDto> memberList = this.memberRepository.findMemberList(name, phoneNo);
            return memberList;
        } catch (Exception e) {
            log.error("[findMemberList] error : {}", e.getMessage());
                return null;
        }
    }
}
