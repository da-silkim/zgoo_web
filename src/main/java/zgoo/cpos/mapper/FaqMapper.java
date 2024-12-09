package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.users.Faq;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.FaqDto;

public class FaqMapper {

    /* 
     * Dto >> Entity
     */
    public static Faq toEntity(FaqDto.FaqRegDto dto, Users users) {
        Faq faq = Faq.builder() 
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .section(dto.getSection())
                    .delYn("N")
                    .user(users)
                    .regDt(LocalDateTime.now())
                    .build();
        return faq;
    }

    /* 
     * Entity >> Dto
     */
    public static FaqDto.FaqRegDto toDto(Faq entity) {
        FaqDto.FaqRegDto dto = FaqDto.FaqRegDto.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .content(entity.getContent())
                        .userId(entity.getUser().getUserId())
                        .delYn(entity.getDelYn())
                        .section(entity.getSection())
                        .regDt(entity.getRegDt())
                        .build();
        return dto;
    }
}
