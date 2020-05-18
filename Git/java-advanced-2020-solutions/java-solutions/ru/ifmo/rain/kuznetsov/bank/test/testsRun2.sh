Lib="../../java-advanced-2020/lib/"
Artifacts="../../java-advanced-2020/artifacts/"

cd ../../../../../../
javac -p "$Lib:$Artifacts" $(find . -name "*.java") &&
java -p .:"$Lib:$Artifacts" org.junit.runner.JUnitCore java-solutions.ru.ifmo.rain.kuznetsov.bank.test.BankTests