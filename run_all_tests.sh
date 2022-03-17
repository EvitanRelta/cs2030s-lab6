javac -Xlint:rawtypes,unchecked ./cs2030s/fp/*.java
javac -Xlint:rawtypes,unchecked *.java
java Test1 | grep failed;
java Test2 | grep failed;
java Test3 | grep failed;
java Test4 | grep failed;
java Lab5
printf "A\nA-\nA+\nA\nC\nNo such entry\nNo such entry\nNo such entry\n"
java -jar ~cs2030s/bin/checkstyle.jar -c ~cs2030s/bin/cs2030_checks.xml cs2030s/fp/*.java;
java -jar ~cs2030s/bin/checkstyle.jar -c ~cs2030s/bin/cs2030_checks.xml *.java;
