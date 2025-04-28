package zgoo.cpos.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.payment.ChargerPaymentInfo;

public interface ChargerPaymentInfoRepository
        extends JpaRepository<ChargerPaymentInfo, Long>, ChargerPaymentInfoRepositoryCustom {

}
