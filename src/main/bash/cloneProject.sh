cd src/main/resources/git || exit 1
echo "$1"
mkdir "$1"
cd "$1" || exit 1
git clone git@github.com:nilsstre/dd2480-assignment-2.git
if [ "$2" != "master" ];
then
  cd dd2480-assignment-2 || 1
  git checkout "$2"
fi