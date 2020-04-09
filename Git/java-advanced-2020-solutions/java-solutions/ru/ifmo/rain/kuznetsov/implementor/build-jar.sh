# shellcheck disable=SC2164
Lib="../../java-advanced-2020/lib/"
Artifacts="../../java-advanced-2020/artifacts/"
Build="../_build"
cd ../../../../../
# shellcheck disable=SC2046
javac -p "$Lib:$Artifacts" -d "$Build" $(find . -name "*.java")
echo "Main-Class: ru.ifmo.rain.kuznetsov.implementor.JarImplementor" > ru/ifmo/rain/kuznetsov/implementor/Manifest
jar cmf ru/ifmo/rain/kuznetsov/implementor/Manifest _implementor.jar -C "$Build/" .
rm ru/ifmo/rain/kuznetsov/implementor/Manifest
