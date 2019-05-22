package com.kuyuzhiqi.antiemulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.kuyuzhiqi.antiemulator.debugger.FindDebugger
import com.kuyuzhiqi.antiemulator.emulator.FindEmulator
import com.kuyuzhiqi.antiemulator.monkey.FindMonkey
import com.kuyuzhiqi.antiemulator.taint.FindTaint
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        object : Thread() {
            override fun run() {
                super.run()
                isTaintTrackingDetected()

                isMonkeyDetected()

                isDebugged()

                isQEmuEnvDetected()

            }
        }.start()
    }

    fun isQEmuEnvDetected(): Boolean {
        log("Checking for QEmu env...")
        log("hasKnownDeviceId : " + FindEmulator.hasKnownDeviceId(applicationContext))
        log("hasKnownPhoneNumber : " + FindEmulator.hasKnownPhoneNumber(applicationContext))
        log("isOperatorNameAndroid : " + FindEmulator.isOperatorNameAndroid(applicationContext))
        log("hasKnownImsi : " + FindEmulator.hasKnownImsi(applicationContext))
        log("hasEmulatorBuild : " + FindEmulator.hasEmulatorBuild(applicationContext))
        log("hasPipes : " + FindEmulator.hasPipes())
        log("hasQEmuDriver : " + FindEmulator.hasQEmuDrivers())
        log("hasQEmuFiles : " + FindEmulator.hasQEmuFiles())
        log("hasGenyFiles : " + FindEmulator.hasGenyFiles())
        log("hasEmulatorAdb :" + FindEmulator.hasEmulatorAdb())
        log("hitsQemuBreakpoint : " + FindEmulator.checkQemuBreakpoint())
        if (FindEmulator.hasKnownDeviceId(applicationContext)
            || FindEmulator.hasKnownImsi(applicationContext)
            || FindEmulator.hasEmulatorBuild(applicationContext)
            || FindEmulator.hasKnownPhoneNumber(applicationContext) || FindEmulator.hasPipes()
            || FindEmulator.hasQEmuDrivers() || FindEmulator.hasEmulatorAdb()
            || FindEmulator.hasQEmuFiles()
            || FindEmulator.hasGenyFiles()
        ) {
            log("QEmu environment detected.")
            return true
        } else {
            log("QEmu environment not detected.")
            return false
        }
    }

    fun isTaintTrackingDetected(): Boolean {
        log("Checking for Taint tracking...")
        log("hasAppAnalysisPackage : " + FindTaint.hasAppAnalysisPackage(applicationContext))
        log("hasTaintClass : " + FindTaint.hasTaintClass())
        log("hasTaintMemberVariables : " + FindTaint.hasTaintMemberVariables())
        if (FindTaint.hasAppAnalysisPackage(applicationContext) || FindTaint.hasTaintClass()
            || FindTaint.hasTaintMemberVariables()
        ) {
            log("Taint tracking was detected.")
            return true
        } else {
            log("Taint tracking was not detected.")
            return false
        }
    }

    fun isMonkeyDetected(): Boolean {
        log("Checking for Monkey user...")
        log("isUserAMonkey : " + FindMonkey.isUserAMonkey())

        if (FindMonkey.isUserAMonkey()) {
            log("Monkey user was detected.")
            return true
        } else {
            log("Monkey user was not detected.")
            return false
        }
    }

    fun isDebugged(): Boolean {
        log("Checking for debuggers...")

        var tracer = false
        try {
            tracer = FindDebugger.hasTracerPid()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        if (FindDebugger.isBeingDebugged() || tracer) {
            log("Debugger was detected")
            return true
        } else {
            log("No debugger was detected.")
            return false
        }
    }

    fun log(msg: String) {
        Log.v("AntiEmulator", msg)
    }
}
