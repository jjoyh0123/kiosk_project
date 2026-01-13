# 🍔 Kiosk Project

## 📌 프로젝트 개요
패스트푸드점의 키오스크를 웹 기반으로 구현한 팀 프로젝트입니다.  
관리자와 고객 화면을 분리하여 주문 · 결제 · 쿠폰 관리 · 고객 성향 분석 등  
실제 매장 운영 흐름을 시뮬레이션할 수 있도록 설계했습니다.  

---

## 👨‍💻 담당 역할 (My Contribution)
- 관리자 로그인 및 권한 설정
- 주문 관리 기능
- 쿠폰 관리 기능
- 고객 성향 분석 모듈

---

## 🔧 Tech Stack
- **Language**: Java  
- **Web**: JSP / Servlet 기반 웹 애플리케이션  
- **Database**: MySQL  
- **Persistence**: MyBatis (Mapper XML 기반)  
- **Frontend**: HTML, CSS, JavaScript  
- **Tools & IDE**: Eclipse, GitHub  
- **Libraries**:  
  - MyBatis 3.5.16  
  - MySQL Connector/J 8.0.33  

---

## 🏗️ Architecture & Structure
- `config/config.xml` → DB 및 MyBatis 환경 설정  
- `mapper/*.xml` → 관리자/고객 기능별 SQL 매핑 (로그인, 주문, 쿠폰, 성향분석 등)  
- `src/kiosk/*` → 관리자/클라이언트 기능별 Java 클래스 패키지  
- `static/` → UI용 이미지 리소스  

---

## 🖼️ Architecture Diagram
사용자와 관리자가 각각 웹 화면을 통해 요청을 보내면,  
Controller → Service Layer → Mapper → Database 순으로 동작합니다.  

