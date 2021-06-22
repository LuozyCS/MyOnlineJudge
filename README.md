# My Online Judge

#### 介绍
**以下是本项目开发文档**


#### 数据库说明
用户表user-information  
评论表			已经做好
题目内容表		题目号 题目内容
用户-题目表      用户号 题目号 用户通过的次数 每次通过时间
测试样例表        题号  十个样例  输入输各占一行

**记录暂时未修改的内容**

 1. 注册时，没填写邮箱不可以注册，但仍然会转跳到login且并未写入数据库。bug
 2. 普通用户右上角图标应该显示历史做题记录，管理员显示管理界面。demand



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
