# Generic DAO 

## 개요

JDBC 를 이용한 CRUD작업시 중복되는 코드가 대량으로 발생하고
데이터가 하나 수정될때마다 많은줄의 코드가 수정되어야 합니다.
이를 개인적인 방법으로 개선해보고자 했던 프로젝트입니다.

Connection Pool 에대한 간단한 구현.
## 구현

sql 에 의존적이 않은 객체에 따라 데이터를 매핑할수 있는 클래스 

SqlMapper.class

Classname == Table 
Field == Column 

DB Naming convention 대문자 스네이크 케이스와 카멜케이스를 1대1 매칭시켜서 query를 생성합니다.

Reflection, Generic 을 이용해 데이터를 객체에 매핑시켜서 리턴했습니다.

Generic Access Interface

기본적인 CRUD 기능을 가지고있는 인터페이스

내부적으로 sql mapper 를 이용해서 작동합니다.

Factory pattern 을 사용해서 명시적으로 객체타입을 지정해 사용할수 있도록 만들었습니다.

`private static final GenericAccess<User> userAccess = GenericAccessFactory.getInstance(User.class);`

## 문제점

Type Unsafe

sql mapper에 몰려있는 설계
