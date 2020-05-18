Lib="../../../../../../../../java-advanced-2020/lib/"
Artifacts="../../../../../../../../java-advanced-2020/artifacts/"

cd .. &&
./build.sh &&
java -p "$Lib:$Artifacts" test/BankTests