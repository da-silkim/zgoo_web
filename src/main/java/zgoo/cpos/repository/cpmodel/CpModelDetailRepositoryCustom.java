package zgoo.cpos.repository.cpmodel;

import zgoo.cpos.domain.cp.CpModelDetail;

public interface CpModelDetailRepositoryCustom {
    CpModelDetail findByModelId(Long modelId);
    void deleteByModelId(Long modelId);
}
