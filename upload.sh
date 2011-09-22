cd target
file=`ls | grep "jar"`
version=$PROMOTED_NUMBER
php /var/minecraft/scripts/upload.php --file=$file --slug=chunky --version=$version --plugin=Chunky