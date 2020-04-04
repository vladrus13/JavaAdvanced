Solution="../java-advanced-2020-solutions/java-solutions"
Artifacts="../../java-advanced-2020/artifacts/"
Lib="../../java-advanced-2020/lib/"
cd $Solution
javac -p "$Lib:$Artifacts" $(find . -name "*.java") && \
echo "Compile" && \
java -cp . -p .:"$Artifacts:$Lib" -m info.kgeorgiy.java.advanced.mapper advanced ru.ifmo.rain.kuznetsov.concurrent.IterativeParallelism 12348304;
rm $(find . -name "*.class")
