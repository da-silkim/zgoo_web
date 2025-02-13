package zgoo.cpos.domain.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CONDITION_VERSION_HIST")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ConditionVersionHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_version_id")
    private Long id;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_name", nullable = false)
    private String storedName;

    @Column(name = "memo")
    private String memo;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "apply_dt")
    private LocalDateTime applyDt;

    @Column(name = "apply_yn")
    private String applyYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_code")
    private ConditionCode conditionCode;

    public void updateConditionVersionHistInfo(String applyYn) {
        this.applyYn = applyYn;
    }
}
