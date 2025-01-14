package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.biz.BizInfo;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.member.MemberCondition;
import zgoo.cpos.domain.member.Member;
import zgoo.cpos.domain.member.MemberCar;
import zgoo.cpos.domain.member.MemberCreditCard;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;
import zgoo.cpos.dto.member.MemberDto.MemberCarDto;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;
import zgoo.cpos.dto.member.MemberDto.MemberRegDto;

public class MemberMapper {

    /*
     * 법인회원
     * member(dto >> entity)
     */
    public static Member toEntityMemberBiz(MemberRegDto dto, Company company, BizInfo biz) {
        Member member = Member.builder()
                .company(company)
                .biz(biz)
                .memLoginId(dto.getMemLoginId())
                .password(dto.getPassword())
                .name(dto.getName())
                .bizType(dto.getBizType())
                .phoneNo(dto.getPhoneNo())
                .idTag(dto.getIdTag())
                .email(dto.getEmail())
                .birth(dto.getBirth())
                .zipCode(dto.getZipCode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .userState(dto.getUserState())
                .joinedDt(LocalDateTime.now())
                .creditcardStat(dto.getCreditcardStat())
                .build();
        return member;
    }

    /*
     * 개인회원
     * member(dto >> entity)
     */
    public static Member toEntityMember(MemberRegDto dto, Company company) {
        Member member = Member.builder()
                .company(company)
                .memLoginId(dto.getMemLoginId())
                .password(dto.getPassword())
                .name(dto.getName())
                .bizType(dto.getBizType())
                .phoneNo(dto.getPhoneNo())
                .idTag(dto.getIdTag())
                .email(dto.getEmail())
                .birth(dto.getBirth())
                .zipCode(dto.getZipCode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .userState(dto.getUserState())
                .joinedDt(LocalDateTime.now())
                .creditcardStat(dto.getCreditcardStat())
                .build();
        return member;
    }

    /* 
     * member credit card(dto >> entity)
     */
    public static MemberCreditCard toEntityCard(MemberCreditCardDto dto, Member member) {
        MemberCreditCard card = MemberCreditCard.builder()
                .member(member)
                .tid(dto.getTid())
                .cardNum(dto.getCardNum())
                .fnCode(dto.getFnCode())
                .representativeCard(dto.getRepresentativeCard())
                .regDt(LocalDateTime.now())
                .build();
        return card;
    }

    /* 
     * member car(dto >> entity)
     */
    public static MemberCar toEntityCar(MemberCarDto dto, Member member) {
        MemberCar car = MemberCar.builder()
                .member(member)
                .carType(dto.getCarType())
                .carNum(dto.getCarNum())
                .model(dto.getModel())
                .regDt(LocalDateTime.now())
                .build();
        return car;
    }

    /* 
     * condition(dto >> entity)
     */
    public static MemberCondition toEntityCondition(MemberConditionDto dto, Member member) {
        MemberCondition condition = MemberCondition.builder()
                .member(member)
                .conditionCode(dto.getConditionCode())
                .agreeYn(dto.getAgreeYn())
                .section(dto.getSection())
                .build();
        return condition;
    }
}
