평소에 자주 이용해서 익숙했던 커뮤니티 이트들의 주요 기능들을 참고하여
비슷하게 구현해보는걸 목표로 하고 약 3.5주 동안 개발했습니다.

#사용된 기술 : Spring, JSP, MySQL, MyBatis, Jquery

#주요 기능 :  

1. 실시간 인기순 홈 화면 - 
최근 100개의 전체 게시물에 대하여 각 게시물이 속한 게시판별로 조회수를 합산해서 자동으로 인기 게시판을 선정하고, 
그 순서대로 홈 화면에 나타냅니다.

2. 카테고리, 게시판 등록 - 
관리자 페이지에서 새로운 카테고리와 게시판을 쉽게 추가할 수 있습니다.

3. 즐겨 찾기 - 
새로고침 없이 즐겨찾기를 추가/삭제할 수 있습니다.

4. 무한 계층 댓글 & 연쇄 삭제 - 
무한 계층 댓글을 구현하였고, 댓글 삭제시 해당 댓글에 달린 자식 댓글이 있을 경우 숨김 처리만 됩니다. 
마지막 대댓글 삭제시엔 더이상 의미가 없는 연속된 숨김 댓글 체인을 재귀적으로 모두 삭제합니다.

5. 실시간 댓글 알림 - 
3초 주기로 ajax GET 요청을 보내서 화면 새로고침 없이 실시간으로 댓글 알림을 받을 수 있습니다.
