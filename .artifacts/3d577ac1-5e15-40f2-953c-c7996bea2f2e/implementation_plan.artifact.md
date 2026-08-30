# Restore Android Studio Taskbar Icon

The user is experiencing an issue where the Android Studio icon in the Windows taskbar appears as a generic file icon. This is typically caused by a corrupted shortcut or a stale Windows icon cache.

## User Review Required

> [!IMPORTANT]
> Since this issue is related to the Windows operating system's taskbar and icon management, the fix involves manual steps or running a script to refresh the system's icon cache. I cannot directly modify the Windows taskbar configuration through project code.

## Proposed Steps

### 1. Manual Fix: Unpin and Repin (Recommended First Step)
The most common solution is to refresh the taskbar shortcut.
- Right-click the "file icon" in your taskbar and select **Unpin from taskbar**.
- Open the Start menu, search for **Android Studio**.
- Right-click the result and select **Pin to taskbar**.

### 2. Manual Fix: Reset Icon Cache
If the icon is still incorrect after repinning, the Windows icon cache might be corrupted.
- Open **Command Prompt** as Administrator.
- Run the following commands:
  ```cmd
  taskkill /F /IM explorer.exe
  cd /d %userprofile%\AppData\Local
  attrib -h IconCache.db
  del IconCache.db
  start explorer
  ```

### 3. Verification of Executable Icon
I will verify if the executable itself has the correct icon properties to ensure the installation is not corrupted.

## Verification Plan

### Manual Verification
- The user should confirm if unpinning and repinning restores the icon.
- If not, the user should follow the icon cache reset steps.
