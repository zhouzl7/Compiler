all : TinyC build

libpath = /usr/local/lib
antlr4 = java -jar $(libpath)/antlr-4.7.1-complete.jar
grun = java org.antlr.v4.gui.TestRig

env:
	export CLASSPATH=".:$(libpath)/antlr-4.7.1-complete.jar:$CLASSPATH"

TinyC : TinyC.g4 env
	$(antlr4) TinyC.g4
	# javac TinyC*.java

testGrammer : TinyC
ifdef FILE
	$(grun) TinyC compilationUnit -gui < $(FILE)
else
	@echo "No FILE to test！！！"
endif

test : env
ifdef FILE
	@echo "#!/usr/bin/node" > js/test.js
	@cat js/stdio.js >> js/test.js
	@cat js/string.js >> js/test.js
	@java Main $(FILE) >> js/test.js
	@cat js/main.js >> js/test.js
	@echo "******* Environment Setup *********"
	@cd js && chmod 755 test.js
	@cd js && ./test.js $(ARG1) $(ARG2)
else
	@echo "No FILE to test！！！"
endif
build : env
	$(antlr4) -visitor TinyC.g4
	javac -d . Main.java MyVisitor.java TinyCBaseVisitor.java

.PHONY : clean
clean :
	-rm -f ./TinyC*.java ./*.tokens ./*.class ./*.interp 
