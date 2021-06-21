 --create database blog;

-- SELECT  COLLATIONPROPERTY('Chinese_PRC_Stroke_CI_AI_KS_WS', 'CodePage');

-- create table user_information
-- (
--     id int primary key identity(-2147483648, 1),
--     telephone bigint,
--     wechat char(32),
--     nickname char(32),
--     pwd char(20),
-- );

 --create table problem
 --(
 --    id int primary key identity(-2147483648, 1),
 --    title varchar(300),
 --    content nvarchar(4000),
 --    publish_time datetime,
 --    publisher_id int references user_information(id) not null,
 --);
 --drop table comment;
 --create table comment
 --(
 --    article_id int references problem(id) not null,
 --    id int check(id != -1),
 --    publisher_id int references user_information(id) not null,
 --    content nvarchar(500),
 --    publish_time datetime,
 --    origin_id int,
 --    parent_id int,
 ----    next_brother_id int,
 ----    first_child_id int,
 --    primary key(article_id, id),
 --);

--insert into comment values(-2147483648, 0, -2147483648, 'first comment', getdate(), -1, -1);
--select * from comment;

 --insert into user_information values(110, '金海滩派出所', 'SE123', 'SE123');

 --select * from user_information;

 --create user SE123 for login SE123 with default_schema = dbo;
 --exec sp_addrolemember 'db_owner', 'SE123';

-- insert into user_information values(17860071778, '好名字可以让你的朋友更容易记住你', 'xiF616', 'L''hospital');

-- select * from user_information;

-- create user xiF616 for login xiF616 with default_schema = dbo;
-- exec sp_addrolemember 'db_owner', 'xiF616';

-- select telephone, wechat, nickname, pwd from user_information where telephone = 17860071778

-- delete from problem;

 --insert into problem values('title1', 'content1', getdate(), -2147483648);
 --insert into problem values('title2', 'content2', getdate(), -2147483648);
 --insert into problem values('title3', 'content3', getdate(), -2147483648);
 --insert into problem values('title4', 'content4', getdate(), -2147483648);
 --insert into problem values('title5', 'content5', getdate(), -2147483648);

 --select * from problem;

-- select userid, nickname, problem.id, title, convert(char(23), publish_time, 25) pub_time
-- from user_information, problem
-- where user_information.id = userid
-- order by publish_time desc

-- publish_time, convert(char(23), publish_time, 121), 

-- exec sp_rename 'problem.userid', 'publisher_id', 'column'

-- select user_information.id, nickname, comment.id, content, publish_time
-- from user_information, (select id, content, publish_time, publisher_id from comment where article_id = -2147483645 and origin_id = id)comment
-- where user_information.id = publisher_id
-- order by publish_time desc

        --select publisher_id, rtrim(nickname), comment.id, content, publish_time
        --from user_information, (select id, content, publish_time, publisher_id
        --    from comment where article_id = -2147483644 and origin_id = -1)comment
        --where user_information.id = publisher_id
        --order by publish_time desc

-- select * from problem order by publish_time desc, id asc

-- exec sp_rename 'user_information.wechat', 'username', 'column'

-- Add a new column '[NewColumnName]' to table '[TableName]' in schema '[dbo]'
-- alter table user_information add email char(50)

-- alter table user_information drop column telephone

-- select * from user_information

-- update user_information set email='180400511@stu.hit.edu.cn'

-- alter table problem add content nvarchar(4000)

-- update problem set content='content1' where id = -2147483644

select * from user_information
select * from problem
select * from comment