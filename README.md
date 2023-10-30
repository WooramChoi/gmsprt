# gmsprt
Game supporter

## Spring Coding Convention
* Controller

    * {name}List() -> GET / 목록 조회
    * {name}Details() -> GET / 단건 조회
    * {name}Add() -> POST / 생성
    * {name}Modify() -> PATCH / 수정
    * {name}Remove() -> DELETE / 삭제
    * {name}Save() -> POST / 생성, 수정, 삭제
    
* Service

    * find{Name}() -> 조회
    * add{Name}() -> 생성
    * modify{Name}() -> 수정
    * remove{Name}() -> 삭제
    * save{Name}() -> 생성, 수정, 삭제

* Mapper

    * select{Name}() -> 조회
    * insert{Name}() -> 생성
    * update{Name}() -> 수정
    * delete{Name}() -> 삭제

Dooray 테스트 여섯번째
