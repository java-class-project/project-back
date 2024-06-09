# OOP Class Project

## 1. 프로젝트 소개
학생들이 팀 프로젝트를 조직하고 관리할 수 있도록 돕는 웹 애플리케이션입니다. 
이 애플리케이션을 통해 사용자는 팀을 만들고, 프로젝트를 생성하고, 참여 신청을 받고 관리할 수 있습니다.

해당 프로젝트는 2024년 1학기 객체지향프로그래밍 팀프로젝트 과제로 진행하였습니다.

로컬 환경에서 개발 후 AWS EC2 우분투 서버에서 배포하였습니다. 

## 2. 시작 가이드

### (1) 요구 사항
- Java 11 이상
- Spring Boot 2.6 이상
- PostgreSQL 데이터베이스
- Redis 서버
- Gradle

### (2) 설치 및 실행
다음 단계를 따라 프로젝트를 로컬 환경에 설정하고 실행할 수 있습니다:

1. 프로젝트 클론
    ```bash
    git clone https://github.com/your-repository/oop-class.git
    cd oop-class
    ```

2. 패키지 설치
    ```bash
    ./gradlew build
    ```

3. 환경변수 설정
    `resources` 디렉토리에 `.env` 파일을 생성하고 필요한 값을 추가합니다.
    

4. 애플리케이션 실행
    ```bash
    ./gradlew bootRun
    ```

## 3. 기술 스택
- **Environment:** Java 11, Spring Boot 2.6, Gradle
- **Config:** Spring Data JPA, PostgreSQL, Redis, Spring Security, JWT
- **Development:** IntelliJ IDEA, Lombok
- **Communication:** Spring WebSocket, STOMP, Swagger (OpenAPI)

## 4. API 주소 목록

로그인 후에는 반환된 토큰을 `Authorization: Bearer <토큰>` 형식으로 Header에 넣어서 요청.

### 인증 API
| 기능 | 메서드 | URL | 설명 | Body |
|---|---|---|---|---|
| 로그인 | POST | /v1/auth/login | 사용자 로그인 | `{ "userId": "string", "password": "string" }` |
| 로그아웃 | POST | /v1/auth/logout | 사용자 로그아웃 | `None` |

SpringSecurity + JWT Auth 방식으로 구현하였다.

사용자가 로그인 요청을 하면 서버는 JWT 토큰을 생성하여 클라이언트에 반환한다.

이후 작업들은 header에 토큰을 넣어서 함께 요청하는 방식으로 인증된 사용자를 구분한다.

### 유저 API
| 기능 | 메서드 | URL | 설명 | Body / Params |
|---|---|---|---|---|
| 유저 정보 조회 | GET | /v1/user/{userUuid} | 특정 유저 정보 조회 | `None` |
| 유저 정보 수정 | PUT | /v1/user/{userUuid} | 특정 유저 정보 수정 | `{ "username": "string", "studentNumber": "string", "mainMajorUuid": "UUID", "subMajor1Uuid": "UUID", "subMajor2Uuid": "UUID" }` |
| 유저 등록 | POST | /v1/user/register | 새로운 유저 등록 | `{ "userId": "string", "username": "string", "password": "string", "confirmPassword": "string", "studentNumber": "string", "mainMajor": "UUID", "subMajor1": "UUID", "subMajor2": "UUID" }` |
| 아이디 중복 확인 | GET | /v1/user/register/check-userid | 아이디 중복 확인 | `userId=string` |
| 학번 중복 확인 | GET | /v1/user/register/check-studentnumber | 학번 중복 확인 | `studentNumber=string` |

### 모임 API
| 기능 | 메서드 | URL | 설명 | Body / Params |
|---|---|---|---|---|
| 모임 생성 | POST | /v1/meetings | 새로운 모임 생성 | `{ "teamType": "string", "classNum": "int", "majorUuid": "UUID", "subjectUuid": "UUID", "desiredCount": "int", "title": "string", "description": "string" }` |
| 모임 수정 | PUT | /v1/meetings/{meetingId} | 특정 모임 정보 수정 | `{ "teamType": "string", "classNum": "int", "majorUuid": "UUID", "subjectUuid": "UUID", "desiredCount": "int", "title": "string", "description": "string" }` |
| 모임 삭제 | DELETE | /v1/meetings/{meetingId} | 특정 모임 삭제 | `None` |
| 모든 모임 조회 | GET | /v1/meetings | 모든 모임 조회 | `None` |
| 모임 검색 | GET | /v1/meetings/search | 조건에 따른 모임 검색 | `majorUuid=UUID&subjectUuid=UUID&teamTypes=list&classNum=int&desiredCount=int&searchText=string&status=list` |
| 모임 신청 | POST | /v1/meetings/{meetingId}/apply | 특정 모임에 신청 | `None` |
| 모임 신청 응답 | POST | /v1/meetings/{meetingId}/respond | 특정 모임 신청에 대한 응답 | `applicantId=string&accepted=boolean` |
| 특정 유저의 모임 조회 | GET | /v1/meetings/{userUuid} | 특정 유저가 생성하거나 참여한 모임 조회 | `None` |
| 모임 유저 조회 | GET | /v1/meetings/{meetingId}/users | 특정 모임의 유저 조회 | `None` |

모임 신청 및 수락 여부 결정은 Redis와 Websocket 을 이용해 실시간으로 알림 데이터를 전송, 저장, 관리하게끔 구현하였다.

