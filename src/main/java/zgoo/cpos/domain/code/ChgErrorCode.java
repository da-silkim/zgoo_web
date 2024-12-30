package zgoo.cpos.domain.code;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeRegDto;

@Entity
@Table(name = "CHG_ERRORCD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ChgErrorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "errcd_id")
    private Long id;

    @Column(name = "err_code", unique = true)
    private String errCode;

    @Column(name = "menuf_code")
    private String menufCode;

    @Column(name = "err_name")
    private String errName;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    public void updateChgErrorCode(ChgErrorCodeRegDto dto) {
        this.errCode = dto.getErrCode();
        this.menufCode = dto.getMenufCode();
        this.errName = dto.getErrName();
        this.modDt = LocalDateTime.now();
    }
}
