# 금연인

**하루에 하나씩 줄여가는 금연 보조 안드로이드 애플리케이션입니다.**

**· 개발인원 :** 1명

**· 개발환경 :** Android Studio, AWS EC2(Ubuntu)

**· 프로토콜 :** HTTP, TCP/IP

**· 개발언어 :** JAVA, PHP

**· 웹서버 :** Apache

**· 데이터베이스 :** MySql

**· 상세내용**

1. 전체적인 UX/UI : BottomNavigationView & Fragment를 사용하여 메뉴와 3개의 화면(홈, 통계, 계정)을 구현하였습니다. CalendarView를 사용하여 달력 UI 구현하였습니다.

2. 자동로그인 : sharedpreferences를 사용하여 자동로그인 구현하였습니다.

3. 하루 단위 정보 저장 : Linux crontab을 사용하여 DB에 사용자의 일별 목표개수와 흡연횟수를 저장하였습니다.
