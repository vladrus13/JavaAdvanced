Solution="../java-advanced-2020-solutions/java-solutions"
Artifacts="../../java-advanced-2020/artifacts/"
Lib="../../java-advanced-2020/lib/"
cd $Solution
javac -p "$Lib:$Artifacts" $(find . -name "*.java") > "../../test/compileResult.txt"
