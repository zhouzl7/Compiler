# Compiler


## 如何测试
1. 项目依赖:  
项目需要使用 ``JDK`` ,``antlr`` 以及 ``nodejs``.  
安装示例 (Ubuntu or Debian):  
```shell
sudo apt install openjdk-8-jdk # install jdk

# install antlr, from https://www.antlr.org/
cd /usr/local/lib
sudo curl -O https://www.antlr.org/download/antlr-4.7.2-complete.jar
export CLASSPATH=".:/usr/local/lib/antlr-4.7.2-complete.jar:$CLASSPATH" # you can add CLASSPATH to your ~/.bashrc
```
安装``antlr``可以按照其官网首页进行操作

对于安装``nodejs``, 可以参考[https://nodejs.org/zh-cn/download/](https://nodejs.org/zh-cn/download/)，或者通过``sudo apt install nodejs``安装（版本非最新，可以通过npm的n模块进行升级）

2. 编辑 ``Makefile``:  
``antlr``的版本可能不同，将你在说明中实际下载到的文件名替换到``Makefile``中

3. 构建:
```shell
make build
```
js 目录下的``test.js``就是生成的目标文件

4. 测试  
运行以下命令进行测试 ``make test FILE=<C file to translate> ARG1=<command line argument1> ARG2=<command line argument2>``

- FILE: 指定的要翻译的C文件
- ARG1: 第一个命令行参数
- ARG2: 第二个命令行擦书

例子
```
$ make test FILE=tests/Palindrome.c ARG1=123654
$ False

$ make test FILE=tests/KMP.c ARG1=123654 ARG2=36
$ 2

$ make test FILE=tests/Calculator.c ARG1=1+2+3+4
$ 10
```

**特别注意**: 测试程序的输入都是从命令行参数中读取的，对于一些在shell中的特殊字符如括号``(``,``)``,``*``不能直接在命令行中输入，可以考虑使用文件如(in.txt)

然后通过执行``node js/test.js ARG1=$(cat in.txt)``等方式进行测试



