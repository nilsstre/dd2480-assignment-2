cd src/main/resources/git/"$1"/dd2480-assignment-2 || exit 1
mvn clean install > ../../reports/"$1"
