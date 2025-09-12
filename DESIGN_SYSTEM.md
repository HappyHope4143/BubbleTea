# BubbleTea Design System

## Overview
The BubbleTea design system is built on Material 3 principles with a warm, inviting bubble tea theme. It emphasizes comfort, quality, and the authentic bubble tea experience.

## Color System

### Brand Colors
Our color palette is inspired by the rich, warm tones of bubble tea ingredients:

#### Primary Colors
- **Primary Brown** (`#8B4513`): Rich brown reminiscent of brown sugar and roasted tea
- **Milk Tea Tan** (`#D2B48C`): Warm, creamy tan representing milk tea
- **Tapioca Dark** (`#4A4A4A`): Deep gray representing tapioca pearls

#### Light Theme
- **Background**: Warm cream (`#FFFBF5`) - provides a cozy, inviting backdrop
- **Surface**: Slightly warmer surface (`#FAF7F0`) - for cards and elevated components
- **Text Colors**: Dark brown (`#3E2723`) for optimal readability on light backgrounds

#### Dark Theme
- **Primary**: Lighter brown (`#DDBB88`) - adjusted for dark mode visibility
- **Background**: Standard dark (`#1C1B1F`) - follows system conventions
- **Text Colors**: Light cream (`#E6E1E5`) for dark backgrounds

#### Accent Colors
- **Success**: Green (`#4CAF50`) - for positive actions and confirmations
- **Warning**: Orange (`#FF9800`) - for cautions and alerts
- **Error**: Red (`#F44336`) - for errors and critical issues
- **Info**: Blue (`#2196F3`) - for informational content

### Usage Guidelines
1. **Primary colors** should be used for main actions, navigation, and brand elements
2. **Background and surface** colors create hierarchy and depth
3. **Accent colors** provide semantic meaning and user feedback
4. **Dynamic color is disabled** to maintain consistent brand identity across devices

## Typography

### Material 3 Typography Scale
Our typography system implements the complete Material 3 scale with consistent spacing and hierarchy:

#### Display Styles (Large, prominent text)
- **Display Large**: 57sp, -0.25sp letter spacing
- **Display Medium**: 45sp, 0sp letter spacing  
- **Display Small**: 36sp, 0sp letter spacing

#### Headline Styles (Medium prominence)
- **Headline Large**: 32sp, 0sp letter spacing
- **Headline Medium**: 28sp, 0sp letter spacing
- **Headline Small**: 24sp, 0sp letter spacing

#### Title Styles (Section headers)
- **Title Large**: 22sp, 0sp letter spacing
- **Title Medium**: 16sp, 0.15sp letter spacing, Medium weight
- **Title Small**: 14sp, 0.1sp letter spacing, Medium weight

#### Body Styles (Main content)
- **Body Large**: 16sp, 0.5sp letter spacing
- **Body Medium**: 14sp, 0.25sp letter spacing
- **Body Small**: 12sp, 0.4sp letter spacing

#### Label Styles (Buttons, tabs)
- **Label Large**: 14sp, 0.1sp letter spacing, Medium weight
- **Label Medium**: 12sp, 0.5sp letter spacing, Medium weight
- **Label Small**: 11sp, 0.5sp letter spacing, Medium weight

### Typography Guidelines
1. Use **Display** styles for hero sections and major announcements
2. Use **Headline** styles for page titles and major sections
3. Use **Title** styles for subsections and card headers
4. Use **Body** styles for paragraphs and general content
5. Use **Label** styles for interactive elements and UI labels

## Design Tokens

### Spacing System
Consistent spacing creates visual rhythm and hierarchy:
- **Extra Small**: 4dp - for tight spacing within components
- **Small**: 8dp - for component internal padding
- **Medium**: 16dp - for standard margins and padding
- **Large**: 24dp - for section spacing
- **Extra Large**: 32dp - for major section breaks
- **Huge**: 48dp - for page-level spacing

### Shape System
Rounded corners create a friendly, approachable feel:
- **Extra Small**: 4dp radius - for small elements
- **Small**: 8dp radius - for buttons and small cards
- **Medium**: 12dp radius - for standard cards
- **Large**: 16dp radius - for prominent cards
- **Extra Large**: 24dp radius - for hero cards
- **Circular**: 50% radius - for fully rounded elements

### Elevation System
Shadows create depth and hierarchy:
- **None**: 0dp - for flat surfaces
- **Small**: 2dp - for subtle elevation
- **Medium**: 4dp - for interactive elements
- **Large**: 8dp - for prominent elements
- **Extra Large**: 16dp - for modal dialogs

### Icon Sizes
Consistent icon sizing improves visual balance:
- **Small**: 16dp - for inline icons
- **Medium**: 24dp - for standard UI icons
- **Large**: 32dp - for prominent actions
- **Extra Large**: 48dp - for hero icons

## Component Guidelines

### Cards
- Use **default elevation** (2dp) for standard content cards
- Use **elevated elevation** (4dp) for interactive or important cards
- Apply **medium corner radius** (12dp) for friendly appearance

### Buttons
- **Primary buttons** use brand brown with white text
- **Secondary buttons** use tan background with dark text
- Apply **small elevation** (2dp) that increases on press
- Use **small corner radius** (8dp) for approachable feel

### Navigation
- **Status bar** uses primary brand color
- **Light status bar icons** in light theme, dark icons in dark theme
- Consistent with system UI guidelines

## Implementation

### Jetpack Compose Usage
```kotlin
// Using design tokens
Card(
    modifier = Modifier.padding(BubbleTeaTokens.Spacing.medium),
    shape = BubbleTeaTokens.Shapes.medium,
    elevation = BubbleTeaTokens.Cards.defaultElevation()
) {
    Text(
        text = "Bubble Tea Menu",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}
```

### Theme Application
```kotlin
BubbleTeaTheme {
    // Your app content here
    // Colors and typography are automatically applied
}
```

## Accessibility

### Color Contrast
- All color combinations meet WCAG AA standards
- Text contrast ratios exceed 4.5:1 for normal text
- UI element contrast ratios exceed 3:1

### Touch Targets
- Minimum touch target size of 48dp
- Adequate spacing between interactive elements
- Clear visual feedback for interactions

### Typography Accessibility
- Scalable text that respects system font size settings
- Adequate line spacing for readability
- Clear hierarchy with sufficient size differences

## Brand Consistency

### Logo and Icons
- Custom bubble tea icon represents brand identity
- Consistent use of brand colors in iconography
- Scalable vector graphics for all screen densities

### Voice and Tone
- Warm, welcoming, and authentic
- Focus on quality and craftsmanship
- Friendly but professional communication

### Visual Language
- Organic, rounded shapes
- Warm, earthy color palette
- Clean, uncluttered layouts
- Focus on content and usability

## Future Considerations

### Custom Fonts
Consider implementing custom typography for stronger brand identity:
- Display fonts for headlines and hero text
- Readable body fonts optimized for mobile
- Consistent weight and style variations

### Illustrations
Develop custom illustrations that complement the color system:
- Bubble tea ingredients and preparation
- Abstract patterns inspired by tea culture
- Consistent style and color usage

### Animation
Implement subtle animations that enhance user experience:
- Gentle transitions between screens
- Delightful micro-interactions
- Loading states that feel organic

### Internationalization
Ensure design system works across languages:
- Flexible layouts for text expansion
- RTL layout support
- Cultural color considerations