# shellcheck disable=SC2164
cd ..
cd ..
cd ..
cd ..
cd ..
javac -cp ../../java-advanced-2020/artifacts/info.kgeorgiy.java.advanced.implementor.jar ru/ifmo/rain/kuznetsov/implementor/JarImplementor.java
jar cmf ru/ifmo/rain/kuznetsov/implementor/Manifest _implementor.jar ru/ifmo/rain/kuznetsov/implementor/*.class
