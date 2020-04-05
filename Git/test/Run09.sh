Solutions="../java-advanced-2020-solutions/java-solutions"
Artifacts="../../java-advanced-2020/artifacts/"
Lib="../../java-advanced-2020/lib/"

cd $Solutions && \
javac -p "$Lib:$Artifacts" $(find . -name "*.java") && \
echo "Compile" && \
java -cp . -p .:"$Artifacts:$Lib" ru.ifmo.rain.kuznetsov.crawler.WebCrawler http://neerc.ifmo.ru 3 160 160
