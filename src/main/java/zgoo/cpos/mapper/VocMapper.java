package zgoo.cpos.mapper;

import java.time.LocalDateTime;

import zgoo.cpos.domain.member.Member;
import zgoo.cpos.domain.member.Voc;
import zgoo.cpos.dto.member.VocDto.VocRegDto;

public class VocMapper {

    /* 
     * dto >> entity
     */
    public static Voc toEntity(VocRegDto dto, Member member) {
        Voc voc = Voc.builder()
            .member(member)
            .type(dto.getType())
            .title(dto.getTitle())
            .content(dto.getContent())
            .regDt(LocalDateTime.now())
            .delYn("N")
            .replyStat(dto.getReplyStat())
            .replyDt(dto.getReplyDt())
            .replyContent(dto.getReplyContent())
            .channel(dto.getChannel())
            .regUserId(dto.getRegUserId())
            .replyUserId(dto.getReplyUserId())
            .build();
        return voc;
    }
}
