; mn61 Client installer (NSIS). Build on Windows with: makensis mn61-installer.nsi
; Put these next to this file before building:
;   mn61-client-1.0.jar   (from gradle build -> build/libs)
;   icon.ico              (from ../branding)
Unicode true
!include "MUI2.nsh"

Name "mn61 Client"
OutFile "mn61-Client_1.0.0_x64-setup.exe"
InstallDir "$APPDATA\.minecraft"
RequestExecutionLevel user
!define MUI_ICON "icon.ico"
!define MUI_UNICON "icon.ico"

!insertmacro MUI_PAGE_WELCOME
!define MUI_DIRECTORYPAGE_TEXT_TOP "Select your .minecraft folder (Forge 1.8.9 must be installed)."
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_LANGUAGE "English"

Section "Install"
  SetOutPath "$INSTDIR\mods"
  File "mn61-client-1.0.jar"
  SetOutPath "$INSTDIR\mn61"
  File "icon.ico"
  WriteUninstaller "$INSTDIR\mn61\uninstall.exe"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\mn61Client" "DisplayName" "mn61 Client"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\mn61Client" "UninstallString" "$INSTDIR\mn61\uninstall.exe"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\mn61Client" "DisplayIcon" "$INSTDIR\mn61\icon.ico"
SectionEnd

Section "Uninstall"
  Delete "$INSTDIR\mods\mn61-client-1.0.jar"
  Delete "$INSTDIR\mn61\icon.ico"
  Delete "$INSTDIR\mn61\uninstall.exe"
  RMDir "$INSTDIR\mn61"
  DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\mn61Client"
SectionEnd
