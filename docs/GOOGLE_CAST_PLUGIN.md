# Google Cast Plugin Project

This is the single entry point for the BiliPai Google Cast plugin work. Keep it concise, update it after each completed slice, and remove stale details instead of adding parallel notes.

## Working Rules

- Build this as a BiliPai source-level native Kotlin plugin.
- Use sub-agent driven development: architecture, task slicing, review, and verification stay with the controller; coding slices are implemented through `claude -p`.
- After every completed slice, update this file so another worker can resume quickly.
- Keep documentation progressive: this file is the only project entry. Link to existing repo docs only when needed.
- Avoid unnecessary files. Delete obsolete notes and update stale information immediately.
- Use `D:\Temp` for temporary scratch data if needed; avoid writing to `C:` unless a tool requires it.
- Do not work directly on `main`; use the `feature/google-cast-plugin` worktree at `D:\DATA\Codes\BiliPai\worktrees\google-cast-plugin`.
- Branch names, commits, PR titles, and PR bodies must use normal human engineering wording without tool or authorship labels.

## Existing Context

- Native plugins are registered in `app/src/main/java/com/android/purebilibili/app/PureApplication.kt`.
- The plugin framework lives in `app/src/main/java/com/android/purebilibili/core/plugin/`.
- Built-in plugins live in `app/src/main/java/com/android/purebilibili/feature/plugin/`.
- Existing DLNA casting lives in `app/src/main/java/com/android/purebilibili/feature/cast/`.
- Existing player cast UI is wired in `app/src/main/java/com/android/purebilibili/feature/video/ui/overlay/VideoPlayerOverlay.kt`.
- Existing BiliBili TV cast URL policy is in `app/src/main/java/com/android/purebilibili/data/repository/VideoCastPolicy.kt`.

## Architecture Decision

Implement Google Cast as a native plugin plus a focused Cast integration layer:

- Add a native plugin entry so the feature appears in the plugin center and can be enabled or disabled.
- Add a small cast-provider abstraction so the player overlay can offer DLNA and Google Cast without mixing protocol details into UI code.
- Use Google Cast CAF for Chromecast discovery/session/media loading, and reuse the current BiliBili TV cast URL fallback path where possible.
- Keep DLNA behavior intact and avoid replacing the existing DLNA manager in this project.

Official references checked on 2026-05-26:

- Google Cast Android sender setup recommends `play-services-cast-framework:22.3.1` and a Cast options provider.
- AndroidX MediaRouter latest stable is `androidx.mediarouter:mediarouter:1.8.1`.
- Media3 CastPlayer docs exist, but this slice starts with CAF session/media loading to avoid a broad player-service rewrite.

## Slices

### Slice 0: Baseline And Scaffolding

Goal: confirm the worktree, baseline focused tests, and add this documentation entry.

Status: complete.

Verification target:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "*Cast*"
```

Result on 2026-05-26: passed with `BUILD SUCCESSFUL`.

### Slice 1: Google Cast Plugin Shell

Goal: add build/manifest wiring, a native plugin class, and small policy tests proving enablement metadata and route visibility behavior.

Likely files:

- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/android/purebilibili/app/PureApplication.kt`
- `app/src/main/java/com/android/purebilibili/feature/plugin/GoogleCastPlugin.kt`
- New focused tests under `app/src/test/java/com/android/purebilibili/feature/cast/`

### Slice 2: Chromecast Discovery And Selection

Goal: discover Google Cast routes, present them alongside existing DLNA entries, and honor plugin enabled state.

Likely files:

- New Google Cast manager/provider files under `feature/cast/`
- `DeviceListDialog.kt`
- Focused discovery/presentation policy tests

### Slice 3: Chromecast Media Load

Goal: when a Chromecast device is selected, resolve the current playable cast URL and load it through the active Google Cast session.

Likely files:

- Google Cast session/media client wrapper
- `VideoPlayerOverlay.kt`
- Focused tests for media metadata/request construction and URL fallback decisions

### Slice 4: Review, Verification, And Cleanup

Goal: run targeted tests and compile checks, remove dead notes/files, update this document, and prepare commits.

Verification ladder:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "*Cast*"
.\gradlew.bat :app:compileDebugKotlin
```

## Progress Log

- 2026-05-26: Created isolated worktree `feature/google-cast-plugin`.
- 2026-05-26: Confirmed existing casting is DLNA/SSDP based; Google Cast/Chromecast support is not implemented yet.
- 2026-05-26: Completed Slice 0 documentation and focused Cast baseline.
