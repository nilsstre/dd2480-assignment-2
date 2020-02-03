cd src/main/resources/git/"$1"/"$2" || exit 1
mvn clean install > ../../reports/"$1"
