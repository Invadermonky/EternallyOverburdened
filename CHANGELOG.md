# Changelog
## 1.12.2-1.1.3
### Added
- Added Iron Chests shulker box compat
### Fixed
- Fixed broken `ru_ru.lang` language keys
- Fixed player max carry weight resetting on death

---

## 1.12.2-1.1.2
### Fixed
- Fixed a crash that when Quark is not installed
- Fixed issue parsing item ids that ended in numbers

---

## 1.12.2-1.1.1
### Added
- Added new carry weight update event handler
- Added support for HBM Nuclear Tech Crates and Ammo Bags

---

## 1.12.2-1.1.0
### Added
- Added new Overburdened potion icon, courtesy of [Foreck1](https://www.curseforge.com/members/foreck1/projects)
- Added `ru_ru.lang`, courtesy of [MetalloloM-Edition](https://github.com/MetalloloM-Edition)
- Added new API method for adding custom item weights
- Added new API method for adding custom capability handling
- Added support for Shulker Box inventory weight
- Added support for Actually Additions Traveler's Sack inventory weight
- Added support for Immersive Engineering barrel and crate inventory weight
- Added support for Travelers Backpack backpack inventory weight
- Added command allowing dynamic modification of player carry weight

### Changed
- Configuration item weights now support ore dictionary strings
- Carry weight can now be accessed and modified with an IAttribute value
- Refactored mod id due to previous id exceeding forges 20 character limit

### Fixed
- Fixed a number of issues caused by the mod id exceeding forge's 20 character limit

---

## 1.12.2-1.0.0
- Initial Release