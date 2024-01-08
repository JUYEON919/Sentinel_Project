# Sentinal_JAVA

1. Main Branches에서 개인 Branches로 Merge 후 작업을 시작하실 때 아래 사항들을 체크 후 작업을 시작하시기 바랍니다.
      - application.properties의 DB URL, ID, PW 알맞게 수정 후 작업하시기 바랍니다.
      - Mapper.xml 파일들의 <!DOCTYPE> 태그 부분을 자신의 mybatis-3-mapper.dtd 파일의 경로 혹은 설치 주소를 입력하시기 바랍니다.
      - Mapper.xml 파일들의 쿼리에서 테이블 명이 올바른지 확인하시기 바랍니다.
      - 작업 중 코드의 수정 사항이 있으시면 Project -> Clean 후 수정 사항을 확인하시기 바랍니다.
2. SetupService.java의 'fianl String path'를 WAS서버의 static한 path로 변경 및 설정이 필요합니다.
