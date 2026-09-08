---
name: EcoTrack Carbon Intelligence
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#3d4a42'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#6d7a72'
  outline-variant: '#bccac0'
  surface-tint: '#006c4a'
  primary: '#006948'
  on-primary: '#ffffff'
  primary-container: '#00855d'
  on-primary-container: '#f5fff7'
  inverse-primary: '#68dba9'
  secondary: '#565e74'
  on-secondary: '#ffffff'
  secondary-container: '#dae2fd'
  on-secondary-container: '#5c647a'
  tertiary: '#006194'
  on-tertiary: '#ffffff'
  tertiary-container: '#007bb9'
  on-tertiary-container: '#fdfcff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#85f8c4'
  primary-fixed-dim: '#68dba9'
  on-primary-fixed: '#002114'
  on-primary-fixed-variant: '#005137'
  secondary-fixed: '#dae2fd'
  secondary-fixed-dim: '#bec6e0'
  on-secondary-fixed: '#131b2e'
  on-secondary-fixed-variant: '#3f465c'
  tertiary-fixed: '#cce5ff'
  tertiary-fixed-dim: '#93ccff'
  on-tertiary-fixed: '#001d31'
  on-tertiary-fixed-variant: '#004b73'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 36px
    fontWeight: '600'
    lineHeight: 44px
    letterSpacing: -0.025em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.015em
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0em
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0em
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '500'
    lineHeight: 18px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.04em
  metric-val:
    fontFamily: Inter
    fontSize: 30px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: -0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  space-xxs: 0.25rem
  space-xs: 0.5rem
  space-sm: 0.75rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
  sidebar-width: 16.5rem
  sidebar-collapsed: 4.5rem
  gutter-desktop: 1.5rem
  gutter-mobile: 1rem
  content-max-w: 100rem
---

## Brand & Style

This design system serves enterprise sustainability officers, ESG analysts, and operations leads who require rigorous, auditable carbon accounting without administrative exhaustion. The aesthetic aligns with **Modern Corporate Minimalism** paired with high-clarity data ergonomics. 

The emotional tone balances institutional credibility with environmental optimism: deliberate, transparent, forward-looking, and calm. Visual tension is minimized through structured layouts, soft boundaries, and purposeful use of color. Rather than decorative greenwashing motifs, the UI presents environmental performance as a core operational metric through crisp data surfaces, disciplined hierarchy, and generous breathing room.

## Colors

The palette operates under strict semantic division between functional workspace elements, brand accents, and compliance-level data metrics.

- **Canvas & Layering**: The primary foundation relies on `#F8FAFC` (slate-50) for outer canvas backgrounds and `#FFFFFF` for data card surfaces, framed by soft structural dividers in `#E2E8F0` (slate-200). A subtle contrast container layer uses `#F1F5F9` (slate-100).
- **Navigation Anchor (Secondary)**: The global sidebar uses an intentional deep dark treatment (`#0F172A` / `#020617` surface, `#334155` borders) to anchor complex dashboard navigation and isolate app-level controls from operational dashboards.
- **Primary & Positive Trajectory**: `#059669` (emerald-600) functions as primary action anchor and positive decarbonization milestone indicators, complemented by `#10B981` (emerald-500) for interactive states and focus rings.
- **Data Semantics (Scopes & ESG Tiers)**:
  - **Scope 1 (Direct Emissions)**: Amber (`#D97706`)
  - **Scope 2 (Purchased Energy)**: Sky Blue (`#0284C7`)
  - **Scope 3 (Value Chain)**: Violet (`#7C3AED`)
  - **Alerts / Non-Compliance**: Crimson (`#E11D48`)
- Avoid gratuitous fills. Tint percentages (`emerald-50` at 10-15% opacity) should be reserved for delta pills, status badges, and active state highlights.

## Typography

Typography relies entirely on **Inter** configured with OpenType tabular figures (`tnum`) activated across all metrics tables, delta badges, and carbon ledger summaries. 

- Metric readouts feature tightly tracked letterforms (`-0.03em`) to anchor executive summary cards without consuming unnecessary horizontal real estate.
- Section headers maintain high contrast (`slate-900`), while supportive captions and column keys drop back to low-strain neutrals (`slate-500` / `slate-400`).
- Uppercase styling is strictly reserved for `label-sm` (e.g., metric units like `tCO2e`, scope tags, status indicators), with letter-spacing expanded to `0.04em` to preserve legibility at micro scales.

## Layout & Spacing

The dashboard employs a hybrid fixed-and-fluid grid model:

- **Structural Shell**: A fixed vertical sidebar (`16.5rem` / `264px`) docks the global navigation. The workspace content occupies a fluid container bounded by a maximum width of `100rem` (1600px) to prevent multi-column charts from over-stretching on ultrawide enterprise monitors.
- **Grid Structure**: Internal page layouts utilize a 12-column responsive grid with `1.5rem` (24px) gutters on desktop and `1rem` (16px) gutters on tablet/mobile screens.
- **Rhythm & Padding**: Data cards must maintain generous internal breathing space (`1.5rem` standard card padding, dropping to `1rem` for nested breakdown rows). Component groupings utilize an 8px base increment (`space-xs` through `space-2xl`).
- **Responsive Adaptations**:
  - **Desktop (≥ 1280px)**: Full multi-column analytical views (4-card metric hero, 2-column comparative visualizers, full tabular ledger).
  - **Tablet (768px – 1279px)**: Sidebar shifts to icon-rail or collapsible off-canvas drawer; metric cards collapse to 2-column grid; table columns prioritize primary emission totals with horizontal scroll on breakdowns.
  - **Mobile (< 768px)**: Single-column stacked workflow. Analytics collapse to summarized trend sparklines with secondary metadata placed in drawer modals.

