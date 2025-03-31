package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.users.Notice;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.NoticeDto;

public class NoticeMapper {

    /* 
     * Dto >> Entity
     */
    public static Notice toEntity(NoticeDto.NoticeRegDto dto, Users users) {
        Notice notice = Notice.builder()
                            .title(dto.getTitle())
                            .content(dto.getContent())
                            .type(dto.getType())
                            .views(0L)
                            .delYn("N")
                            .user(users)
                            .regDt(LocalDateTime.now())
                            .startDate(dto.getStartDate())
                            .endDate(dto.getEndDate())
                            .build();
        return notice;
    }

    /* 
     * Entity >> Dto
     */
    public static NoticeDto.NoticeRegDto toDto(Notice entity) {
        NoticeDto.NoticeRegDto dto = NoticeDto.NoticeRegDto.builder()
                            .idx(entity.getIdx())
                            .title(entity.getTitle())
                            .content(entity.getContent())
                            .type(entity.getType())
                            .userId(entity.getUser().getUserId())
                            .delYn(entity.getDelYn())
                            .regDt(entity.getRegDt())
                            .startDate(entity.getStartDate())
                            .endDate(entity.getEndDate())
                            .build();
        return dto;
    }
}
