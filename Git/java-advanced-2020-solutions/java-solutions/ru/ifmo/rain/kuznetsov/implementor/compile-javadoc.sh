Link="https://docs.oracle.com/en/java/javase/11/docs/api"
Lib="../java-advanced-2020/lib/"
Artifacts="../java-advanced-2020/artifacts/"

cd ../../../../../../

# shellcheck disable=SC2046
javadoc $(find . -name "*.java") -d "_javadoc" -link "$Link" -private -p "$Lib:$Artifacts"
