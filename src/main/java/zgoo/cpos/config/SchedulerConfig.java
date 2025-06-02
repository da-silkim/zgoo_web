package zgoo.cpos.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.ConditionVersionHist;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;
import zgoo.cpos.dto.member.MemberDto.MemberBaseDto;
import zgoo.cpos.service.ConditionService;
import zgoo.cpos.service.EmailService;
import zgoo.cpos.service.MemberService;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final ConditionService conditionService;
    private final MemberService memberService;
    private final EmailService emailService;

    // 약관 적용 여부 확인(매일 자정)
    @Scheduled(cron = "0 0 0 * * *")
    public void conditionApplyUpdate() {
        log.info("[SchedulerConfig] >> conditionApplyUpdate: {}", LocalDateTime.now());

        // 약관 전체조회(MS, PI, ES, MK, SS)
        List<ConditionCodeBaseDto> conList = this.conditionService.findConditionCodeAll();
        
        // 적용일시에 따른 약관 적용여부 업데이트
        for (ConditionCodeBaseDto con : conList) {
            String conditionCode = con.getConditionCode();
            conditionService.updateApplyYn(conditionCode);
        }
    }

    // 개정약관 적용 안내 메일 발송(매일 오전 9시)
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void sendEmailRevisionCondition() {
        log.info("[SchedulerConfig] >> sendEmailRevisionCondition: {}", LocalDateTime.now());

        // 약관 전체조회(MS, PI, ES, MK, SS)
        List<ConditionCodeBaseDto> conList = this.conditionService.findConditionCodeAll();
        log.info("[sendEmailRevisionCondition] conList >> {}", conList.toString());

        /* 
         * 1. 약관별 개정약관이 있는지 확인
         * 2. 개정된 약관과 이메일 정보가 있으며 이메일수신 동의를 한 회원 조회
         * 3. 회원에게 약관 개정 메일 발송
         */
        for (ConditionCodeBaseDto con : conList) {
            String conditonCode = con.getConditionCode();
            String conditionName = con.getConditionName();
            // 현재일 이후, 적용이 안 되어있고, 적용일 30일 전 약관 1건 조회
            ConditionVersionHist revision = this.conditionService.findRevisionConditionByConditionCode(conditonCode);
            if (revision != null) {
                log.info("[sendEmailRevisionCondition] revision >> {}", revision.toString());
                String text = revision.getMemo();

                // 이메일 정보가 있으며 이메일수신 동의를 한 회원 조회
                List<MemberBaseDto> memberList = this.memberService.findAllMembersWithEmailAndMarketing();
                log.info("[sendEmailRevisionCondition] memberList >> {}", memberList.toString());
                for (MemberBaseDto member : memberList) {
                    String to = member.getEmail();
                    emailService.sendMailMessage(to, conditionName, text);
                }
            } else {
                log.info("[sendEmailRevisionCondition] revision is null");
            }
        }
    }
}
