## 🗺프로젝트 아키텍처

![image](https://github.com/jhchoi1182/pet-project-back/assets/116577489/32d82649-c833-4ca3-9d02-517964391e2d)

## 🎉소개
이 프로젝트는 배운 기술을 실제로 적용하며 점진적으로 기능을 추가해 나가는 것을 목표로 진행되고 있는 개인 프로젝트입니다.

<br>

현재 기능 - 스프링 부트를 이용해 개발한 게시판 API   
프론트엔드 코드 : https://github.com/jhchoi1182/pet-project-front

<br>

## ✨특징
* User, Post, Comment 모델의 데이터 테이블: RDBMS를 이용해 Entity 간 관계 매핑
* **DTO를 활용**함으로써 계층 간 데이터 관리의 안정성, 효율성 확보
* **불필요한 쿼리 요청 최적화**
  * 게시글 삭제 시 @Modifying 및 @Query 어노테이션을 활용하여 Comment 삭제에 대한 **벌크 업데이트**
* **AWS EC2, Docker Compose를 이용해 배포**하여 배포 일관성 유지
  * 프로젝트의 일관성을 보장하고 관리를 간소화하기 위해 프론트엔드, 백엔드, 데이터베이스를 통합된 컨테이너 환경으로 구성하는 것을 목표로 Docker Compose를 도입
  * Docker로 배포 시 AWS EC2의 메모리 부족으로 인한 **CPU 과부하 문제 발생**
  * 구글링을 통해 AWS 공식 문서 확인 후 **추가적인 스왑 공간 할당을 통해 CPU 과부하 문제 해결**
  * 프론트엔드 프로젝트는 별도로 정적 배포하는 것으로 배포 안정성 확보
* **Github Actions를 통한 CI/CD 프로세스 구축**으로 개발 편의성 개선 및 배포 속도 향상
* **Nginx를 활용하여 외부 서버에서 HTTPS 프로토콜을 구성**
  * http 요청을 https로 rewrite하고 리버스 프록시를 통해 컨테이너에 연결
* 유저에 대한 보안 강화
  * **Jwt 토큰을 사용**하여 보안 강화
  * jwt 토큰 생성 후 **응답 헤더에 쿠키 설정 (httpOnly, seecure, sameSite 설정으로 XSS, CSRF 공격 대비)**
  * Spring Security를 이용해 인증 로직 전역 핸들링
* Swagger를 통한 API 명세 작업

<br>

## ✏배운점
* **REST API 설계 및 구현에 대한 이해**
  * Spring Security와 JWT 토큰을 이용해 API에 대한 권한 부여와 인증 방식 이해
  * 각종 에러 케이스 핸들링과 상태 코드에 대한 학습
  * 관계형 데이터베이스 모델에서의 모델 간 관계 맵핑 방식 이해
* **아키텍처에 대한 이해 및 설계 능력 향상**
  * Data-Domain-Presentation의 클린 아키텍처 구조 이해 
  * 계층 간 분리된 데이터 관리를 위한 DTO 개념 학습
  * 개발, 테스팅, 배포를 일관되고 효율적으로 관리하기 위한 Docker 학습
* **배포 프로세스 이해**
  * EC2, Docker, 외부 서버(Nginx), SSL/TLS 인증서를 통한 HTTPS 설정, 리버스 프록시에 대해 공부하고 이 이해를 바탕으로 CI/CD 설정
  * 프로젝트의 배포 과정에 대한 이해 향상
