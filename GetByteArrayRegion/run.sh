

set -e

JAVA_HOME=`javac -J-XshowSettings:properties -version 2>&1 | awk '/java.home/{print $NF}'`

rm -f ByteArrayRegionBenchmark.class

# 1. Generate header file
javac ByteArrayRegionBenchmark.java
javac -h . ByteArrayRegionBenchmark.java

# 2. Compile native library (Linux/Mac)
g++ -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -shared -fPIC -O2 ByteArrayRegionBenchmark.cpp -o libbenchmark.so

# 3. Run test
java -Djava.library.path=. ByteArrayRegionBenchmark