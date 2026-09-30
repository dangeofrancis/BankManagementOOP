compile with "mkdir -p bin
javac -d bin -cp "lib/sqlite-jdbc.jar" src/*.java"
Run with "javajava --enable-native-access=ALL-UNNAMED -cp "bin:lib/sqlite-jdbc.jar" main -cp "bin:lib/sqlite-jdbc.jar" main" 
while running iff the flag --enable-native-access=ALL-UNNAMED is not givven , the program might throw warning due to reading naitive db file, you are safe to ignore.
