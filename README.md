# ⚔️ Sparta Menu

A sleek, modern IL2CPP Android mod menu template with a glassmorphism UI.
Based on [R3DNETWORK](https://github.com/seedhollow/R3DNETWORK) (which is based on LGL Mod Menu).

## Features
- 🎨 Glassmorphism floating menu with smooth animations
- 🔍 Search bar to filter features
- ⭐ Favorites system
- 💾 Config export/import
- 🎨 In-menu accent themes
- 🛡 BlackObfuscator + LSParanoid string protection built in

## Integration into your target app
1. Add overlay permission to your app's manifest:
   `<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />`
2. Register the menu service in your manifest
3. Call `com.android.support.Main.start(context)` from your app

(Expanded docs coming — see the original template's README for full details.)

## Build
`./gradlew assembleDebug` or via GitHub Actions (automatic on push).
