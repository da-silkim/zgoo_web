package zgoo.cpos.domain.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "CONDITION_CODE")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ConditionCode {

    @Id
    @Column(name = "condition_code")
    private String conditionCode;

    @Column(name = "condition_name")
    private String conditionName;

    @Column(name = "section")
    private String section;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;
}
