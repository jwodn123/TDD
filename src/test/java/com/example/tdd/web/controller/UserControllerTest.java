package com.example.tdd.web.controller;

import com.example.tdd.web.entity.User;
import com.example.tdd.web.repository.UserRepository;
import com.example.tdd.web.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.*;

// JUnit5로 CRUD Test

// @SpringBootTest -> 통합 테스트이며 테스트를 위해 Spring 애플리케이션 컨텍스트를 시작하는 데 사용
// (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) -> 내장된 웹 서버에 임의 포트를 사용하도록 테스트를 구성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    // 테스트 서버가 실행 중인 로컬 포트 번호를 포트 변수에 삽입
    @LocalServerPort
    int port;

    @Autowired
    // TestRestTemplate는 Spring RESTful 응용 프로그램을 테스트하기 위해 특별히 설계된 RestTemplate의 편리한 하위 클래스
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    // Junit5 생명주기 메서드
    // userRepository.deleteAll()을 호출하여 데이터베이스에서 모든 사용자 데이터를 삭제
    // 이렇게 하면 각 테스트 전에 깨끗한 상태가 보장
    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    // TestRestTemplate을 이용해 POST 요청으로 유저의 정보를 넘겨 DB에 저장하고,
    // DB에서 해당 유저의 정보를 조회하여 입력한 정보와 같은지 검증
    @Test
    void create_test() {
        // given
        String name = "JEON";
        int age = 28;
        String address = "Seoul";

        User user = User.builder()
                .name(name)
                .age(age)
                .address(address)
                .build();

        String url = "http://localhost:" + port + "/user";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, user, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        User savedUser = userService.findById(responseEntity.getBody());
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getAge()).isEqualTo(age);
        assertThat(savedUser.getAddress()).isEqualTo(address);
    }

    @Test
    void read_test() {
        // given
        String name = "JEON";
        int age = 28;
        String address = "Seoul";

        Long savedId = userRepository.save(User.builder()
                .name(name)
                .age(age)
                .address(address)
                .build()).getId();

        String url = "http://localhost:" + port + "/user/" + savedId;

        // when
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(url, User.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        User readUser = responseEntity.getBody();
        assertThat(readUser.getName()).isEqualTo(name);
        assertThat(readUser.getAge()).isEqualTo(age);
        assertThat(readUser.getAddress()).isEqualTo(address);
    }


    @Test
    void update_test() {
        // given
        String name = "JEON";
        int age = 28;
        String address = "Seoul";

        Long savedId = userRepository.save(User.builder()
                .name(name)
                .age(age)
                .address(address)
                .build()).getId();

        String url = "http://localhost:" + port + "/user/" + savedId + "/update";
        String address2 = "Busan";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, address2, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        User updatedUser = userService.findById(savedId);
        assertThat(updatedUser.getAddress()).isEqualTo(address2);
    }

    @Test
    void delete_test() {
        // given
        String name = "JEON";
        int age = 28;
        String address = "Seoul";

        Long savedId = userRepository.save(User.builder()
                .name(name)
                .age(age)
                .address(address)
                .build()).getId();

        String url = "http://localhost:" + port + "/user/" + savedId + "/delete";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, null, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThatThrownBy(() -> userService.findById(savedId)).isInstanceOf(IllegalArgumentException.class);
    }


}
