cd src/main/resources/git/assignment-1 || exit
fileName="result_$(date +"%Y-%m-%d_%T")"
mvn clean install > ../"$fileName"
