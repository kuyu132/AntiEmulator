//
// Created by kuyuzhiqi on 2019-05-22.
//

#include <stdlib.h>
#include <signal.h>
#include <sys/wait.h>
#include <jni.h>
#include <unistd.h>

void handler_sigtrap(int signo) {
    exit(-1);
}

void handler_sigbus(int signo) {
    exit(-1);
}


void setupSigTrap() {
    // BKPT throws SIGTRAP on nexus 5 / oneplus one (and most devices)
    signal(SIGTRAP, handler_sigtrap);
    // BKPT throws SIGBUS on nexus 4
    signal(SIGBUS, handler_sigbus);
}

// This will cause a SIGSEGV on some QEMU or be properly respected
//bkpt 255:ARM BKPT指令
void tryBKPT() {
    __asm__ __volatile__ ("bkpt 255");
}

extern "C" jint Java_com_kuyuzhiqi_antiemulator_emulator_FindEmulator_qemuBkpt(JNIEnv *env, jclass type) {
    pid_t  child = fork();
    int child_status, status = 0;

    if(child == 0) {
        setupSigTrap();
        tryBKPT();
    } else if(child == -1) {
        status = -1;
    } else {
        int timeout = 0;
        int i = 0;
        while (waitpid(child, &child_status, WNOHANG) == 0) {
            sleep(1);
            // Time could be adjusted here, though in my experience if the child has not returned instantly
            // then something has gone wrong and it is an emulated device
            if(i++ == 1) {
                timeout = 1;
                break;
            }
        }

        if(timeout == 1) {
            // Process timed out - likely an emul
            status = 1;
        }

        if(WIFEXITED(child_status)) {
            // Likely a real device
            status = 0;
        } else {
            // Didn't exit properly - very likely an emulator
            status = 2;
        }

        kill(child, SIGKILL);
    }

    return status;
}