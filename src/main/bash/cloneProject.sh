cd src/main/resources/git || exit 1
echo "$1"
mkdir "$1"
cd "$1" || exit 1
git clone "$4"
cd "$2" || 1
git checkout "$3"