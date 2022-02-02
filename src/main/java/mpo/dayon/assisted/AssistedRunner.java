package mpo.dayon.assisted;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import mpo.dayon.assisted.gui.Assisted;
import mpo.dayon.common.Runner;
import mpo.dayon.common.error.FatalErrorHandler;
import mpo.dayon.common.log.Log;

import javax.swing.SwingUtilities;
import java.io.File;
import java.util.Map;

import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

class AssistedRunner implements Runner {
    public static void main(String[] args) {
        try {
            Runner.setDebug(args);
            Map<String, String> programArgs = Runner.extractProgramArgs(args);
            Runner.overrideLocale(programArgs.get("lang"));
            Runner.disableDynamicScale();
            Runner.logAppInfo("dayon_assisted");
            fixUacBehaviour();
            SwingUtilities.invokeLater(() -> launchAssisted(programArgs.get("ah"), programArgs.get("ap")));
        } catch (Exception ex) {
            FatalErrorHandler.bye("The assisted is dead!", ex);
        }
    }

    private static void launchAssisted(String assistantHost, String assistantPort) {
        final Assisted assisted = new Assisted();
        assisted.configure();
        assisted.start(assistantHost, assistantPort);
    }

    private static void fixUacBehaviour() {
        if (File.separatorChar == '/') {
            return;
        }
        final int off = 0x00000000;
        final int on = 0x00000001;
        final int secureDesktop = Advapi32Util.registryGetIntValue
                (HKEY_LOCAL_MACHINE,
                        "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System",
                        "PromptOnSecureDesktop");
        if (off != secureDesktop) {
            try {
                Advapi32Util.registrySetIntValue
                        (HKEY_LOCAL_MACHINE,
                                "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System", "PromptOnSecureDesktop", off);
                Advapi32Util.registrySetIntValue
                        (HKEY_LOCAL_MACHINE,
                                "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System", "EnableLUA", on);
            } catch(Win32Exception e) {
                Log.warn("Could not fix UAC behaviour, UAC dialogs will not be visible");
                Log.warn("Rerun the assisted with admin rights to fix this");
            }
        }
    }
}
