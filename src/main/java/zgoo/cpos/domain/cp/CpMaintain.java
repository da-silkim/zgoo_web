package zgoo.cpos.domain.cp;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CP_MAINTAIN")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CpMaintain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpmaintain_id")
    private Long id;

    @Column(name = "charger_id", nullable = false)
    private String chargerId;

    @Column(name = "regDt", nullable = false)
    private LocalDateTime regDt;

    @Column(name = "error_type")
    private String errorType;

    @Column(name = "error_content")
    private String errorContent;

    @Column(name = "picture_loc1")
    private String pictureLoc1;

    @Column(name = "picture_loc2")
    private String pictureLoc2;

    @Column(name = "picture_loc3")
    private String pictureLoc3;

    @Column(name = "process_date")
    private LocalDateTime processDate;

    @Column(name = "process_status")
    private String processStatus;

    @Lob
    @Column(name = "process_content")
    private String processContent;

    @Column(name = "reg_user_id")
    private String regUserId;

    public void updateCpMaintainInfo() {
        
    }
}
