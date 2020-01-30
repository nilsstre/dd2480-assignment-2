cd src/main/resources/git || exit 1
echo "$1"
mkdir "$1"
cd "$1" || exit 1
git clone git@github.com:DD2480-Group-22/assignment-1.git
if [ "$2" != "master" ];
then
  cd assignment-1 || 1
  git checkout "$2"
fi