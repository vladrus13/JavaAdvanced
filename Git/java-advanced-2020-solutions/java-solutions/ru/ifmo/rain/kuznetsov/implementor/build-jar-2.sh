# shellcheck disable=SC2164
Lib="../../java-advanced-2020/lib/"
Artifacts="../../java-advanced-2020/artifacts/"
Modules="../../java-advanced-2020/modules/"
Build="../_build"
cd ../../../../../
# shellcheck disable=SC2046
[ -e $Build ] && rm -R $Build
mkdir $Build
javac -p "$Lib:$Artifacts" -d "$Build" $(find . -name "*.java")
cd $Build
jar -c --file="../_implementor.jar" --main-class="ru.ifmo.rain.kuznetsov.implementor.JarImplementor" --module-path="$Lib:$Artifacts" module-info.class $(find . -name "*.class")
