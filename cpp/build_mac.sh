rm -rf *.o *.so
gcc -dynamiclib -I/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include/darwin -lbz2 -o libGdiff.so AndroidApkUtils.c
rm -rf ../src/main/resources/jniLibs/mac/*
mv libGdiff.so ../src/main/resources/jniLibs/mac/