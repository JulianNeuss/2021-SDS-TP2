OUT_PATH = out
JAVA_FILES=$(shell find src/ -name *.java)

CLASS_FILES=$(shell find out/ -type f)

compile:
	mkdir -p $(OUT_PATH)
	javac -d $(OUT_PATH) $(JAVA_FILES)

GenerateInitialFile: compile
	java -cp out $(inputFilename) $(rows) $(columns) $(depths) $(initialRows) $(initialColumns) $(initialDepths) $(percentageAlive) $(dimension) $(outputFilename) $(maxIterations) $(endOnTouchBorder) simulation.generator.GenerateInitialFile

SimulationApp: compile
	java -cp out $(inputFilename) $(rows) $(columns) $(depths) $(initialRows) $(initialColumns) $(initialDepths) $(percentageAlive) $(dimension) $(outputFilename) $(maxIterations) $(endOnTouchBorder) simulation.SimulationApp

visualizer:
	@bash -c "cd visualization;source .env/bin/activate;python visualizer.py"

ifneq ($(inputFilename),)
override inputFilename := -DinputFilename=$(inputFilename)
endif
ifneq ($(rows),)
override rows := -Drows=$(rows)
endif
ifneq ($(columns),)
override columns := -Dcolumns=$(columns)
endif
ifneq ($(depths),)
override depths := -Ddepths=$(depths)
endif
ifneq ($(initialRows),)
override initialRows := -DinitialRows=$(initialRows)
endif
ifneq ($(initialColumns),)
override initialColumns := -DinitialColumns=$(initialColumns)
endif
ifneq ($(initialDepths),)
override initialDepths := -DinitialDepths=$(initialDepths)
endif
ifneq ($(percentageAlive),)
override percentageAlive := -DpercentageAlive=$(percentageAlive)
endif
ifneq ($(dimension),)
override dimension := -Ddimension=$(dimension)
endif
ifneq ($(outputFilename),)
override outputFilename := -DoutputFilename=$(outputFilename)
endif
ifneq ($(maxIterations),)
override maxIterations := -DmaxIterations=$(maxIterations)
endif
ifneq ($(endOnTouchBorder),)
override endOnTouchBorder := -DendOnTouchBorder=$(endOnTouchBorder)
endif