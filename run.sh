# CREATE FOLDER FOR BINARIES
mkdir bin


# COMPILE JAVA SOURCE CODE
javac -cp ".:./ext/lucene-analyzers-common-6.2.1.jar:./ext/lucene-core-6.2.1.jar:./ext/lucene-queryparser-6.2.1.jar" ./src/org/myorg/ResumeMatcher/*.java -d bin -Xlint


# CREATE JAR FILE
jar -cvf ResumeMatcher.jar -C bin/ .


# RUN RESUME MATCHER APPLICATION
java -cp .:ext/lucene-analyzers-common-6.2.1.jar:ext/lucene-core-6.2.1.jar:./ext/lucene-queryparser-6.2.1.jar:ResumeMatcher.jar org.myorg.ResumeMatcher.ResumeMatcher "./inverted_index" "./input" "java"

