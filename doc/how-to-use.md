### cpp函数名/cpp function name：
```
Java_com_kuyuzhiqi_antiemulator_emulator_FindEmulator_qemuBkpt
```
### 包名/package name：
```
com.kuyuzhiqi.antiemulator.emulator
```
### 类名/class name：
```
FindEmulator
```
### 方法定义/method define：
```
public native static int qemuBkpt();
```
### 加载库/load library
```
static {
        // This is only valid for arm
        System.loadLibrary("anti-lib");
    }
```

