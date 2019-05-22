cpp函数名：
```
Java_com_kuyuzhiqi_antiemulator_emulator_FindEmulator_qemuBkpt
```
包名：
```
com.kuyuzhiqi.antiemulator.emulator
```

类名：
```
FindEmulator
```
方法定义：
```
public native static int qemuBkpt();
```

加载库
```
static {
        // This is only valid for arm
        System.loadLibrary("anti-lib");
    }
```

