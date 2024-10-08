-- 시퀀스 생략 -> identity 문법으로 대체 (12c)
-- User 주키, Profile 외래키로 잡은 1:1 관계 (User 네임 사용 불가로 Member 변경)
create table Member (
  user_id number generated always as identity primary key,
  id varchar2(255) not null,
  pw varchar2(255) not null,
  nickname varchar2(255) not null
);

create table Profile (
  profile_id number primary key,
  name varchar2(255) not null,
  col varchar2(255),
  email varchar2(255),
  constraint fk_user foreign key (profile_id) references Member(user_id)
);

SELECT * FROM all_tables WHERE table_name = 'MEMBER'; -- 테이블 존재 체크
SELECT * FROM all_tab_privs WHERE table_name = 'MEMBER'; -- 권한 있는 계정 체크

create user testUser identified by 1234;
grant connect, resource to testUser;
ALTER USER testUser DEFAULT TABLESPACE USERS QUOTA UNLIMITED ON USERS; -- 테이블 스페이스 권한도 필요 (INSERT 하려면 할당량도 권한을 줘야하거든 ㅎㅋ)
GRANT SELECT, INSERT, DELETE ON MEMBER TO testUser;

select * from SYS.member; -- testUser 로 접근시 스키마.테이블명 -> 에잉.. testUser 계정으로 테이블 다시 만들자.

drop table profile;
drop table member;

desc member;
desc profile;
select * from profile;
select * from member;
truncate table profile;
truncate table member;
insert into member (id, pw, nickname) values ('test1234', '1234', 'admin2');
delete from member where id='test';
commit; -- commit 해야 delete 적용

-- cascade 추가
alter table profile drop constraint fk_user;
alter table profile add constraint fk_user foreign key (profile_id) references member(user_id) on delete cascade;

-- ChatRoom, Message 
create table ChatRoom (
    chatRoom_id number generated always as identity primary key,
    title varchar2(255) unique not null,
    created_id number not null,
    created_at timestamp default current_timestamp,
    constraint fk_user2 foreign key (created_id) references Member(user_id)
);
create table Message (
    message_id number generated always as identity primary key,
    chatRoom_id number references ChatRoom(chatRoom_id),
    user_id number references Member(user_id),
    content varchar2(1000) not null,
    created_at timestamp default current_timestamp
);
desc ChatRoom;
desc Message;

select * from chatroom;
select * from message;
select * from Message where user_id = 47 and chatRoom_id = 23;


