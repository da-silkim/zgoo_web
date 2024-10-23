package zgoo.cpos.type;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommonCodeKey implements Serializable {

    private String grpCode;
    @Column(name = "common_code")
    private String commonCode;
}
