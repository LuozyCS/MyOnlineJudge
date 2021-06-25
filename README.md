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

 1.注册时，没填写邮箱不可以注册，但仍然会转跳到login且并未写入数据库。bug
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
## 关于数据库
**accept** pass=0
**wrong answer** pass=1
**compile error** pass=2
**runtime error** pass=3
**Time Limit Exceeded** pass=4
**若没有通过时间pass_time,则pass_time=-1**

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
