cd src/main/resources/git/"$1"/assignment-2 || exit
mvn clean install > ../../reports/"$1"
