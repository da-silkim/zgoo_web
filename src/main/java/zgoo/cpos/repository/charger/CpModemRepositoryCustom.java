package zgoo.cpos.repository.charger;

public interface CpModemRepositoryCustom {

    // 모뎀 시리얼번호 중복체크
    boolean isModemSerialDuplicate(String serialnum);

    // 모뎀번호 중복체크
    boolean isModemNumDuplicate(String modemNum);
}
