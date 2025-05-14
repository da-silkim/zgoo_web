package zgoo.cpos.repository.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.history.ChgCommlogDto;

public interface ChgCommLogRepositoryCustom {
    Page<ChgCommlogDto> findAllChgCommlog(Pageable pageable, String levelPath, boolean isSuperAdmin);

    Page<ChgCommlogDto> findChgCommlog(String searchOp, String searchContent, String recvFrom,
            String recvTo, Pageable pageable, String levelPath, boolean isSuperAdmin);
}
