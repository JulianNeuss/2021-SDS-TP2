OUT_PATH = out
JAVA_FILES=$(shell find src/ -name *.java)

CLASS_FILES=$(shell find out/ -type f)

compile:
	mkdir -p $(OUT_PATH)
	javac -d $(OUT_PATH) $(JAVA_FILES)

GenerateInitialFile: compile
	java -cp out simulation.generator.GenerateInitialFile
	
SimulationApp: compile
	java -cp out simulation.SimulationApp