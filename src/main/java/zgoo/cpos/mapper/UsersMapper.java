package zgoo.cpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.UsersDto;

public class UsersMapper {

    /*
     * Dto >> Entity
     */
    public static Users toEntity(UsersDto.UsersRegDto dto, Company company) {
        Users user = Users.builder()
            .userId(dto.getUserId())
            .name(dto.getName())
            .password(dto.getPassword())
            .phone(dto.getPhone())
            .email(dto.getEmail())
            .authority(dto.getAuthority())
            .regDt(LocalDateTime.now())
            .company(company)
            .build();

        return user;
    }

    /*
     * Entity >> Dto (등록용)
     */
    public static UsersDto.UsersRegDto toDto(Users entity) {
        UsersDto.UsersRegDto dto = UsersDto.UsersRegDto.builder()
            .companyId(entity.getCompany().getId())
            .userId(entity.getUserId())
            .name(entity.getName())
            .password(entity.getPassword())
            .phone(entity.getPhone())
            .email(entity.getEmail())
            .authority(entity.getAuthority())
            .regDt(entity.getRegDt())
            .build();

        return dto;
    }

    /*
     * Entity >> Dto (리스트 조회용)
     */
    public static UsersDto.UsersListDto toListDto(Users entity) {
        UsersDto.UsersListDto dto = UsersDto.UsersListDto.builder()
            .companyId(entity.getCompany().getId())
            .companyName(entity.getCompany().getCompanyName())
            .companyType(entity.getCompany().getCompanyType())
            .userId(entity.getUserId())
            .name(entity.getName())
            .password(entity.getPassword())
            .phone(entity.getPhone())
            .email(entity.getEmail())
            .authority(entity.getAuthority())
            .regDt(entity.getRegDt())
            .build();

        return dto;
    }

    /*
     * Entity list >> Dto list
     */
    public static List<UsersDto.UsersListDto> toDtoList(List<Users> entitylist) {
        List<UsersDto.UsersListDto> dtolist = entitylist.stream()
                        .map(userinfo -> UsersDto.UsersListDto.builder()
                            .companyId(userinfo.getCompany().getId())
                            .companyName(userinfo.getCompany().getCompanyName())
                            .companyType(userinfo.getCompany().getCompanyType())
                            .userId(userinfo.getUserId())
                            .password(userinfo.getPassword())
                            .name(userinfo.getName())
                            .phone(userinfo.getPhone())
                            .email(userinfo.getEmail())
                            .authority(userinfo.getAuthority())
                            .regDt(userinfo.getRegDt())
                            .build())
                        .collect(Collectors.toList());
        return dtolist;
    }
}
