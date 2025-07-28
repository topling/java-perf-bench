

set -xe

JAVA_HOME=`javac -J-XshowSettings:properties -version 2>&1 | awk '/java.home/{print $NF}'`

rm -f ByteArrayBenchmark.class
javac --add-exports java.base/jdk.internal.misc=ALL-UNNAMED -h . ByteArrayBenchmark.java

g++ -O2 -Wall -Wextra -shared -fpic -I ${JAVA_HOME}/include -I ${JAVA_HOME}/include/linux \
    -o libbytearraybench.so bytearraybench.cpp

java --add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Djava.library.path=. ByteArrayBenchmark $@
