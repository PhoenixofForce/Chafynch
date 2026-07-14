# Tea Tracker - Roadmap

> **Design notes:**
> - **Units** — Store metric internally (g, °C, ml). Convert for display based on user preference. Keep in mind for all inputs/outputs.
> - **Currency** — User selects a currency in their settings. All prices are entered in that currency, no automatic conversion.
> - **Core data model (decided 2026-07-11)** — Split the flat `Tea` entity into three layers:
>   - **TeaProduct** (catalog / "what is it") — identity `(vendor, name, harvestYear)` + cultivar, teaType, origin. `harvestYear` int, nullable, **in** identity; `harvestLabel` free text ("1. Ernte April 2024") display-only, **not** in identity. Unique `(vendor_id, name, COALESCE(harvest_year, 0))` (NULL-in-unique trick, like `location`). Flush deferred → lives in the label.
>   - **Purchase** (inventory / cost) — `product_id` + date, price, weight.
>   - **Session** (experience) — `product_id` + notes / rating / brew params; hangs on the product so repurchases share one note overview.
>   - **Vendor** = producer/brand → part of product identity → do **not** `SET NULL` on delete (RESTRICT or soft-delete). Revisit `V5__null_vendor_on_delete.sql`, which contradicts this. Pragmatically it's the reseller; accepted cost: same tea from two shops = two products.

## Phase 1 — Core Foundation

### UI Shell
- [x] Header
- [x] Sidebar navigation
- [x] Light / dark mode toggle

### Tea Types
- [x] Manage tea types (green, black, oolong, white, pu-erh, etc.)
- [ ] ~~Assign a color per tea type~~
- [ ] ~~Display as colored dots / accent color in UI~~

### Vendors
- [x] Vendor management (CRUD)
- [x] Link / URL to vendor shop


### Tea Product Metadata (basics)
- [ ] Description (markdown)
- [ ] Harvest year — `harvestYear` int (nullable, part of product identity) 
- [ ] `harvestLabel` free text ("1. Ernte April 2024", display only)
- [ ] Link to buy
- [ ] Cultivar
- [ ] Origin / Terroir
- [ ] Rating (future: Tea Session Enity)
- [ ] Tasting Notes (future: Tea Session Enity)
- [ ] Price (future: Tea Purchase Enity)
- [ ] Gram (future: Tea Purchase Enity)

### Tea UI
- [ ] Detail page

> **Note:** Design all diary/rating/tasting tables with a user FK from the start to avoid migrations later when multi-user (Phase 7) lands.

---

## Phase 2 — Extended Metadata & Collection Management

> **Design notes:**
> - **Flexible fields** — Hybrid approach: universal fields as real columns, type-specific/niche fields in a `metadata JSONB` column. Lets the scraper dump whatever it finds; promote fields to real columns later if universally useful.
> - **UI for metadata** — Progressive disclosure to avoid overwhelming forms:
>   - Quick-add mode (name + type + vendor) vs. full detail mode
>   - Grouped accordion sections (Origin, Processing, Certification) — open only what's relevant
>   - Flexible fields as "Add field" / Notion-style properties, optionally filtered by tea type

### Tea Metadata (extended)
- [ ] Elevation
- [ ] Picking & processing details
- [ ] Season / harvest period
- [ ] Shading technique / duration
- [ ] Steaming method (e.g. Asamushi)
- [ ] Cultivation method (e.g. organic certified)
- [ ] Lab tests / certifications
- [ ] Photo upload (dry leaf, liquor, wet leaf)

### Inventory
- [ ] Add Tea Purchase Split (id, product_id, date, gram, price)
- [ ] Track remaining weight / "tea is empty" status
- [ ] Storage location (shelf, tin, bag)

### Vendor (extended)
- [ ] Track owned teas per vendor
- [ ] Average price across vendor's teas
- [ ] Average rating across vendor's teas

---

## Phase 3 — Tea Diary

> **Design notes:**
> - Design all diary/rating/tasting tables with a user FK from the start to avoid migrations later when multi-user (Phase 7) lands.
> - Tasting notes UI as separate tab/stepper (Eyes → Nose → Mouth → Body) or card-per-sense layout

### Session Logging
- [ ] Add Tea Session Split (id, product_id, rating, tasting notes)
- [ ] Log tea drinking session
- [ ] Rating per session
- [ ] Infusion parameters per session (time, temp, amount)
- [ ] Steep counter — track how many infusions from a session
- [ ] Water type / hardness notes
- [ ] Teaware used (gaiwan, kyusu, grandpa style...)

### Session Context
- [ ] People you drank with
- [ ] Location where you drank (home, cafe, friend's place)
- [ ] Mood / occasion tag (morning routine, meditation, work, social)

### Tasting Notes (structured)
- [ ] Eyes - Dry Leaf
- [ ] Nose - Dry Leaf
- [ ] Nose - Wet Leaf
- [ ] Eyes - Liquor
- [ ] Mouth - Texture
- [ ] Mouth - Taste
- [ ] Nose - Empty Cup
- [ ] Mouth - Finish
- [ ] Eyes - Wet Leaf
- [ ] Body Sensation
- [ ] Character summary (e.g. refreshing, vitalizing, umami, mineral)
- [ ] Taste chart / flavor wheel

### Aggregation
- [ ] Aggregate diary entries on tea detail page
- [ ] Brewing presets per tea type (temp, ratio, times for each steep)

---

## Phase 4 — Dashboard & Analytics

- [ ] Teas drunk over time (chart)
- [ ] Spending over time / per vendor
- [ ] Most brewed teas ranking
- [ ] Average rating per tea type / vendor / origin

---

## Phase 5 — Discovery & Sharing

### Discovery
- [ ] "Similar teas" suggestions (same cultivar, origin, or type)
- [ ] Comparison view — side-by-side two teas

### Sharing
- [ ] Share a tea card / tasting note as image or link
- [ ] Barcode / QR scan to quickly log a session
- [ ] Gift tracker — teas gifted to/from others

---

## Phase 6 — Import / Export

- [ ] CSV / JSON export of full collection
- [ ] CSV / JSON import
- [ ] Import tea metadata from URL
- [ ] Scraper for tea shop sites

---

## Phase 7 — Multi-User & Polish

- [ ] OIDC / OAuth
- [ ] Multiple users (shared teas, shared diary when drank together, aggregated ranking)
- [ ] i18n