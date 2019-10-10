rm -rf *.o *.so *.c.*.*
gcc -I/root/JDK1_8/include/ -I/root/JDK1_8/include/linux -shared -lbz2 -fPIC -c AndroidApkUtils.c
gcc -shared  AndroidApkUtils.o -o libGdiff.so
mv libGdiff.so ../src/main/resources/jniLibs/linux/