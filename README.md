************************
*    RESUME MATCHER    *
************************

DESCRIPTION
-----------
This application creates an inverted index over all available resumes. It then performs query search, 
once a particular keyword is given. Finally, it outputs top resumes matching a given keyword.


INSTRUCTION
-----------
1. cd ResumeMatcher/
2. Copy all the resumes into './input/' folder.
3. Set r+w+x permissions for ‘run.sh’ (i.e. chmod 777 run.sh).
4. Execute ‘run.sh’ file. (Note: You may have to execute doc2unix command 
   on shell script file before executing it.)

Note: You can change any parameter set in shell script file and accordingly 
      you will get the results.


COMPILATION
-----------
javac -cp ".:./ext/lucene-analyzers-common-6.2.1.jar:./ext/lucene-core-6.2.1.jar:./ext/lucene-queryparser-6.2.1.jar" ./src/org/myorg/ResumeMatcher/*.java -d bin -Xlint


CREATE A JAR
------------
jar -cvf ResumeMatcher.jar -C bin/ .


RUN APPLICATION
---------------
java -cp .:ext/lucene-analyzers-common-6.2.1.jar:ext/lucene-core-6.2.1.jar:./ext/lucene-queryparser-6.2.1.jar:ResumeMatcher.jar org.myorg.ResumeMatcher.ResumeMatcher "./inverted_ind
ex" "./input" <Keywords to be searched>

e.g.
java -cp .:ext/lucene-analyzers-common-6.2.1.jar:ext/lucene-core-6.2.1.jar:./ext/lucene-queryparser-6.2.1.jar:ResumeMatcher.jar org.myorg.ResumeMatcher.ResumeMatcher "./inverted_index" "./input" "java"


SAMPLE OUTPUT
-------------
Indexing: /Users/chetan/Desktop/demo/input/David-Newberry.pdf
Indexing: /Users/chetan/Desktop/demo/input/David_Newberry.txt
Indexing: /Users/chetan/Desktop/demo/input/Marc-chambers.pdf
Indexing: /Users/chetan/Desktop/demo/input/Marc-chambers.txt
Indexing: /Users/chetan/Desktop/demo/input/PRANAV_KHAITAN.txt
Indexing: /Users/chetan/Desktop/demo/input/praveen_kumarnelli.txt
Indexing: /Users/chetan/Desktop/demo/input/Praveenkumar-Nelli.pdf
Indexing: /Users/chetan/Desktop/demo/input/Roger-Beatty-II-PE.pdf
Indexing: /Users/chetan/Desktop/demo/input/Roger-Beatty-II-PE.txt
Indexing: /Users/chetan/Desktop/demo/input/sam_manekshaw.txt
Indexing: /Users/chetan/Desktop/demo/input/Subramanian-Venkatachalam.pdf
Indexing: /Users/chetan/Desktop/demo/input/Subramanian-Venkatachalam.txt
Total resumes indexed: 36
Time taken: 429 ms

Searching: java
Resume: /Users/chetan/Desktop/demo/input/praveen_kumarnelli.txt	Score: 2.6663783
Resume: /My Computer/Academic/GitHub/ResumeMatcher/input/praveen_kumarnelli.txt	Score: 2.6663783
Resume: /Users/chetan/Desktop/demo/input/David_Newberry.txt	Score: 2.341707
Resume: /My Computer/Academic/GitHub/ResumeMatcher/input/David_Newberry.txt	Score: 2.341707
Resume: /Users/chetan/Desktop/demo/input/PRANAV_KHAITAN.txt	Score: 2.0497391
Resume: /My Computer/Academic/GitHub/ResumeMatcher/input/PRANAV_KHAITAN.txt	Score: 2.0497391
Time taken: 21 ms


