package zgoo.cpos.repository.member;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.member.MemberCar;
import zgoo.cpos.domain.member.QMemberCar;
import zgoo.cpos.dto.member.MemberDto.MemberCarDto;

@Slf4j
@RequiredArgsConstructor
public class MemberCarRepositoryCustomImpl implements MemberCarRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMemberCar car = QMemberCar.memberCar;
    QCommonCode carTypeName = new QCommonCode("carType");

    @Override
    public List<MemberCarDto> findAllByMemberIdDto(Long memberId) {
        return queryFactory.select(Projections.fields(MemberCarDto.class,
            car.carType.as("carType"),
            car.carNum.as("carNum"),
            car.model.as("model"),
            car.regDt.as("carRegDt"),
            carTypeName.name.as("carTypeName")))
            .from(car)
            .leftJoin(carTypeName).on(car.carType.eq(carTypeName.commonCode))
            .where(car.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public List<MemberCar> findAllByMemberId(Long memberId) {
        return queryFactory
            .selectFrom(car)
            .where(car.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        queryFactory
            .delete(car)
            .where(car.member.id.eq(memberId))
            .execute();
    }
}
