javac -Xlint:rawtypes,unchecked ./cs2030s/fp/*.java
javac -Xlint:rawtypes,unchecked Test1.java; java Test1
javac -Xlint:rawtypes,unchecked Test2.java; java Test2

# Check style
java -jar ~cs2030s/bin/checkstyle.jar -c ~cs2030s/bin/cs2030_checks.xml cs2030s/fp/*.java;

# Test generating 'javadoc'
javadoc -quiet -private -d docs cs2030s/fp/Lazy.java
