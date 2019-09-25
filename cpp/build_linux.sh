rm -rf *.o *.so *.c.*.*
gcc -dynamiclib -I/root/JDK1_8/include/ -I/root/JDK1_8/include/linux -L/usr/lib -lbz2 -o libGdiff.so AndroidApkUtils.c