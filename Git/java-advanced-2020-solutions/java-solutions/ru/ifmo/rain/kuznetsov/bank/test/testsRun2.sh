Lib="../../java-advanced-2020/lib/junit-4.11.jar"
Artifacts="../../java-advanced-2020/lib/hamcrest-core-1.3.jar"

cd .. &&
./build.sh &&
cd ../../../../../ &&
java -cp .:"$Lib:$Artifacts" org.junit.runner.JUnitCore ru.ifmo.rain.kuznetsov.bank.test.BankTests &&
echo 1 &&
cd ru/ifmo/rain/kuznetsov/bank &&
./debuild.sh &&
exit

echo 0