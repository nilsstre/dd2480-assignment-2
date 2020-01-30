cd src/main/resources/git/"$1"/assignment-1 || exit
mvn clean install > ../../reports/"$1"
