package zgoo.cpos.repository.fw;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.fw.CpFwversionDto;

public interface CpFwVersionRepositoryCustom {
    Page<CpFwversionDto> findAll(Pageable pageable, String levelPath, boolean isAdmin);

    List<CpFwversionDto> findFwVersionListByCompanyAndModel(Long companyId, String modelCode);

    String findFwUrlByCompanyAndModelAndVersion(Long companyId, String modelCode, String version);
}
