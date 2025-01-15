package zgoo.cpos.service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.biz.BizInfo;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.member.Member;
import zgoo.cpos.domain.member.MemberCar;
import zgoo.cpos.domain.member.MemberCondition;
import zgoo.cpos.domain.member.MemberCreditCard;
import zgoo.cpos.dto.member.MemberDto.MemberCarDto;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;
import zgoo.cpos.dto.member.MemberDto.MemberDetailDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.MemberDto.MemberPasswordDto;
import zgoo.cpos.dto.member.MemberDto.MemberRegDto;
import zgoo.cpos.mapper.MemberMapper;
import zgoo.cpos.repository.biz.BizRepository;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.member.MemberCarRepository;
import zgoo.cpos.repository.member.MemberConditionRepository;
import zgoo.cpos.repository.member.MemberCreditCardRepository;
import zgoo.cpos.repository.member.MemberRepository;
import zgoo.cpos.util.AESUtil;
import zgoo.cpos.util.EncryptionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    public final MemberRepository memberRepository;
    public final MemberCreditCardRepository memberCreditCardRepository;
    public final MemberCarRepository memberCarRepository;
    public final MemberConditionRepository memberConditionRepository;
    public final CompanyRepository companyRepository;
    public final BizRepository bizRepository;

    // 회원 조회
    public Page<MemberListDto> findMemberInfoWithPagination(Long companyId, String idTag, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<MemberListDto> memberList;

            if (companyId == null && (idTag == null || idTag.isEmpty()) && (name == null || name.isEmpty())) {
                log.info("Executing the [findMemberWithPagination]");
                memberList = this.memberRepository.findMemberWithPagination(pageable);
            } else {
                log.info("Executing the [searchMemberWithPagination]");
                memberList = this.memberRepository.searchMemberWithPagination(companyId, idTag, name, pageable);
            }

            return memberList;
        } catch (Exception e) {
            log.error("[findMemberInfoWithPagination] error: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 회원 단건 조회
    public MemberRegDto findMemberOne(Long memberId) {
        Member member = this.memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("member not found with id: " + memberId));

        try {
            List<MemberCarDto> carInfo = this.memberCarRepository.findAllByMemberIdDto(memberId);
            List<MemberConditionDto> conditionInfo = this.memberConditionRepository.findAllByMemberIdDto(memberId);
            
            if ("PB".equals(member.getBizType())) { // 개인
                List<MemberCreditCardDto> cardInfo = this.memberCreditCardRepository.findAllByMemberIdDto(memberId);
                return this.memberRepository.findMemberOne(memberId, cardInfo, carInfo, conditionInfo);
            } else if ("CB".equals(member.getBizType())) { // 법인
                return this.memberRepository.findBizMemberOne(memberId, carInfo, conditionInfo);
            } 

            log.error("[findMemberOne] 사업자구분 조회 오류");
            return null;
        } catch (Exception e) {
            log.error("[findMemberOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 회원 단건 조회(detail)
    public MemberDetailDto findMemberDetailOne(Long memberId) {
        Member member = this.memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("member not found with id: " + memberId));

        try {
            List<MemberCarDto> carInfo = this.memberCarRepository.findAllByMemberIdDto(memberId);
            List<MemberConditionDto> conditionInfo = this.memberConditionRepository.findAllByMemberIdDto(memberId);

            if ("PB".equals(member.getBizType())) { // 개인
                List<MemberCreditCardDto> cardInfo = this.memberCreditCardRepository.findAllByMemberIdDto(memberId);
                for (MemberCreditCardDto dto : cardInfo) {
                    dto.setCardNum(AESUtil.decrypt(dto.getCardNum()));
                }
                return this.memberRepository.findMemberDetailOne(memberId, cardInfo, carInfo, conditionInfo);
            } else if ("CB".equals(member.getBizType())) { // 법인
                return this.memberRepository.findBizMemberDetailOne(memberId, carInfo, conditionInfo);
            }

            log.error("[findMemberDetailOne] 사업자구분 조회 오류");
            return null;
        } catch (Exception e) {
            log.error("[findMemberDetailOne] error : {}", e.getMessage());
            return null;
        }
    }

    // memLoginId 중복 검사
    public boolean isMemLoginIdDuplicate(String memLoginId) {
        return this.memberRepository.existsByMemLoginId(memLoginId);
    }

    // 회원 저장
    @Transactional
    public void saveMember(MemberRegDto dto) {
        try {
            Company company = this.companyRepository.findById(dto.getCompanyId())
                            .orElseThrow(() -> new IllegalArgumentException("company not found with id: " + dto.getCompanyId()));

            Member member;

            // 회원번호 자동부여
            dto.setIdTag(generateIdTag());

            // password SHA-256
            dto.setPassword(EncryptionUtils.encryptSHA256(dto.getPassword()));

            if ("PB".equals(dto.getBizType())) {
                member = MemberMapper.toEntityMember(dto, company);
            } else {
                BizInfo biz = this.bizRepository.findById(dto.getBizId())
                    .orElseThrow(() -> new IllegalArgumentException("bizInfo not found with id: " + dto.getBizId()));
                member = MemberMapper.toEntityMemberBiz(dto, company, biz);

                // 법인회원 결제카드 상태
                if ((biz.getCardNum() != null || !biz.getCardNum().isEmpty()) && (biz.getTid() !=null || !biz.getTid().isEmpty())) {
                    member.updateCreditStatInfo("MCNORMAL");    // 정상
                } else if ((biz.getCardNum() != null || !biz.getCardNum().isEmpty()) && (biz.getTid() ==null || biz.getTid().isEmpty())) {
                    member.updateCreditStatInfo("MCNOBILLKEY"); // 빌키없음
                } else {
                    member.updateCreditStatInfo("MCNOTREG");    // 미등록
                }
            }

            Member saved = this.memberRepository.save(member);

            // 개인회원일 경우에만 결제카드 정보 저장
            if ("PB".equals(dto.getBizType())) {
                log.info("[saveCondition] 실행 전");
                saveCreditCard(saved, dto.getCard());
            }
            
            saveCar(saved, dto.getCar());
            saveCondition(saved, dto.getCondition());
        } catch (Exception e) {
            log.error("[saveMember] error: {}", e.getMessage());
        }
    }

    // 개인회원 결제카드 정보 저장
    public void saveCreditCard(Member member, List<MemberCreditCardDto> dtos) {
        try {
            // 카드 미등록
            if (dtos == null) {
                log.info("[saveCondition] 카드 미등록");
                member.updateCreditStatInfo("MCNOTREG");
            } else {
                log.info("[saveCondition] 카드 등록 처리");
                for (MemberCreditCardDto dto : dtos) {
                    if ("Y".equals(dto.getRepresentativeCard())) {
                        log.info("[updateRepresentativeCard] 결제카드 업데이트 실행 전");
                        updateRepresentativeCard(member, dto);
                    }

                    encryptCardNumAndTidRegDto(dto);

                    MemberCreditCard card = MemberMapper.toEntityCard(dto, member);
                    this.memberCreditCardRepository.save(card);
                }
            }
        } catch (Exception e) {
            log.error("[saveCreditCard] error: {}", e.getMessage());
        }
    }

    // 회사별 보유 차량 정보 저장
    public void saveCar(Member member, List<MemberCarDto> dtos) {
        try {
            if (dtos != null) {
                for (MemberCarDto dto : dtos) {
                    MemberCar car = MemberMapper.toEntityCar(dto, member);
                    this.memberCarRepository.save(car);
                }
            }
        } catch (Exception e) {
            log.error("[saveCar] error: {}", e.getMessage());
        }
    }

    // 약관 정보 저장
    public void saveCondition(Member member, List<MemberConditionDto> dtos) {
        try {
            if (dtos != null) {
                for (MemberConditionDto dto : dtos) {
                    MemberCondition condition = MemberMapper.toEntityCondition(dto, member);
                    this.memberConditionRepository.save(condition);
                }
            }
        } catch (Exception e) {
            log.error("[saveCondition] error: {}", e.getMessage());
        }
    }

    // 회원 리스트 수정
    @Transactional
    public Member updateMemberInfo(Long memberId, MemberRegDto dto) {
        Member member = this.memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("member not found with id: " + memberId));
        try {
            member.updateMemberInfo(dto);

            // 결제카드 정보 수정
            if ("PB".equals(dto.getBizType())) {    // 개인
                updateCreditCardInfo(dto, member);
            } else {                                // 법인
                BizInfo biz = this.bizRepository.findById(dto.getBizId())
                    .orElseThrow(() -> new IllegalArgumentException("bizInfo not found with id: " + dto.getBizId()));

                // 법인회원 결제카드 상태
                if ((biz.getCardNum() != null || !biz.getCardNum().isEmpty()) && (biz.getTid() !=null || !biz.getTid().isEmpty())) {
                    member.updateCreditStatInfo("MCNORMAL");    // 정상
                } else if ((biz.getCardNum() != null || !biz.getCardNum().isEmpty()) && (biz.getTid() ==null || biz.getTid().isEmpty())) {
                    member.updateCreditStatInfo("MCNOBILLKEY"); // 빌키없음
                } else {
                    member.updateCreditStatInfo("MCNOTREG");    // 미등록
                }
            }

            updateCarInfo(dto, member);
            updateMemberConditionInfo(dto, member);
        } catch (Exception e) {
            log.error("[updateMemberInfo] error: {}", e.getMessage());
        }
        return member;
    }

    // 개인회원 결제카드 정보 수정
    public void updateCreditCardInfo(MemberRegDto dto, Member member) {
        try {
            if (dto.getCard() != null && !dto.getCard().isEmpty()) {
                List<MemberCreditCard> cardList = this.memberCreditCardRepository.findAllByMemberId(member.getId());
                log.info("=== updateCreditCardInfo >> cnt:{}", cardList.size());

                Set<String> updatedCardNum = new HashSet<>();

                for (MemberCreditCardDto cardDto : dto.getCard()) {
                    MemberCreditCard matchedOne = cardList.stream()
                        .filter(c -> c.getCardNum().equals(cardDto.getCardNum()))
                        .findFirst()
                        .orElse(null);

                    if ("Y".equals(cardDto.getRepresentativeCard())) {
                        updateRepresentativeCard(member, cardDto);
                    }

                    if (matchedOne != null) {
                        matchedOne.updateMemberCreditCardInfo(cardDto);
                        updatedCardNum.add(cardDto.getCardNum());

                    } else {
                        // 매칭되는 카드 정보가 없으면 새로 추가
                        encryptCardNumAndTidRegDto(cardDto);
                        MemberCreditCard newCard = MemberMapper.toEntityCard(cardDto, member);
                        this.memberCreditCardRepository.save(newCard);
                    }
                }

                // dto에 없는 기존 카드 정보 삭제
                for (MemberCreditCard card : cardList) {
                    if (!updatedCardNum.contains(card.getCardNum())) {
                        this.memberCreditCardRepository.delete(card);
                    }
                }
            } else {
                // dto 카드 정보가 없을 경우 기존 카드 정보 모두 삭제
                this.memberCreditCardRepository.deleteAllByMemberId(member.getId());
            }
        } catch (Exception e) {
            log.error("[updateCreditCardInfo] error: {}", e.getMessage());
        }
    }

    // 회사별 보유 차량 정보 수정
    public void updateCarInfo(MemberRegDto dto, Member member) {
        try {
            if (dto.getCar() != null & !dto.getCar().isEmpty()) {
                List<MemberCar> carList = this.memberCarRepository.findAllByMemberId(member.getId());
                log.info("=== updateCarInfo >> cnt:{}", carList.size());

                Set<String> updatedCarNum = new HashSet<>();

                for (MemberCarDto carDto : dto.getCar()) {
                    MemberCar matchedOne = carList.stream()
                        .filter(c -> c.getCarNum().equals(carDto.getCarNum()))
                        .findFirst()
                        .orElse(null);

                    if (matchedOne != null) {
                        matchedOne.updateMemberCarInfo(carDto);
                        updatedCarNum.add(carDto.getCarNum());
                    } else {
                        // 매칭되는 카드 정보가 없으면 새로 추가
                        MemberCar newCar = MemberMapper.toEntityCar(carDto, member);
                        this.memberCarRepository.save(newCar);
                    }
                }

                // dto에 없는 기존 차량 정보 삭제
                for (MemberCar car : carList) {
                    if (!updatedCarNum.contains(car.getCarNum())) {
                        this.memberCarRepository.delete(car);
                    }
                }
            } else {
                // dto 차량 정보가 없을 경우 기존 차량 정보 모두 삭제
                this.memberCarRepository.deleteAllByMemberId(member.getId());
            }
        } catch (Exception e) {
            log.error("[updateCarInfo] error: {}", e.getMessage());
        }
    }

    // 약관 정보 수정
    public void updateMemberConditionInfo(MemberRegDto dto, Member member) {
        try {
            List<MemberCondition> conditionList = this.memberConditionRepository.findAllByMemberId(member.getId());
            
            for (MemberConditionDto conditionDto : dto.getCondition()) {
                MemberCondition matchedOne = conditionList.stream()
                    .filter(c -> c.getConditionCode().equals(conditionDto.getConditionCode()))
                    .findFirst()
                    .orElse(null);
                
                if (matchedOne != null) {
                    matchedOne.updateMemberConditionInfo(conditionDto);
                } else {
                    log.error("An error occurred while updating the terms and conditions");
                }
            }
        } catch (Exception e) {
            log.error("[updateMemberConditionInfo] error: {}", e.getMessage());
        }
    }

    // 비밀번호 변경
    @Transactional
    public Integer updateMemberPasswordInfo(Long memberId, MemberPasswordDto dto) {
        Member member = this.memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found with id: " + memberId));

        try {
            dto.setExistPassword(EncryptionUtils.encryptSHA256(dto.getExistPassword()));

            // 1. 현재 비밀번호 일치여부
            if(!member.getPassword().equals(dto.getExistPassword())) {
                log.info("[updateMemberPasswordInfo] doesn't match the current password");
                return 0;
            }
            // 2. 새 비밀번호 == 새 비밀번호 확인 값이 같은지 체크
            if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
                log.info("[updateMemberPasswordInfo] password change complete");
                return 2;
            }
            // password SHA-256
            dto.setNewPassword(EncryptionUtils.encryptSHA256(dto.getNewPassword()));
            member.updatePasswordInfo(dto.getNewPassword());
            log.info("[updateMemberPasswordInfo] two password values do not match");
            return 1;
        } catch (Exception e) {
            log.error("[updateMemberPasswordInfo] error: {}", e.getMessage());
            return null;
        }
    }

    // 회원 리스트 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = this.memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("member not found with id: " + memberId));
        
        try {
            this.memberCreditCardRepository.deleteAllByMemberId(memberId);
            this.memberCarRepository.deleteAllByMemberId(memberId);
            this.memberConditionRepository.deleteAllByMemberId(memberId);
            this.memberRepository.delete(member);
            log.info("==== memberId: {} is deleted..", memberId);
        } catch (Exception e) {
            log.error("[deleteMember] error: {}", e.getMessage());
        }
    }

    // 카드번호, TID 암호화(값이 있을 경우에만 암호화 처리)
    private void encryptCardNumAndTidRegDto(MemberCreditCardDto dto) throws Exception {
        if (dto.getCardNum() != null && !dto.getCardNum().isEmpty()) {
            dto.setCardNum(AESUtil.encrypt(dto.getCardNum()));
        }
        if (dto.getTid() != null && !dto.getTid().isEmpty()) {
            dto.setTid(AESUtil.encrypt(dto.getTid()));
        }
    }
    
    // 대표결제카드에 따른 결제카드정상여부 업데이트
    private void updateRepresentativeCard(Member member, MemberCreditCardDto dto) {
        log.info("[updateRepresentativeCard] 카드 업데이트 실행");

        if (dto.getTid() == null || dto.getTid().isEmpty()) { // 카드번호 not null && TID null
            log.info("[updateRepresentativeCard] 결제카드 업데이트 MCNOBILLKEY");
            member.updateCreditStatInfo("MCNOBILLKEY");
        } else if ((dto.getCardNum() != null || !dto.getCardNum().isEmpty()) && (dto.getTid() != null || !dto.getTid().isEmpty())) { // 카드번호 && TID not null
            log.info("[updateRepresentativeCard] 결제카드 업데이트 MCNORMAL");
            member.updateCreditStatInfo("MCNORMAL");
        } else {
            log.error("[updateRepresentativeCard] error");
        }
    }

    // UUID를 기반으로 16자리 숫자 생성
    public String generateUUIDNumber() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 16);
    }

    // 중복되지 않게 UUID 기반 숫자 생성
    public String generateUUIDNumberIdTag() {
        String randomNumber;

        do {
            randomNumber = generateUUIDNumber();
        } while (memberRepository.existsByIdTag(randomNumber));

        return randomNumber;
    }

    // 무작위 16자리 숫자 생성
    public String generateRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

    // 중복되지 않게 숫자 생성
    public String generateIdTag() {
        String randomNumber;
        
        do {
            randomNumber = generateRandomNumber();
        } while (memberRepository.existsByIdTag(randomNumber));

        return randomNumber;
    }
}