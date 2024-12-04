# 테이블 구조

---

### 축약어 정리
#### PK: Primary Key
#### FK: Foreign Key
#### AI: Auto_Increment
#### NN: Not Null
#### UK: Unique Key

---

### admin (관리자)
| 키  | 컬럼명      | 데이터타입  |한글명|기본값|추가| 설명                            |
|----|----------|--------|--|--|--|-------------------------------|
| **PK** | adminIdx | bigint |관리자 인덱스||AI| 고유 식별자, 자동 증가                 |
|    |adminId|varchar(20)|관리자 아이디||NN|                               |
||adminPassWord|varchar(20)|관리자 비밀번호||NN|                               |
||adminGrade|varchar(20)|관리자 등급||NN| senior: 주매니저<br>junior: 부매니저 |

### user (사용자)
| 키  | 컬럼명      | 데이터타입  |한글명|기본값|추가| 설명                         |
|----|----------|--------|--|--|--|----------------------------|


