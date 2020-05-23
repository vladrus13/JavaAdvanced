Lib="../../java-advanced-2020/lib"
Artifacts="../../java-advanced-2020/artifacts/"

cd .. &&
./build.sh &&
cd ../../../../../ &&
java -jar "$Lib""/junit-4.11.jar" -cp . -c ru.ifmo.rain.kuznetsov.bank.BankTests