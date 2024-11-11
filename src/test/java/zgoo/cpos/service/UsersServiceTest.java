package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.dto.users.UsersDto.UsersListDto;
import zgoo.cpos.dto.users.UsersDto.UsersRegDto;

@SpringBootTest
@Transactional
public class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Test
    @DisplayName("사용자 - 전체 조회")
    public void findUsersAll() throws Exception {

        List<UsersDto.UsersListDto> ulist = this.usersService.findUsersAll();
        
        for (UsersListDto usersListDto : ulist) {
            System.out.println("=== find all users info reslut : " + usersListDto.toString());
        }
    }

    @Test
    @Rollback(false)
    @DisplayName("사용자 - 등록")
    public void createUsers() throws Exception {

        UsersRegDto dto = UsersRegDto.builder()
                .companyId(1L)
                .userId("test4")
                .password("1234")
                .name("test4")
                .phone("01022224444")
                .email("teset4@test.com")
                .authority("AD")
                .regDt(LocalDateTime.now())
                .build();

        this.usersService.saveUsers(dto);

        List<UsersDto.UsersListDto> ulist = this.usersService.findUsersAll();
        for (UsersListDto usersListDto : ulist) {
            System.out.println("=== full search after saving user info : " + usersListDto.toString());
        }
    }

    @Test
    @Rollback(false)
    @DisplayName("사용자 - 수정")
    public void updateUsers() throws Exception {

        // before update
        UsersRegDto before = this.usersService.findUserOne("test4");
        System.out.println("=== before update: " + before.toString());


        // after update
        UsersRegDto dto = UsersRegDto.builder()
                .companyId(1L)
                .userId("test4")
                .password("12345")
                .name("test4")
                .phone("01022227777")
                .email("teset4@test.com")
                .authority("AD")
                .regDt(LocalDateTime.now())
                .build();
        
        
        this.usersService.updateUsers(dto);
        UsersRegDto after = this.usersService.findUserOne("test4");
        System.out.println("=== after update: " + after.toString());
    }

    @Test
    @Rollback(false)
    @DisplayName("사용자 - 삭제")
    public void deleteUsers() throws Exception {

        // before delete
        UsersRegDto before = this.usersService.findUserOne("test4");
        System.out.println("=== before delete: " + before.toString());

        // after delete
        this.usersService.deleteUsers("test4");

        List<UsersDto.UsersListDto> ulist = this.usersService.findUsersAll();
        for (UsersListDto usersListDto : ulist) {
            System.out.println("=== full search after deleting user info : " + usersListDto.toString());
        }
    }
}
