create database blog;
create table user_information
(
    id int primary key auto_increment,
    username char(32) not null,
    email char(50),
    nickname char(32),
    pwd char(20) not null
);
create table problem
(
    id int primary key auto_increment,
    title varchar(300),
    content nvarchar(4000),
    publish_time datetime not null,
    publisher_id int references user_information(id)
);
create table discussion
(
    article_id int references problem(id),
    id int check(id != -1),
    publisher_id int references user_information(id),
    content nvarchar(500),
    publish_time datetime not null,
    origin_id int references comment(id),
    parent_id int references comment(id),
    primary key(article_id, id)
);