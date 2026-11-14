package io.github.henr1klmb.patches.shared;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@SuppressLint({"SoonBlockedPrivateApi", "PrivateApi", "DiscouragedPrivateApi"})
public class SpoofSignatureHelper {

    private static final ThreadLocal<Boolean> IS_CALLING = new ThreadLocal<Boolean>();

    public static String getAppPackageName() {
        return "";
    }

    public static String getOriginalSignatureHex() {
        return "";
    }

    public static PackageInfo getPackageInfo(PackageManager pm, String packageName, int flags)
            throws PackageManager.NameNotFoundException {
        if (pm == null) {
            return null;
        }
        if (Boolean.TRUE.equals(IS_CALLING.get())) {
            return pm.getPackageInfo(packageName, flags);
        }
        IS_CALLING.set(Boolean.TRUE);
        try {
            PackageInfo info = pm.getPackageInfo(packageName, flags);
            spoofPackageInfo(packageName, (long) flags, info);
            return info;
        } finally {
            IS_CALLING.remove();
        }
    }

    @TargetApi(33)
    @SuppressLint("UseRequiresApi")
    public static PackageInfo getPackageInfo(PackageManager pm, String packageName, PackageManager.PackageInfoFlags flags)
            throws PackageManager.NameNotFoundException {
        if (pm == null) {
            return null;
        }
        if (Boolean.TRUE.equals(IS_CALLING.get())) {
            return pm.getPackageInfo(packageName, flags);
        }
        IS_CALLING.set(Boolean.TRUE);
        try {
            PackageInfo info = pm.getPackageInfo(packageName, flags);
            long flagsValue = 0L;
            if (flags != null) {
                flagsValue = flags.getValue();
            }
            spoofPackageInfo(packageName, flagsValue, info);
            return info;
        } finally {
            IS_CALLING.remove();
        }
    }

    private static void spoofPackageInfo(String packageName, long flags, PackageInfo info) {
        if (info == null) {
            return;
        }

        String targetPkg = getAppPackageName();
        // If targetPkg is configured and doesn't match the queried package, don't spoof.
        if (targetPkg != null && !targetPkg.isEmpty() && packageName != null && !targetPkg.equals(packageName)) {
            return;
        }

        String originalHex = getOriginalSignatureHex();
        if (originalHex == null || originalHex.isEmpty()) {
            return;
        }

        try {
            Signature originalSig = new Signature(originalHex);
            Signature[] signatures = new Signature[] { originalSig };

            // GET_SIGNATURES = 0x00000040 (64)
            boolean requestedSignatures = (flags & PackageManager.GET_SIGNATURES) != 0L;
            if (requestedSignatures || info.signatures != null) {
                info.signatures = signatures;
            }

            // GET_SIGNING_CERTIFICATES = 0x08000000 (134217728)
            boolean requestedSigningCertificates = (flags & 0x08000000L) != 0L;
            if (requestedSigningCertificates || (Build.VERSION.SDK_INT >= 28 && info.signingInfo != null)) {
                applySigningInfo(info, signatures);
            }
        } catch (Throwable ignored) {
        }
    }

    private static void applySigningInfo(PackageInfo info, Signature[] signatures) {
        if (Build.VERSION.SDK_INT < 28) {
            return;
        }

        try {
            if (info.signingInfo != null) {
                Field detailsField = SigningInfo.class.getDeclaredField("mSigningDetails");
                detailsField.setAccessible(true);
                Object details = detailsField.get(info.signingInfo);
                if (details != null) {
                    Field sigsField = details.getClass().getDeclaredField("signatures");
                    sigsField.setAccessible(true);
                    sigsField.set(details, signatures);
                    return;
                }
            }

            Class<?> detailsClass;
            try {
                detailsClass = Class.forName("android.content.pm.PackageParser$SigningDetails");
            } catch (ClassNotFoundException e) {
                detailsClass = Class.forName("android.content.pm.SigningDetails");
            }

            Constructor<?> detailsCtor = detailsClass.getDeclaredConstructor(Signature[].class, int.class);
            detailsCtor.setAccessible(true);
            Object details = detailsCtor.newInstance(signatures, 1);

            Constructor<SigningInfo> signingInfoCtor = SigningInfo.class.getDeclaredConstructor(detailsClass);
            signingInfoCtor.setAccessible(true);
            info.signingInfo = signingInfoCtor.newInstance(details);
        } catch (Throwable ignored) {
        }
    }
}