## Elevation & Depth

Visual hierarchy is communicated through **low-contrast micro-borders coupled with diffused ambient shadows** rather than high-elevation drops:

- **Level 0 (App Canvas)**: `#F8FAFC` flat surface. No elevation.
- **Level 1 (Default Data Containers)**: Pure `#FFFFFF` background with a hairline border (`1px solid #E2E8F0`) and an extra-soft shadow: `0 1px 3px 0 rgba(15, 23, 42, 0.04), 0 1px 2px -1px rgba(15, 23, 42, 0.02)`.
- **Level 2 (Hover & Interactive Cards)**: On hover, metric cards translate 0px (stable layout) and transition to `border-color: #CBD5E1` with shadow `0 10px 15px -3px rgba(15, 23, 42, 0.05), 0 4px 6px -4px rgba(15, 23, 42, 0.03)`.
- **Level 3 (Dropdowns & Popovers)**: `#FFFFFF` surface with `1px solid #E2E8F0` border and elevated blur: `0 20px 25px -5px rgba(15, 23, 42, 0.08), 0 8px 10px -6px rgba(15, 23, 42, 0.04)`.
- **Level 4 (Audit Modals & Detail Drawers)**: Backdropped by a tinted obscuring wash (`rgba(15, 23, 42, 0.5)` with `backdrop-filter: blur(4px)`).
- **Navigation Isolation**: The dark sidebar (`#0F172A`) uses a right border `1px solid rgba(255, 255, 255, 0.08)` without ambient cast shadows, maintaining a clean architectural division.

## Shapes

The design system embraces modern, friendly precision with generous rounding across primary surfaces, counterbalancing analytical rigor with accessible software ergonomics.

- **Primary Cards & Containers**: Standardized on `rounded-2xl` (1.25rem / 20px) to establish clean, distinct module grouping.
- **Inner Nesting & Sub-panels**: Sub-elements inside cards (e.g., benchmark comparison panels, table containers) use `rounded-xl` (0.75rem / 12px) to prevent corner collision.
- **Interactive Controls (Inputs, Buttons, Tabs)**: Standardized on `rounded-lg` (0.5rem / 8px) for crisp tap targets and clean baseline alignment.
- **Pills & Indicator Badges**: Full pill radius (`rounded-full` / 9999px) is used strictly for contextual metrics (e.g., `-14.2% YoY`, `Scope 1`, `Verified`).

## Components

### Buttons
- **Primary**: Background `#059669`, text `#FFFFFF`, font `label-md`, radius `rounded-lg`. Hover: `#047857`. Active: `#065F46`. Focus: `ring-2 ring-emerald-500 ring-offset-2`.
- **Secondary / Outline**: Background `#FFFFFF`, border `1px solid #E2E8F0`, text `#334155`. Hover: `#F8FAFC`, border `#CBD5E1`.
- **Tertiary / Ghost**: Transparent fill, text `#64748B`. Hover: `#F1F5F9`, text `#0F172A`.
- **Destructive**: Background `#FFFFFF`, border `1px solid #FECDD3`, text `#E11D48`. Hover: `#FFF1F2`.

### Metric & KPI Cards
- Contained inside `rounded-2xl` surfaces (`#FFFFFF`, `1px solid #E2E8F0`).
- **Structure**:
  - Top row: Metric label (`label-md`, `#64748B`) paired with an icon or benchmark pill.
  - Middle row: Formatted quantitative readout (`metric-val`, `#0F172A`) with trailing unit of measure (`label-sm`, `#64748B`).
  - Bottom row: Trajectory indicator with micro-arrow and contextual percentage badge (emerald for net reductions, amber/red for growth spikes).

### Chips & Badges
- Height: `24px`. Padding: `0 8px`. Radius: `rounded-full`.
- **Positive / Reduction Pill**: Background `#ECFDF5`, text `#047857`, border `1px solid #A7F3D0`.
- **Scope 1 / Warning Pill**: Background `#FFFBEB`, text `#B45309`, border `1px solid #FDE68A`.
- **Scope 2 / Informational Pill**: Background `#F0F9FF`, text `#0369A1`, border `1px solid #BAE6FD`.
- **Scope 3 / Neutral Pill**: Background `#F8FAFC`, text `#475569`, border `1px solid #E2E8F0`.

### Form Inputs & Selectors
- Height: `40px`. Padding: `0 12px`. Radius: `rounded-lg`.
- Border: `1px solid #CBD5E1`, background `#FFFFFF`, text `#0F172A`, placeholder `#94A3B8`.
- Focus state: `border-color: #059669`, outline `none`, `box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.15)`.

### Data Tables & Emissions Ledgers
- Outer frame: `rounded-xl`, border `1px solid #E2E8F0`, overflow hidden.
- Header row: `#F8FAFC`, text `label-sm` uppercase, height `40px`, border-bottom `1px solid #E2E8F0`.
- Data rows: `#FFFFFF`, height `52px`, border-bottom `1px solid #F1F5F9`, alternating hover `#F8FAFC`.
- All numeric emissions totals right-aligned with monospace numerals (`font-feature-settings: 'tnum'`).

### Navigation Sidebar
- Surface `#0F172A`, border-right `1px solid #1E293B`.
- Items: Height `40px`, radius `rounded-lg`, margin `0 12px`, padding `0 12px`. Text `#94A3B8`.
- Active item: Background `rgba(16, 185, 129, 0.12)`, text `#34D399`, font weight `500`. Left indicator or clean icon fill accent.