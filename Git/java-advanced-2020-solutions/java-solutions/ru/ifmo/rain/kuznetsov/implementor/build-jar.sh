# shellcheck disable=SC2164
Lib="../../java-advanced-2020/lib/"
Artifacts="../../java-advanced-2020/artifacts/"
Build="../_build"
cd ../../../../../
javac -p "$Lib:$Artifacts" -d "$Build" $(find . -name "*.java")
jar cmf ru/ifmo/rain/kuznetsov/implementor/Manifest _implementor.jar -C "$Build/" .
