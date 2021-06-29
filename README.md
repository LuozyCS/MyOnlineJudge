# My Online Judge

#### 介绍
**以下是本项目开发文档**


#### 数据库说明
**用户表user-information**  
**评论表**			已经做好
**题目内容表**		题目号 题目内容
**用户-题目表**      用户号 题目号 用户通过的次数 每次通过时间
**测试样例表**        题号  十个样例  输入输各占一行

## **记录暂时未修改的内容**

 ~~1.注册时，没填写邮箱不可以注册，但仍然会转跳到login且并未写入数据库。bug~~
 ~~2. 普通用户右上角图标应该显示历史做题记录，管理员显示管理界面。demand~~
~~3.对于运行时间过长的程序，需要终止并返回错误，避免恶意代码。bug~~
~~4.做题分析的标头无法显示。bug~~
~~5.做题分析的题目名无法显示。bug~~
~~6.管理员创建problem 并传入样例。demand~~
~~7.response。demand~~
8.安全问题。bug
~~9.对于死循环代码的超时报错。bug~~
~~10.修改题目没有传入标题。bug~~
~~11.创建题目时，若样例为空，则禁止创建题目，返回界面。bug~~
~~12.example的个数取出来并放到accontroller中。demand~~  感觉不需要了 恒定三个就行
~~13.未登录提交代码会报很恐怖的错误。bug~~
~~14.倒叙输出historyList。demand~~
~~15.添加题目难度分类demand~~
~~16.添加暂存题目状态，暂存的题目只有管理员看到（某一个管理员可见？是不是需要草稿箱）（文章修改也换位草稿修改）demand~~
~~17.添加废弃题目状态demand~~
~~18.删除按钮替换为废弃按钮，废弃题目后，主界面不可见，而且不可以做。demand~~
19.发布文章做约束。（各个DTO层对象都做约束）bug
~~20.存储用户提交的历史代码~~
21.不允许添加重复的题目


## 关于数据库
**accept** pass=0
**wrong answer** pass=1
**compile error** pass=2
**runtime error** pass=3
**Time Limit Exceeded** pass=4
**若没有通过时间pass_time,则pass_time=-1**

**正常已发布** state=0
**未发布仍在修改** state=1
**已废弃无法作答的题目** state=2

**简单题** difficulty=0
**中等题** difficulty=1
**困难题** difficulty=2

## 关于文件路径的记录
 //创建cpp文件的地址，到myoj下，src外面  
////      File file = new File("lzy.cpp");  
////      file.createNewFile();  
//  
//// 编译的地址  
//    //  .\\能在src外面  即同cpp同一位置生成exe文件  
//    //  .\\a.exe也是和cpp在同一目录  
//    String commandStr = "g++ .\\hello.c";  
//    BufferedReader br;  
//    BufferedWriter bw;  
//    Process p1 = Runtime.getRuntime().exec(commandStr);  
//    Process p = Runtime.getRuntime().exec(".\\a.exe");  
//  
//    bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));  
//    bw.write("1 2 3");  
//    bw.close();  
//    br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
//    String line = null;  
//    StringBuilder sb = new StringBuilder();  
//    while ((line = br.readLine()) != null) {  
//       sb.append(line + "\n");  
//    }  
//        System.err.println(sb.toString());

## 访问路径解释
在发布界面：
- 发布:/addProblem                             (insert)state为0

- 暂存:/addDraft                                   (insert)state为1        

在草稿箱界面：通过点击修改按钮进来

- 发布:/updateDraftToProblem         (update)state为0

- 暂存:/updateDraft                             (update)state为1


## 存储过程


drop procedure if exists publish_problem;
delimiter //
create procedure publish_problem(in p_title varchar(300), in p_content varchar(10000), in p_publisher_id int, in p_difficulty int, in p_state int) begin
if ifnull((select 1 = all(select state from problem where title = p_title or content = p_content)), true) then
 	insert into problem(title,content,publish_time,publisher_id,difficulty,state) values(p_title,p_content,NOW(),p_publisher_id,p_difficulty,p_state);
end if;
end //
delimiter ;

start transaction;
call publish_problem('test','0',15,0,0);
select @@identity;
select row_count() = 1;
rollback;

start transaction;
insert into problem(title,content,publish_time,publisher_id,difficulty,state) values('p_title','p_content',NOW(),15,0,1);
rollback;

set character_set_database = utf8;
set character_set_server = utf8;

drop procedure if exists publish_draft;
delimiter //
create procedure publish_draft(in p_id int, in p_title varchar(300), in p_content varchar(10000), in p_publisher_id int, in p_difficulty int, in p_state int) begin
if ifnull((select 1 = all(select state from problem where title = p_title or content = p_content)), true) then
 	update problem set title = p_title, content = p_content, publish_time = NOW(), publisher_id=p_publisher_id,difficulty = p_difficulty,state = p_state  where id=p_id;
end if;
end //
delimiter ;
