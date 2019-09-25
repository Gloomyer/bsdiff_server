rm -rf *.o *.so *.c.*.*
gcc -I/root/JDK1_8/include/ -I/root/JDK1_8/include/linux -shared -lbz2 -o libGdiff.so AndroidApkUtils.c