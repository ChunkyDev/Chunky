cd target
echo $1
file=`ls | grep "jar"`
version=$BUILD_NUMBER
php /var/minecraft/scripts/upload.php --file=$file --slug=chunky --version=$version --plugin=Chunky