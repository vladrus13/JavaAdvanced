Solution="../java-advanced-2020-solutions/java-solutions"
Artifacts="../../java-advanced-2020/artifacts/"
Lib="../../java-advanced-2020/lib/"
cd $Solution && \
javac -p "$Lib:$Artifacts" $(find . -name "*.java") && \
echo "Compile" && \
java -cp . -p .:"$Artifacts:$Lib" -m info.kgeorgiy.java.advanced.hello server-evil ru.ifmo.rain.kuznetsov.hello.HelloUDPServer;
rm $(find . -name "*.class")
