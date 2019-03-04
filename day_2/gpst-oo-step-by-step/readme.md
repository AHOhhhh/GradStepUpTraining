# 面向对象编程基础

## 需求说明
### 练习1
写一个Person类，要有name，age属性，要有一个introduce方法，
introduce方法返回一个字符串形如：

`My name is Tom. I am 21 years old.`

再写一个Student类继承Person类，除了name，age属性，还有要有clazz属性（表示班级，因为 class 是关键字）。也有一个introduce方法，
introduce方法返回一个字符串形如：

`My name is Tom. I am 21 years old. I am a Student. I am at Class 2.`

再写一个Worker类继承Person类，只有name，age属性。也有一个introduce方法，
introduce方法返回一个字符串形如：

`My name is Tom. I am 21 years old. I am a Worker. I have a job.`

所有这三个类的这段文字

`My name is Tom. I am 21 years old.`

应该调用Person的一个方法basicIntroduce来返回

### 练习2
用原型的方式写一个Person类，要有id，name，age属性，靠id来判断是否是同一个人。要有一个introduce方法， introduce方法返回一个字符串形如：

`My name is Tom. I am 21 years old.`

再写一个Student类继承Person类，除了id，name，age属性，还有要有class属性。也有一个introduce方法， introduce方法返回一个字符串形如：

`My name is Tom. I am 21 years old. I am a Student. I am at Class 2.`

但是Student的class属性不是一个数字，而是一个对象，写一个Class类，有number属性还有一个leader属性。但是leader属性不在构造器里。

Student构造的时候把Class的一个实例传给Student，参见测试用例。 Class有一个assignLeader方法，接收一个Student实例。意为将一名学生设置为该Class的班长。 如果Class的Leader是Tom，那么Tom调用introduce的方法就要返回：

`My name is Tom. I am 21 years old. I am a Student. I am Leader of Class 2.`

如果没有就继续返回旧的字符串

再写一个Teacher类继承Person类，除了id，name，age属性，也有classes属性。也有一个introduce方法， introduce方法返回一个字符串形如：

`My name is Tom. I am 21 years old. I am a Teacher. I teach Class 2,3.`

如果class为空，就会返回

`My name is Tom. I am 21 years old. I am a Teacher. I teach No Class.`

所有Person的子类的这段文字

`My name is Tom. I am 21 years old.`

都应该调用Person的introduce方法来获得

### 练习3

在*练习2*的基础上变动，增加以下需求：

Class还有一个appendMember方法，接受一个Student实例。意味将一名学生加入该班级。

如果学生加入该班级, 那么在调用assignLeader方法的时候, assign成功，会打印一句话：

`Assign team leader successfully.`

如果学生没有加入该班级，不会assign成功，会打印一句话：

`It is not one of us.`

相应的调用Student的introduce方法也只会返回字符串(而学生是否在class中这件事情，应该是Class有一个方法hasStudent来判断。):

`My name is Tom. I am 21 years old. I am a Student. I haven't been allowed to join Class.`

Teacher还有一个isTeaching方法，传入一个学生，返回true/false。只要学生在classes中的任一个class中，就是在教他。 而学生是否在class中这件事情，应该是Class有一个方法hasStudent来判断。

当学生加入Teacher教的班级的时候，Teacher会打印一个句话,形如：

`I am Tom. I know Jerry has joined Class 2.`

当学生成为Teacher教的班级的班长的时候，Teacher会打印一个句话,形如：

`I am Tom. I know Jerry become Leader of Class 2.`

## 挑战
* 声明类
* 用构造函数初始化对象
* 声明属性
* 给属性赋值
* 读取属性值
* 继承一个类
* 区分重载和重写

## 要求
* 通过所有测试用例
* 代码通过小步骤提交并附上意义的评论
* 使用快捷键编码

## 交付物
根据题目需求修改`main`目录下的代码，通过`spec`目录下的所有测试。

## 环境
JavaScript ES6。

## 开始
在命令行中使用以下命令在用户本地任意目录下clone此题目库
```
git clone repo_of_this_template
```
首先初次下载完需要安装依赖：
```
npm install
```
运行所有测试：
```
npm test
```
运行指定测试：
```
node_modules/jasmine/bin/jasmine.js spec/practice-1/main-spec.js
```
用任意编辑器打开clone下来的文件夹，内部会存在两个文件夹
```
spec  //测试文件夹
src   //源文件
```
完成函数通过测试后，使用以下命令设置github远程仓库地址 (my_url代表你自己的新的github地址)
```
git remote set-url origin my_url
```
## 学习资源
1. [JavaScript基础](http://codefordream.com/courses/js_basic/sections)
2. [JavaScript初级训练营](http://codefordream.com/courses/js_learning_camps/sections)
3. [面向对象编程 - 廖雪峰的官方网站](https://www.liaoxuefeng.com/wiki/001434446689867b27157e896e74d51a89c25cc8b43bdb3000/001434499763408e24c210985d34edcabbca944b4239e20000)
4. [ECMAScript 6 入门](http://es6.ruanyifeng.com/)
5. [Node 下载安装](https://github.com/creationix/nvm)
6. [NPM 下载安装](https://github.com/npm/npm)
7. [Jasmine用法](http://jasmine.github.io/2.4/introduction.html)
8. [Git 参考手册](https://git-scm.com/docs)
