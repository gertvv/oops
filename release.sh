VERSION=$1

mkdir oops
cp application/target/oops-${VERSION}-jar-with-dependencies.jar oops/oops-${VERSION}.jar
cp LICENSE README.md oops/
( cd oops && markdown README.md > README.html )

zip -r oops-${VERSION}.zip oops/
rm -rf oops/