- 모임 신청
    
    사용자가 모임 신청 요청을 보낸다.
    이때 신청 정보가 Redis에 저장되어 빠른 데이터 접근 및 처리가 가능해지고, Websocket을 이용해 모임 생성자에게 실시간으로 알림이 전송된다.
    
- 모임 신청 수락 및 거절
    
    모임 생성자가 신청을 수락하거나 거절한다.
    이때 신청 상태는 Redis에서 업데이트가 되며, 데이터베이스에서 모임 정보 테이블의 모임 구성 인원 컬럼값이 1이 추가되면서 업데이트 된다.

### 전공 API
| 기능 | 메서드 | URL | 설명 | Body / Params |
|---|---|---|---|---|
| 전공 생성 | POST | /v1/major | 새로운 전공 생성 | `{ "majorName": "string" }` |
| 모든 전공 조회 | GET | /v1/major | 모든 전공 조회 | `None` |

### 과목 API
| 기능 | 메서드 | URL | 설명 | Body / Params |
|---|---|---|---|---|
| 과목 생성 | POST | /v1/subjects | 새로운 과목 생성 | `{ "subjectName": "string", "majorUuid": "UUID" }` |
| 모든 과목 조회 | GET | /v1/subjects | 모든 과목 조회 | `major=UUID` (Optional) |

### 알림 API
| 기능 | 메서드 | URL | 설명 | Body / Params |
|---|---|---|---|---|
| 알림 삭제 | DELETE | /v1/notifications/{notificationId} | 특정 알림 삭제 | `None` |

## 5. 주요 기능
- 사용자 인증 및 권한 관리
- 모임 생성, 수정, 삭제
- 모임 검색 및 필터링
- 모임 신청 및 응답 관리
- 실시간 알림 시스템 (WebSocket)
- 전공 및 과목 관리

## 6. 아키텍처
본 프로젝트는 Spring Boot 프레임워크를 기반으로 하며, JWT를 사용한 인증 및 권한 관리, PostgreSQL을 사용한 데이터베이스 관리, Redis를 사용한 캐시 및 실시간 알림 시스템을 포함합니다.

## 7. 데이터베이스
<img width="824" alt="image" src="https://github.com/java-class-project/project-back/assets/70712293/c70da90e-a8bb-4f80-a0ad-0b2fa7099dbf">

### users 테이블
| 컬럼명           | 데이터 타입  | 설명                |
|------------------|--------------|---------------------|
| main_major_uuid  | uuid         | 메인 전공 UUID       |
| sub_major1_uuid  | uuid         | 서브 전공 1 UUID     |
| sub_major2_uuid  | uuid         | 서브 전공 2 UUID     |
| user_uuid        | uuid         | 사용자 UUID         |
| password         | varchar(255) | 비밀번호            |
| role             | varchar(255) | 사용자 역할         |
| student_number   | varchar(255) | 학번                |
| user_id          | varchar(255) | 사용자 아이디       |
| username         | varchar(255) | 사용자 이름         |

### major 테이블
| 컬럼명    | 데이터 타입  | 설명          |
|-----------|--------------|---------------|
| major_uuid| uuid         | 전공 UUID     |
| major_name| varchar(255) | 전공 이름     |

### subject 테이블
| 컬럼명       | 데이터 타입   | 설명         |
|--------------|---------------|--------------|
| subject_uuid | uuid          | 과목 UUID    |
| subject_name | varchar(50000)| 과목 이름    |
| major_uuid   | uuid          | 전공 UUID    |

### meetings 테이블
| 컬럼명                         | 데이터 타입   | 설명                     |
|--------------------------------|---------------|--------------------------|
| subject_uuid                   | uuid          | 과목 UUID                |
| team_type                      | varchar(5000) | 팀 타입                  |
| created_at                     | timestamp(6)  | 생성 일시                |
| updated_at                     | timestamp(6)  | 업데이트 일시            |
| deleted_at                     | timestamp(6)  | 삭제 일시                |
| description                    | varchar(255)  | 설명                     |
| desired_count                  | integer       | 원하는 인원              |
| title                          | varchar(255)  | 제목                     |
| major_uuid                     | uuid          | 전공 UUID                |
| user_uuid                      | uuid          | 사용자 UUID              |
| class_num                      | integer       | 클래스 번호              |
| meeting_uuid                   | uuid          | 모임 UUID                |

### meeting_info 테이블
| 컬럼명                         | 데이터 타입   | 설명                     |
|--------------------------------|---------------|--------------------------|
| meeting_info_uuid              | uuid          | 모임 정보 UUID           |
| meeting_recruitment            | integer       | 모집 인원                |
| meeting_recruitment_finished   | integer       | 모집 완료 인원           |
| meeting_uuid                   | uuid          | 모임 UUID                |

### meeting_status 테이블
| 컬럼명                         | 데이터 타입   | 설명                     |
|--------------------------------|---------------|--------------------------|
| meeting_uuid                   | uuid          | 모임 UUID                |
| user_uuid                      | uuid          | 사용자 UUID              |
| meeting_status_uuid            | uuid          | 모임 상태 UUID           |

### notifications 테이블
| 컬럼명            | 데이터 타입  | 설명           |
|-------------------|--------------|----------------|
| message           | text         | 알림 메시지    |
| created_at        | timestamp    | 생성 일시      |
| is_read           | boolean      | 읽음 여부      |
| user_id           | varchar(255) | 사용자 아이디  |
| notification_uuid | uuid         | 알림 UUID      |
