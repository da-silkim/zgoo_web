package zgoo.cpos.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.calc.PurchaseInfo;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseAccountDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseDetailDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseElecDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseRegDto;
import zgoo.cpos.mapper.PurchaseMapper;
import zgoo.cpos.repository.calc.PurchaseRepository;
import zgoo.cpos.repository.cs.CsRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final CsRepository csRepository;
    private final PurchaseRepository purchaseRepository;

    // 매입 조회
    public Page<PurchaseListDto> findPurchaseInfoWithPagination(String searchOp, String searchContent,
            LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<PurchaseListDto> purList;

            if ((searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty()) && 
                    startDate == null && endDate == null) {
                log.info("Executing the [findPurchaseWithPagination]");
                purList = this.purchaseRepository.findPurchaseWithPagination(pageable);
            } else {
                log.info("Executing the [searchPurchaseWithPagination]");
                purList = this.purchaseRepository.searchPurchaseWithPagination(searchOp, searchContent, startDate,
                    endDate, pageable);
            }

            return purList;
        } catch (Exception e) {
            log.error("[findPurchaseInfoWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 매입 단건 조회
    public PurchaseRegDto findPurchaseOne(Long id) {
        try {
            return this.purchaseRepository.findPurchaseOne(id);
        } catch (Exception e) {
            log.error("[findPurchaseOne] error: {}", e.getMessage());
            return null;
        }
    }

    // 계정과목 정보 조회
    public PurchaseAccountDto searchPurchaseAccount(String accountCode, String stationId) {
        try {

            PurchaseAccountDto result = switch (accountCode) {
                case "LANDFEE" -> {
                    PurchaseAccountDto landResult = this.purchaseRepository.searchAccountLand(stationId);
                    if ("RATE".equals(landResult.getLandUseType())) {
                        yield this.purchaseRepository.searchAccountLand(stationId);
                    } else {
                        yield landResult;
                    }
                }
                case "SMFEE" -> this.purchaseRepository.searchAccountSafety(stationId);
                default -> PurchaseAccountDto.builder()
                    .unitPrice(0)
                    .supplyPrice(0)
                    .vat(0)
                    .totalAmount(0)
                    .build();
            };

            return result;
        } catch (Exception e) {
            log.error("[searchPurchaseAccount] error: {}", e.getMessage());
            return PurchaseAccountDto.builder()
                    .unitPrice(0)
                    .supplyPrice(0)
                    .vat(0)
                    .totalAmount(0)
                    .build();
        }
    }

    // 매입 상세 조회
    public PurchaseDetailDto findPurchaseDetailOne(Long id) {
        try {
            return this.purchaseRepository.findPurchaseDetailOne(id);
        } catch (Exception e) {
            log.error("[findPurchaseDetailOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 매입 등록
    @Transactional
    public void savePurchase(PurchaseRegDto dto, String regUserId) {
        try {
            CsInfo station = this.csRepository.findStationOne(dto.getStationId());
            PurchaseInfo purchase = PurchaseMapper.toEntity(dto, station, regUserId);
            this.purchaseRepository.save(purchase);
        } catch (Exception e) {
            log.error("[savePurchase] error: {}", e.getMessage());
        }
    }

    // 한전 등록
    @Transactional
    public int savePurchaseElec(PurchaseElecDto dtos, String regUserId) {
        List<PurchaseRegDto> regDtos = dtos.getElectricity();
        int count = 0;

        try {
            for (PurchaseRegDto dto : regDtos) {
                CsInfo station = this.csRepository.findStationByKepcoCustNo(dto.getKepcoCustNo());

                if (station == null) {
                    log.warn("충전소에 등록되지 않은 고객번호입니다. kepcoCustNo: {}", dto.getKepcoCustNo());
                    continue;
                }

                if ("자동이체".equals(dto.getPaymentMethod())) {
                    dto.setPaymentMethod("PM05");
                } else {
                    dto.setPaymentMethod("PM07");  // 직접입금
                }
                
                PurchaseInfo purchase = PurchaseMapper.toEntityElec(dto, station, regUserId);
                this.purchaseRepository.save(purchase);
                count++;
            }
        } catch (Exception e) {
            log.error("[savePurchaseElec] error: {}", e.getMessage());
        }

        return count;
    }

    // 매입 수정
    @Transactional
    public void updatePurchaseInfo(Long id, PurchaseRegDto dto, String modUserId) {
        PurchaseInfo purchase = this.purchaseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("purchase info not found with id: " + id));

        try {
            purchase.updatePurchase(dto, modUserId);
        } catch (Exception e) {
            log.error("[updatePurchaseInfo] error: {}", e.getMessage());
        }
    }
    
    // 매입 삭제
    @Transactional
    public void deletePurchaseInfo(Long id) {
        try {
            Long count = this.purchaseRepository.deletePurchaseOne(id);
            log.info("=== delete purchase Info: {}", count);
        } catch (Exception e) {
            log.error("[deletePurchaseInfo] error: {}", e.getMessage());
        }
    }

    // 매입 조회(엑셀)
    public List<PurchaseListDto> findAllPurchaseWithoutPagination(String searchOp, String searchContent,
            LocalDate startDate, LocalDate endDate) {
        log.info("=== Finding all station list: searchOp={}, searchContent={}, startDate={}, endDate={} ===", 
            searchOp, searchContent, startDate, endDate);

        try {
            return this.purchaseRepository.findAllPurchaseWithoutPagination(searchOp, searchContent, startDate, endDate);
        } catch (Exception e) {
            log.error("[findAllPurchaseWithoutPagination] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
