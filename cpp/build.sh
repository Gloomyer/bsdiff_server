rm -rf *.o
gcc -I/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include/darwin -c AndroidApkUtils.c -o libGDiff.o
gcc -dynamiclib -o libGidff.so libGDiff.o